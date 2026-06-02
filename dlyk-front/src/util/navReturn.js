/**
 * 跨模块跳转时的「返回上一页」约定：通过 query.from 记录来源完整路径
 */

/** query 中保存返回地址的键名 */
export const NAV_FROM_KEY = 'from'

/**
 * 去掉 fullPath 中已有的 from，避免 from 链式嵌套过长
 * @param {string} fullPath 例如 /dashboard/clue/5?page=3&from=...
 */
function stripFromParam(fullPath) {
  const qIndex = fullPath.indexOf('?')
  if (qIndex === -1) {
    return fullPath
  }
  const path = fullPath.slice(0, qIndex)
  const params = new URLSearchParams(fullPath.slice(qIndex + 1))
  params.delete(NAV_FROM_KEY)
  const rest = params.toString()
  return rest ? `${path}?${rest}` : path
}

/**
 * 生成带「返回来源」的 query（用于 router.push 的 query 字段）
 * @param {import('vue-router').RouteLocationNormalizedLoaded} route 当前路由
 * @param {Record<string, string>} [extra] 额外参数，如 page、customerId
 */
export function buildNavQuery(route, extra = {}) {
  const q = { ...extra }
  q[NAV_FROM_KEY] = encodeURIComponent(stripFromParam(route.fullPath))
  return q
}

/**
 * 点击「返回」：优先回到 query.from，否则使用 fallback
 * @param {import('vue-router').RouteLocationNormalizedLoaded} route
 * @param {import('vue-router').Router} router
 * @param {string | import('vue-router').RouteLocationRaw} fallback 无 from 时的默认跳转
 */
export function goBackFromRoute(route, router, fallback) {
  const raw = route.query[NAV_FROM_KEY]
  if (raw != null && String(raw) !== '') {
    try {
      const target = decodeURIComponent(String(raw))
      router.push(target)
      return
    } catch {
      /* 解码失败则走 fallback */
    }
  }
  if (typeof fallback === 'string') {
    router.push(fallback)
  } else if (fallback) {
    router.push(fallback)
  }
}

/**
 * 跳转到目标页，并自动附带当前页作为返回地址
 * @param {import('vue-router').Router} router
 * @param {import('vue-router').RouteLocationNormalizedLoaded} route
 * @param {string | { path: string, query?: Record<string, string> }} to
 * @param {Record<string, string>} [extraQuery] 额外 query
 */
export function pushWithReturn(router, route, to, extraQuery = {}) {
  const query = buildNavQuery(route, extraQuery)
  if (typeof to === 'string') {
    router.push({ path: to, query })
  } else {
    router.push({ path: to.path, query: { ...to.query, ...query } })
  }
}

/** 当前路由是否带有返回来源（用于列表页显示「返回」按钮） */
export function hasReturnFrom(route) {
  const raw = route.query[NAV_FROM_KEY]
  return raw != null && String(raw) !== ''
}
