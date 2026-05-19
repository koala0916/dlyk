/** 将搜索表单合并为 GET 请求参数，去掉空值 */
export function buildSearchParams(search, extra = {}) {
  const params = { ...extra }
  if (!search || typeof search !== 'object') {
    return params
  }
  Object.keys(search).forEach((key) => {
    const v = search[key]
    if (v === null || v === undefined || v === '') {
      return
    }
    if (Array.isArray(v) && v.length === 0) {
      return
    }
    params[key] = v
  })
  return params
}

/** 从日期范围组件取值 [start,end] */
export function pickRange(arr) {
  if (!arr || !Array.isArray(arr) || arr.length === 0) {
    return { start: null, end: null }
  }
  return { start: arr[0] || null, end: arr[1] || null }
}
