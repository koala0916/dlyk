/** ECharts 环形饼图配置（与官方示例风格一致） */
export function buildDonutOption(title, data) {
  return {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { top: '5%', left: 'center' },
    series: [
      {
        name: title,
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false, position: 'center' },
        emphasis: {
          label: { show: true, fontSize: 18, fontWeight: 'bold' },
        },
        labelLine: { show: false },
        data: data || [],
      },
    ],
  }
}

/** 将后端金额（可能是数字、字符串或 BigDecimal 对象）转为 ECharts 可用的 number */
function toChartNumber(v) {
  if (v == null) return 0
  if (typeof v === 'number' && !Number.isNaN(v)) return v
  if (typeof v === 'string') {
    const n = parseFloat(v)
    return Number.isNaN(n) ? 0 : n
  }
  if (typeof v === 'object') {
    if (v.value != null) return toChartNumber(v.value)
    if (v.amount != null) return toChartNumber(v.amount)
  }
  const n = Number(v)
  return Number.isNaN(n) ? 0 : n
}

export function buildLineOption(months, series) {
  const list = series || []
  return {
    tooltip: { trigger: 'axis' },
    legend: { top: 8, type: 'scroll' },
    grid: { left: '3%', right: '4%', top: 56, bottom: '3%', containLabel: true },
    xAxis: { type: 'category', boundaryGap: false, data: months || [] },
    yAxis: { type: 'value', name: '交易金额(元)', scale: true },
    series: list.map((s) => ({
      name: s.name,
      type: 'line',
      smooth: true,
      showSymbol: list.length <= 8,
      data: (s.data || []).map((v) => toChartNumber(v)),
    })),
  }
}

export function buildFunnelOption(data) {
  return {
    tooltip: { trigger: 'item', formatter: '{b}: {c}' },
    series: [
      {
        name: '销售漏斗',
        type: 'funnel',
        left: '10%',
        width: '80%',
        sort: 'descending',
        gap: 4,
        label: { show: true, position: 'inside' },
        data: (data || []).map((d) => ({ name: d.name, value: d.value })),
      },
    ],
  }
}
