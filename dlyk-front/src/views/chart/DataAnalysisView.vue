<!--
  数据分析页：左右各一块「客户 / 线索」环形饼图
  交互逻辑：
    - 左侧「范围筛选」= 先缩小统计样本（可多维度、多选）
    - 右侧「按什么维度统计」= 在样本里按某一字段分组计数，画饼图
    - 某维度已在左侧用于筛选时，右侧不能再选同一维度统计（避免重复）
  对应后端：ChartStatisticsController 的 /api/chart/analysis/*
-->
<template>
  <div class="analysis-page">
    <el-row :gutter="16">
      <!-- ==================== 左侧：客户数据分析 ==================== -->
      <el-col :span="12">
        <el-card shadow="hover" class="analysis-card">
          <template #header><span class="card-title">客户数据分析</span></template>
          <el-row :gutter="12">
            <!-- 左半：范围筛选（决定统计哪些客户） -->
            <el-col :span="12" class="filter-col">
              <div class="col-title">范围筛选（可多选）</div>
              <!-- 根据后端返回的 dimensions 动态生成多个下拉框 -->
              <div v-for="dim in customerOptions.dimensions" :key="'cs-' + dim" class="filter-block">
                <div class="filter-label">{{ dimLabel(dim) }}</div>
                <el-select v-model="customerScopes[dim]" multiple collapse-tags clearable filterable
                  :placeholder="'选择' + dimLabel(dim)" @change="onCustomerScopeChange">
                  <el-option v-for="op in customerOptions.options[dim]" :key="op.value"
                    :label="op.name" :value="String(op.value)" />
                </el-select>
              </div>
            </el-col>
            <!-- 右半：选择饼图的分组维度 + 生成按钮 -->
            <el-col :span="12" class="filter-col">
              <div class="col-title">按什么维度统计</div>
              <el-radio-group v-model="customerDimension" class="dim-radio">
                <el-radio v-for="dim in customerOptions.dimensions" :key="'cd-' + dim"
                  :label="dim" :value="dim"
                  :disabled="isScopeUsed(customerScopes, dim)">
                  {{ dimLabel(dim) }}
                </el-radio>
              </el-radio-group>
              <el-button type="primary" class="gen-btn" @click="loadCustomerPie">生成客户饼图</el-button>
            </el-col>
          </el-row>
          <div ref="customerPieRef" class="chart-box" />
        </el-card>
      </el-col>

      <!-- ==================== 右侧：线索数据分析（结构与客户侧对称） ==================== -->
      <el-col :span="12">
        <el-card shadow="hover" class="analysis-card">
          <template #header><span class="card-title">线索数据分析</span></template>
          <el-row :gutter="12">
            <el-col :span="12" class="filter-col">
              <div class="col-title">范围筛选（可多选）</div>
              <div v-for="dim in clueOptions.dimensions" :key="'ls-' + dim" class="filter-block">
                <div class="filter-label">{{ clueDimLabel(dim) }}</div>
                <el-select v-model="clueScopes[dim]" multiple collapse-tags clearable filterable
                  :placeholder="'选择' + clueDimLabel(dim)" @change="onClueScopeChange">
                  <el-option v-for="op in clueOptions.options[dim]" :key="op.value"
                    :label="op.name" :value="String(op.value)" />
                </el-select>
              </div>
            </el-col>
            <el-col :span="12" class="filter-col">
              <div class="col-title">按什么维度统计</div>
              <el-radio-group v-model="clueDimension" class="dim-radio">
                <el-radio v-for="dim in clueOptions.dimensions" :key="'ld-' + dim"
                  :label="dim" :value="dim"
                  :disabled="isScopeUsed(clueScopes, dim)">
                  {{ clueDimLabel(dim) }}
                </el-radio>
              </el-radio-group>
              <el-button type="primary" class="gen-btn" @click="loadCluePie">生成线索饼图</el-button>
            </el-col>
          </el-row>
          <div ref="cluePieRef" class="chart-box" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
/**
 * 数据分析页逻辑说明：
 * 1. 页面加载时 GET 拉取「可选维度 + 各维度下拉选项」
 * 2. 用户配置 scopes（筛选）和 dimension（分组字段），点按钮 POST 请求饼图数据
 * 3. 后端 ChartStatisticsServiceImpl 在内存里 filter + groupBy，返回 [{name, value}, ...]
 * 4. 前端用 buildDonutOption 转成 ECharts 环形图
 */

