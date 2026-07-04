<template>
  <div class="page">
    <h1 class="page-title">抢课选课</h1>
    <section class="plain-panel">
      <div class="status-text">当前轮次：第一轮选课。已选同一课程时，需要先退选原教学班，再选择其他教学班。</div>
    </section>
    <section class="plain-panel" v-loading="loading">
      <el-table :data="rows" border>
        <el-table-column prop="course_code" label="课程代码" width="110" />
        <el-table-column prop="course_name" label="课程名称" min-width="150" />
        <el-table-column prop="class_name" label="教学班" min-width="160" />
        <el-table-column prop="teacher_name" label="教师" width="100" />
        <el-table-column label="时间地点" min-width="180">
          <template #default="{ row }">周{{ row.weekday }} {{ row.start_period }}-{{ row.end_period }}节 {{ row.classroom }}</template>
        </el-table-column>
        <el-table-column label="容量" width="130">
          <template #default="{ row }">{{ row.selected_count }} / {{ row.capacity }}</template>
        </el-table-column>
        <el-table-column label="状态" width="160">
          <template #default="{ row }">
            <el-tag v-if="row.selection_status === 'selected'" type="success">已选</el-tag>
            <el-tag v-else-if="row.default_class" type="info">默认教学班</el-tag>
            <el-tag v-else-if="row.waitlist_status === 'waiting'" type="warning">候补中</el-tag>
            <el-tag v-else-if="row.remaining_count <= 0" type="danger">已满</el-tag>
            <el-tag v-else>可选</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.selection_status === 'selected'" type="danger" plain size="small" @click="drop(row)">退选</el-button>
            <el-button v-else-if="row.remaining_count > 0" type="primary" size="small" @click="select(row)">选课</el-button>
            <el-button v-else type="warning" size="small" @click="waitlist(row)">候补</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { apiGet, apiPost } from '../../api/http'
import { useSessionStore } from '../../stores/session'

const session = useSessionStore()
const rows = ref<any[]>([])
const loading = ref(false)
const studentId = session.user?.related_id || 1
const roundId = 1

onMounted(load)

async function load() {
  loading.value = true
  try {
    rows.value = await apiGet<any[]>(`/student/${studentId}/available-courses`)
  } catch (error) {
    ElMessage.error((error as Error).message)
  } finally {
    loading.value = false
  }
}

async function select(row: any) {
  try {
    await apiPost(`/student/${studentId}/select`, { teachingClassId: row.teaching_class_id, roundId })
    ElMessage.success('选课成功')
    await load()
  } catch (error) {
    ElMessage.error((error as Error).message)
  }
}

async function waitlist(row: any) {
  try {
    await apiPost(`/student/${studentId}/waitlist`, { teachingClassId: row.teaching_class_id, roundId })
    ElMessage.success('已加入候补队列')
    await load()
  } catch (error) {
    ElMessage.error((error as Error).message)
  }
}

async function drop(row: any) {
  try {
    await ElMessageBox.confirm('退选后如需换班，需要重新选择或候补。确定退选吗？', '退选确认', { type: 'warning' })
    await apiPost(`/student/${studentId}/drop/${row.selection_id}`)
    ElMessage.success('退选成功')
    await load()
  } catch (error) {
    if (error instanceof Error) ElMessage.error(error.message)
  }
}
</script>
