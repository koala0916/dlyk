<template>
  <el-card class="search-card" shadow="never">
    <el-form :model="search" label-width="76px" label-position="right" class="list-search-form clue-search-form" @submit.prevent="doSearch">
      <!-- 第一行：姓名、年龄、电话、备注、线索状态 -->
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
        <el-col :span="4" class="col-xs col-status">
          <el-form-item label="线索状态">
            <el-select v-model="search.clueStatus" clearable placeholder="全部">
              <el-option label="未联系" value="未联系" />
              <el-option label="已联系" value="已联系" />
              <el-option label="已转客户" value="已转客户" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第二行：意向课程、意向强度、体验课时间（体验课列略宽，与意向强度间距更小） -->
      <el-row :gutter="8" class="search-row search-row-course">
        <el-col :span="7">
          <el-form-item label="意向课程"><el-input v-model="search.intentionCourse" clearable placeholder="意向课程" /></el-form-item>
        </el-col>
        <el-col :span="7" class="col-strength">
          <el-form-item label="意向强度">
            <SearchNumberRange
              class="range-inline"
              v-model:op="search.intentionStrengthOp"
              v-model:value="search.intentionStrengthValue"
              v-model:min="search.intentionStrengthMin"
              v-model:max="search.intentionStrengthMax"
            />
          </el-form-item>
        </el-col>
        <el-col :span="10" class="col-trial-time">
          <el-form-item label="体验课时间" label-width="88px">
            <SearchDateField compact v-model:start="search.trialClassTimeStart" v-model:end="search.trialClassTimeEnd" v-model:point="search.trialClassTimePoint" v-model:pointType="search.trialClassTimePointType" />
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

  <div class="clue-toolbar">
    <el-button type="primary" @click="addClue">添加线索</el-button>
    <el-button type="success" @click="batchExportExcel">批量导出(Excel)</el-button>
    <el-button type="success" @click="chooseExportExcel">选择导出(Excel)</el-button>
  </div>

  <el-table :data="cluePageInfo.list" style="width: 100%" :row-class-name="tableRowClassName" @selection-change="handSelection">
    <el-table-column type="selection" width="50" />
    <el-table-column type="index" label="序号" width="55" />
    <el-table-column label="姓名" width="100">
      <template #default="scope">
        <el-link v-if="scope.row.customerId" type="primary" @click="goCustomer(scope.row.customerId)">{{ scope.row.name }}</el-link>
        <span v-else>{{ scope.row.name }}</span>
      </template>
    </el-table-column>
    <el-table-column property="phone" label="电话" width="120" />
    <el-table-column property="age" label="年龄" width="70" />
    <el-table-column property="intentionCourse" label="意向课程" min-width="120" show-overflow-tooltip />
    <el-table-column label="意向强度" width="100">
      <template #default="scope">{{ scope.row.intentionStrength ?? '' }}</template>
    </el-table-column>
    <el-table-column property="source" label="来源" width="100" show-overflow-tooltip />
    <el-table-column property="clueStatus" label="线索状态" width="100" />
    <el-table-column property="trialClassTime" label="体验课时间" width="170" show-overflow-tooltip />
    <el-table-column property="remark" label="备注" min-width="120" show-overflow-tooltip />
    <el-table-column property="createByDO.name" label="创建人" width="100" />
    <el-table-column property="createTime" label="创建时间" width="170" show-overflow-tooltip />
    <el-table-column label="操作" width="200" align="center" fixed="right" class-name="clue-op-column">
      <template #default="scope">
        <div class="clue-row-actions">
          <el-button type="success" :icon="View" circle @click="view(scope.row.id)" />
          <el-button type="primary" :icon="Edit" circle @click="edit(scope.row.id)" />
          <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)" />
          <el-button type="warning" :icon="Switch" circle :disabled="scope.row.clueStatus === '已转客户'" @click="openConvert(scope.row)" />
        </div>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination background layout="prev, pager, next, jumper, total" :total="cluePageInfo.total"
    :page-size="cluePageInfo.pageSize" v-model:current-page="currentPage" @current-change="toPage" />

  <el-dialog v-model="convertVisible" title="线索转客户" width="720px" destroy-on-close>
    <el-form :model="convertForm" label-width="120px">
      <el-form-item label="姓名"><el-input v-model="convertForm.name" /></el-form-item>
      <el-form-item label="电话"><el-input v-model="convertForm.phone" /></el-form-item>
      <el-form-item label="年龄"><el-input-number v-model="convertForm.age" :min="1" :max="120" /></el-form-item>
      <el-form-item label="课程类型"><el-input v-model="convertForm.courseType" /></el-form-item>
      <el-form-item label="剩余课时"><el-input-number v-model="convertForm.remainingLessons" :min="0" :max="9999" controls-position="both" /></el-form-item>
      <el-form-item label="课程到期时间">
        <el-date-picker v-model="convertForm.courseExpireTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
      </el-form-item>
      <el-form-item label="备注"><el-input v-model="convertForm.remark" type="textarea" :rows="3" /></el-form-item>
      <el-form-item label="来源"><el-input v-model="convertForm.source" /></el-form-item>
      <el-form-item label="是否正在学习">
        <el-select v-model="convertForm.studying"><el-option label="是" :value="1" /><el-option label="否" :value="0" /></el-select>
      </el-form-item>
      <el-form-item label="创建人"><el-input v-model="convertForm.createByName" disabled /></el-form-item>
      <el-form-item label="交易金额"><el-input-number v-model="convertForm.tranMoney" :min="0" :precision="2" style="width: 100%" /></el-form-item>
      <el-form-item label="交易备注"><el-input v-model="convertForm.tranRemark" type="textarea" :rows="2" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="convertVisible = false">取消</el-button>
      <el-button type="primary" :loading="convertLoading" @click="submitConvert">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { View, Edit, Delete, Switch } from '@element-plus/icons-vue'
