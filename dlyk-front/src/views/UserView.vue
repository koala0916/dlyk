<template>
    <el-button type="primary" @click="addUser">添加用户</el-button>
    <el-button type="danger" @click="batchDel">批量删除</el-button>


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
            <el-button type="success" :icon="View" @click="view(scope.row.id)" circle />
            <el-button type="primary" :icon="Edit" @click="edit(scope.row.id)" circle />
            <el-button type="danger" :icon="Delete" @click="del(scope.row.id)" circle />
            </template>
        </el-table-column>
    </el-table>

    <!--分页条-->
    <!--
   background: 是否为分页按钮添加背景色
   layout: 组件布局，子组件名用逗号分隔
   total: 总条目数 (冒号绑定变量，如果是数字、布尔值，可以直接写)
   page-size: 每页显示条目个数
   change: current-page 或 page-size 更改时触发事件
  -->
    <!-- 分页：绑定当前页，翻页时同步 URL 的 page 参数，便于从详情/编辑返回后仍停留在该页 -->
    <el-pagination
        background
        layout="prev, pager, next,jumper"
        :total="userPageInfo.total"
        :page-size="userPageInfo.pageSize"
        v-model:current-page="currentPage"
        @current-change="toPage"
    />
</template>

<script setup>

import { View, Edit, Delete } from '@element-plus/icons-vue'
import { ref, onMounted, inject } from 'vue';
import { doDelete, doGet } from '../http/httpRequest'
import { confirmMessage, showMessage } from '../util/message';
import { useRouter, useRoute } from 'vue-router'

const userPageInfo = ref([])
// 当前页码，与地址栏 ?page= 及分页组件双向同步
const currentPage = ref(1)
const route = useRoute()
const router = useRouter()

// 从路由 query 解析合法页码，非法或缺失时默认为 1
const resolvePageFromRoute = () => {
    const p = Number(route.query.page)
    return Number.isFinite(p) && p >= 1 ? Math.floor(p) : 1
}

onMounted(() => {
    // 进入列表时按 URL 中的 page 加载，便于从详情/编辑返回后恢复页码
    currentPage.value = resolvePageFromRoute()
    loadUserList(currentPage.value)
})

const loadUserList = (current) => {
    doGet('api/users', {
        current: current, // 当前页
    }).then((resp) => {
        console.log(resp.data)
        if (resp.data.code == 200) {
            // 将后端的分页数据对象 PageInfo 赋值给前端的分页数据对象 userPageInfo
            userPageInfo.value = resp.data.info
            // 若后端纠正了页码（例如当前页已无数据），与分页器同步
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

// 用户切换分页：地址栏与列表数据已随 v-model 更新当前页，这里只做 replace 与拉数
const toPage = (current) => {
    router.replace({ path: '/dashboard/user', query: { page: String(current) } })
    loadUserList(current)
}

// 查看用户详情（携带当前页，便于返回列表时恢复）
const view = (id) => {
    router.push({
        path: '/dashboard/user/' + id,
        query: { page: String(currentPage.value) },
    })
}

// 跳转到添加用户页面（携带当前页，便于返回/保存后回到原页）
const addUser = () => {
    router.push({
        path: '/dashboard/user/input',
        query: { page: String(currentPage.value) },
    })
}

// 编辑用户（携带当前页）
const edit = (id) => {
    router.push({
        path: '/dashboard/user/edit/' + id,
        query: { page: String(currentPage.value) },
    })
}

//删除用户
const del = (id) => {
   //弹出确认框
   confirmMessage('确认删除该用户吗？').then(() => {
      doDelete('api/user/' + id).then(resp => {
         if (resp.data.code == 200) {
            showMessage(resp.data.msg, 'success')
            //原生js刷新当前页面
            // location.reload()
            //调用局部刷新函数
            flushPage()
         }else{
            showMessage(resp.data.msg,'error')
         }
      })
   }).catch(() => {
       //取消删除
       console.log('取消删除')
       
   })
}

let idArray = [] 

//获取选中的用户id
const selectId = (item) => {
    //item就是选中的数据
    //遍历item，将id添加到数组中
    idArray = []

    for(let index in item){
        idArray.push(item[index].id)
    }

    console.log(idArray)
}

//批量删除
const batchDel = () => {
   //判断数组是否为空
   if(idArray.length == 0){
     showMessage('请选择要删除的用户','warning')
   }else{
      //弹出确认框
      confirmMessage('确认删除选中的用户吗？').then(() => {
         //数组 [1,2,6,8] =》 字符串 1,2,6,8
         let ids = idArray.join(',')
         //前面的ids是名字，后面的ids是值
         doDelete('api/user',{ids:ids}).then(resp => {
            if (resp.data.code == 200) {
               showMessage(resp.data.msg,'success')
               //原生js刷新当前页面
               //location.reload()
               //调用局部刷新函数
               flushPage()
            }else{
               showMessage(resp.data.msg,'error')
            }
         })
      }).catch(() => {
          //取消删除
          console.log('取消删除')
      })
   }

}


//注入 刷新函数
let flushPage = inject('flush')
</script>

<style scoped></style>