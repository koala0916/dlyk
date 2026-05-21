<template>
  <el-tabs v-model="activeTab" type="border-card" class="perm-manage-tabs">
    <!-- 角色：维护角色与权限关系 -->
    <el-tab-pane label="角色" name="role">
      <div class="tab-toolbar">
        <div class="toolbar-actions">
          <el-button type="primary" @click="openAddRoleDialog">添加角色</el-button>
          <el-button type="danger" @click="openDeleteRoleDialog">删除角色</el-button>
        </div>
      </div>

      <el-table :data="roleTableData" v-loading="roleLoading" style="width: 100%">
        <el-table-column prop="roleName" label="角色名" width="180" />
        <el-table-column label="权限">
          <template #default="{ row }">
            <div class="tag-cell">
              <div class="tag-list">
                <template v-for="perm in row.permissions" :key="perm.permissionId">
                  <el-tag
                    closable
                    class="member-tag"
                    @close="confirmRemoveRolePerm(row.roleName, perm.name, row.roleId, perm.permissionId)"
                  >
                    {{ perm.name }}
                  </el-tag>
                </template>
                <span v-if="!row.permissions || row.permissions.length === 0" class="empty-hint">暂无权限</span>
              </div>
              <el-button type="primary" :icon="Plus" circle class="add-btn"
                @click="openAddPermToRoleDialog(row)" />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>

    <!-- 权限：维护权限与角色关系 -->
    <el-tab-pane label="权限" name="permission">
      <div class="tab-toolbar">
        <div class="toolbar-actions">
          <el-button type="primary" @click="openAddPermDialog">添加权限</el-button>
          <el-button type="danger" @click="openDeletePermDialog">删除权限</el-button>
        </div>
      </div>

      <el-table :data="permTableData" v-loading="permLoading" style="width: 100%">
        <el-table-column prop="name" label="权限名称" width="160" />
        <el-table-column prop="code" label="编码" width="140" />
        <el-table-column prop="url" label="URL" min-width="160" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="72" />
        <el-table-column label="角色">
          <template #default="{ row }">
            <div class="tag-cell">
              <div class="tag-list">
                <template v-for="role in row.roles" :key="role.roleId">
                  <el-tag
                    closable
                    class="member-tag"
                    @close="confirmRemoveRolePerm(role.roleName, row.name, role.roleId, row.permissionId)"
                  >
                    {{ role.roleName }}
                  </el-tag>
                </template>
                <span v-if="!row.roles || row.roles.length === 0" class="empty-hint">暂无角色</span>
              </div>
              <el-button type="primary" :icon="Plus" circle class="add-btn"
                @click="openAddRoleToPermDialog(row)" />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
  </el-tabs>

  <!-- 删除角色 -->
  <el-dialog v-model="deleteRoleDialogVisible" title="删除角色" width="480px" destroy-on-close
    @closed="resetDeleteRolePicker">
    <el-table ref="deleteRoleTableRef" :data="roleTableData" highlight-current-row max-height="360"
      @current-change="onDeleteRoleCurrentChange">
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="role" label="角色编码" />
    </el-table>
    <p class="dialog-tip">请先点击选中一行角色，再点「确定删除」</p>
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

  <!-- 删除权限 -->
  <el-dialog v-model="deletePermDialogVisible" title="删除权限" width="520px" destroy-on-close
    @closed="resetDeletePermPicker">
    <el-table ref="deletePermTableRef" :data="permTableData" highlight-current-row max-height="360"
      @current-change="onDeletePermCurrentChange">
      <el-table-column prop="name" label="权限名称" />
      <el-table-column prop="code" label="编码" />
      <el-table-column prop="type" label="类型" width="72" />
    </el-table>
    <p class="dialog-tip">请先选中权限；若该权限下还有子菜单需先删除子菜单</p>
    <template #footer>
      <el-button @click="deletePermDialogVisible = false">取消</el-button>
      <el-button type="danger" @click="submitDeletePerm">确定删除</el-button>
    </template>
  </el-dialog>

  <!-- 添加权限 -->
  <el-dialog v-model="addPermDialogVisible" title="添加权限" width="480px" destroy-on-close>
    <el-form ref="addPermFormRef" :model="addPermForm" :rules="addPermRules" label-width="90px">
      <el-form-item label="权限名称" prop="name">
        <el-input v-model="addPermForm.name" placeholder="如 客户管理" />
      </el-form-item>
      <el-form-item label="权限编码" prop="code">
        <el-input v-model="addPermForm.code" placeholder="如 customer:list" />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="addPermForm.type" style="width: 100%">
          <el-option label="子菜单(menu)" value="menu" />
          <el-option label="目录(M)" value="M" />
        </el-select>
      </el-form-item>
      <el-form-item label="父菜单" prop="parentId">
        <el-select v-model="addPermForm.parentId" clearable placeholder="无则选顶级(0)" style="width: 100%">
          <el-option label="顶级(0)" :value="0" />
          <el-option v-for="p in parentOptions" :key="p.id" :label="p.name" :value="p.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="URL" prop="url">
        <el-input v-model="addPermForm.url" placeholder="如 /dashboard/customer，目录可留空" />
      </el-form-item>
      <el-form-item label="排序" prop="orderNo">
        <el-input-number v-model="addPermForm.orderNo" :min="0" :max="999" />
      </el-form-item>
      <el-form-item label="图标" prop="icon">
        <el-input v-model="addPermForm.icon" placeholder="Element Plus 图标名，如 User" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="addPermDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitAddPerm">确定</el-button>
    </template>
  </el-dialog>

  <!-- 为角色添加权限 -->
  <el-dialog v-model="addPermDialogVisible2" :title="addPermDialogTitle2" width="600px" destroy-on-close
    @closed="resetPermPicker">
    <el-table ref="permPickerTableRef" :data="permOptionList" @selection-change="onPermSelectionChange"
      max-height="360">
      <el-table-column type="selection" width="48" :selectable="permRowSelectable" />
      <el-table-column prop="name" label="权限名称" />
      <el-table-column prop="code" label="编码" width="120" />
      <el-table-column prop="type" label="类型" width="72" />
    </el-table>
    <template #footer>
      <el-button @click="addPermDialogVisible2 = false">取消</el-button>
      <el-button type="primary" @click="submitAddPermsToRole">确定添加</el-button>
    </template>
  </el-dialog>

  <!-- 为权限添加角色 -->
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
      <el-button type="primary" @click="submitAddRolesToPerm">确定添加</el-button>
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
const permTableData = ref([])
const roleLoading = ref(false)
const permLoading = ref(false)
const parentOptions = ref([])

