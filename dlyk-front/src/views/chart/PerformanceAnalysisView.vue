<!--
  业绩分析页：三个 ECharts 图表
  1. 折线图 - 近 12 个月交易金额趋势（可按员工 / 全公司切换）
  2. 环形饼图 - 员工业绩占比（按月或按年筛选）
  3. 漏斗图 - 线索→客户→续费 转化漏斗（按时间范围、员工筛选）
  对应后端：ChartStatisticsController 的 /api/chart/performance/*
-->
<template>
  <div class="chart-page">
    <!-- ========== 第一行：折线图（占满整行） ========== -->
    <el-row :gutter="16">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header-flex">
              <span class="card-title">每月交易金额趋势</span>
              <!-- 切换折线显示：多条员工线 vs 仅一条全公司汇总线 -->
              <div class="card-tools">
                <el-select v-model="lineViewMode" style="width: 120px" @change="applyLineChart">
                  <el-option label="按员工" value="employee" />
                  <el-option label="全公司" value="company" />
                </el-select>
              </div>
            </div>
          </template>
          <!-- ref="lineRef" 供 script 里 echarts.init(lineRef.value) 挂载折线图 -->
          <div ref="lineRef" class="chart-box chart-box-lg" />
        </el-card>
      </el-col>
    </el-row>

    <!-- ========== 第二行：左侧饼图 + 右侧漏斗图 ========== -->
    <el-row :gutter="16" class="chart-row">
      <!-- 员工业绩占比（环形饼图） -->
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header-flex">
              <span class="card-title">员工业绩占比</span>
              <div class="card-tools">
                <!-- 按月：选具体年月；按年：只选年份 -->
                <el-select v-model="piePeriodType" style="width: 88px" @change="loadPie">
                  <el-option label="按月" value="month" />
                  <el-option label="按年" value="year" />
                </el-select>
                <el-date-picker v-if="piePeriodType === 'month'" v-model="pieMonth" type="month"
                  value-format="YYYY-MM" style="width: 140px" @change="loadPie" />
                <el-date-picker v-else v-model="pieYear" type="year" value-format="YYYY"
                  style="width: 100px" @change="loadPie" />
              </div>
            </div>
          </template>
          <div ref="pieRef" class="chart-box" />
        </el-card>
      </el-col>

      <!-- 销售漏斗 -->
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header-flex">
              <span class="card-title">销售漏斗</span>
              <div class="card-tools funnel-tools">
                <!-- 统计时间范围（可选，不传则后端用默认范围） -->
                <el-date-picker v-model="funnelRange" type="datetimerange" value-format="YYYY-MM-DD HH:mm:ss"
                  start-placeholder="开始" end-placeholder="结束" style="width: 280px" @change="loadFunnel" />
                <!-- 0=全公司；选具体员工则只统计该员工创建的线索/交易 -->
                <el-select v-model="funnelUserId" clearable placeholder="全部公司" style="width: 130px" @change="loadFunnel">
                  <el-option label="全公司" :value="0" />
                  <el-option v-for="u in owners" :key="u.id" :label="u.name" :value="u.id" />
                </el-select>
              </div>
            </div>
          </template>
          <div ref="funnelRef" class="chart-box" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
/**
 * 业绩分析页逻辑说明：
 * - 使用 Vue 3 script setup + ECharts 5
 * - 三个图表各自对应一个 DOM ref 和一个 echarts 实例变量
 * - 页面挂载时 init 图表 → 请求接口 → setOption 渲染
 * - 窗口 resize 时调用 chart.resize() 避免图表变形
 */

import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'
import { doGet } from '../../http/httpRequest'
import { showMessage } from '../../util/message'
import { buildDonutOption, buildLineOption, buildFunnelOption } from '../../util/donutChart'

/** 后端折线图里「全公司汇总」那条线的名称，筛选时要认这个名字 */
const LINE_TOTAL_NAME = '金额总量'

// ---------- DOM 引用：指向模板里三个 chart-box div ----------
const lineRef = ref(null)
const pieRef = ref(null)
const funnelRef = ref(null)

// ---------- ECharts 实例（非响应式普通变量即可，不必 ref） ----------
let lineChart = null
let pieChart = null
let funnelChart = null

// ---------- 饼图筛选条件 ----------
const piePeriodType = ref('month') // month | year
const pieMonth = ref(new Date().toISOString().slice(0, 7)) // 默认当前月，格式 2026-05
const pieYear = ref(String(new Date().getFullYear())) // 默认当前年

// ---------- 漏斗图筛选条件 ----------
const funnelRange = ref([]) // [开始时间, 结束时间]
const funnelUserId = ref(0) // 0 表示全公司
const owners = ref([]) // 负责人下拉列表，来自 api/owners

// ---------- 折线图：视图模式 + 原始数据缓存 ----------
/** employee=显示各员工线；company=只显示全公司一条汇总线 */
const lineViewMode = ref('employee')
/** 保存接口返回的完整折线数据，切换「按员工/全公司」时不必重新请求 */
const lineChartRaw = ref(null)

/**
 * 初始化或获取折线图实例。
 * 必须在 DOM 有宽高后调用，否则 ECharts 会认为容器宽度为 0，图是空白。
 */
const ensureLineChart = () => {
  if (!lineRef.value) return
  if (!lineChart) {
    lineChart = echarts.init(lineRef.value)
  }
}

