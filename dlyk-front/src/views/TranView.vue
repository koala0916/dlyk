<template>
  <el-card class="search-card" shadow="never">
    <el-form :model="search" label-width="76px" label-position="right" class="list-search-form tran-search-form" @submit.prevent="doSearch">
      <el-row v-if="search.customerId" :gutter="16" class="search-row">
        <el-col :span="24">
          <el-alert type="info" :closable="false" show-icon title="当前按客户筛选交易，可点击重置清除" />
        </el-col>
      </el-row>
      <!-- 第一行：流水号、学生姓名、课程类型、交易金额 -->
      <el-row :gutter="16" class="search-row">
        <el-col :span="6">
          <el-form-item label="流水号"><el-input v-model="search.tranNo" clearable placeholder="流水号" /></el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="学生姓名"><el-input v-model="search.studentName" clearable placeholder="学生姓名" /></el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="课程类型"><el-input v-model="search.courseType" clearable placeholder="课程类型" /></el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="交易金额">
            <SearchNumberRange
              class="range-inline"
              v-model:op="search.moneyOp"
              v-model:value="search.moneyValue"
              v-model:min="search.moneyMin"
              v-model:max="search.moneyMax"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第二行：创建人、创建时间、成交时间 -->
      <el-row :gutter="16" class="search-row">
        <el-col :span="6">
          <el-form-item label="创建人">
            <el-select v-model="search.createBy" clearable filterable placeholder="选择" @visible-change="loadOwners">
              <el-option v-for="u in ownerOptions" :key="u.id" :label="u.name" :value="u.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="创建时间">
            <SearchDateField compact v-model:start="search.createTimeStart" v-model:end="search.createTimeEnd" v-model:point="search.createTimePoint" v-model:pointType="search.createTimePointType" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="成交时间" label-width="76px">
            <SearchDateField compact v-model:start="search.dealTimeStart" v-model:end="search.dealTimeEnd" v-model:point="search.dealTimePoint" v-model:pointType="search.dealTimePointType" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row class="search-row-actions">
        <el-col :span="24"><SearchFormActions @search="doSearch" @reset="resetSearch" /></el-col>
      </el-row>
    </el-form>
  </el-card>

  <div class="tran-toolbar">
    <el-button v-if="showReturnBtn" plain @click="goBackToPrev">返回上一页</el-button>
    <el-button type="primary" @click="addTran">添加交易</el-button>
    <el-button type="success" @click="batchExport">批量导出(Excel)</el-button>
    <el-button type="success" @click="chooseExport">选择导出(Excel)</el-button>
  </div>

  <el-table :data="pageInfo.list" style="width: 100%" @selection-change="onSelect">
    <el-table-column type="selection" width="50" />
    <el-table-column type="index" label="序号" width="55" />
    <el-table-column property="tranNo" label="流水号" min-width="140" show-overflow-tooltip />
    <el-table-column label="姓名" width="100">
      <template #default="scope">
        <el-link v-if="scope.row.customerId" type="primary" @click="goCustomer(scope.row.customerId)">{{ scope.row.studentName }}</el-link>
        <span v-else>{{ scope.row.studentName }}</span>
      </template>
    </el-table-column>
    <el-table-column property="money" label="交易金额" width="110" />
    <el-table-column property="courseType" label="课程类型" min-width="120" show-overflow-tooltip />
    <el-table-column property="dealTime" label="成交时间" width="170" />
    <el-table-column property="tranRemark" label="交易备注" min-width="120" show-overflow-tooltip />
    <el-table-column property="createByDO.name" label="创建人" width="100" />
    <el-table-column label="操作" width="160" align="center" fixed="right">
      <template #default="scope">
        <div class="tran-row-actions">
          <el-button type="success" :icon="View" circle @click="view(scope.row.id)" />
          <el-button type="primary" :icon="Edit" circle @click="edit(scope.row.id)" />
          <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)" />
        </div>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination background layout="prev, pager, next, jumper, total" :total="pageInfo.total"
    :page-size="pageInfo.pageSize" v-model:current-page="currentPage" @current-change="toPage" />
</template>

