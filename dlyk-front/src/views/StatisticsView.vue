<template>
    <el-row>
        <!-- 概览统计 -->
        <el-col :span="6">
            <el-statistic :value="summaryData.goingActivityCount">
                <template #title>
                    <div style="display: inline-flex; align-items: center">
                        市场活动
                        <el-icon style="margin-left: 4px" :size="12">
                            <Male />
                        </el-icon>
                    </div>
                </template>
                <template #suffix>/{{ summaryData.totalActivityCount }}</template>
            </el-statistic>
        </el-col>
        <el-col :span="6">
            <el-statistic title="线索总数" :value="summaryData.totalClueCount" />
        </el-col>
        <el-col :span="6">
            <el-statistic title="客户总数" :value="summaryData.totalCustomerCount" />
        </el-col>
        <el-col :span="6">
            <el-statistic :value="summaryData.successTranAmount">
                <template #title>
                    <div style="display: inline-flex; align-items: center">
                        交易总额
                        <el-icon style="margin-left: 4px" :size="12">
                            <Male />
                        </el-icon>
                    </div>
                </template>
                <template #suffix>/{{ summaryData.totalTranAmount }}</template>
            </el-statistic>
        </el-col>
    </el-row>

    <!-- 销售漏斗图 -->
    <div id="saleFunnelChart" style="width: 500px;height: 500px;"></div>

    <!-- 线索来源饼图 -->
    <div id="cluePieChart" style="width: 500px;height: 500px;"></div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { doGet } from '../http/httpRequest'
import { showMessage } from '../util/message'
import * as echarts from 'echarts'

let summaryData = ref({})

onMounted(() => {
    loadSummaryData()
    loadSaleFunnelChart()
    loadCluePieData()
})

// 加载统计数据
const loadSummaryData = () => {
    doGet('/api/summary/data', {}).then(resp => {
        if (resp && resp.data.code === 200) {
            summaryData.value = resp.data.info;
        } else {
            showMessage('数据加载失败', 'error');
        }
    })
}


//销售漏斗图
const loadSaleFunnelChart = () => {
    doGet('/api/sale/funnel', {}).then(resp => {
        if (resp && resp.data.code === 200) {
            // 获取漏斗数据
            let funnelData = resp.data.info
            console.log(funnelData)
            //获取图表展示的div的对象
            let chartDom = document.getElementById('saleFunnelChart')
            //创建一个echarts实例，并传入图表显示的位置
            let myChart = echarts.init(chartDom)
            //3、配置图表的可选项（也就是配置图表的显示效果）
            let option = {
                //标题组件配置，包含主标题和副标题。
                title: {
                    //主标题文本
                    text: '销售漏斗图',
                    //title组件离容器左侧的距离
                    left: 'center',
                    //title组件离容器上侧的距离
                    top: 'bottom'
                },
                //提示框组件
                tooltip: {
                    //触发类型， item：数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
                    trigger: 'item',
                    //提示框浮层内容格式器，饼图、仪表盘、漏斗图: {a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
                    formatter: '{a} <br/>{b} : {c}%'
                },
                //工具栏
                toolbox: {
                    //各工具配置项
                    feature: {
                        //数据视图工具，可以展现当前图表所用的数据，编辑后可以动态更新
                        dataView: {
                            //是否不可编辑（只读）
                            readOnly: true
                        },
                        //配置项还原
                        restore: {},
                        //保存为图片
                        saveAsImage: {}
                    }
                },
                //图例组件
                legend: {
                    //图例的数据数组,数据要和数据项目的名称相同
                    //data: ['Show', 'Click', 'Visit', 'Inquiry', 'Order']
                },
                //系列，总共有22个系列，一个系列是一种类型的图表
                series: [
                    {
                        //系列名称
                        name: '销售数据统计',
                        //漏斗图系列类型
                        type: 'funnel',
                        //漏斗图组件离容器左侧的距离
                        left: '10%',
                        //漏斗图组件离容器上侧的距离
                        top: 40,
                        //漏斗图组件离容器下侧的距离
                        bottom: 40,
                        //漏斗图组件的宽度。默认自适应
                        width: '90%',
                        //指定的数据最小值
                        min: 0,
                        //指定的数据最大值
                        max: 100,
                        //数据最小值 min 映射的宽度
                        minSize: '0%',
                        //数据最小值max映射的宽度。
                        maxSize: '100%',
                        //数据排序
                        sort: 'descending',
                        //数据图形间距
                        gap: 2,
                        //漏斗图图形上的文本标签，可用于说明图形的一些数据信息，比如值，名称等。
                        label: {
                            //漏斗图图形上的文本是否显示
                            show: true,
                            //漏斗图图形上的文本显示的位置
                            position: 'inside'
                        },
                        //标签的视觉引导线样式
                        labelLine: {
                            //视觉引导线的长度
                            length: 10,
                            //视觉引导线的样式
                            lineStyle: {
                                //视觉引导线的宽度
                                width: 1,
                                //视觉引导线的类型
                                type: 'solid'
                            }
                        },
                        //图形样式
                        itemStyle: {
                            //图形的描边颜色
                            borderColor: '#fff',
                            borderWidth: 1
                        },
                        //高亮的标签文本和图形样式
                        emphasis: {
                            //标签的文本
                            label: {
                                //标签的文本字体大小
                                fontSize: 30
                            }
                        },
                        //数据项，系列中的数据内容数组
                        // data: [
                        //  //数据项名称， 数据项值
                        //  { name: '线索', value: 100 },
                        //  { name: '客户', value: 40 },
                        //  { name: '交易', value: 20 },
                        //  { name: '成交', value: 10 },
                        // ]
                        data: funnelData
                    }
                ]
            };
            //4、如果配置了图表的可选项，把可选项设置到div元素（让图表在div元素位置显示）
            option && myChart.setOption(option);
        } else {
            showMessage('数据加载失败', 'error');
        }
    })
}



