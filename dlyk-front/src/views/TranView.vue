<template>
  <div class="tran-toolbar">
    <el-button type="primary" @click="addTran">添加交易</el-button>
    <el-button type="success" @click="batchExport">批量导出(Excel)</el-button>
    <el-button type="success" @click="chooseExport">选择导出(Excel)</el-button>
  </div>
  <el-table :data="pageInfo.list" style="width: 100%" @selection-change="onSelect">
    <el-table-column type="selection" width="50" />
    <el-table-column type="index" label="序号" width="55" />
    <el-table-column property="tranNo" label="流水号" min-width="140" show-overflow-tooltip />
    <el-table-column property="studentName" label="姓名" width="100" />
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
import { ref, onMounted, inject } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { doGet, doDelete, download } from '../http/httpRequest'
import { showMessage, confirmMessage } from '../util/message'
import { saveAs } from 'file-saver'

const pageInfo = ref({})
const currentPage = ref(1)
const router = useRouter()
const route = useRoute()
const flushPage = inject('flush')
let ids = []

onMounted(() => {
  const p = Number(route.query.page)
  currentPage.value = Number.isFinite(p) && p >= 1 ? p : 1
  load(currentPage.value)
})

const load = (current) => {
  doGet('/api/trans', { current }).then((r) => {
    if (r.data.code === 200) pageInfo.value = r.data.info
  })
}

const toPage = (c) => {
  router.replace({ path: '/dashboard/tran', query: { page: String(c) } })
  load(c)
}

const onSelect = (rows) => { ids = rows.map((x) => x.id) }
const batchExport = () => download('/api/tran/exportExcel', { ids: '' }).then((r) => saveAs(new Blob([r.data]), '交易列表.xlsx'))
const chooseExport = () => {
  if (!ids.length) return showMessage('请选择要导出的交易', 'warning')
  download('/api/tran/exportExcel', { ids: ids.join(',') }).then((r) => saveAs(new Blob([r.data]), '交易列表.xlsx'))
}
const addTran = () => router.push({ path: '/dashboard/tran/input', query: { page: String(currentPage.value) } })
const view = (id) => router.push({ path: '/dashboard/tran/' + id, query: { page: String(currentPage.value) } })
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
.tran-toolbar { margin-bottom: 12px; }
.tran-row-actions { display: inline-flex; gap: 6px; flex-wrap: nowrap; }
.tran-row-actions :deep(.el-button + .el-button) { margin-left: 0; }
</style>
