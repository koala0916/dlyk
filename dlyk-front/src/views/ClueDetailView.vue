<template>
    <el-form label-width="120px" :model="clueRemark" ref="clueRemarkFormRef">
        <el-form-item label="ID">
            <div class="detail">{{ clueDetail.id }}</div>
        </el-form-item>

        <el-form-item label="负责人">
            <div class="detail">{{ clueDetail.ownerDO.name }}</div>
        </el-form-item>

        <el-form-item label="所属活动">
            <div class="detail"> {{ clueDetail.activityDO.name }} </div>
        </el-form-item>

        <el-form-item label="姓名">
            <div class="detail"> {{ clueDetail.fullName }} </div>
        </el-form-item>

        <el-form-item label="称呼">
            <div class="detail">{{ clueDetail.appellationDO.typeValue }}</div>
        </el-form-item>

        <el-form-item label="手机">
            <div class="detail">{{ clueDetail.phone }}</div>
        </el-form-item>

        <el-form-item label="微信">
            <div class="detail">{{ clueDetail.weixin }}元</div>
        </el-form-item>

        <el-form-item label="QQ">
            <div class="detail">{{ clueDetail.qq }}</div>
        </el-form-item>

        <el-form-item label="邮箱">
            <div class="detail">{{ clueDetail.email }}</div>
        </el-form-item>

        <el-form-item label="年龄">
            <div class="detail">{{ clueDetail.age }}</div>
        </el-form-item>

        <el-form-item label="职业">
            <div class="detail">{{ clueDetail.job }}</div>
        </el-form-item>

        <el-form-item label="年收入">
            <div class="detail">{{ clueDetail.yearIncome }}</div>
        </el-form-item>

        <el-form-item label="住址">
            <div class="detail">{{ clueDetail.address }}</div>
        </el-form-item>

        <el-form-item label="是否贷款">
            <div class="detail">{{ clueDetail.loanDO.typeValue }}</div>
        </el-form-item>
        <el-form-item label="意向状态">
            <div class="detail">{{ clueDetail.intentionStateDO.typeValue }}</div>
        </el-form-item>
        <el-form-item label="意向产品">
            <div class="detail">{{ clueDetail.intentionProductDO.name }}</div>
        </el-form-item>
        <el-form-item label="线索状态">
            <div class="detail">{{ clueDetail.stateDO.typeValue }}</div>
        </el-form-item>
        <el-form-item label="线索来源">
            <div class="detail">{{ clueDetail.sourceDO.typeValue }}</div>
        </el-form-item>

        <el-form-item label="线索描述">
            <div class="detail">{{ clueDetail.description }}</div>
        </el-form-item>
        <el-form-item label="下次跟踪时间">
            <div class="detail">{{ clueDetail.nextContactTime }}</div>
        </el-form-item>
        <el-form-item label="创建时间">
            <div class="detail">{{ clueDetail.createTime }}</div>
        </el-form-item>
        <el-form-item label="创建人">
            <!-- <div class="detail">{{ clueDetail.createByDO.name }}</div> -->
        </el-form-item>
        <el-form-item label="编辑时间">
            <div class="detail">{{ clueDetail.editTime }}</div>
        </el-form-item>
        <el-form-item label="编辑人">
            <!-- <div class="detail">{{ clueDetail.editByDO.name }}</div> -->
        </el-form-item>

        <el-form-item label="线索跟踪记录" prop="noteContent">
            <!--
   rows 输入框行数，仅type为'textarea'时有效
   -->
            <el-input v-model="clueRemark.noteContent" rows="8" type="textarea" placeholder="请输入活动备注记录" />
        </el-form-item>


        <el-form-item label="跟踪方式">
            <el-select v-model="clueRemark.noteWay" placeholder="请选择" @click="loadDicData('noteWay')">
                <el-option v-for="item in noteWayOptions" :key="item.id" :label="item.typeValue" :value="item.id" />
            </el-select>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="clueRemarkSubmit()">提 交</el-button>
            <el-button type="success" @click="convertCustomer" v-if="clueDetail.state != -1">转换客户</el-button>
            <el-button type="primary" plain v-on:click="goBack">返 回</el-button>
        </el-form-item>

    </el-form>


    <el-dialog v-model="convertCustomerDialogVisible" title="转换客户" width="800">
        <el-form :model="customer" label-width="auto">
            <el-form-item label="购买产品">
                <el-select v-model="customer.product" placeholder="请选择" @click="loadProduct">
                    <el-option v-for="item in productOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="客户描述">
                <el-input v-model="customer.description" rows="8" type="textarea" placeholder="请输入客户描述" />
            </el-form-item>
            <el-form-item label="下次跟踪时间">
                <el-date-picker 
                  v-model="customer.nextContactTime"
                  type="datetime" 
                  value-format="YYYY-MM-DD HH:mm:ss"
                  placeholder="选择日期时间">
                </el-date-picker>
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="convertCustomerDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="convertCustomerSubmit">确 认</el-button>
        </template>
    </el-dialog>