//线索来源饼图
const loadCluePieData = () => {
    doGet('/api/clue/pie', {}).then(resp => {
        if (resp) {
            if (resp.data.code === 200) {
                let pieChatData = resp.data.info;
                //1、根据id拿到div元素
                let chartDom = document.getElementById('cluePieChart');
                //2、初始化div元素
                let myChart = echarts.init(chartDom);
                //3、配置图表的可选项（也就是配置图表的显示效果）
                let option = {
                    //标题组件配置，包含主标题和副标题。
                    title: {
                        //主标题文本
                        text: '线索来源统计',
                        //title组件离容器左侧的距离
                        left: 'center',
                        //title组件离容器上侧的距离
                        top: 'bottom'
                    },
                    //提示框
                    tooltip: {
                        trigger: 'item',
                        //提示框浮层内容格式器，饼图、仪表盘、漏斗图: {a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
                        formatter: '{a} <br/>{b} : {c}条'
                    },
                    //图例
                    legend: {
                    },
                    //系列
                    series: [
                        {
                            //系列名称
                            name: '线索来源统计',
                            //系列图的类型是饼图
                            type: 'pie',
                            //饼图的半径(前面的值是 内圈半径，后面的值是 外圈半径)
                            radius: ['30%', '80%'],
                            //是否启用防止标签重叠策略
                            avoidLabelOverlap: false,
                            padAngle: 5,
                            itemStyle: {
                                borderRadius: 10
                            },
                            label: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                label: {
                                    show: true,
                                    fontSize: 40,
                                    fontWeight: 'bold'
                                }
                            },
                            labelLine: {
                                show: false
                            },
                            //数据项，系列中的数据内容数组
                            // data: [
                            //  { name: 'Search Engine', value: 1048 },
                            //  { name: 'Direct', value: 735 },
                            //  { name: 'Email', value: 580 },
                            //  { name: 'Union Ads', value: 484 },
                            //  { name: 'Video Ads', value: 300 }
                            // ]
                            data: pieChatData
                        }
                    ]
                };
                //4、如果配置了图表的可选项，把可选项设置到div元素（让图表在div元素位置显示）
                option && myChart.setOption(option);
            }
        }
    })
}
</script>

<style scoped></style>