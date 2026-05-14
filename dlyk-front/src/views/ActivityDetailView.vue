<template>
    <el-form ref="activityRemarkRefForm" label-width="150px" :model="activityRemark" :rules="activityRemarkRules">
        <el-form-item label="ID">
            <div class="detail">{{ activity.id }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="负责人">
            <div class="detail"> {{ activity.ownerDO.name }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="活动名称">
            <div class="detail">{{ activity.name }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="开始时间">
            <div class="detail">{{ activity.startTime }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="结束时间">
            <div class="detail">{{ activity.endTime }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="活动预算">
            <div class="detail">{{ activity.cost }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="活动描述">
            <div class="detail">{{ activity.description }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="创建时间">
            <div class="detail">{{ activity.createTime }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="创建人">
            <div class="detail">{{ activity.createByDO.name }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="编辑时间">
            <div class="detail">{{ activity.editTime }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="编辑人">
            <div class="detail">{{ activity.editByDO.name }}&nbsp;</div>
        </el-form-item>

        <el-form-item label="填写备注">
            <el-input v-model="activityRemark.noteContent" type="textArea" placeholder="请输入活动备注"></el-input>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="activityRemarkSubmit()">提 交</el-button>
            <el-button @click="goBack">返 回</el-button>
        </el-form-item>
    </el-form>


    <el-form-item>
        <el-table :data="activityRemarkPageInfo.list" style="width: 100%">
            <el-table-column type="index" label="序号" width="60" />
            <el-table-column property="noteContent" label="备注内容" />
            <el-table-column property="createTime" label="创建时间" />
            <el-table-column property="createByDO.name" label="创建人" />
            <el-table-column property="editTime" label="编辑时间" />
            <el-table-column property="editByDO.name" label="编辑人" show-overflow-tooltip />
            <el-table-column label="操作">
                <template #default="scope">
                    <el-button type="primary" :icon="Edit" circle @click="edit(scope.row.id)" />
                    <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)" />
                </template>
            </el-table-column>
        </el-table>
        <el-pagination background layout="prev, pager, next, jumper, total" :total="activityRemarkPageInfo.total"
            :page-size="activityRemarkPageInfo.pageSize" @change="toPage" />
    </el-form-item>


</template>

<script setup>

import { ref, onMounted, inject } from 'vue'
import { useRouter, useRoute } from 'vue-router';
import { doGet, doPost } from '../http/httpRequest';
import { showMessage } from '../util/message';

let activity = ref({
    ownerDO: {},
    createByDO: {},
    editByDO: {}
})

//活动备注
let activityRemark = ref({})

//分页查询活动备注
let activityRemarkPageInfo = ref({})

let router = useRouter()//调路由方法
//返回
const goBack = () => {
    router.back()
}


//获取活动信息
let route = useRoute()//获取路由信息

onMounted(() => {
    loadActivity()
    //分页查询活动备注
    loadActivityRemarkPage(1)
})

const loadActivity = () => {
    //获取url中的id
    let id = route.params.activityId;

    doGet('/api/activity/' + id, {}).then(resp => {
        if (resp.data.code == 200) {
            activity.value = resp.data.info
        } else {
            showMessage(resp.data.message, 'error')
        }
    })

}

//注入刷新函数
let flushPage = inject('flush')

//活动备注提交
const activityRemarkSubmit = () => {
    //通过json数据给后端传参
    doPost('/api/activity/remark', {
        activityId: activity.value.id,
        noteContent: activityRemark.value.noteContent
    }).then(resp => {
        if (resp.data.code == 200) {
            showMessage(resp.data.message, 'success')
            flushPage()//局部刷新
        } else {
            showMessage(resp.data.message, 'error')
        }
    })
}


//分页查询活动备注
const loadActivityRemarkPage = (current) => {
    let activityId = route.params.activityId;
    doGet('/api/activity/remark', {
        current: current,
        activityId: activityId //活动
    }).then(resp => {
        if (resp.data.code == 200) {
            activityRemarkPageInfo.value = resp.data.info
        } else {
            showMessage(resp.data.message, 'error')
        }
    })
}

const toPage = (current) => {
    loadActivityRemarkPage(current)
}
</script>
<style></style>