const loadRoleView = () => {
  roleLoading.value = true
  doGet('api/permission/manage/roles', {}).then((resp) => {
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

const loadPermView = () => {
  permLoading.value = true
  doGet('api/permission/manage/permissions', {}).then((resp) => {
    permLoading.value = false
    if (resp.data.code === 200) {
      permTableData.value = resp.data.info || []
    } else {
      showMessage(resp.data.msg, 'error')
    }
  }).catch(() => {
    permLoading.value = false
    showMessage('加载权限列表失败', 'error')
  })
}

const loadParentOptions = () => {
  doGet('api/permission/manage/parent-options', {}).then((resp) => {
    if (resp.data.code === 200) {
      parentOptions.value = resp.data.info || []
    }
  })
}

const refreshAll = () => {
  loadRoleView()
  loadPermView()
}

onMounted(() => {
  refreshAll()
  loadParentOptions()
})

const confirmRemoveRolePerm = (roleName, permName, roleId, permissionId) => {
  confirmMessage(`确认删除角色「${roleName}」的权限「${permName}」吗？`).then(() => {
    doDelete('api/role-permission', { roleId, permissionId }).then((resp) => {
      if (resp.data.code === 200) {
        showMessage(resp.data.msg || '删除成功', 'success')
        refreshAll()
      } else {
        showMessage(resp.data.msg, 'error')
      }
    })
  }).catch(() => {})
}

/* ---------- 删除角色 ---------- */
const deleteRoleDialogVisible = ref(false)
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
  confirmMessage(`确认删除角色「${role.roleName}」吗？将同时移除用户关系与权限关系。`).then(() => {
    doDelete('api/permission/manage/role/' + role.roleId).then((resp) => {
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
  if (!addRoleFormRef.value) return
  addRoleFormRef.value.validate((valid) => {
    if (!valid) return
    const formData = new FormData()
    formData.append('role', addRoleForm.value.role)
    formData.append('roleName', addRoleForm.value.roleName)
    doPost('api/permission/manage/role', formData).then((resp) => {
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

/* ---------- 删除权限 ---------- */
const deletePermDialogVisible = ref(false)
const selectedDeletePerm = ref(null)

const openDeletePermDialog = () => {
  selectedDeletePerm.value = null
  if (permTableData.value.length === 0) {
    showMessage('当前没有可删除的权限', 'warning')
    return
  }
  deletePermDialogVisible.value = true
}

const onDeletePermCurrentChange = (row) => {
  selectedDeletePerm.value = row
}

const resetDeletePermPicker = () => {
  selectedDeletePerm.value = null
}

const submitDeletePerm = () => {
  if (!selectedDeletePerm.value) {
    showMessage('请先选择要删除的权限', 'warning')
    return
  }
  const perm = selectedDeletePerm.value
  confirmMessage(`确认删除权限「${perm.name}」吗？`).then(() => {
    doDelete('api/permission/' + perm.permissionId).then((resp) => {
      if (resp.data.code === 200) {
        showMessage(resp.data.msg || '删除权限成功', 'success')
        deletePermDialogVisible.value = false
        refreshAll()
        loadParentOptions()
      } else {
        showMessage(resp.data.msg, 'error')
      }
    })
  }).catch(() => {})
}

/* ---------- 添加权限 ---------- */
const addPermDialogVisible = ref(false)
const addPermFormRef = ref(null)
const addPermForm = ref({
  name: '', code: '', url: '', type: 'menu', parentId: 0, orderNo: 99, icon: 'Menu',
})
const addPermRules = {
  name: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
}

const openAddPermDialog = () => {
  addPermForm.value = {
    name: '', code: '', url: '', type: 'menu', parentId: 0, orderNo: 99, icon: 'Menu',
  }
  loadParentOptions()
  addPermDialogVisible.value = true
}

const submitAddPerm = () => {
  if (!addPermFormRef.value) return
  addPermFormRef.value.validate((valid) => {
    if (!valid) return
    const formData = new FormData()
    formData.append('name', addPermForm.value.name)
    formData.append('code', addPermForm.value.code)
    formData.append('url', addPermForm.value.url || '')
    formData.append('type', addPermForm.value.type)
    formData.append('parentId', addPermForm.value.parentId ?? 0)
    formData.append('orderNo', addPermForm.value.orderNo ?? 99)
    formData.append('icon', addPermForm.value.icon || 'Menu')
    doPost('api/permission', formData).then((resp) => {
      if (resp.data.code === 200) {
        showMessage(resp.data.msg || '添加权限成功', 'success')
        addPermDialogVisible.value = false
        refreshAll()
        loadParentOptions()
      } else {
        showMessage(resp.data.msg, 'error')
      }
    })
  })
}

/* ---------- 为角色添加权限 ---------- */
const addPermDialogVisible2 = ref(false)
const addPermDialogTitle2 = ref('')
const currentRoleId = ref(null)
const permOptionList = ref([])
const selectedPermIds = ref([])

const openAddPermToRoleDialog = (row) => {
  currentRoleId.value = row.roleId
  addPermDialogTitle2.value = `为「${row.roleName}」添加权限`
  selectedPermIds.value = []
  doGet(`api/permission/manage/role/${row.roleId}/permission-options`, {}).then((resp) => {
    if (resp.data.code === 200) {
      permOptionList.value = resp.data.info || []
      addPermDialogVisible2.value = true
    } else {
      showMessage(resp.data.msg, 'error')
    }
  })
}

const permRowSelectable = (row) => !row.assigned

const onPermSelectionChange = (rows) => {
  selectedPermIds.value = rows.map((r) => r.permissionId)
}

const resetPermPicker = () => {
  permOptionList.value = []
  selectedPermIds.value = []
  currentRoleId.value = null
}

const submitAddPermsToRole = () => {
  if (!currentRoleId.value) return
  if (selectedPermIds.value.length === 0) {
    showMessage('请至少选择一个权限', 'warning')
    return
  }
  const formData = new FormData()
  formData.append('roleId', currentRoleId.value)
  formData.append('permissionIds', selectedPermIds.value.join(','))
  doPost('api/role-permission/add-to-role', formData).then((resp) => {
    if (resp.data.code === 200) {
      showMessage(resp.data.msg || '添加成功', 'success')
      addPermDialogVisible2.value = false
      refreshAll()
    } else {
      showMessage(resp.data.msg, 'error')
    }
  })
}

/* ---------- 为权限添加角色 ---------- */
const addRoleDialogVisible2 = ref(false)
const addRoleDialogTitle2 = ref('')
const currentPermissionId = ref(null)
const roleOptionList = ref([])
const selectedRoleIds = ref([])

const openAddRoleToPermDialog = (row) => {
  currentPermissionId.value = row.permissionId
  addRoleDialogTitle2.value = `为「${row.name}」添加角色`
  selectedRoleIds.value = []
  doGet(`api/permission/manage/permission/${row.permissionId}/role-options`, {}).then((resp) => {
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
  currentPermissionId.value = null
}

const submitAddRolesToPerm = () => {
  if (!currentPermissionId.value) return
  if (selectedRoleIds.value.length === 0) {
    showMessage('请至少选择一个角色', 'warning')
    return
  }
  const formData = new FormData()
  formData.append('permissionId', currentPermissionId.value)
  formData.append('roleIds', selectedRoleIds.value.join(','))
  doPost('api/role-permission/add-to-permission', formData).then((resp) => {
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
.perm-manage-tabs {
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

.dialog-tip {
  margin: 12px 0 0;
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
