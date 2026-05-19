<template>
  <div class="search-number-range">
    <el-select v-model="localOp" placeholder="条件" clearable class="op-select">
      <el-option label="等于" value="eq" />
      <el-option label="大于" value="gt" />
      <el-option label="小于" value="lt" />
      <el-option label="区间" value="between" />
    </el-select>
    <template v-if="localOp === 'between'">
      <el-input-number v-model="localMin" :controls="false" placeholder="最小" class="num-input" />
      <span class="sep">-</span>
      <el-input-number v-model="localMax" :controls="false" placeholder="最大" class="num-input" />
    </template>
    <el-input-number v-else v-model="localValue" :controls="false" placeholder="数值" class="num-input" />
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  op: String,
  value: Number,
  min: Number,
  max: Number,
})
const emit = defineEmits(['update:op', 'update:value', 'update:min', 'update:max'])

const localOp = computed({ get: () => props.op, set: (v) => emit('update:op', v) })
const localValue = computed({ get: () => props.value, set: (v) => emit('update:value', v) })
const localMin = computed({ get: () => props.min, set: (v) => emit('update:min', v) })
const localMax = computed({ get: () => props.max, set: (v) => emit('update:max', v) })
</script>

<style scoped>
.search-number-range { display: flex; align-items: center; gap: 6px; flex-wrap: wrap; }
.op-select { width: 88px; }
.num-input { width: 100px; }
.sep { color: #999; }
</style>
