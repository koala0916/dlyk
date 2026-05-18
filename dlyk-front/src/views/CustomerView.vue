<template>
  <!-- 顶部操作区：添加客户（蓝）+ 导出 Excel（绿） -->
  <div class="customer-toolbar">
    <el-button type="primary" @click="addCustomer">添加客户</el-button>
    <el-button type="success" @click="batchExportExcel">批量导出(Excel)</el-button>
    <el-button type="success" @click="chooseExportExcel">选择导出(Excel)</el-button>
  </div>

  <!-- 学员列表表格，支持多选、行高亮 -->
  <el-table
    :data="customerPageInfo.list"
    style="width: 100%"
    :row-class-name="tableRowClassName"
    @selection-change="handSelection"
  >
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
      <template #default="scope">
        {{ studyingLabel(scope.row.studying) }}
      </template>
    </el-table-column>
    <el-table-column property="remark" label="备注" min-width="140" show-overflow-tooltip />
    <el-table-column property="createByDO.name" label="创建人" width="100" />
    <el-table-column property="createTime" label="创建时间" width="170" show-overflow-tooltip />
    <!-- 操作列固定宽度并禁止换行，保证三个圆形按钮始终同一行 -->
    <el-table-column label="操作" width="160" align="center" fixed="right" class-name="customer-op-column">
      <template #default="scope">
        <div class="customer-row-actions">
          <el-button type="success" :icon="View" circle @click="view(scope.row.id)" />
          <el-button type="primary" :icon="Edit" circle @click="edit(scope.row.id)" />
          <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)" />
        </div>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination
    background
    layout="prev, pager, next, jumper, total"
    :total="customerPageInfo.total"
    :page-size="customerPageInfo.pageSize"
    v-model:current-page="currentPage"
    @current-change="toPage"
  />
</template>

<script setup>
// 引入 Element Plus 图标
import { View, Edit, Delete } from '@element-plus/icons-vue'
// 引入 Vue 组合式 API
import { ref, onMounted, inject } from 'vue'
// 引入路由
import { useRouter, useRoute } from 'vue-router'
// 引入 HTTP 与消息工具
import { doGet, doDelete, download } from '../http/httpRequest'
import { showMessage, confirmMessage } from '../util/message'
// 引入文件保存（Excel 下载）
import { saveAs } from 'file-saver'

// 分页数据对象
const customerPageInfo = ref({})
// 当前页码，与 URL query.page 同步
const currentPage = ref(1)
const router = useRouter()
const route = useRoute()
// 注入 Dashboard 提供的局部刷新方法
const flushPage = inject('flush')

// 从路由解析页码
const resolvePageFromRoute = () => {
  const p = Number(route.query.page)
  return Number.isFinite(p) && p >= 1 ? Math.floor(p) : 1
}

// 页面挂载后加载列表
onMounted(() => {
  currentPage.value = resolvePageFromRoute()
  loadCustomerList(currentPage.value)
})

// 请求分页列表
const loadCustomerList = (current) => {
  doGet('/api/customers', { current }).then((resp) => {
    if (resp.data.code === 200) {
      customerPageInfo.value = resp.data.info
      const pn = resp.data.info.pageNum
      if (pn != null && pn >= 1 && pn !== currentPage.value) {
        currentPage.value = pn
        router.replace({ path: '/dashboard/customer', query: { page: String(pn) } })
      }
    } else {
      showMessage(resp.data.msg || '数据加载失败', 'error')
    }
  })
}

// 翻页
const toPage = (current) => {
  router.replace({ path: '/dashboard/customer', query: { page: String(current) } })
  loadCustomerList(current)
}

// 是否正在学习：显示文字
const studyingLabel = (studying) => {
  if (studying === 1) return '是'
  if (studying === 0) return '否'
  return ''
}

// 判断是否需要淡红底色：仅「正在学习」为是(1) 时才预警；已停课(0) 不再标红
const isWarningRow = (row) => {
  if (row.studying !== 1) {
    return false
  }
  const lessons = row.remainingLessons
  if (lessons != null && lessons > 0 && lessons <= 3) {
    return true
  }
  if (!row.courseExpireTime) {
    return false
  }
  const expireMs = new Date(row.courseExpireTime).getTime()
  const diffDays = (expireMs - Date.now()) / (24 * 60 * 60 * 1000)
  return diffDays < 15
}

// el-table 行样式回调
const tableRowClassName = ({ row }) => {
  return isWarningRow(row) ? 'customer-row-warning' : ''
}

// 批量导出全部
const batchExportExcel = () => {
  download('/api/exportExcel', { ids: '' }).then((resp) => {
    saveAs(new Blob([resp.data]), '学员列表.xlsx')
  })
}

// 选中行的 id 数组
let idArray = []

const handSelection = (item) => {
  idArray = []
  for (const index in item) {
    idArray.push(item[index].id)
  }
}

// 按选中行导出
const chooseExportExcel = () => {
  if (idArray.length === 0) {
    showMessage('请选择要导出的客户', 'warning')
    return
  }
  const ids = idArray.join(',')
  download('/api/exportExcel', { ids }).then((resp) => {
    saveAs(new Blob([resp.data]), '学员列表.xlsx')
  })
}

// 添加客户
const addCustomer = () => {
  router.push({
    path: '/dashboard/customer/input',
    query: { page: String(currentPage.value) },
  })
}

// 查看详情
const view = (id) => {
  router.push({
    path: '/dashboard/customer/' + id,
    query: { page: String(currentPage.value) },
  })
}

// 编辑
const edit = (id) => {
  router.push({
    path: '/dashboard/customer/edit/' + id,
    query: { page: String(currentPage.value) },
  })
}

// 删除
const del = (id) => {
  confirmMessage('确认删除该客户吗？').then(() => {
    doDelete('/api/customer/' + id).then((resp) => {
      if (resp.data.code === 200) {
        showMessage(resp.data.msg || '删除成功', 'success')
        flushPage()
      } else {
        showMessage(resp.data.msg || '删除失败', 'error')
      }
    })
  }).catch(() => {})
}
</script>

<style scoped>
.customer-toolbar {
  margin-bottom: 12px;
}
.customer-toolbar .el-button + .el-button {
  margin-left: 8px;
}
/* 预警行：淡红色背景 */
:deep(.customer-row-warning) {
  background-color: #fde8e8 !important;
}
:deep(.customer-row-warning:hover > td) {
  background-color: #fcd4d4 !important;
}
/* 操作列：横向排列、不换行 */
.customer-row-actions {
  display: inline-flex;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: center;
  gap: 6px;
  white-space: nowrap;
}
/* 缩小按钮默认左边距，避免挤换行 */
.customer-row-actions :deep(.el-button + .el-button) {
  margin-left: 0;
}
/* 操作列单元格减少内边距，给按钮留足横向空间 */
:deep(.customer-op-column .cell) {
  overflow: visible;
  white-space: nowrap;
}
</style>
