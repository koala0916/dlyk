<template>
  <el-tabs v-model="activeTab" type="border-card" class="role-manage-tabs">
    <!-- 角色卡片 -->
    <el-tab-pane label="角色" name="role">
      <div class="tab-toolbar">
        <div class="toolbar-actions">
          <el-button type="primary" @click="openAddRoleDialog">添加角色</el-button>
          <el-button type="danger" @click="openDeleteRoleDialog">删除角色</el-button>
        </div>
      </div>

      <el-table :data="roleTableData" v-loading="roleLoading" style="width: 100%">
        <el-table-column prop="roleName" label="角色名" width="180" />
        <el-table-column label="成员">
          <template #default="{ row }">
            <div class="tag-cell">
              <div class="tag-list">
                <template v-for="user in row.users" :key="user.userId">
                  <el-tag
                    closable
                    class="member-tag"
                    @close="confirmRemoveUserRole(user.name, row.roleName, user.userId, row.roleId)"
                  >
                    {{ user.name }}
                  </el-tag>
                </template>
                <span v-if="!row.users || row.users.length === 0" class="empty-hint">暂无成员</span>
              </div>
              <el-button type="primary" :icon="Plus" circle class="add-btn"
                @click="openAddUserToRoleDialog(row)" />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>

    <!-- 用户卡片 -->
    <el-tab-pane label="用户" name="user">
      <div class="tab-toolbar tab-toolbar--hint">
        <span class="hint-text">添加用户请到用户管理页面进行操作</span>
      </div>

      <el-table :data="userTableData" v-loading="userLoading" style="width: 100%">
        <el-table-column prop="name" label="用户名" width="180" />
        <el-table-column label="角色">
          <template #default="{ row }">
            <div class="tag-cell">
              <div class="tag-list">
                <template v-for="role in row.roles" :key="role.roleId">
                  <el-tag
                    closable
                    class="member-tag"
                    @close="confirmRemoveUserRole(row.name, role.roleName, row.userId, role.roleId)"
                  >
                    {{ role.roleName }}
                  </el-tag>
                </template>
                <span v-if="!row.roles || row.roles.length === 0" class="empty-hint">暂无角色</span>
              </div>
              <el-button type="primary" :icon="Plus" circle class="add-btn"
                @click="openAddRoleToUserDialog(row)" />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
  </el-tabs>

  <!-- 删除角色：选择要删除的角色 -->
  <el-dialog v-model="deleteRoleDialogVisible" title="删除角色" width="480px" destroy-on-close
    @closed="resetDeleteRolePicker">
    <el-table
      ref="deleteRoleTableRef"
      :data="roleTableData"
      highlight-current-row
      max-height="360"
      @current-change="onDeleteRoleCurrentChange"
    >
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="role" label="角色编码" />
    </el-table>
    <p class="delete-role-tip">请先点击选中一行角色，再点「确定删除」</p>
    <template #footer>
      <el-button @click="deleteRoleDialogVisible = false">取消</el-button>
      <el-button type="danger" @click="submitDeleteRole">确定删除</el-button>
    </template>
  </el-dialog>

  <!-- 添加角色 -->
  <el-dialog v-model="addRoleDialogVisible" title="添加角色" width="420px" destroy-on-close>
    <el-form ref="addRoleFormRef" :model="addRoleForm" :rules="addRoleRules" label-width="90px">
      <el-form-item label="角色编码" prop="role">
        <el-input v-model="addRoleForm.role" placeholder="如 admin" />
      </el-form-item>
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="addRoleForm.roleName" placeholder="如 管理员" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="addRoleDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitAddRole">确定</el-button>
    </template>
  </el-dialog>

  <!-- 为角色添加用户 -->
  <el-dialog v-model="addUserDialogVisible" :title="addUserDialogTitle" width="560px" destroy-on-close
    @closed="resetUserPicker">
    <el-table ref="userPickerTableRef" :data="userOptionList" @selection-change="onUserSelectionChange"
      max-height="360">
      <el-table-column type="selection" width="48" :selectable="userRowSelectable" />
      <el-table-column prop="loginAct" label="登录账号" />
      <el-table-column prop="name" label="姓名" />
    </el-table>
    <template #footer>
      <el-button @click="addUserDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitAddUsersToRole">确定添加</el-button>
    </template>
  </el-dialog>

  <!-- 为用户添加角色 -->
  <el-dialog v-model="addRoleDialogVisible2" :title="addRoleDialogTitle2" width="520px" destroy-on-close
    @closed="resetRolePicker">
    <el-table ref="rolePickerTableRef" :data="roleOptionList" @selection-change="onRoleSelectionChange"
      max-height="360">
      <el-table-column type="selection" width="48" :selectable="roleRowSelectable" />
      <el-table-column prop="role" label="角色编码" />
      <el-table-column prop="roleName" label="角色名称" />
    </el-table>
    <template #footer>
      <el-button @click="addRoleDialogVisible2 = false">取消</el-button>
      <el-button type="primary" @click="submitAddRolesToUser">确定添加</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { Plus } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import { doGet, doPost, doDelete } from '../http/httpRequest'
