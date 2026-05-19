<template>
  <div class="chart-page">
    <el-row :gutter="16">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="card-title">每月交易金额趋势</span></template>
          <div ref="lineRef" class="chart-box chart-box-lg" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header-flex">
              <span class="card-title">员工业绩占比</span>
              <div class="card-tools">
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

      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header-flex">
              <span class="card-title">销售漏斗</span>
              <div class="card-tools funnel-tools">
                <el-date-picker v-model="funnelRange" type="datetimerange" value-format="YYYY-MM-DD HH:mm:ss"
                  start-placeholder="开始" end-placeholder="结束" style="width: 280px" @change="loadFunnel" />
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
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'
import { doGet } from '../../http/httpRequest'
import { showMessage } from '../../util/message'
import { buildDonutOption, buildLineOption, buildFunnelOption } from '../../util/donutChart'

const lineRef = ref(null)
const pieRef = ref(null)
const funnelRef = ref(null)
let lineChart, pieChart, funnelChart

const piePeriodType = ref('month')
const pieMonth = ref(new Date().toISOString().slice(0, 7))
const pieYear = ref(String(new Date().getFullYear()))
const funnelRange = ref([])
const funnelUserId = ref(0)
const owners = ref([])

/** 确保 DOM 已有宽高后再初始化，避免折线图容器宽度为 0 导致空白 */
const ensureLineChart = () => {
  if (!lineRef.value) return
  if (!lineChart) {
    lineChart = echarts.init(lineRef.value)
  }
}

const initCharts = () => {
  ensureLineChart()
  if (pieRef.value && !pieChart) pieChart = echarts.init(pieRef.value)
  if (funnelRef.value && !funnelChart) funnelChart = echarts.init(funnelRef.value)
}

const resize = () => {
  lineChart?.resize()
  pieChart?.resize()
  funnelChart?.resize()
}

const renderLine = (info) => {
  if (!info) return
  ensureLineChart()
  if (!lineChart) return
  lineChart.setOption(buildLineOption(info.months, info.series), true)
  requestAnimationFrame(() => lineChart.resize())
  setTimeout(() => lineChart.resize(), 120)
}

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
      const data = (r.data.info || []).map((x) => ({ name: x.name, value: x.value }))
      pieChart?.setOption(buildDonutOption('业绩占比', data), true)
    }
  })
}

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

const loadOwners = () => {
  doGet('api/owners', {}).then((r) => {
    if (r.data.code === 200) owners.value = r.data.info || []
  })
}

onMounted(() => {
  nextTick(() => {
    initCharts()
    loadPie()
    loadFunnel()
    loadOwners()
    // 延迟一帧再拉折线数据，确保顶部卡片已完成布局（否则 ECharts 宽度常为 0）
    requestAnimationFrame(() => {
      loadLine()
    })
    window.addEventListener('resize', resize)
  })
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resize)
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
.chart-box { width: 100%; min-width: 200px; height: 360px; }
.chart-box-lg { height: 420px; min-height: 420px; }
</style>