</template>

<script setup>
import { ref, onMounted, inject } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { doGet, doPost } from '../http/httpRequest';
import { showMessage } from '../util/message';


let route = useRoute()
let router = useRouter

//转换客户弹窗是否展示
let convertCustomerDialogVisible = ref(false)

//表单顾客对象
let customer = ref({})
//购买产品
let productOptions = ref([{}])
//点击转换客户弹出模态框
const convertCustomer = () => {
    convertCustomerDialogVisible.value = true
}

//加载产品
const loadProduct = () => {
    doGet('/api/product', {}).then(resp => {
        if (resp.data.code == 200) {
            productOptions.value = resp.data.info
        } else {
            showMessage(resp.data.message, 'error')
        }
    })
}

//点击转换客户提交
const convertCustomerSubmit = () => {
   doPost('/api/customer', {
      clueId: route.params.clueId, //线索id
      product: customer.value.product, //购买产品
      description: customer.value.description, //客户描述
      nextContactTime: customer.value.nextContactTime //下次跟踪时间
   }).then(resp => {
    if (resp.data.code == 200) {
        showMessage(resp.data.message,'success')
        //局部刷新
        flushPage()
        //关闭弹窗
        convertCustomerDialogVisible.value = false
      }else{
        showMessage(resp.data.message,'error')
      }
   })
}


//线索跟踪方式
let noteWayOptions = ref([{}])

//线索备注
let clueRemark = ref({})

//线索明细对象
let clueDetail = ref({
    ownerDO: {},
    activityDO: {},
    appellationDO: {},
    loanDO: {},
    intentionStateDO: {},
    intentionProductDO: {},
    stateDO: {},
    sourceDO: {},
    createByDO: {},
    editByDO: {}
})

//页面渲染之后
onMounted(() => {
    loadClueDetail()
})

//加载线索明细信息
const loadClueDetail = () => {
    //获取参数id
    let id = route.params.clueId


    doGet('/api/clue/' + id, {}).then(resp => {
        if (resp.data.code == 200) {
            clueDetail.value = resp.data.info
        } else {
            showMessage(resp.data.message, 'error')
        }
    })
}



//加载字典数据 跟踪方式noteWay
const loadDicData = (dicCode) => {
    doGet('/api/dic/' + dicCode, {}).then(resp => {
        if (resp.data.code == 200) {
            noteWayOptions.value = resp.data.info
        } else {
            showMessage(resp.data.message, 'error')
        }
    })
}


let flushPage = inject('flush')

//线索详情备注添加
const clueRemarkSubmit = () => {
   doPost('/api/clue/remark', {
    clueId: route.params.clueId, //线索id
    noteContent: clueRemark.value.noteContent, //备注内容
    noteWay: clueRemark.value.noteWay //备注方式
   }).then(resp => {
      if (resp.data.code == 200) {
        showMessage(resp.data.message, 'success')
        //局部刷新
        flushPage()
      }else{
        showMessage(resp.data.message,'error')
      }
   })
}
</script>

<style scoped></style>