import { ref, reactive, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'
import { doGet, doPost } from '../../http/httpRequest'
import { showMessage } from '../../util/message'
import { buildDonutOption } from '../../util/donutChart'

// ---------- 图表 DOM 与 ECharts 实例 ----------
const customerPieRef = ref(null)
const cluePieRef = ref(null)
let customerChart = null
let clueChart = null

/**
 * 后端返回的筛选配置结构（AnalysisFilterOptionsVO）示例：
 * {
 *   dimensions: ['createBy','source','age',...],
 *   options: {
 *     createBy: [{name:'张三', value:'2'}, ...],
 *     source: [{name:'抖音', value:'抖音'}, ...],
 *     ...
 *   }
 * }
 */
const customerOptions = ref({ dimensions: [], options: {} })
const clueOptions = ref({ dimensions: [], options: {} })

/**
 * 用户在各维度下拉框里选中的值（多选数组）
 * reactive 对象，键为维度英文名如 source、age，值为 string[]
 * 例：customerScopes.source = ['抖音团购','转介绍']
 */
const customerScopes = reactive({})
const clueScopes = reactive({})

/** 当前选中的「统计维度」字段名，对应后端 AnalysisPieQuery.dimension */
const customerDimension = ref('age')
const clueDimension = ref('source')

/** 维度英文字段 → 界面中文标签（与后端 dimension 字符串一致） */
const dimLabelMap = {
  createBy: '创建人',
  source: '来源',
  age: '年龄',
  courseType: '课程类型',
  studying: '是否正在学习',
  intentionCourse: '意向课程',
  intentionStrength: '意向强度',
  clueStatus: '线索状态',
}
const dimLabel = (d) => dimLabelMap[d] || d
const clueDimLabel = dimLabel

/**
 * 判断某个维度是否已在「范围筛选」里选了值。
 * 若已选，则右侧单选框禁用该维度（不能既按来源筛、又按来源统计）。
 */
const isScopeUsed = (scopes, dim) => {
  const v = scopes[dim]
  return v && v.length > 0
}

/**
 * 把 reactive scopes 转成 POST 请求体里的 scopes 对象。
 * 只带有值的维度，空数组不传。
 */
const buildScopesPayload = (scopes) => {
  const payload = {}
  Object.keys(scopes).forEach((k) => {
    if (scopes[k] && scopes[k].length) payload[k] = [...scopes[k]]
  })
  return payload
}

/**
 * 客户侧：若用户把当前统计维度加入了范围筛选，自动换一个未使用的维度。
 * 避免 dimension 与 scopes 里同一字段冲突。
 */
const onCustomerScopeChange = () => {
  if (isScopeUsed(customerScopes, customerDimension.value)) {
    const first = customerOptions.value.dimensions.find((d) => !isScopeUsed(customerScopes, d))
    customerDimension.value = first || 'age'
  }
}

/** 线索侧：同上 */
const onClueScopeChange = () => {
  if (isScopeUsed(clueScopes, clueDimension.value)) {
    const first = clueOptions.value.dimensions.find((d) => !isScopeUsed(clueScopes, d))
    clueDimension.value = first || 'source'
  }
}

/**
 * 请求客户饼图数据并渲染。
 * POST /api/chart/analysis/pie
 * body: { entityType:'customer', scopes:{...}, dimension:'age' }
 */
const loadCustomerPie = () => {
  doPost('/api/chart/analysis/pie', {
    entityType: 'customer',
    scopes: buildScopesPayload(customerScopes),
    dimension: customerDimension.value,
  }).then((r) => {
    if (r.data.code === 200) {
      const data = (r.data.info || []).map((x) => ({ name: x.name, value: x.value }))
      customerChart?.setOption(buildDonutOption('客户分析', data), true)
    } else showMessage(r.data.msg || '加载失败', 'error')
  })
}

/**
 * 请求线索饼图数据并渲染（接口相同，entityType 为 clue）。
 */
const loadCluePie = () => {
  doPost('/api/chart/analysis/pie', {
    entityType: 'clue',
    scopes: buildScopesPayload(clueScopes),
    dimension: clueDimension.value,
  }).then((r) => {
    if (r.data.code === 200) {
      const data = (r.data.info || []).map((x) => ({ name: x.name, value: x.value }))
      clueChart?.setOption(buildDonutOption('线索分析', data), true)
    } else showMessage(r.data.msg || '加载失败', 'error')
  })
}

/**
 * 初始化：拉取客户/线索的可选维度与下拉选项，并为每个维度在 scopes 里建空数组。
 */
const initOptions = () => {
  doGet('/api/chart/analysis/customer/options', {}).then((r) => {
    if (r.data.code === 200) {
      customerOptions.value = r.data.info
      r.data.info.dimensions.forEach((d) => { customerScopes[d] = [] })
    }
  })
  doGet('/api/chart/analysis/clue/options', {}).then((r) => {
    if (r.data.code === 200) {
      clueOptions.value = r.data.info
      r.data.info.dimensions.forEach((d) => { clueScopes[d] = [] })
    }
  })
}

onMounted(() => {
  nextTick(() => {
    if (customerPieRef.value) customerChart = echarts.init(customerPieRef.value)
    if (cluePieRef.value) clueChart = echarts.init(cluePieRef.value)
    initOptions()
    // 进入页面用默认维度各画一张图（客户按年龄、线索按来源）
    loadCustomerPie()
    loadCluePie()
    window.addEventListener('resize', () => { customerChart?.resize(); clueChart?.resize() })
  })
})

onBeforeUnmount(() => {
  customerChart?.dispose()
  clueChart?.dispose()
})
</script>

<style scoped>
.analysis-page { padding: 4px; }
.analysis-card { border-radius: 10px; min-height: 640px; }
.card-title { font-weight: 600; font-size: 15px; }
.col-title { font-size: 13px; font-weight: 600; color: #606266; margin-bottom: 10px; }
.filter-col { border-right: 1px solid #ebeef5; }
.filter-block { margin-bottom: 10px; }
.filter-label { font-size: 12px; color: #909399; margin-bottom: 4px; }
.dim-radio { display: flex; flex-direction: column; align-items: flex-start; gap: 6px; }
.gen-btn { margin-top: 12px; width: 100%; }
.chart-box { width: 100%; height: 380px; margin-top: 16px; }
</style>