import { ref, onMounted, inject } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { doGet, doDelete, doPost, download } from '../http/httpRequest'
import { showMessage, confirmMessage } from '../util/message'
import { saveAs } from 'file-saver'
import { buildSearchParams } from '../util/searchParams'
import SearchNumberRange from '../components/search/SearchNumberRange.vue'
import SearchDateField from '../components/search/SearchDateField.vue'
import SearchFormActions from '../components/search/SearchFormActions.vue'
import { pushWithReturn } from '../util/navReturn'

const defaultSearch = () => ({
  name: '', phone: '', intentionCourse: '', source: '', remark: '', clueStatus: null, createBy: null,
  createTimeStart: null, createTimeEnd: null, createTimePoint: null, createTimePointType: null,
  trialClassTimeStart: null, trialClassTimeEnd: null, trialClassTimePoint: null, trialClassTimePointType: null,
  ageOp: null, ageValue: null, ageMin: null, ageMax: null,
  intentionStrengthOp: null, intentionStrengthValue: null, intentionStrengthMin: null, intentionStrengthMax: null,
})

const search = ref(defaultSearch())
const cluePageInfo = ref({})
const currentPage = ref(1)
const ownerOptions = ref([])
const router = useRouter()
const route = useRoute()
const flushPage = inject('flush')
const convertVisible = ref(false)
const convertLoading = ref(false)
const convertForm = ref({ studying: 1 })
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
  loadList(currentPage.value)
})

const loadList = (current) => {
  const params = buildSearchParams(search.value, { current })
  doGet('/api/clues', params).then((resp) => {
    if (resp.data.code === 200) cluePageInfo.value = resp.data.info
    else showMessage(resp.data.msg || '加载失败', 'error')
  }).catch(() => showMessage('线索列表加载失败，请确认后端已启动', 'error'))
}

const doSearch = () => { currentPage.value = 1; loadList(1) }
const resetSearch = () => { search.value = defaultSearch(); doSearch() }

const toPage = (current) => {
  router.replace({ path: '/dashboard/clue', query: { page: String(current) } })
  loadList(current)
}

const goCustomer = (customerId) => {
  pushWithReturn(router, route, '/dashboard/customer/' + customerId)
}

const isWarningRow = (row) => {
  if (row.clueStatus === '已转客户') return false
  if (!row.trialClassTime) return false
  const diff = (new Date(row.trialClassTime).getTime() - Date.now()) / 86400000
  return diff >= 0 && diff < 7
}
const tableRowClassName = ({ row }) => (isWarningRow(row) ? 'clue-row-warning' : '')

const handSelection = (rows) => { idArray = rows.map((r) => r.id) }
const batchExportExcel = () => download('/api/clue/exportExcel', { ids: '' }).then((r) => saveAs(new Blob([r.data]), '线索列表.xlsx'))
const chooseExportExcel = () => {
  if (!idArray.length) return showMessage('请选择要导出的线索', 'warning')
  download('/api/clue/exportExcel', { ids: idArray.join(',') }).then((r) => saveAs(new Blob([r.data]), '线索列表.xlsx'))
}

