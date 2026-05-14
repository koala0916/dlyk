<template>
  <!-- 用户详情：用表格展示键值对，仅调整布局样式 -->
  <div class="user-detail-wrap">
    <el-table
      :data="detailTableRows"
      border
      stripe
      class="user-detail-table"
      empty-text="暂无数据"
    >
      <!-- 左侧列：字段名称（单元格左对齐） -->
      <el-table-column prop="label" label="项目" width="200" align="left" header-align="left" />
      <!-- 右侧列：字段取值 -->
      <el-table-column prop="value" label="内容" min-width="280" show-overflow-tooltip />
    </el-table>

    <!-- 底部操作区 -->
    <div class="user-detail-actions">
      <el-button type="primary" @click="goBack">返 回</el-button>
    </div>
  </div>
</template>

<script setup>
// 引入 Vue 的 ref、onMounted 与 computed
import { ref, onMounted, computed } from 'vue';
// 引入路由相关 API
import { useRoute, useRouter } from 'vue-router';
// 引入封装的 GET 请求方法
import { doGet } from '../http/httpRequest';
// 引入消息提示工具
import { showMessage } from '../util/message';

// 用户详情数据，结构与接口返回一致，未改字段含义
let user = ref({
  createByDO: {},
  editByDO: {},
});

// 将 user 转为表格行数据，仅用于展示，不修改后端数据结构
const detailTableRows = computed(() => {
  const u = user.value;
  return [
    { label: 'ID', value: u.id },
    { label: '账号', value: u.loginAct },
    { label: '密码', value: '******' },
    { label: '姓名', value: u.name },
    { label: '手机', value: u.phone },
    { label: '邮箱', value: u.email },
    { label: '账号是否未过期', value: u.accountNoExpired === 1 ? '是' : '否' },
    { label: '密码是否未过期', value: u.credentialsNoExpired === 1 ? '是' : '否' },
    { label: '账号是否未锁定', value: u.accountNoLocked === 1 ? '是' : '否' },
    { label: '账号是否已启用', value: u.accountEnabled === 1 ? '是' : '否' },
    { label: '创建时间', value: u.createTime },
    { label: '创建人', value: u.createByDO?.name ?? '' },
    { label: '编辑时间', value: u.editTime },
    { label: '编辑人', value: u.editByDO?.name ?? '' },
    { label: '最近登录时间', value: u.lastLoginTime },
  ];
});

const router = useRouter();
const route = useRoute();

// 页面挂载后拉取详情
onMounted(() => {
  loadUser();
});

// 根据路由 id 查询用户详情，逻辑与原先一致
const loadUser = () => {
  let id = route.params.userId;

  doGet('api/user/' + id, {}).then((resp) => {
    if (resp.data.code == 200) {
      user.value = resp.data.info;

      if (!resp.data.info.createByDO) {
        user.value.createByDO = {};
      }
      if (!resp.data.info.editByDO) {
        user.value.editByDO = {};
      }
    } else {
      showMessage(resp.data.msg, 'error');
    }
  });
};

// 返回用户列表，并带上进入详情前的页码（与 UserView 中 query.page 约定一致）
const goBack = () => {
  const page = route.query.page;
  router.push({
    path: '/dashboard/user',
    ...(page != null && String(page) !== '' ? { query: { page: String(page) } } : {}),
  });
};
</script>

<style scoped>
/* 整体留白，避免表格贴边 */
.user-detail-wrap {
  padding: 16px;
  max-width: 960px;
}

/* 表格圆角与表头样式微调 */
.user-detail-table {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

/* 操作按钮与表格间距 */
.user-detail-actions {
  margin-top: 16px;
}
</style>
