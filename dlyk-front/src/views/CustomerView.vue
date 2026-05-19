<template>
  <el-card class="search-card" shadow="never">
    <el-form :model="search" label-width="76px" label-position="right" class="list-search-form customer-search-form" @submit.prevent="doSearch">
      <!-- 第一行：姓名、年龄、电话、备注、是否学习 -->
      <el-row :gutter="16" class="search-row">
        <el-col :span="4" class="col-xs">
          <el-form-item label="姓名"><el-input v-model="search.name" clearable placeholder="姓名" /></el-form-item>
        </el-col>
        <el-col :span="4" class="col-xs">
          <el-form-item label="年龄">
            <SearchNumberRange class="range-tight" v-model:op="search.ageOp" v-model:value="search.ageValue" v-model:min="search.ageMin" v-model:max="search.ageMax" />
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="电话"><el-input v-model="search.phone" clearable placeholder="电话" /></el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="备注"><el-input v-model="search.remark" clearable placeholder="备注" /></el-form-item>
        </el-col>
        <el-col :span="4" class="col-xs">
          <el-form-item label="是否学习">
            <el-select v-model="search.studying" clearable placeholder="全部">
              <el-option label="是" :value="1" /><el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第二行：课程类型、到期时间、剩余课时 -->
      <el-row :gutter="16" class="search-row">
        <el-col :span="8">
          <el-form-item label="课程类型"><el-input v-model="search.courseType" clearable placeholder="课程类型" /></el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="到期时间">
            <SearchDateField compact v-model:start="search.courseExpireTimeStart" v-model:end="search.courseExpireTimeEnd" v-model:point="search.courseExpireTimePoint" v-model:pointType="search.courseExpireTimePointType" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="剩余课时">
            <SearchNumberRange
              class="range-inline"
              v-model:op="search.remainingLessonsOp"
              v-model:value="search.remainingLessonsValue"
              v-model:min="search.remainingLessonsMin"
              v-model:max="search.remainingLessonsMax"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第三行：创建人、创建时间、来源 -->
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
          <el-form-item label="来源"><el-input v-model="search.source" clearable placeholder="来源" /></el-form-item>
        </el-col>
      </el-row>
      <el-row class="search-row-actions">
        <el-col :span="24"><SearchFormActions @search="doSearch" @reset="resetSearch" /></el-col>
      </el-row>
    </el-form>
  </el-card>

  <div class="customer-toolbar">
    <el-button type="primary" @click="addCustomer">添加客户</el-button>
    <el-button type="success" @click="batchExportExcel">批量导出(Excel)</el-button>
    <el-button type="success" @click="chooseExportExcel">选择导出(Excel)</el-button>
  </div>

  <el-table :data="customerPageInfo.list" style="width: 100%" :row-class-name="tableRowClassName" @selection-change="handSelection">
    <el-table-column type="selection" width="50" />
    <el-table-column type="index" label="序号" width="55" />
    <el-table-column property="name" label="姓名" width="100" />
    <el-table-column property="phone" label="电话" width="120" />
    <el-table-column property="age" label="年龄" width="70" />
    <el-table-column property="courseType" label="课程类型" min-width="120" show-overflow-tooltip />
    <el-table-column property="remainingLessons" label="剩余课时" width="90" />
    <el-table-column property="courseExpireTime" label="课程到期时间" width="170" show-overflow-tooltip />
    <el-table-column property="source" label="来源" width="100" show-overflow-tooltip />
    <el-table-column label="是否正在学习" width="120">
      <template #default="scope">{{ studyingLabel(scope.row.studying) }}</template>
    </el-table-column>
    <el-table-column property="createByDO.name" label="创建人" width="100" />
    <el-table-column property="createTime" label="创建时间" width="170" show-overflow-tooltip />
    <el-table-column label="操作" width="200" align="center" fixed="right" class-name="customer-op-column">
      <template #default="scope">
        <div class="customer-row-actions">
          <el-button type="success" :icon="View" circle @click="view(scope.row.id)" />
          <el-button type="primary" :icon="Edit" circle @click="edit(scope.row.id)" />
          <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)" />
          <el-button type="warning" :icon="CirclePlus" circle @click="openAddTran(scope.row)" />
        </div>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination background layout="prev, pager, next, jumper, total" :total="customerPageInfo.total"
    :page-size="customerPageInfo.pageSize" v-model:current-page="currentPage" @current-change="toPage" />

  <el-dialog v-model="tranVisible" title="添加交易" width="520px" destroy-on-close>
    <el-form :model="tranForm" label-width="100px">
      <el-form-item label="学员姓名"><el-input v-model="tranForm.studentName" disabled /></el-form-item>
      <el-form-item label="交易金额"><el-input-number v-model="tranForm.money" :min="0" :precision="2" style="width:100%" /></el-form-item>
      <el-form-item label="课程类型"><el-input v-model="tranForm.courseType" /></el-form-item>
      <el-form-item label="成交时间">
        <el-date-picker v-model="tranForm.dealTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" />
      </el-form-item>
      <el-form-item label="交易备注"><el-input v-model="tranForm.tranRemark" type="textarea" :rows="3" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="tranVisible=false">取消</el-button>
      <el-button type="primary" :loading="tranLoading" @click="submitTran">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { View, Edit, Delete, CirclePlus } from '@element-plus/icons-vue'