/** 三个图表统一做一次 init（仅首次创建实例） */
const initCharts = () => {
  ensureLineChart()
  if (pieRef.value && !pieChart) pieChart = echarts.init(pieRef.value)
  if (funnelRef.value && !funnelChart) funnelChart = echarts.init(funnelRef.value)
}

/** 浏览器窗口大小变化时，让图表重新适应容器宽度 */
const resize = () => {
  lineChart?.resize()
  pieChart?.resize()
  funnelChart?.resize()
}

/**
 * 根据下拉框模式，从后端返回的多条折线里挑出要显示的部分。
 * @param {Array} series 后端 series，每项 { name, data: [金额,...] }
 * @param {string} mode 'employee' | 'company'
 */
const filterLineSeries = (series, mode) => {
  const list = series || []
  if (mode === 'company') {
    // 只保留「金额总量」那条，并重命名为「全公司」便于图例阅读
    const total = list.find((s) => s.name === LINE_TOTAL_NAME)
    return total ? [{ ...total, name: '全公司' }] : []
  }
  // 按员工：去掉汇总线，避免和员工线尺度差太大叠在一起看不清
  return list.filter((s) => s.name !== LINE_TOTAL_NAME)
}

/**
 * 用缓存的 lineChartRaw + 当前 lineViewMode 重新 setOption。
 * 切换「按员工/全公司」时只调这个，不重新请求接口。
 */
const applyLineChart = () => {
  const info = lineChartRaw.value
  if (!info) return
  ensureLineChart()
  if (!lineChart) return
  const series = filterLineSeries(info.series, lineViewMode.value)
  // buildLineOption 在 util/donutChart.js，组装 x 轴月份 + 多条折线
  lineChart.setOption(buildLineOption(info.months, series), true)
  // 布局完成后多次 resize，解决卡片刚展开时宽度为 0 的问题
  requestAnimationFrame(() => lineChart.resize())
  setTimeout(() => lineChart.resize(), 120)
}

/** 接口成功后写入缓存并渲染折线图 */
const renderLine = (info) => {
  if (!info) return
  lineChartRaw.value = info
  applyLineChart()
}

/** 请求 GET /api/chart/performance/line → 近 12 月各员工+公司总金额 */
const loadLine = () => {
  doGet('/api/chart/performance/line', {}).then((r) => {
    if (r?.data?.code === 200) {
      renderLine(r.data.info)
    } else {
      showMessage(r?.data?.msg || '折线图数据加载失败', 'error')
    }
  }).catch((err) => {
    const msg = err?.response?.data?.msg || err?.message || '折线图请求失败'
    showMessage(msg, 'error')
  })
}

/**
 * 请求 GET /api/chart/performance/pie
 * 参数：periodType、year、（按月时还有 month）
 */
const loadPie = () => {
  const params = { periodType: piePeriodType.value }
  if (piePeriodType.value === 'month' && pieMonth.value) {
    const [y, m] = pieMonth.value.split('-')
    params.year = Number(y)
    params.month = Number(m)
  } else if (pieYear.value) {
    params.year = Number(pieYear.value)
  }
  doGet('/api/chart/performance/pie', params).then((r) => {
    if (r.data.code === 200) {
      // 转成 ECharts 需要的 { name, value } 数组
      const data = (r.data.info || []).map((x) => ({ name: x.name, value: x.value }))
      pieChart?.setOption(buildDonutOption('业绩占比', data), true)
    }
  })
}

/**
 * 请求 GET /api/chart/performance/funnel
 * 返回各级数量：线索数、已联系、已转客户、续费交易数 等
 */
const loadFunnel = () => {
  const params = { userId: funnelUserId.value || 0 }
  if (funnelRange.value && funnelRange.value.length === 2) {
    params.startTime = funnelRange.value[0]
    params.endTime = funnelRange.value[1]
  }
  doGet('/api/chart/performance/funnel', params).then((r) => {
    if (r.data.code === 200) {
      funnelChart?.setOption(buildFunnelOption(r.data.info || []), true)
    }
  })
}

/** 加载销售/负责人列表，供漏斗图「按员工」下拉使用 */
const loadOwners = () => {
  doGet('api/owners', {}).then((r) => {
    if (r.data.code === 200) owners.value = r.data.info || []
  })
}

// ---------- 生命周期 ----------
onMounted(() => {
  // nextTick：等 template 里的 ref DOM 渲染出来再 init ECharts
  nextTick(() => {
    initCharts()
    loadPie()
    loadFunnel()
    loadOwners()
    // 折线图放最后：等顶部卡片布局完成再请求+绘制，减少空白图
    requestAnimationFrame(() => {
      loadLine()
    })
    window.addEventListener('resize', resize)
  })
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resize)
  // 销毁实例，防止内存泄漏
  lineChart?.dispose()
  pieChart?.dispose()
  funnelChart?.dispose()
})
</script>

<style scoped>
.chart-page { padding: 4px; }
.chart-row { margin-top: 16px; }
.chart-card { border-radius: 10px; }
.card-title { font-weight: 600; font-size: 15px; color: #303133; }
.card-header-flex { display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 8px; }
.card-tools { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
.funnel-tools { justify-content: flex-end; }
/* chart-box 必须有明确高度，否则 ECharts 高度为 0 */
.chart-box { width: 100%; min-width: 200px; height: 360px; }
.chart-box-lg { height: 420px; min-height: 420px; }
</style>