const addClue = () => router.push({ path: '/dashboard/clue/input', query: { page: String(currentPage.value) } })
const view = (id) => pushWithReturn(router, route, '/dashboard/clue/' + id, { page: String(currentPage.value) })
const edit = (id) => router.push({ path: '/dashboard/clue/edit/' + id, query: { page: String(currentPage.value) } })

const del = (id) => {
  confirmMessage('确认删除该线索吗？').then(() => {
    doDelete('/api/clue/' + id).then((resp) => {
      if (resp.data.code === 200) { showMessage(resp.data.msg || '删除成功', 'success'); flushPage() }
      else showMessage(resp.data.msg || '删除失败', 'error')
    })
  }).catch(() => {})
}

const openConvert = (row) => {
  convertForm.value = {
    clueId: row.id, name: row.name, phone: row.phone, age: row.age,
    courseType: row.intentionCourse, source: row.source, remark: row.remark,
    studying: 1, createBy: row.createBy, createByName: row.createByDO?.name ?? '',
    remainingLessons: null, courseExpireTime: null, tranMoney: null, tranRemark: '',
  }
  convertVisible.value = true
}

const submitConvert = () => {
  convertLoading.value = true
  const payload = { ...convertForm.value }
  delete payload.createByName
  doPost('/api/clue/convert', payload).then((resp) => {
    convertLoading.value = false
    if (resp.data.code === 200) {
      showMessage(resp.data.msg || '转客户成功', 'success')
      convertVisible.value = false
      loadList(currentPage.value)
    } else showMessage(resp.data.msg || '转客户失败', 'error')
  }).catch(() => { convertLoading.value = false; showMessage('网络异常，转客户失败', 'error') })
}
</script>

<style scoped>
.search-card { margin-bottom: 12px; }
/* 搜索区样式与客户管理保持一致 */
.clue-search-form :deep(.el-form-item) {
  margin-bottom: 0;
  width: 100%;
}
.clue-search-form :deep(.el-form-item__label) {
  width: 76px !important;
  padding-right: 8px;
  justify-content: flex-end;
}
.clue-search-form :deep(.el-form-item__content) {
  flex: 1;
  min-width: 0;
}
.clue-search-form .search-row {
  margin-bottom: 12px;
}
.clue-search-form .search-row-actions {
  margin-bottom: 0;
  padding-top: 4px;
}
.clue-search-form :deep(.el-input),
.clue-search-form :deep(.el-select) {
  width: 100%;
}
.clue-search-form :deep(.col-xs .search-number-range) {
  flex-wrap: nowrap;
  gap: 4px;
}
.clue-search-form :deep(.range-tight .op-select) {
  width: 68px !important;
}
.clue-search-form :deep(.range-tight .num-input) {
  width: 84px !important;
  min-width: 84px;
  flex: 0 0 84px;
}
.clue-search-form :deep(.range-tight .num-input .el-input__inner) {
  padding-left: 8px;
  padding-right: 8px;
}
.clue-search-form :deep(.col-xs .el-select .el-select__wrapper) {
  padding-left: 6px;
  padding-right: 6px;
}
.clue-search-form :deep(.range-inline.search-number-range) {
  flex-wrap: nowrap;
  align-items: center;
  gap: 8px;
}
.clue-search-form :deep(.range-inline .op-select) {
  width: 88px !important;
  flex-shrink: 0;
}
.clue-search-form :deep(.range-inline .num-input) {
  width: 84px !important;
  min-width: 84px;
  flex: 0 0 84px;
}
.clue-search-form :deep(.range-inline .num-input .el-input__inner) {
  padding-left: 8px;
  padding-right: 8px;
}
/* 第二行：体验课时间标签单行显示，并靠近左侧意向强度 */
.clue-search-form .search-row-course :deep(.col-strength) {
  padding-right: 2px !important;
}
.clue-search-form .search-row-course :deep(.col-trial-time) {
  padding-left: 2px !important;
}
.clue-search-form :deep(.col-trial-time .el-form-item__label) {
  width: 88px !important;
  white-space: nowrap;
  line-height: 32px;
}
.clue-toolbar { margin-bottom: 12px; }
.clue-row-actions { display: inline-flex; flex-wrap: nowrap; gap: 6px; white-space: nowrap; }
.clue-row-actions :deep(.el-button + .el-button) { margin-left: 0; }
:deep(.clue-row-warning) { background-color: #fff9e6 !important; }
:deep(.clue-row-warning:hover > td) { background-color: #fff3cc !important; }
</style>
