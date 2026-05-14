<script setup>
import { ref, onMounted, inject } from "vue";
import { doGet, doPost } from "../http/httpRequest";
import { useRoute } from "vue-router";
import { showMessage } from "../util/message";

let route = useRoute();

let tranDetail = ref({
    stageDO: {},
    createByDO: {},
    editByDO: {}
})
let tranRemark = ref({})

onMounted(() => {
    //加载交易详情
    loadTranDetail();
})

//加载交易详情
const loadTranDetail = () => {
    //拿到路由地址中的tranId参数
    let id = route.params.tranId;

    
    doGet('/api/tran/' + id, {}).then(resp => {
        if (resp && resp.data.code === 200) {
            tranDetail.value = resp.data.info
            
        } else {
            showMessage('数据加载失败', 'error');
        }
    })
}


let updateTranDialogVisible = ref(false);

//显示弹窗
const updateTran = () => {
    updateTranDialogVisible.value = true;
}

let tranHistory = ref({})

let stageOptions = ref([])

//加载字典值
const loadDicValue = (dicType) => {
    doGet('/api/dic/' + dicType, {}).then(resp => {
        console.log("resp == ", resp)
        if (resp && resp.data.code === 200) {
            stageOptions.value = resp.data.info;
        } else {
            showMessage('数据加载失败', 'error');
        }
    })
}


let flushPage = inject('flush');

//更新交易阶段
const updateTranSubmit = () => {
    doPost('/api/tran/stage', {
        tranId: route.params.tranId,
        stage: tranHistory.value.stage,
        money: tranHistory.value.money,
        expectedDate: tranHistory.value.expectedDate,
    }).then(resp => {
        if (resp && resp.data.code === 200) {
            showMessage('交易阶段更新成功', 'success')
            flushPage()
        }else{
            showMessage('交易阶段更新失败','error')
        }
    })
}

</script>

<template>
    <el-form ref="clueRemarkRuleFormRef" :rules="tranRemarkRules" :model="tranRemark" label-width="121">

        <el-form-item label="ID">
            <div class="detail">&nbsp;{{ tranDetail.id }}</div>
        </el-form-item>

        <el-form-item label="交易流水号">
            <div class="detail">&nbsp;{{ tranDetail.tranNo }}</div>
        </el-form-item>

        <el-form-item label="客户详情">
            <div class="detail">&nbsp;<router-link
                    :to="'/dashboard/customer/' + tranDetail.customerId">查看客户详情</router-link></div>
        </el-form-item>

        <el-form-item label="交易金额">
            <div class="detail">&nbsp;{{ tranDetail.money }}</div>
        </el-form-item>

        <el-form-item label="预计成交时间">
            <div class="detail">&nbsp;{{ tranDetail.expectedDate }}</div>
        </el-form-item>

        <el-form-item label="交易阶段">
            <div class="detail">&nbsp;{{ tranDetail.stageDO.typeValue }}</div>
        </el-form-item>

        <el-form-item label="交易描述">
            <div class="detail">&nbsp;{{ tranDetail.description }}</div>
        </el-form-item>

        <el-form-item label="下次跟踪时间">
            <div class="detail">&nbsp;{{ tranDetail.nextContactTime }}</div>
        </el-form-item>

        <!-- <el-form-item label="创建人">
            <div class="detail">&nbsp;{{ tranDetail.createByDO.name }}</div>
        </el-form-item> -->

        <el-form-item label="编辑时间">
            <div class="detail">&nbsp;{{ tranDetail.editTime }}</div>
        </el-form-item>

        <!-- <el-form-item label="编辑人">
            <div class="detail">&nbsp;{{ tranDetail.editByDO.name }}</div>
        </el-form-item> -->

        <el-form-item label="跟踪记录" prop="noteContent">
            <el-input v-model="tranRemark.noteContent" style="padding-left: 0;" :rows="8" type="textarea"
                placeholder="请输入交易跟踪记录" />
        </el-form-item>

        <el-form-item label="跟踪方式" prop="noteWay">
            <el-select v-model="tranRemark.noteWay" placeholder="请选择跟踪方式" style="width: 100%;padding-left: 0;" clearable
                @click="loadDicValue('noteWay')">
                <el-option v-for="item in noteWayOptions" :key="item.id" :label="item.typeValue" :value="item.id" 
                   
                />
            </el-select>
        </el-form-item>

        <el-form-item>
            <el-button type="success" @click="customerRemarkSubmit(clueRemarkRuleFormRef)">提 交</el-button>
            <el-button type="primary" @click="updateTran">更新交易阶段</el-button>
            <el-button type="success" plain @click="goBack">返 回</el-button>
        </el-form-item>
    </el-form>

    <el-dialog title="交易阶段更新" v-model="updateTranDialogVisible" width="800">
        <el-form :model="tranHistory" label-width="100">
            <el-form-item label="交易阶段">
                <el-select v-model="tranHistory.stage" placeholder="请选择交易阶段" style="width: 100%;padding-left: 0;"
                    clearable @click="loadDicValue('stage')">
                        <el-option v-for="item in stageOptions" :key="item.id" :label="item.typeValue" :value="item.id" 
                        
                         :disabled="item.order <= tranDetail.stageDO.order"
                        />
                    </el-select>
            </el-form-item>
            
            <el-form-item label="交易金额">
                <el-input v-model="tranHistory.money" style="width: 100%;padding-left: 0;" placeholder="请输入交易金额" />
            </el-form-item>


            <el-form-item label="预计成交时间">
                <el-date-picker v-model="tranHistory.expectedDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="选择日期" style="width: 100%;padding-left: 0;"></el-date-picker>
            </el-form-item>
            
        </el-form>

        <template #footer>
            <el-button type="primary" @click="updateTranSubmit">更 新</el-button>
            <el-button @click="updateTranDialogVisible = false">取 消</el-button>
        </template>
    </el-dialog>
</template>

<style scoped></style>
