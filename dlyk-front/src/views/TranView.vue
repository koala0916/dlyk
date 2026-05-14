<script setup>
import { onMounted, ref } from "vue";
import { doGet } from "../http/httpRequest.js";
import { showMessage } from "../util/message.js";
import { Check, Delete, Edit } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

let router = useRouter();

let tranPageInfo = ref({})

onMounted(() => {
    loadTranList(1)
})

//加载交易列表数据
const loadTranList = (current) => {
    doGet('/api/trans', {
        current: current,
    }).then(resp => {
        if (resp.data.code === 200) {
            tranPageInfo.value = resp.data.info;
        } else {
            showMessage('数据加载失败', 'error');
        }

    })
}

//分页函数，触发change事件的时候，会自动传currentPage, pageSize这两个参数
const toPage = (currentPage, pageSize) => {
    loadTranList(currentPage);
}

//查看详情
const view = (id) => {
    router.push('/dashboard/tran/' + id);
}





</script>

<template>
    <el-table :data="tranPageInfo.list" style="width: 100%" @selection-change="handSelection">
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" label="序号" width="55" />
        <el-table-column property="tranNo" label="交易流水号">
            <template #default="scope"><a href="javascript:void(0)" v-on:click="view(scope.row.id)">{{ scope.row.tranNo
            }}</a></template>
            <!-- 插槽：scope.row == user对象 -->
        </el-table-column>
        <el-table-column property="clueDO.fullName" label="客户姓名">
            <template #default="scope"><a href="javascript:void(0)" @click="view(scope.row.id)">{{
                scope.row.clueDO.fullName }}</a></template>
            <!-- 插槽：scope.row == user对象 -->
        </el-table-column>
        <el-table-column property="money" label="交易金额" />
        <el-table-column property="expectedDate" label="预计成交时间" />
        <el-table-column property="stageDO.typeValue" label="交易阶段" />
        <el-table-column property="nextContactTime" label="下次跟踪时间" />
        <el-table-column property="createTime" label="创建时间" />
        <el-table-column property="createByDO.name" label="创建人" />
        <el-table-column label="操作" width="145">
            <template #default="scope">
                <el-button type="success" :icon="Check" circle @click="view(scope.row.id)" />
                <el-button type="primary" :icon="Edit" circle @click="edit(scope.row.id)" />
                <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)" />
            </template>
        </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next, jumper, total" :total="tranPageInfo.total"
        :page-size="tranPageInfo.pageSize" @change="toPage" />
</template>

<style scoped></style>
