//导入axios
import axios from 'axios'

import { TOKEN_NAME } from '../util/constant'
import { confirmMessage, showMessage } from '../util/message';

//设置请求的基础路径
axios.defaults.baseURL = 'http://localhost:8088'


//添加请求拦截器
axios.interceptors.request.use(function (config) {
  // 将token添加到请求头中
  let token = sessionStorage.getItem(TOKEN_NAME);
  if (token) {
    //将token放入请求头的Authorization字段中
    config.headers.Authorization = token;
  }

  return config;
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error);
});

//添加响应拦截器
axios.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  if (response && response.data.code > 900) {
    confirmMessage(response.data.msg + "，是否重新登录？").then(() => { // 点击确认后执行的操作
      // 点击确定后执行的操作，例如重新登录
      sessionStorage.removeItem(TOKEN_NAME) // 清除token
      location.href = "/" // 跳转到登录页面
    }).catch(() => { // 点击取消后执行的操作
      showMessage('取消重新登录', "info")
    })

  } else {
    return response;
  }

}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});


//封装post请求，导出我们定义的doPost函数
export const doPost = (url, param) => {
  return axios({
    method: 'post',
    url: url,
    responseType: 'json',
    data: param,
  })
}

//封装get请求，导出我们定义的doGet函数
export const doGet = (url, param) => {
  return axios({
    method: 'get',
    url: url,
    responseType: 'json',
    params: param,
  })
}

//封装put请求，导出我们定义的doPut函数
export const doPut = (url, param) => {
  return axios({
    method: 'put',
    url: url,
    responseType: 'json',
    data: param,
  })
}

//封装delete请求，导出我们定义的doDelete函数
export const doDelete = (url, param) => {
  return axios({
    method: 'delete',
    url: url,
    responseType: 'json',
    params: param,
  })
}


//下载函数
export const download = (url, param) => {
  return axios({
    method: 'get',
    url: url,
    responseType: 'blob', // 告诉axios返回的数据类型是blob  方便二进制处理
    params: param,
  })
}