<template>
  <!-- 日期搜索：支持时间段 / 单时间点；compact 用于一行多列时的紧凑布局 -->
  <div class="search-date-field" :class="{ compact }">
    <el-radio-group v-model="mode" size="small" class="mode-group">
      <el-radio-button value="range">时间段</el-radio-button>
      <el-radio-button value="point">时间点</el-radio-button>
    </el-radio-group>
    <el-date-picker
      v-if="mode === 'range'"
      v-model="rangeVal"
      type="datetimerange"
      start-placeholder="开始"
      end-placeholder="结束"
      value-format="YYYY-MM-DD HH:mm:ss"
      class="picker"
    />
    <template v-else>
      <el-date-picker v-model="pointVal" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="时间" class="picker-point" />
      <el-select v-model="pointTypeVal" class="point-type">
        <el-option label="之前" value="before" />
        <el-option label="之后" value="after" />
      </el-select>
    </template>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  start: String,
  end: String,
  point: String,
  pointType: String,
  /** 紧凑模式：与数值范围等同排展示 */
  compact: { type: Boolean, default: false },
})
const emit = defineEmits(['update:start', 'update:end', 'update:point', 'update:pointType'])

const mode = ref('range')
const rangeVal = ref(null)
const pointVal = ref(null)
const pointTypeVal = ref('after')

watch(rangeVal, (v) => {
  if (mode.value !== 'range') return
  emit('update:start', v && v[0] ? v[0] : null)
  emit('update:end', v && v[1] ? v[1] : null)
  emit('update:point', null)
  emit('update:pointType', null)
})

watch([pointVal, pointTypeVal, mode], () => {
  if (mode.value !== 'point') return
  emit('update:point', pointVal.value)
  emit('update:pointType', pointTypeVal.value)
  emit('update:start', null)
  emit('update:end', null)
})

watch(() => [props.start, props.end], () => {
  if (props.start || props.end) {
    rangeVal.value = props.start && props.end ? [props.start, props.end] : null
  }
}, { immediate: true })
</script>

<style scoped>
.search-date-field {
  display: flex;
  flex-direction: column;
  gap: 6px;
  width: 100%;
}
.search-date-field.compact {
  flex-direction: row;
  flex-wrap: wrap;
  align-items: center;
  gap: 4px;
}
.search-date-field.compact .mode-group {
  margin-bottom: 0;
  flex-shrink: 0;
}
.mode-group { margin-bottom: 2px; }
.picker { width: 100%; max-width: 360px; }
.search-date-field.compact .picker {
  flex: 1;
  min-width: 120px;
  max-width: none;
}
.picker-point { width: 200px; }
.search-date-field.compact .picker-point {
  width: auto;
  flex: 1;
  min-width: 100px;
}
.point-type { width: 88px; margin-left: 6px; }
.search-date-field.compact .point-type {
  margin-left: 0;
  flex-shrink: 0;
}
</style>