import { confirmMessage, showMessage } from '../util/message'

const activeTab = ref('role')

const roleTableData = ref([])
const userTableData = ref([])
const roleLoading = ref(false)
const userLoading = ref(false)

const loadRoleView = () => {
  roleLoading.value = true
  doGet('api/role/manage/roles', {}).then((resp) => {
    roleLoading.value = false
    if (resp.data.code === 200) {
      roleTableData.value = resp.data.info || []
    } else {
      showMessage(resp.data.msg, 'error')
    }
  }).catch(() => {
    roleLoading.value = false
    showMessage('加载角色列表失败', 'error')
  })
}

const loadUserView = () => {
  userLoading.value = true
  doGet('api/role/manage/users', {}).then((resp) => {
    userLoading.value = false
    if (resp.data.code === 200) {
      userTableData.value = resp.data.info || []
    } else {
      showMessage(resp.data.msg, 'error')
    }
  }).catch(() => {
    userLoading.value = false
    showMessage('加载用户列表失败', 'error')
  })
}

const refreshAll = () => {
  loadRoleView()
  loadUserView()
}

onMounted(() => {
  refreshAll()
})

/** 点击标签叉号：弹出确认框（与用户管理删除一致），确认后再删关系 */
const confirmRemoveUserRole = (userName, roleName, userId, roleId) => {
  confirmMessage(`确认删除${userName}的「${roleName}」角色吗？`).then(() => {
    handleRemoveUserRole(userId, roleId)
  }).catch(() => {})
}

/** 删除用户-角色关系 */
const handleRemoveUserRole = (userId, roleId) => {
  doDelete('api/user-role', { userId, roleId }).then((resp) => {
    if (resp.data.code === 200) {
      showMessage(resp.data.msg || '删除成功', 'success')
      refreshAll()
    } else {
      showMessage(resp.data.msg, 'error')
    }
  })
}

/* ---------- 删除角色 ---------- */
const deleteRoleDialogVisible = ref(false)
const deleteRoleTableRef = ref(null)
const selectedDeleteRole = ref(null)

const openDeleteRoleDialog = () => {
  selectedDeleteRole.value = null
  if (roleTableData.value.length === 0) {
    showMessage('当前没有可删除的角色', 'warning')
    return
  }
  deleteRoleDialogVisible.value = true
}

const onDeleteRoleCurrentChange = (row) => {
  selectedDeleteRole.value = row
}

const resetDeleteRolePicker = () => {
  selectedDeleteRole.value = null
}

const submitDeleteRole = () => {
  if (!selectedDeleteRole.value) {
    showMessage('请先选择要删除的角色', 'warning')
    return
  }
  const role = selectedDeleteRole.value
  confirmMessage(`确认删除角色「${role.roleName}」吗？将同时移除该角色下的用户关系与权限关系。`).then(() => {
    doDelete('api/role/' + role.roleId).then((resp) => {
      if (resp.data.code === 200) {
        showMessage(resp.data.msg || '删除角色成功', 'success')
        deleteRoleDialogVisible.value = false
        refreshAll()
      } else {
        showMessage(resp.data.msg, 'error')
      }
    })
  }).catch(() => {})
}

/* ---------- 添加角色 ---------- */
const addRoleDialogVisible = ref(false)
const addRoleFormRef = ref(null)
const addRoleForm = ref({ role: '', roleName: '' })
const addRoleRules = {
  role: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
}

const openAddRoleDialog = () => {
  addRoleForm.value = { role: '', roleName: '' }
  addRoleDialogVisible.value = true
}

