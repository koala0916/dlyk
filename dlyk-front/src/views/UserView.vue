<template>
  <el-card class="search-card" shadow="never">
    <el-form :model="search" label-width="76px" label-position="right" class="list-search-form user-search-form" @submit.prevent="doSearch">
      <!-- 第一行：账号、姓名、手机、邮箱、账号状态 -->
      <el-row :gutter="16" class="search-row">
        <el-col :span="4" class="col-xs">
          <el-form-item label="账号"><el-input v-model="search.loginAct" clearable placeholder="账号" /></el-form-item>
        </el-col>
        <el-col :span="4" class="col-xs">
          <el-form-item label="姓名"><el-input v-model="search.name" clearable placeholder="姓名" /></el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="手机"><el-input v-model="search.phone" clearable placeholder="手机" /></el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="邮箱"><el-input v-model="search.email" clearable placeholder="邮箱" /></el-form-item>
        </el-col>
        <el-col :span="4" class="col-xs">
          <el-form-item label="账号状态">
            <el-select v-model="search.accountEnabled" clearable placeholder="全部">
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第二行：创建人、创建时间 -->
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
      </el-row>
      <!-- 第三行：编辑人、编辑时间 -->
      <el-row :gutter="16" class="search-row">
        <el-col :span="6">
          <el-form-item label="编辑人">
            <el-select v-model="search.editBy" clearable filterable placeholder="选择" @visible-change="loadOwners">
              <el-option v-for="u in ownerOptions" :key="'e' + u.id" :label="u.name" :value="u.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="编辑时间">
            <SearchDateField compact v-model:start="search.editTimeStart" v-model:end="search.editTimeEnd" v-model:point="search.editTimePoint" v-model:pointType="search.editTimePointType" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第四行：最近登录 -->
      <el-row :gutter="16" class="search-row">
        <el-col :span="10">
          <el-form-item label="最近登录">
            <SearchDateField compact v-model:start="search.lastLoginTimeStart" v-model:end="search.lastLoginTimeEnd" v-model:point="search.lastLoginTimePoint" v-model:pointType="search.lastLoginTimePointType" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row class="search-row-actions">
        <el-col :span="24"><SearchFormActions @search="doSearch" @reset="resetSearch" /></el-col>
      </el-row>
    </el-form>
  </el-card>

  <div class="user-toolbar">
    <el-button type="primary" @click="addUser">添加用户</el-button>
    <el-button type="danger" @click="batchDel">批量删除</el-button>
  </div>

  <el-table :data="userPageInfo.list" style="width: 100%" @selection-change="selectId">
    <el-table-column type="selection" width="55" />
    <el-table-column type="index" label="序号" width="55" />
    <el-table-column property="loginAct" label="账号" />
    <el-table-column property="name" label="姓名" />
    <el-table-column property="phone" label="手机" />
    <el-table-column property="email" label="邮箱" />
    <el-table-column property="createTime" label="创建时间" show-overflow-tooltip />
    <el-table-column label="操作">
      <template #default="scope">
        <el-button type="success" :icon="View" circle @click="view(scope.row.id)" />
        <el-button type="primary" :icon="Edit" circle @click="edit(scope.row.id)" />
        <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)" />
      </template>
    </el-table-column>
  </el-table>

  <el-pagination background layout="prev, pager, next, jumper, total" :total="userPageInfo.total"
    :page-size="userPageInfo.pageSize" v-model:current-page="currentPage" @current-change="toPage" />
</template>

<script setup>
import { View, Edit, Delete } from '@element-plus/icons-vue'
import { ref, onMounted, inject } from 'vue'
import { doDelete, doGet } from '../http/httpRequest'
import { confirmMessage, showMessage } from '../util/message'
import { useRouter, useRoute } from 'vue-router'
import { buildSearchParams } from '../util/searchParams'
import SearchDateField from '../components/search/SearchDateField.vue'
import SearchFormActions from '../components/search/SearchFormActions.vue'

