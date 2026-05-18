<template>
  <div class="clue-toolbar">
    <el-button type="primary" @click="addClue">添加线索</el-button>
    <el-button type="success" @click="batchExportExcel">批量导出(Excel)</el-button>
    <el-button type="success" @click="chooseExportExcel">选择导出(Excel)</el-button>
  </div>

  <el-table
    :data="cluePageInfo.list"
    style="width: 100%"
    :row-class-name="tableRowClassName"
    @selection-change="handSelection"
  >
    <el-table-column type="selection" width="50" />
    <el-table-column type="index" label="序号" width="55" />
    <el-table-column property="name" label="姓名" width="100" />
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
          <el-button
            type="warning"
            :icon="Switch"
            circle
            :disabled="scope.row.clueStatus === '已转客户'"
            @click="openConvert(scope.row)"
          />
        </div>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination
    background
    layout="prev, pager, next, jumper, total"
    :total="cluePageInfo.total"
    :page-size="cluePageInfo.pageSize"
    v-model:current-page="currentPage"
    @current-change="toPage"
  />

  <el-dialog v-model="convertVisible" title="线索转客户" width="720px" destroy-on-close>
    <el-form :model="convertForm" label-width="120px">
      <el-form-item label="姓名"><el-input v-model="convertForm.name" /></el-form-item>
      <el-form-item label="电话"><el-input v-model="convertForm.phone" /></el-form-item>
      <el-form-item label="年龄"><el-input-number v-model="convertForm.age" :min="1" :max="120" /></el-form-item>
      <el-form-item label="课程类型"><el-input v-model="convertForm.courseType" /></el-form-item>
      <el-form-item label="剩余课时">
        <el-input-number v-model="convertForm.remainingLessons" :min="0" :max="9999" controls-position="both" />
      </el-form-item>
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

const cluePageInfo = ref({})
const currentPage = ref(1)
const router = useRouter()
const route = useRoute()
const flushPage = inject('flush')

const convertVisible = ref(false)
const convertLoading = ref(false)
const convertForm = ref({ studying: 1 })

let idArray = []

const resolvePage = () => {
  const p = Number(route.query.page)
  return Number.isFinite(p) && p >= 1 ? Math.floor(p) : 1
}

onMounted(() => {
  currentPage.value = resolvePage()
  loadList(currentPage.value)
})

const loadList = (current) => {
  doGet('/api/clues', { current }).then((resp) => {
    if (resp.data.code === 200) {
      cluePageInfo.value = resp.data.info
    } else {
      showMessage(resp.data.msg || '加载失败', 'error')
    }
  })
}

const toPage = (current) => {
  router.replace({ path: '/dashboard/clue', query: { page: String(current) } })
  loadList(current)
}

const isWarningRow = (row) => {
  if (row.clueStatus === '已转客户') return false
  if (!row.trialClassTime) return false
  const diff = (new Date(row.trialClassTime).getTime() - Date.now()) / (86400000)
  return diff >= 0 && diff < 7
}

const tableRowClassName = ({ row }) => (isWarningRow(row) ? 'clue-row-warning' : '')

const handSelection = (rows) => {
  idArray = rows.map((r) => r.id)
}

const batchExportExcel = () => {
  download('/api/clue/exportExcel', { ids: '' }).then((resp) => saveAs(new Blob([resp.data]), '线索列表.xlsx'))
}

const chooseExportExcel = () => {
  if (!idArray.length) {
    showMessage('请选择要导出的线索', 'warning')
    return
  }
  download('/api/clue/exportExcel', { ids: idArray.join(',') }).then((resp) => saveAs(new Blob([resp.data]), '线索列表.xlsx'))
}

const addClue = () => router.push({ path: '/dashboard/clue/input', query: { page: String(currentPage.value) } })
const view = (id) => router.push({ path: '/dashboard/clue/' + id, query: { page: String(currentPage.value) } })
const edit = (id) => router.push({ path: '/dashboard/clue/edit/' + id, query: { page: String(currentPage.value) } })

const del = (id) => {
  confirmMessage('确认删除该线索吗？').then(() => {
    doDelete('/api/clue/' + id).then((resp) => {
      if (resp.data.code === 200) {
        showMessage(resp.data.msg || '删除成功', 'success')
        flushPage()
      } else {
        showMessage(resp.data.msg || '删除失败', 'error')
      }
    })
  }).catch(() => {})
}

const openConvert = (row) => {
  convertForm.value = {
    clueId: row.id,
    name: row.name,
    phone: row.phone,
    age: row.age,
    courseType: row.intentionCourse,
    source: row.source,
    remark: row.remark,
    studying: 1,
    createBy: row.createBy,
    createByName: row.createByDO?.name ?? '',
    remainingLessons: null,
    courseExpireTime: null,
    tranMoney: null,
    tranRemark: '',
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
    } else {
      showMessage(resp.data.msg || '转客户失败', 'error')
    }
  }).catch(() => {
    convertLoading.value = false
    showMessage('网络异常，转客户失败', 'error')
  })
}
</script>

<style scoped>
.clue-toolbar { margin-bottom: 12px; }
.clue-toolbar .el-button + .el-button { margin-left: 8px; }
.clue-row-actions { display: inline-flex; flex-wrap: nowrap; gap: 6px; white-space: nowrap; }
.clue-row-actions :deep(.el-button + .el-button) { margin-left: 0; }
:deep(.clue-row-warning) { background-color: #fff9e6 !important; }
:deep(.clue-row-warning:hover > td) { background-color: #fff3cc !important; }
</style>
