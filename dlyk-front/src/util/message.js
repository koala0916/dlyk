import { ElMessage, ElMessageBox } from 'element-plus'

//封装消息确认窗口
export const confirmMessage = (msg) => {
    return ElMessageBox.confirm(msg, '系统提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    })
}

//封装消息提示
export const showMessage = (msg, type) => {
    ElMessage({
        message: msg,
        type: type,
        duration: 3000, //显示时间
        showClose: true,//是否显示关闭按钮
    })
}
