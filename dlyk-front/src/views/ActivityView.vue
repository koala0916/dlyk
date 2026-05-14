<template>
    <el-form :model="activity">
        <el-form-item label="负责人">
            <el-select v-model="activity.ownerId" placeholder="请选择负责人" clearable @click="loadOwner">
                <el-option v-for="item in ownerOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
        </el-form-item>

        <el-form-item label="活动名称">
            <el-input v-model="activity.name" placeholder="请输入活动名称" clearable></el-input>
        </el-form-item>


        <el-form-item label="活动时间">
            <el-date-picker v-model="activity.activityTime" type="datetimerange" start-placeholder="活动开始时间"
                end-placeholder="活动结束时间" format="YYYY-MM-DD HH:mm:ss" date-format="YYYY-MM-DD" time-format="HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss" clearable />
        </el-form-item>


        <el-form-item label="活动预算">
            <el-input v-model="activity.cost" placeholder="请输入活动名词" clearable />
        </el-form-item>


        <el-form-item label="创建时间">
            <el-date-picker v-model="activity.createTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择创建时间" clearable />
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="search">搜 索</el-button>
            <el-button type="primary" plain>重 置</el-button>
        </el-form-item>
    </el-form>



    <el-button type="primary" @click="addActivity">录入市场活动</el-button>
    <el-button type="danger" @click="batchDel">批量删除</el-button>

    <el-table :data="activityPageInfo.list" style="width: 100%" @selection-change="selectId">
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column property="ownerDO.name" label="负责人" />
        <el-table-column property="name" label="活动名称" />
        <el-table-column property="startTime" label="开始时间" />
        <el-table-column property="endTime" label="结束时间" />
        <el-table-column property="cost" label="活动预算" />
        <el-table-column property="createTime" label="创建时间" show-overflow-tooltip />
        <el-table-column label="操作">
            <template #default="scope">
                <el-button type="success" :icon="Check" circle @click="view(scope.row.id)" />
                <el-button type="primary" :icon="Edit" circle @click="edit(scope.row.id)" />
                <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)" />
            </template>
        </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next, jumper, total" :total="activityPageInfo.total"
        :page-size="activityPageInfo.pageSize" @change="toPage" />

</template>

<script setup>
import { ref, onMounted } from "vue";
import { doGet } from "../http/httpRequest";
import { showMessage } from "../util/message";
import { Check, Edit, Delete } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

let activity = ref({})

//对象数组，用于存储负责人信息
let ownerOptions = ref([{}])

//获取负责人信息
const loadOwner = () => {
    doGet('api/owners', {}).then(resp => {
        if (resp.data.code == 200) {
            ownerOptions.value = resp.data.info
        } else {
            showMessage(resp.data.message, 'error')
        }
    })
}

//页面加载完成后，获取活动信息
onMounted(() => {
    //获取第一页活动信息
    loadActivityList(1)
})

//分页活动对象
let activityPageInfo = ref({})

const loadActivityList = (current) => {
    let timeArray = []
    let activityTime = activity.value.activityTime

    for (let index in activityTime) {
        timeArray.push(activityTime[index])
    }

    doGet('api/activities', {
        current: current,
        ownerId: activity.value.ownerId,
        name: activity.value.name,
        startTime: timeArray[0],
        endTime: timeArray[1],
        cost: activity.value.cost,
        createTime: activity.value.createTime
    }).then(resp => {
        if (resp.data.code == 200) {
            activityPageInfo.value = resp.data.info
        } else {
            showMessage(resp.data.message, 'error')
        }
    })
}

//搜索
const search = () => {
    loadActivityList(1)
}



//页码发生变化的时候会执行该函数
const toPage = (current) => {
    loadActivityList(current)
}


//跳转详细页面
let router = useRouter()

const view = (id) => {
    router.push('/dashboard/activity/' + id)
}

</script>

<style scoped></style>