import { ref, onMounted, inject } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { doGet, doDelete, doPost, download } from '../http/httpRequest'
import { showMessage, confirmMessage } from '../util/message'
import { saveAs } from 'file-saver'
import { buildSearchParams } from '../util/searchParams'
import SearchNumberRange from '../components/search/SearchNumberRange.vue'
import SearchDateField from '../components/search/SearchDateField.vue'
import SearchFormActions from '../components/search/SearchFormActions.vue'

const defaultSearch = () => ({
  name: '', phone: '', courseType: '', source: '', remark: '', studying: null, createBy: null,
  createTimeStart: null, createTimeEnd: null, createTimePoint: null, createTimePointType: null,
  courseExpireTimeStart: null, courseExpireTimeEnd: null, courseExpireTimePoint: null, courseExpireTimePointType: null,
  ageOp: null, ageValue: null, ageMin: null, ageMax: null,
  remainingLessonsOp: null, remainingLessonsValue: null, remainingLessonsMin: null, remainingLessonsMax: null,
})

const search = ref(defaultSearch())
const customerPageInfo = ref({})
const currentPage = ref(1)
const ownerOptions = ref([])
const router = useRouter()
const route = useRoute()
const flushPage = inject('flush')

const tranVisible = ref(false)
const tranLoading = ref(false)
const tranForm = ref({})
let idArray = []

const loadOwners = (visible) => {
  if (visible && ownerOptions.value.length === 0) {
    doGet('api/owners', {}).then((r) => { if (r.data.code === 200) ownerOptions.value = r.data.info })
  }
}

const resolvePage = () => {
  const p = Number(route.query.page)
  return Number.isFinite(p) && p >= 1 ? Math.floor(p) : 1
}

onMounted(() => {
  currentPage.value = resolvePage()
  loadCustomerList(currentPage.value)
})

const loadCustomerList = (current) => {
  const params = buildSearchParams(search.value, { current })
  doGet('/api/customers', params).then((resp) => {
    if (resp.data.code === 200) {
      customerPageInfo.value = resp.data.info
    } else {
      showMessage(resp.data.msg || '数据加载失败', 'error')
    }
  })
}

const doSearch = () => { currentPage.value = 1; loadCustomerList(1) }
const resetSearch = () => { search.value = defaultSearch(); doSearch() }
const toPage = (current) => {
  router.replace({ path: '/dashboard/customer', query: { page: String(current) } })
  loadCustomerList(current)
}

const studyingLabel = (s) => (s === 1 ? '是' : s === 0 ? '否' : '')
const isWarningRow = (row) => {
  if (row.studying !== 1) return false
  if (row.remainingLessons != null && row.remainingLessons > 0 && row.remainingLessons <= 3) return true
  if (!row.courseExpireTime) return false
  return (new Date(row.courseExpireTime).getTime() - Date.now()) / 86400000 < 15
}
const tableRowClassName = ({ row }) => (isWarningRow(row) ? 'customer-row-warning' : '')