const defaultSearch = () => ({
  loginAct: '', name: '', phone: '', email: '', accountEnabled: null, createBy: null, editBy: null,
  createTimeStart: null, createTimeEnd: null, createTimePoint: null, createTimePointType: null,
  editTimeStart: null, editTimeEnd: null, editTimePoint: null, editTimePointType: null,
  lastLoginTimeStart: null, lastLoginTimeEnd: null, lastLoginTimePoint: null, lastLoginTimePointType: null,
})

const search = ref(defaultSearch())
const userPageInfo = ref({})
const currentPage = ref(1)
const ownerOptions = ref([])
const route = useRoute()
const router = useRouter()
const flushPage = inject('flush')
let idArray = []

const loadOwners = (visible) => {
  if (visible && ownerOptions.value.length === 0) {
    doGet('api/owners', {}).then((r) => { if (r.data.code === 200) ownerOptions.value = r.data.info })
  }
}

const resolvePageFromRoute = () => {
  const p = Number(route.query.page)
  return Number.isFinite(p) && p >= 1 ? Math.floor(p) : 1
}

onMounted(() => {
  currentPage.value = resolvePageFromRoute()
  loadUserList(currentPage.value)
})

const loadUserList = (current) => {
  const params = buildSearchParams(search.value, { current })
  doGet('api/users', params).then((resp) => {
    if (resp.data.code === 200) {
      userPageInfo.value = resp.data.info
      const pn = resp.data.info.pageNum
      if (pn != null && pn >= 1 && pn !== currentPage.value) {
        currentPage.value = pn
        router.replace({ path: '/dashboard/user', query: { page: String(pn) } })
      }
    } else {
      showMessage(resp.data.msg, 'error')
    }
  })
}

const doSearch = () => { currentPage.value = 1; loadUserList(1) }
const resetSearch = () => { search.value = defaultSearch(); doSearch() }

const toPage = (current) => {
  router.replace({ path: '/dashboard/user', query: { page: String(current) } })
  loadUserList(current)
}

const view = (id) => router.push({ path: '/dashboard/user/' + id, query: { page: String(currentPage.value) } })
const addUser = () => router.push({ path: '/dashboard/user/input', query: { page: String(currentPage.value) } })
const edit = (id) => router.push({ path: '/dashboard/user/edit/' + id, query: { page: String(currentPage.value) } })

const del = (id) => {
  confirmMessage('确认删除该用户吗？').then(() => {
    doDelete('api/user/' + id).then((resp) => {
      if (resp.data.code === 200) { showMessage(resp.data.msg, 'success'); flushPage() }
      else showMessage(resp.data.msg, 'error')
    })
  }).catch(() => {})
}

const selectId = (item) => { idArray = item.map((x) => x.id) }

const batchDel = () => {
  if (!idArray.length) return showMessage('请选择要删除的用户', 'warning')
  confirmMessage('确认删除选中的用户吗？').then(() => {
    doDelete('api/user', { ids: idArray.join(',') }).then((resp) => {
      if (resp.data.code === 200) { showMessage(resp.data.msg, 'success'); flushPage() }
      else showMessage(resp.data.msg, 'error')
    })
  }).catch(() => {})
}
</script>

<style scoped>
.search-card { margin-bottom: 12px; }
/* 搜索区样式与客户管理保持一致 */
.user-search-form :deep(.el-form-item) {
  margin-bottom: 0;
  width: 100%;
}
.user-search-form :deep(.el-form-item__label) {
  width: 76px !important;
  padding-right: 8px;
  justify-content: flex-end;
}
.user-search-form :deep(.el-form-item__content) {
  flex: 1;
  min-width: 0;
}
.user-search-form .search-row {
  margin-bottom: 12px;
}
.user-search-form .search-row-actions {
  margin-bottom: 0;
  padding-top: 4px;
}
.user-search-form :deep(.el-input),
.user-search-form :deep(.el-select) {
  width: 100%;
}
.user-search-form :deep(.col-xs .el-select .el-select__wrapper) {
  padding-left: 6px;
  padding-right: 6px;
}
.user-toolbar { margin-bottom: 12px; }
</style>
