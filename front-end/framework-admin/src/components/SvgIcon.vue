<script setup>
  const props = defineProps({
    // 图标名称
    name: String,
    // 图标颜色
    color: String,
    // 图标大小
    size: {
      // 字符串或数字类型
      type: [String, Number],
      // 默认值
      default: 1
    }
  })
</script>
<script>
  export default {
    name: 'SvgIcon'
  }
</script>
<template>
  <svg
    aria-hidden="true"
    :style="{
      width: props.size + 'em',
      height: props.size + 'em'
    }"
  >
    <use :href="`#icon-${props.name}`" :fill="props.color" />
  </svg>
</template>

<style lang="scss" scoped></style>