const handSelection = (item) => { idArray = item.map((x) => x.id) }
const batchExportExcel = () => download('/api/exportExcel', { ids: '' }).then((r) => saveAs(new Blob([r.data]), '学员列表.xlsx'))
const chooseExportExcel = () => {
  if (!idArray.length) return showMessage('请选择要导出的客户', 'warning')
  download('/api/exportExcel', { ids: idArray.join(',') }).then((r) => saveAs(new Blob([r.data]), '学员列表.xlsx'))
}
const addCustomer = () => router.push({ path: '/dashboard/customer/input', query: { page: String(currentPage.value) } })
const view = (id) => router.push({ path: '/dashboard/customer/' + id, query: { page: String(currentPage.value) } })
const edit = (id) => router.push({ path: '/dashboard/customer/edit/' + id, query: { page: String(currentPage.value) } })
const del = (id) => {
  confirmMessage('确认删除该客户吗？').then(() => {
    doDelete('/api/customer/' + id).then((resp) => {
      if (resp.data.code === 200) { showMessage('删除成功', 'success'); flushPage() }
      else showMessage(resp.data.msg || '删除失败', 'error')
    })
  }).catch(() => {})
}

const openAddTran = (row) => {
  tranForm.value = {
    customerId: row.id,
    studentName: row.name,
    courseType: row.courseType,
    money: null,
    dealTime: null,
    tranRemark: '',
  }
  tranVisible.value = true
}

const submitTran = () => {
  if (tranForm.value.money == null) return showMessage('请填写交易金额', 'warning')
  tranLoading.value = true
  const fd = new FormData()
  fd.append('customerId', tranForm.value.customerId)
  fd.append('studentName', tranForm.value.studentName)
  fd.append('money', tranForm.value.money)
  if (tranForm.value.courseType) fd.append('courseType', tranForm.value.courseType)
  if (tranForm.value.dealTime) fd.append('dealTime', tranForm.value.dealTime)
  if (tranForm.value.tranRemark) fd.append('tranRemark', tranForm.value.tranRemark)
  doPost('/api/tran/add', fd).then((r) => {
    tranLoading.value = false
    if (r.data.code === 200) { showMessage('交易添加成功', 'success'); tranVisible.value = false }
    else showMessage(r.data.msg || '添加失败', 'error')
  }).catch(() => { tranLoading.value = false })
}
</script>

<style scoped>
.search-card { margin-bottom: 12px; }
/* 搜索区统一左对齐、行距一致 */
.customer-search-form :deep(.el-form-item) {
  margin-bottom: 0;
  width: 100%;
}
.customer-search-form :deep(.el-form-item__label) {
  width: 76px !important;
  padding-right: 8px;
  justify-content: flex-end;
}
.customer-search-form :deep(.el-form-item__content) {
  flex: 1;
  min-width: 0;
}
.customer-search-form .search-row {
  margin-bottom: 12px;
}
.customer-search-form .search-row-actions {
  margin-bottom: 0;
  padding-top: 4px;
}
.customer-search-form :deep(.el-input),
.customer-search-form :deep(.el-select) {
  width: 100%;
}
/* 窄列：年龄、姓名、是否学习 */
.customer-search-form :deep(.col-xs .search-number-range) {
  flex-wrap: nowrap;
  gap: 4px;
}
.customer-search-form :deep(.range-tight .op-select) {
  width: 68px !important;
}
/* 年龄「数值」框：略宽一些，完整显示「数值」两字 */
.customer-search-form :deep(.range-tight .num-input) {
  width: 84px !important;
  min-width: 84px;
  flex: 0 0 84px;
}
.customer-search-form :deep(.range-tight .num-input .el-input__inner) {
  padding-left: 8px;
  padding-right: 8px;
}
.customer-search-form :deep(.col-xs .el-select .el-select__wrapper) {
  padding-left: 8px;
  padding-right: 8px;
}
/* 剩余课时：条件与数值同一行 */
.customer-search-form :deep(.range-inline.search-number-range) {
  flex-wrap: nowrap;
  align-items: center;
  gap: 8px;
}
.customer-search-form :deep(.range-inline .op-select) {
  width: 88px !important;
  flex-shrink: 0;
}
.customer-search-form :deep(.range-inline .num-input) {
  width: 84px !important;
  min-width: 84px;
  flex: 0 0 84px;
}
.customer-search-form :deep(.range-inline .num-input .el-input__inner) {
  padding-left: 8px;
  padding-right: 8px;
}
.customer-toolbar { margin-bottom: 12px; }
.customer-row-actions { display: inline-flex; flex-wrap: nowrap; gap: 6px; }
.customer-row-actions :deep(.el-button + .el-button) { margin-left: 0; }
:deep(.customer-row-warning) { background-color: #fde8e8 !important; }
:deep(.customer-op-column .cell) { overflow: visible; white-space: nowrap; }
</style>
