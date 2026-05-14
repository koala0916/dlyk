<script setup>
import { inject, onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { doGet, doPost } from "../http/httpRequest.js";
import { showMessage } from "../util/message.js";

let route = useRoute()

let customerRemark = ref({})
let customerRemarkPageInfo = ref({})

let customerDetail = ref({
    intentionProductDO: {},
    createByDO: {},
    editByDO: {}
})

onMounted(() => {
    loadCustomerDetail()
})

const loadCustomerDetail = () => {
    //拿到路由地中中的userId参数
    let id = route.params.customerId;

    doGet('/api/customer/' + id, {}).then(resp => {
        if (resp && resp.data.code === 200) {
            customerDetail.value = resp.data.info;
        } else {
            showMessage('数据加载失败', 'error');
        }
    })
}


//定义变量控制弹出窗口的展示与隐藏
let tranDialogVisible = ref(false)

//创建交易弹出
const createTran = () => {
    tranDialogVisible.value = true
}

//交易对象
let tran = ref({})

let stageOptions = ref([])

//加载阶段
const loadDicValue = (typeCode) => {
    doGet('/api/dic/'+typeCode, { }).then(resp => {
        if (resp && resp.data.code === 200) {
            stageOptions.value = resp.data.info;
        } else {
            showMessage('数据加载失败', 'error');
        }
    })
}


let flushPage = inject('flush')

//交易提交
const tranSubmit = () => {
    doPost('/api/tran', {
        customerId: route.params.customerId,
        stage: tran.value.stage,
        money: tran.value.money,
        expectedDate: tran.value.expectedDate,
        nextContactTime: tran.value.nextContactTime,
        description: tran.value.description
    }).then(resp => {
        if (resp && resp.data.code === 200) {
            showMessage('交易创建成功', 'success')
            tranDialogVisible.value = false
            flushPage()
        } else {
            showMessage('交易创建失败', 'error')
        }
    })
}

</script>

<template>
    <el-form label-width="121">

        <el-form-item label="ID">
            <!-- <div class="detail">&nbsp;{{ customerDetail.id }}</div> -->
        </el-form-item>

        <el-form-item label="客户详情">
            <div class="detail">&nbsp;<router-link :to="'/dashboard/clue/' + customerDetail.clueId">查看客户详情</router-link>
            </div>
        </el-form-item>

        <el-form-item label="意向产品">
            <!-- <div class="detail">&nbsp;{{ customerDetail.productDO.name }}</div> -->
        </el-form-item>

        <el-form-item label="客户描述">
            <div class="detail">&nbsp;{{ customerDetail.description }}</div>
        </el-form-item>

        <el-form-item label="下次跟踪时间">
            <div class="detail">&nbsp;{{ customerDetail.nextContactTime }}</div>
        </el-form-item>

        <el-form-item label="创建时间">
            <div class="detail">&nbsp;{{ customerDetail.createTime }}</div>
        </el-form-item>

        <!-- <el-form-item label="创建人">
            <div class="detail">&nbsp;{{ customerDetail.createByDO.name }}</div>
        </el-form-item> -->

        <el-form-item label="编辑时间">
            <div class="detail">&nbsp;{{ customerDetail.editTime }}</div>
        </el-form-item>

        <!-- <el-form-item label="编辑人">
            <div class="detail">&nbsp;{{ customerDetail.editByDO.name }}</div>
        </el-form-item> -->

        <el-form-item label="跟踪记录" prop="noteContent">
            <el-input v-model="customerRemark.noteContent" style="padding-left: 0;" :rows="8" type="textarea"
                placeholder="请输入线索跟踪记录" />
        </el-form-item>

        <el-form-item label="跟踪方式" prop="noteWay">
            <el-select v-model="customerRemark.noteWay" placeholder="请选择跟踪方式" style="width: 100%;padding-left: 0;"
                clearable @click="loadDicValue('noteWay')">
                <el-option v-for="item in noteWayOptions" :key="item.id" :label="item.typeValue" :value="item.id" />
            </el-select>
        </el-form-item>

        <el-form-item>
            <el-button type="success" @click="customerRemarkSubmit(clueRemarkRuleFormRef)">提 交</el-button>
            <el-button type="primary" @click="createTran">创建交易</el-button>
            <el-button type="success" plain @click="goBack">返 回</el-button>
        </el-form-item>

        <el-form-item>
            <el-table :data="customerRemarkPageInfo.list" style="width: 100%" @selection-change="handSelection">
                <el-table-column type="index" label="序号" width="65" />
                <el-table-column property="noteContent" label="跟踪内容" />
                <el-table-column property="createTime" label="创建时间" />
                <el-table-column property="createByDO.name" label="创建人" />
                <el-table-column property="editTime" label="编辑时间" />
                <el-table-column property="editByDO.name" label="编辑人" />
                <el-table-column label="操作" width="110">
                    <template #default="scope">
                        <el-button type="primary" :icon="Edit" circle @click="edit(scope.row.id)" />
                        <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)" />
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination background layout="prev, pager, next, jumper, total" :total="customerRemarkPageInfo.total"
                :page-size="customerRemarkPageInfo.pageSize" @change="toPage" />
        </el-form-item>
    </el-form>


    <!-- 创建交易对话框 -->
    <el-dialog title="创建交易" v-model="tranDialogVisible" width="45%">
        <el-form :model="tran" label-width="110">
            <el-form-item label="交易金额">
                <el-input v-model="tran.money" placeholder="请输入交易金额" />
            </el-form-item>

            <el-form-item label="预计成交时间">
                <el-date-picker v-model="tran.expectedDate" type="datetime" placeholder="选择预计成交时间"
                    value-format="YYYY-MM-DD HH:mm:ss" />
            </el-form-item>

            <el-form-item label="交易阶段">
                <el-select 
                    v-model="tran.stage" placeholder="请选择交易阶段" style="width: 100%;padding-left: 0;" clearable
                    @click="loadDicValue('stage')"
                   
                >
                    <el-option 
                    v-for="item in stageOptions" 
                    :key="item.id"
                     :label="item.typeValue" 
                     :value="item.id"
                      :disabled="item.order>1"
                      />
                </el-select>
            </el-form-item>

            <el-form-item label="交易描述"> 
                <el-input v-model="tran.description" placeholder="请输入交易描述" type="textarea" :rows="8" />
            </el-form-item>

            <el-form-item label="下次联系时间">
                <el-date-picker v-model="tran.nextContactTime" type="datetime" placeholder="下次联系时间"
                    value-format="YYYY-MM-DD HH:mm:ss" />
            </el-form-item>
        </el-form>

        <template #footer>
            <el-button type="primary" @click="tranDialogVisible = false">取 消</el-button>
            <el-button type="success" @click="tranSubmit">确 定</el-button>
        </template>
    </el-dialog>
</template>

<style scoped></style>
