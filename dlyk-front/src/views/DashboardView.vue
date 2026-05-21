<script setup>
import { ArrowDown, Menu as MenuIcon } from "@element-plus/icons-vue";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

import { ref, onMounted, nextTick, provide } from "vue";

import { doGet } from "../http/httpRequest";
import { TOKEN_NAME } from "../util/constant";

import { useRoute } from "vue-router";

//定义userInfo对象，用于存储用户信息
let userInfo = ref({})

/** 将数据库中的 icon 字符串解析为 Element Plus 图标组件 */
const resolveMenuIcon = (iconName) => {
  if (!iconName || typeof iconName !== 'string') {
    return MenuIcon
  }
  const key = iconName.trim()
  return ElementPlusIconsVue[key] || MenuIcon
}

//页面渲染完成后，发送请求获取用户信息
onMounted(() => {
  currentRoutePath()
  //发送请求获取用户信息
  doGet('/api/login/user', {}).then((response) => {
    //判断请求是否成功
    if (response.data.code == 200) {
      userInfo.value = response.data.info

      console.log(response.data.info)
    }
  })
})


//退出
const logout = () => {
  //发送请求退出
  doGet('/api/logout', {}).then(resp => {
    //删除前端的token
    sessionStorage.removeItem(TOKEN_NAME)

    //跳转到登录页面
    window.location.href = '/'
  })
}


function handleOpen() {
  console.log("菜单展开了")
}

function handleClose() {
  console.log("菜单收起来了")
}

//控制右侧的菜单是否显示
let isRouterAlive = ref(true)

//创建局部刷新的函数
const reload = () => {
  //将isRouterAlive的值设置为false   关闭了右侧内容
  isRouterAlive.value = false

  //将isRouterAlive的值设置为true    打开了右侧内容
  nextTick(() => {
    //vue执行完渲染操作之后会执行这里的内容
    isRouterAlive.value = true
  })

}

//向其他子组件传递函数
provide('flush', reload)


//激活当前选中的菜单
let routerPath = ref('')

//监听路由变化，获取当前路由的path
const currentRoutePath = () => {
  //获取当前路由的path
  let tempPath = useRoute().path

  //解决三级目录页面刷新的问题  例如/dashboard/user/input  刷新后会变成/dashboard/user
  let tempArr = tempPath.split("/")
  if (tempArr.length > 3) {
    routerPath.value = "/" + tempArr[1] + "/" + tempArr[2]  //  /dashboard/user
  } else {
    routerPath.value = tempPath
  }

}

</script>

<template>
  <el-container>
    <!--左侧-->
    <el-aside width="200px">
      <div class="asideTitile">杜力优课系统</div>
      <!--
        active-text-color: 活动菜单项的文本颜色
        background-color: 菜单的背景颜色
        default-active: 页面加载时默认激活菜单的index
        text-color: 菜单的文字颜色
        unique-opened: 是否只保持一个子菜单的展开，默认是false
        @open: sub-menu展开的回调
        @closeL sub-menu收起的回调
      -->
      <el-menu :router="true" active-text-color="#409eff" background-color="#f0f2f5" :default-active="routerPath"
        text-color="#5a5e66" unique-opened class="aside-menu" v-on:open="handleOpen()" v-on:close="handleClose">

        <!--动态读取菜单-->
    
        <el-sub-menu :index="String(index)" v-for="(menu, index) in userInfo.tMenuPermissionList" :key="menu.id || index">
          <template #title>
            <el-icon>
              <component :is="resolveMenuIcon(menu.icon)" />
            </el-icon>
            <span>{{ menu.name }}</span>
          </template>
          <el-menu-item :index="subMenu.url" v-for="subMenu in menu.childPermissionList" :key="subMenu.id">
            <el-icon>
              <component :is="resolveMenuIcon(subMenu.icon)" />
            </el-icon>
            <span>{{ subMenu.name }}</span>
          </el-menu-item>
        </el-sub-menu>

      </el-menu>
    </el-aside>

    <!--右侧-->
    <el-container>
      <!--右侧顶部-->
      <el-header style="display: flex; justify-content: flex-end; align-items: center;">
        <el-dropdown>
          <span class="el-dropdown-link">
            {{ userInfo.name }}
            <el-icon class="el-icon--right">
              <ArrowDown />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>我的资料</el-dropdown-item>
              <el-dropdown-item>修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="logout()">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <!--右侧内容体-->
      <el-main>
        <router-view v-if="isRouterAlive" />
      </el-main>
      <!--右侧底部-->
      <el-footer>
         @杜力优课--考拉与桉树的项目
      </el-footer>
    </el-container>
  </el-container>
</template>

<style scoped>
/* 左侧栏：浅灰背景，与深色菜单区分 */
.el-aside {
  background: #f5f7fa;
  height: calc(100vh);
  border-right: 1px solid #e4e7ed;
}

.el-header {
  background: azure;
  height: 35px;
}

.el-footer {
  background: aliceblue;
  height: 40px;
  line-height: 40px;
  text-align: center;
}

.asideTitile {
  color: #303133;
  background: #eef1f6;
  text-align: center;
  height: 35px;
  line-height: 35px;
  border-bottom: 1px solid #e4e7ed;
  font-weight: 600;
}

/* 菜单悬停、选中态 */
.aside-menu {
  border-right: none !important;
}
:deep(.aside-menu .el-menu-item.is-active) {
  background-color: #ecf5ff !important;
}
:deep(.aside-menu .el-menu-item:hover),
:deep(.aside-menu .el-sub-menu__title:hover) {
  background-color: #ebeef5 !important;
}
</style>