const submitAddRole = () => {
  if (!addRoleFormRef.value) {
    return
  }
  addRoleFormRef.value.validate((valid) => {
    if (!valid) {
      return
    }
    const formData = new FormData()
    formData.append('role', addRoleForm.value.role)
    formData.append('roleName', addRoleForm.value.roleName)
    doPost('api/role', formData).then((resp) => {
      if (resp.data.code === 200) {
        showMessage(resp.data.msg || '添加角色成功', 'success')
        addRoleDialogVisible.value = false
        refreshAll()
      } else {
        showMessage(resp.data.msg, 'error')
      }
    })
  })
}

/* ---------- 为角色添加用户 ---------- */
const addUserDialogVisible = ref(false)
const addUserDialogTitle = ref('')
const currentRoleId = ref(null)
const userOptionList = ref([])
const selectedUserIds = ref([])
const userPickerTableRef = ref(null)

const openAddUserToRoleDialog = (row) => {
  currentRoleId.value = row.roleId
  addUserDialogTitle.value = `为「${row.roleName}」添加用户`
  selectedUserIds.value = []
  doGet(`api/role/manage/role/${row.roleId}/user-options`, {}).then((resp) => {
    if (resp.data.code === 200) {
      userOptionList.value = resp.data.info || []
      addUserDialogVisible.value = true
    } else {
      showMessage(resp.data.msg, 'error')
    }
  })
}

const userRowSelectable = (row) => !row.assigned

const onUserSelectionChange = (rows) => {
  selectedUserIds.value = rows.map((r) => r.userId)
}

const resetUserPicker = () => {
  userOptionList.value = []
  selectedUserIds.value = []
  currentRoleId.value = null
}

const submitAddUsersToRole = () => {
  if (!currentRoleId.value) {
    return
  }
  if (selectedUserIds.value.length === 0) {
    showMessage('请至少选择一个用户', 'warning')
    return
  }
  const formData = new FormData()
  formData.append('roleId', currentRoleId.value)
  formData.append('userIds', selectedUserIds.value.join(','))
  doPost('api/user-role/add-to-role', formData).then((resp) => {
    if (resp.data.code === 200) {
      showMessage(resp.data.msg || '添加成功', 'success')
      addUserDialogVisible.value = false
      refreshAll()
    } else {
      showMessage(resp.data.msg, 'error')
    }
  })
}

/* ---------- 为用户添加角色 ---------- */
const addRoleDialogVisible2 = ref(false)
const addRoleDialogTitle2 = ref('')
const currentUserId = ref(null)
const roleOptionList = ref([])
const selectedRoleIds = ref([])
const rolePickerTableRef = ref(null)

const openAddRoleToUserDialog = (row) => {
  currentUserId.value = row.userId
  addRoleDialogTitle2.value = `为「${row.name}」添加角色`
  selectedRoleIds.value = []
  doGet(`api/role/manage/user/${row.userId}/role-options`, {}).then((resp) => {
    if (resp.data.code === 200) {
      roleOptionList.value = resp.data.info || []
      addRoleDialogVisible2.value = true
    } else {
      showMessage(resp.data.msg, 'error')
    }
  })
}

const roleRowSelectable = (row) => !row.assigned

const onRoleSelectionChange = (rows) => {
  selectedRoleIds.value = rows.map((r) => r.roleId)
}

const resetRolePicker = () => {
  roleOptionList.value = []
  selectedRoleIds.value = []
  currentUserId.value = null
}

const submitAddRolesToUser = () => {
  if (!currentUserId.value) {
    return
  }
  if (selectedRoleIds.value.length === 0) {
    showMessage('请至少选择一个角色', 'warning')
    return
  }
  const formData = new FormData()
  formData.append('userId', currentUserId.value)
  formData.append('roleIds', selectedRoleIds.value.join(','))
  doPost('api/user-role/add-to-user', formData).then((resp) => {
    if (resp.data.code === 200) {
      showMessage(resp.data.msg || '添加成功', 'success')
      addRoleDialogVisible2.value = false
      refreshAll()
    } else {
      showMessage(resp.data.msg, 'error')
    }
  })
}
</script>

<style scoped>
.role-manage-tabs {
  margin-top: 8px;
}

.tab-toolbar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 12px;
}

.toolbar-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.delete-role-tip {
  margin: 12px 0 0;
  color: #909399;
  font-size: 13px;
}

.tab-toolbar--hint {
  justify-content: flex-start;
}

.hint-text {
  color: #909399;
  font-size: 13px;
}

.tag-cell {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  min-height: 32px;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.member-tag {
  cursor: default;
}

.add-btn {
  flex-shrink: 0;
}

.empty-hint {
  color: #c0c4cc;
  font-size: 13px;
}
</style>
