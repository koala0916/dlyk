<template>
    <el-button type="primary" @click="addClue">录入线索</el-button>
    <el-button type="success" @click="clueDialogVisible = true">导入线索(Excel)</el-button>
    <el-button type="danger" @click="batchDel">批量删除</el-button>

    <el-table :data="cluePageInfo.list" style="width: 100%" @selection-change="selectId">
        <el-table-column type="selection" width="50" />
        <el-table-column type="index" label="序号" width="65" />
        <el-table-column property="ownerDO.name" label="负责人" width="120" />
        <el-table-column property="activityDO.name" label="所属活动" />
        <el-table-column label="姓名">
            <template #default="scope">
                <a href="javascript:void(0)" @click="view(scope.row.id)">{{ scope.row.fullName }}</a>
            </template>
        </el-table-column>
        <el-table-column property="phone" label="手机" width="120" />
        <el-table-column property="weixin" label="微信" width="120" />
        <el-table-column property="intentionStateDO.typeValue" label="意向状态" />
        <el-table-column property="intentionProductDO.name" label="意向产品" />
        <el-table-column property="stateDO.typeValue" label="线索状态" />
        <el-table-column property="sourceDO.typeValue" label="线索来源" />
        <el-table-column property="nextContactTime" label="下次联系时间" width="165" />
        <el-table-column label="操作" width="230">
            <template #default="scope">
                <el-button type="primary" @click="view(scope.row.id)">详情</el-button>
                <el-button type="success" @click="edit(scope.row.id)">编辑</el-button>
                <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>

    <el-pagination background layout="prev, pager, next, jumper, total" :total="cluePageInfo.total"
        :page-size="cluePageInfo.pageSize" @change="toPage" />


        <!-- 弹窗 -->
         <el-dialog v-model="clueDialogVisible" title="导入线索" width="40%" center>
            <!-- 
                ref 属性：给当前元素起一个名字，方便在其他地方使用
                action 属性：指定上传的地址
                method 属性：指定上传的方式  文件上传必须是 post
                name 属性：指定上传的文件的名称 与后端的参数名一致
                auto-upload 属性：指定是否自动上传  false 不自动上传
                headers 属性：指定上传的请求头
                on-success 属性：指定上传成功的回调函数  上传成功后，会调用这个函数  函数的参数是上传成功后的响应数据
                limit 属性：指定上传的文件的数量  1 表示只能上传一个文件
            -->
            <el-upload
                ref="uploadRef" 
                action="http://localhost:8088/api/importExcel"
                method="post"
                name="excelFile"
                :auto-upload="false"
                :headers="token"
                :on-success="uploadSuccess"
                limit="1"
            >
                <!-- 触发 -->
                <template #trigger>
                    <el-button type="primary">选择excel文件</el-button>
                </template>
            </el-upload>

            <template #footer>
                <el-button @click="clueDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="uploadExcel">导入</el-button>
            </template>
         </el-dialog>
</template>


<script setup>

import {onMounted,inject, ref } from "vue";
import { doGet } from "../http/httpRequest";
import { TOKEN_NAME } from "../util/constant";
import { showMessage } from "../util/message";
import { useRouter } from "vue-router";

//线索分页对象
let cluePageInfo = ref({})

//控制弹窗的变量
let clueDialogVisible = ref(false)

onMounted(() => {
    loadClueList(1)
})
//分页查询线索信息
const loadClueList = (current) => {
    doGet('/api/clue', { current:current }).then(resp => {
        if (resp.data.code == 200) {
            cluePageInfo.value = resp.data.info
        }else{
            showMessage(resp.data.message, 'error')
        }
    })
}

//分页查询
const toPage = (current) => {
    loadClueList(current)
}


//上传excel文件 携带token
const token = ref({
    'Authorization': sessionStorage.getItem(TOKEN_NAME)
})

//上传的ref对象
const uploadRef = ref()

//上传excel文件
const uploadExcel = () => {
    //上传文件
    uploadRef.value.submit()
}


let flushPage = inject('flush')

//上传成功的回调函数  上传成功后，会调用这个函数  函数的参数是上传成功后的响应数据
const uploadSuccess = (resp) => {
    //这里的回调函数（钩子函数）是element plus提供的 ，所以返回的resp与与之前(axios)不一致
    if (resp.code == 200) {
        showMessage('导入成功', 'success')
        //局部刷新页面
        flushPage()
    }else{
        showMessage('导入失败','error')
    }
}

let router = useRouter()
//查看详情
const view = (id) => {
    //路由到详情页面  携带id
    router.push('/dashboard/clue/'+id)
}
</script>

<style scoped></style>