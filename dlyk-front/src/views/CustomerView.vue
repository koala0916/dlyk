<script setup>

import { ref, onMounted } from 'vue'
import { doGet, download } from '../http/httpRequest'
import { showMessage } from '../util/message'
import { saveAs } from 'file-saver'
import { useRouter } from 'vue-router'

//客户分页查询返回的对象，初始值是空 
let customerPageInfo = ref({})

//页面渲染后就加载客户列表数据，那么触发vue生命周期函数钩子
onMounted(() => {
    loadCustomerList(1) //页面渲染时，默认加载第一页数据
})

//加载客户列表的函数
const loadCustomerList = (current) => {
    doGet('/api/customers', {
        current: current, //当前是第几页，前面是参数名，后面是参数值
    }).then(resp => {
        if (resp.data.code === 200) {
            customerPageInfo.value = resp.data.info;
        } else {
            showMessage('数据加载失败', 'error');
        }
    })
}

//分页函数，触发change事件的时候，会自动传currentPage, pageSize这两个参数
const toPage = (currentPage, pageSize) => {
    loadCustomerList(currentPage);
}


//批量导出Excel
const batchExportExcel = () => {
    download('/api/exportExcel', { ids: '' }).then(resp => {
        //resp.data 后端返回的二进制数据，将二进制数据转换为blob对象
        saveAs(new Blob([resp.data]), '客户列表.xlsx')
    })
}


//存放选中数据的id数组
let idArray = []

//勾选或取消勾选时触发的事件
const handSelection = (item) => {
    //每次勾选或取消勾选时，都清空数组
    idArray = []

    //将选中的数据id添加到数组中
    for (let index in item) {
        idArray.push(item[index].id)
    }
}

//选择导出Excel
const chooseExportExcel = () => {
    if (idArray.length == 0) {
        showMessage('请选择要导出的客户', 'warning')
        return
    }

    //将数组转换为字符串，用逗号分隔   [1,2,3] => '1,2,3'
    let ids = idArray.join(',')
    download('/api/exportExcel', { ids: ids }).then(resp => {
        //resp.data 后端返回的二进制数据，将二进制数据转换为blob对象
        saveAs(new Blob([resp.data]), '客户列表.xlsx')
    })
}

let router = useRouter()
//查看客户详情
const view = (id) => {
    router.push('/dashboard/customer/' + id)
}
</script>

<template>
    <el-button type="primary" @click="batchExportExcel">批量导出(Excel)</el-button>
    <el-button type="success" @click="chooseExportExcel">选择导出(Excel)</el-button>

    <el-table :data="customerPageInfo.list" style="width: 100%" @selection-change="handSelection">
        <el-table-column type="selection" width="50" />
        <el-table-column type="index" label="序号" width="55" />
        <el-table-column property="ownerDO.name" label="负责人" />
        <el-table-column property="activityDO.name" label="所属活动" />
        <el-table-column property="clueDO.fullName" label="姓名">
            <template #default="scope">
                <a href="javascript:void(0)" @click="view(scope.row.id)">{{ scope.row.clueDO.fullName }}</a>
            </template>
        </el-table-column>
        <el-table-column property="appellationDO.typeValue" label="称呼" />
        <el-table-column property="clueDO.phone" label="手机" />
        <el-table-column property="clueDO.weixin" label="微信" />
        <el-table-column property="loanDO.typeValue" label="是否贷款" />
        <el-table-column property="intentionStateDO.typeValue" label="意向状态" />
        <el-table-column property="sourceDO.typeValue" label="客户来源" />
        <el-table-column property="productDO.name" label="意向产品" />
        <el-table-column property="nextContactTime" label="下次跟踪时间" />
        <el-table-column label="操作">
            <template #default="scope">
                <el-button type="primary" @click="view(scope.row.id)">详情</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next, jumper, total" :total="customerPageInfo.total"
        :page-size="customerPageInfo.pageSize" @change="toPage" />
</template>

<style scoped></style>