<script setup>
import { View, Edit, Delete } from '@element-plus/icons-vue'
import { ref, onMounted, inject, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { doGet, doDelete, download } from '../http/httpRequest'
import { showMessage, confirmMessage } from '../util/message'
import { saveAs } from 'file-saver'
import { buildSearchParams } from '../util/searchParams'
import SearchNumberRange from '../components/search/SearchNumberRange.vue'
import SearchDateField from '../components/search/SearchDateField.vue'
import SearchFormActions from '../components/search/SearchFormActions.vue'
import { pushWithReturn, goBackFromRoute, hasReturnFrom, NAV_FROM_KEY } from '../util/navReturn'

const defaultSearch = () => ({
  tranNo: '', studentName: '', courseType: '', tranRemark: '', createBy: null, customerId: null,
  dealTimeStart: null, dealTimeEnd: null, dealTimePoint: null, dealTimePointType: null,
  createTimeStart: null, createTimeEnd: null, createTimePoint: null, createTimePointType: null,
  moneyOp: null, moneyValue: null, moneyMin: null, moneyMax: null,
})

const search = ref(defaultSearch())
const pageInfo = ref({})
const currentPage = ref(1)
const ownerOptions = ref([])
const router = useRouter()
const route = useRoute()
const flushPage = inject('flush')
let ids = []

const loadOwners = (visible) => {
  if (visible && ownerOptions.value.length === 0) {
    doGet('api/owners', {}).then((r) => { if (r.data.code === 200) ownerOptions.value = r.data.info })
  }
}

onMounted(() => {
  const p = Number(route.query.page)
  currentPage.value = Number.isFinite(p) && p >= 1 ? p : 1
  const cid = route.query.customerId
  if (cid != null && String(cid) !== '') {
    search.value.customerId = Number(cid)
  }
  load(currentPage.value)
})

const showReturnBtn = computed(() => hasReturnFrom(route))

const buildRouteQuery = (current) => {
  const q = { page: String(current) }
  if (search.value.customerId != null && search.value.customerId !== '') {
    q.customerId = String(search.value.customerId)
  }
  if (route.query[NAV_FROM_KEY]) {
    q[NAV_FROM_KEY] = String(route.query[NAV_FROM_KEY])
  }
  return q
}

const load = (current) => {
  const params = buildSearchParams(search.value, { current })
  doGet('/api/trans', params).then((r) => {
    if (r.data.code === 200) pageInfo.value = r.data.info
    else showMessage(r.data.msg || '加载失败', 'error')
  }).catch(() => showMessage('交易列表加载失败，请确认后端已启动', 'error'))
}

const doSearch = () => { currentPage.value = 1; load(1) }
const resetSearch = () => {
  search.value = defaultSearch()
  router.replace({ path: '/dashboard/tran', query: { page: '1' } })
  currentPage.value = 1
  load(1)
}

const toPage = (c) => {
  router.replace({ path: '/dashboard/tran', query: buildRouteQuery(c) })
  load(c)
}

const goCustomer = (customerId) => {
  pushWithReturn(router, route, '/dashboard/customer/' + customerId)
}

const onSelect = (rows) => { ids = rows.map((x) => x.id) }
const batchExport = () => download('/api/tran/exportExcel', { ids: '' }).then((r) => saveAs(new Blob([r.data]), '交易列表.xlsx'))
const chooseExport = () => {
  if (!ids.length) return showMessage('请选择要导出的交易', 'warning')
  download('/api/tran/exportExcel', { ids: ids.join(',') }).then((r) => saveAs(new Blob([r.data]), '交易列表.xlsx'))
}

const addTran = () => router.push({ path: '/dashboard/tran/input', query: { page: String(currentPage.value) } })
const view = (id) => pushWithReturn(router, route, '/dashboard/tran/' + id, { page: String(currentPage.value) })
const edit = (id) => router.push({ path: '/dashboard/tran/edit/' + id, query: { page: String(currentPage.value) } })

const del = (id) => {
  confirmMessage('确认删除该交易吗？').then(() => {
    doDelete('/api/tran/' + id).then((r) => {
      if (r.data.code === 200) { showMessage(r.data.msg || '删除成功', 'success'); flushPage() }
      else showMessage(r.data.msg || '删除失败', 'error')
    })
  }).catch(() => {})
}
</script>

<style scoped>
.search-card { margin-bottom: 12px; }
/* 搜索区样式与客户管理保持一致 */
.tran-search-form :deep(.el-form-item) {
  margin-bottom: 0;
  width: 100%;
}
.tran-search-form :deep(.el-form-item__label) {
  width: 76px !important;
  padding-right: 8px;
  justify-content: flex-end;
}
.tran-search-form :deep(.el-form-item__content) {
  flex: 1;
  min-width: 0;
}
.tran-search-form .search-row {
  margin-bottom: 12px;
}
.tran-search-form .search-row-actions {
  margin-bottom: 0;
  padding-top: 4px;
}
.tran-search-form :deep(.el-input),
.tran-search-form :deep(.el-select) {
  width: 100%;
}
.tran-search-form :deep(.range-inline.search-number-range) {
  flex-wrap: nowrap;
  align-items: center;
  gap: 8px;
}
.tran-search-form :deep(.range-inline .op-select) {
  width: 88px !important;
  flex-shrink: 0;
}
.tran-search-form :deep(.range-inline .num-input) {
  width: 84px !important;
  min-width: 84px;
  flex: 0 0 84px;
}
.tran-search-form :deep(.range-inline .num-input .el-input__inner) {
  padding-left: 8px;
  padding-right: 8px;
}
.tran-toolbar { margin-bottom: 12px; }
.tran-row-actions { display: inline-flex; gap: 6px; flex-wrap: nowrap; }
.tran-row-actions :deep(.el-button + .el-button) { margin-left: 0; }
</style>
