<template>
  <div class="page">
    <h1 class="page-title">{{ config.title }}</h1>
    <section class="plain-panel">
      <div class="toolbar">
        <el-button type="primary" @click="openCreate">新增</el-button>
        <el-button @click="load">刷新</el-button>
      </div>
      <el-table :data="rows" border height="560" v-loading="loading">
        <el-table-column
          v-for="column in config.columns"
          :key="column.prop"
          :prop="column.prop"
          :label="column.label"
          :width="column.width"
          min-width="130"
        >
          <template #default="{ row }">
            {{ displayValue(column.prop, row[column.prop]) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑' : '新增'" width="640px">
      <el-form label-width="120px">
        <el-form-item v-for="field in visibleFields" :key="field.prop" :label="field.label">
          <el-select
            v-if="field.type === 'select'"
            v-model="form[field.prop]"
            clearable
            filterable
            placeholder="选择"
            no-data-text="暂无数据"
            style="width: 100%"
            @change="onFieldChange(field)"
          >
            <el-option v-for="option in optionsFor(field)" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>
          <el-switch v-else-if="field.type === 'boolean'" v-model="form[field.prop]" active-text="是" inactive-text="否" />
          <el-date-picker v-else-if="field.type === 'date'" v-model="form[field.prop]" value-format="YYYY-MM-DD" placeholder="选择日期" style="width: 100%" />
          <el-date-picker
            v-else-if="field.type === 'datetime'"
            v-model="form[field.prop]"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="选择时间"
            style="width: 100%"
          />
          <el-input v-else-if="field.type === 'textarea'" v-model="form[field.prop]" type="textarea" :rows="3" placeholder="请输入" />
          <el-input v-else-if="field.type === 'number'" v-model="form[field.prop]" inputmode="decimal" placeholder="请输入" />
          <el-input v-else v-model="form[field.prop]" placeholder="请输入" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { apiDelete, apiGet, apiPost, apiPut } from '../../api/http'

interface FieldConfig {
  prop: string
  label: string
  type?: 'text' | 'number' | 'select' | 'boolean' | 'date' | 'datetime' | 'textarea'
  optionsKey?: string
  uiOnly?: boolean
}

interface ResourceConfig {
  title: string
  id: string
  columns: Array<{ prop: string; label: string; width?: string | number }>
  fields: FieldConfig[]
}

const route = useRoute()
const rows = ref<any[]>([])
const lookups = ref<Record<string, any[]>>({})
const loading = ref(false)
const dialogVisible = ref(false)
const editingId = ref<number | null>(null)
const form = reactive<Record<string, any>>({})

const configs: Record<string, ResourceConfig> = {
  colleges: {
    title: '学院管理',
    id: 'college_id',
    columns: [
      { prop: 'college_code', label: '学院编码' },
      { prop: 'college_name', label: '学院名称' },
      { prop: 'contact_phone', label: '联系电话' },
      { prop: 'status', label: '状态' }
    ],
    fields: [
      { prop: 'college_code', label: '学院编码' },
      { prop: 'college_name', label: '学院名称' },
      { prop: 'contact_phone', label: '联系电话' },
      { prop: 'status', label: '状态', type: 'select', optionsKey: 'status' }
    ]
  },
  majors: {
    title: '专业管理',
    id: 'major_id',
    columns: [
      { prop: 'major_code', label: '专业编码' },
      { prop: 'major_name', label: '专业名称' },
      { prop: 'college_name', label: '所属学院' },
      { prop: 'duration_years', label: '学制' },
      { prop: 'min_graduate_credit', label: '最低毕业学分' },
      { prop: 'status', label: '状态' }
    ],
    fields: [
      { prop: 'major_code', label: '专业编码' },
      { prop: 'major_name', label: '专业名称' },
      { prop: 'college_id', label: '所属学院', type: 'select', optionsKey: 'colleges' },
      { prop: 'duration_years', label: '学制', type: 'number' },
      { prop: 'degree_type', label: '学位类型' },
      { prop: 'min_graduate_credit', label: '最低毕业学分', type: 'number' },
      { prop: 'status', label: '状态', type: 'select', optionsKey: 'status' }
    ]
  },
  'admin-classes': {
    title: '行政班管理',
    id: 'admin_class_id',
    columns: [
      { prop: 'class_code', label: '班级编码' },
      { prop: 'class_name', label: '班级名称' },
      { prop: 'college_name', label: '学院' },
      { prop: 'major_name', label: '专业' },
      { prop: 'grade_year', label: '年级' },
      { prop: 'head_teacher_name', label: '班主任' },
      { prop: 'status', label: '状态' }
    ],
    fields: [
      { prop: 'class_code', label: '班级编码' },
      { prop: 'class_name', label: '班级名称' },
      { prop: 'college_id', label: '学院', type: 'select', optionsKey: 'colleges', uiOnly: true },
      { prop: 'major_id', label: '专业', type: 'select', optionsKey: 'majors' },
      { prop: 'grade_year', label: '年级', type: 'number' },
      { prop: 'head_teacher_id', label: '班主任', type: 'select', optionsKey: 'teachers' },
      { prop: 'status', label: '状态', type: 'select', optionsKey: 'status' }
    ]
  },
  students: {
    title: '学生管理',
    id: 'student_id',
    columns: [
      { prop: 'student_no', label: '学号' },
      { prop: 'student_name', label: '姓名' },
      { prop: 'gender', label: '性别' },
      { prop: 'class_name', label: '行政班' },
      { prop: 'phone', label: '联系电话' },
      { prop: 'status', label: '状态' }
    ],
    fields: [
      { prop: 'student_no', label: '学号' },
      { prop: 'student_name', label: '姓名' },
      { prop: 'gender', label: '性别', type: 'select', optionsKey: 'gender' },
      { prop: 'age', label: '年龄', type: 'number' },
      { prop: 'phone', label: '联系电话' },
      { prop: 'admin_class_id', label: '行政班', type: 'select', optionsKey: 'adminClasses' },
      { prop: 'region_id', label: '生源地', type: 'select', optionsKey: 'regions' },
      { prop: 'status', label: '状态', type: 'select', optionsKey: 'studentStatus' }
    ]
  },
  teachers: {
    title: '教师管理',
    id: 'teacher_id',
    columns: [
      { prop: 'teacher_no', label: '工号' },
      { prop: 'teacher_name', label: '姓名' },
      { prop: 'gender', label: '性别' },
      { prop: 'title', label: '职称' },
      { prop: 'college_name', label: '学院' },
      { prop: 'phone', label: '联系电话' },
      { prop: 'status', label: '状态' }
    ],
    fields: [
      { prop: 'teacher_no', label: '工号' },
      { prop: 'teacher_name', label: '姓名' },
      { prop: 'gender', label: '性别', type: 'select', optionsKey: 'gender' },
      { prop: 'age', label: '年龄', type: 'number' },
      { prop: 'title', label: '职称', type: 'select', optionsKey: 'teacherTitle' },
      { prop: 'phone', label: '联系电话' },
      { prop: 'college_id', label: '学院', type: 'select', optionsKey: 'colleges' },
      { prop: 'status', label: '状态', type: 'select', optionsKey: 'teacherStatus' }
    ]
  },
  terms: {
    title: '学期管理',
    id: 'term_id',
    columns: [
      { prop: 'academic_year', label: '学年' },
      { prop: 'semester', label: '学期' },
      { prop: 'start_date', label: '开始日期' },
      { prop: 'end_date', label: '结束日期' },
      { prop: 'is_current', label: '当前学期' }
    ],
    fields: [
      { prop: 'academic_year', label: '学年' },
      { prop: 'semester', label: '学期', type: 'number' },
      { prop: 'start_date', label: '开始日期', type: 'date' },
      { prop: 'end_date', label: '结束日期', type: 'date' },
      { prop: 'is_current', label: '当前学期', type: 'boolean' }
    ]
  },
  courses: {
    title: '课程目录',
    id: 'course_id',
    columns: [
      { prop: 'course_code', label: '课程代码' },
      { prop: 'course_name', label: '课程名称' },
      { prop: 'college_name', label: '开课学院' },
      { prop: 'credit', label: '学分' },
      { prop: 'hours', label: '学时' },
      { prop: 'exam_type', label: '考核方式' },
      { prop: 'course_type', label: '课程类型' },
      { prop: 'status', label: '状态' }
    ],
    fields: [
      { prop: 'course_code', label: '课程代码' },
      { prop: 'course_name', label: '课程名称' },
      { prop: 'college_id', label: '开课学院', type: 'select', optionsKey: 'colleges' },
      { prop: 'credit', label: '学分', type: 'number' },
      { prop: 'hours', label: '学时', type: 'number' },
      { prop: 'exam_type', label: '考核方式', type: 'select', optionsKey: 'examType' },
      { prop: 'course_type', label: '课程类型', type: 'select', optionsKey: 'courseType' },
      { prop: 'description', label: '课程简介', type: 'textarea' },
      { prop: 'status', label: '状态', type: 'select', optionsKey: 'status' }
    ]
  },
  'teaching-classes': {
    title: '教学班管理',
    id: 'teaching_class_id',
    columns: [
      { prop: 'class_code', label: '教学班编号' },
      { prop: 'class_name', label: '教学班名称' },
      { prop: 'course_name', label: '课程' },
      { prop: 'teacher_name', label: '任课教师' },
      { prop: 'academic_year', label: '学年' },
      { prop: 'semester', label: '学期' },
      { prop: 'capacity', label: '容量' },
      { prop: 'selected_count', label: '已选' },
      { prop: 'status', label: '状态' }
    ],
    fields: [
      { prop: 'class_code', label: '教学班编号' },
      { prop: 'class_name', label: '教学班名称' },
      { prop: 'course_id', label: '课程', type: 'select', optionsKey: 'courses' },
      { prop: 'teacher_id', label: '任课教师', type: 'select', optionsKey: 'teachers' },
      { prop: 'term_id', label: '学期', type: 'select', optionsKey: 'terms' },
      { prop: 'capacity', label: '容量', type: 'number' },
      { prop: 'selected_count', label: '已选人数', type: 'number' },
      { prop: 'waitlist_count', label: '候补人数', type: 'number' },
      { prop: 'status', label: '状态', type: 'select', optionsKey: 'classStatus' }
    ]
  },
  rounds: {
    title: '选课轮次',
    id: 'round_id',
    columns: [
      { prop: 'round_name', label: '轮次名称' },
      { prop: 'academic_year', label: '学年' },
      { prop: 'semester', label: '学期' },
      { prop: 'start_time', label: '开始时间', width: 180 },
      { prop: 'end_time', label: '结束时间', width: 180 },
      { prop: 'status', label: '状态' }
    ],
    fields: [
      { prop: 'term_id', label: '学期', type: 'select', optionsKey: 'terms' },
      { prop: 'round_name', label: '轮次名称' },
      { prop: 'start_time', label: '开始时间', type: 'datetime' },
      { prop: 'end_time', label: '结束时间', type: 'datetime' },
      { prop: 'status', label: '状态', type: 'select', optionsKey: 'roundStatus' },
      { prop: 'waitlist_enabled', label: '开启候补', type: 'boolean' }
    ]
  }
}

const staticOptions: Record<string, Array<{ label: string; value: any }>> = {
  status: [
    { label: '启用', value: 'enabled' },
    { label: '停用', value: 'disabled' }
  ],
  gender: [
    { label: '男', value: 'male' },
    { label: '女', value: 'female' }
  ],
  studentStatus: [
    { label: '在读', value: 'active' },
    { label: '休学', value: 'suspended' },
    { label: '毕业', value: 'graduated' }
  ],
  teacherStatus: [
    { label: '在职', value: 'active' },
    { label: '停用', value: 'inactive' }
  ],
  teacherTitle: [
    { label: '助教', value: '助教' },
    { label: '讲师', value: '讲师' },
    { label: '特聘讲师', value: '特聘讲师' },
    { label: '副教授', value: '副教授' },
    { label: '教授', value: '教授' },
    { label: '硕士生导师', value: '硕士生导师' },
    { label: '博士生导师', value: '博士生导师' },
    { label: '实验师', value: '实验师' },
    { label: '高级实验师', value: '高级实验师' }
  ],
  examType: [
    { label: '考试', value: 'exam' },
    { label: '考查', value: 'check' }
  ],
  courseType: [
    { label: '必修', value: 'required' },
    { label: '选修', value: 'elective' },
    { label: '通识', value: 'general' }
  ],
  classStatus: [
    { label: '草稿', value: 'draft' },
    { label: '开放', value: 'open' },
    { label: '关闭', value: 'closed' }
  ],
  roundStatus: [
    { label: '未开始', value: 'not_started' },
    { label: '开放', value: 'open' },
    { label: '结束', value: 'ended' }
  ]
}

const resource = computed(() => String(route.params.resource || 'colleges'))
const config = computed(() => configs[resource.value] || configs.colleges)
const visibleFields = computed(() => config.value.fields)

watch(resource, () => load(), { immediate: true })
onMounted(loadLookups)

async function load() {
  loading.value = true
  try {
    rows.value = await apiGet<any[]>(`/admin/${resource.value}`)
  } catch (error) {
    ElMessage.error((error as Error).message)
  } finally {
    loading.value = false
  }
}

async function loadLookups() {
  try {
    lookups.value = await apiGet<Record<string, any[]>>('/lookups')
  } catch {
    lookups.value = {}
  }
}

function optionsFor(field: FieldConfig) {
  const key = field.optionsKey || ''
  if (staticOptions[key]) return staticOptions[key]

  let list = lookups.value[key] || []
  if (resource.value === 'admin-classes' && field.prop === 'major_id' && form.college_id) {
    list = list.filter((item) => item.college_id === form.college_id)
  }

  return list.map((item) => ({
    value: optionValue(key, item),
    label: optionLabel(key, item)
  }))
}

function optionValue(key: string, item: Record<string, any>) {
  const map: Record<string, string> = {
    colleges: 'college_id',
    majors: 'major_id',
    adminClasses: 'admin_class_id',
    regions: 'region_id',
    teachers: 'teacher_id',
    courses: 'course_id',
    terms: 'term_id',
    teachingClasses: 'teaching_class_id',
    rounds: 'round_id'
  }
  return item[map[key]]
}

function optionLabel(key: string, item: Record<string, any>) {
  if (key === 'colleges') return item.college_name
  if (key === 'majors') return `${item.major_name}`
  if (key === 'adminClasses') return `${item.class_name}（${item.grade_year}级）`
  if (key === 'regions') return item.region_name
  if (key === 'teachers') return item.teacher_name
  if (key === 'courses') return `${item.course_code} ${item.course_name}`
  if (key === 'terms') return `${item.academic_year} 第${item.semester}学期`
  if (key === 'teachingClasses') return `${item.class_code} ${item.class_name}`
  if (key === 'rounds') return item.round_name
  return String(item.name || item.label || '')
}

function displayValue(prop: string, value: any) {
  if (value === null || value === undefined || value === '') return '-'
  if (typeof value === 'boolean') return value ? '是' : '否'

  const field = config.value.fields.find((item) => item.prop === prop)
  const optionKey = field?.optionsKey || optionKeyByColumn(prop)
  const options = optionKey ? staticOptions[optionKey] : undefined
  const option = options?.find((item) => item.value === value)
  return option?.label || value
}

function optionKeyByColumn(prop: string) {
  const map: Record<string, string> = {
    status: resource.value === 'students' ? 'studentStatus' : resource.value === 'teachers' ? 'teacherStatus' : resource.value === 'teaching-classes' ? 'classStatus' : resource.value === 'rounds' ? 'roundStatus' : 'status',
    gender: 'gender',
    exam_type: 'examType',
    course_type: 'courseType',
    title: 'teacherTitle'
  }
  return map[prop]
}

function onFieldChange(field: FieldConfig) {
  if (resource.value === 'admin-classes' && field.prop === 'college_id') {
    form.major_id = undefined
  }
}

function resetForm() {
  Object.keys(form).forEach((key) => delete form[key])
  config.value.fields.forEach((field) => {
    if (field.type === 'boolean') form[field.prop] = false
  })
}

function openCreate() {
  editingId.value = null
  resetForm()
  dialogVisible.value = true
}

function openEdit(row: Record<string, any>) {
  editingId.value = row[config.value.id]
  resetForm()
  config.value.fields.forEach((field) => {
    if (field.prop === 'college_id' && resource.value === 'admin-classes') {
      form.college_id = row.college_id || inferCollegeIdByMajor(row.major_id)
      return
    }
    form[field.prop] = row[field.prop]
  })
  dialogVisible.value = true
}

function inferCollegeIdByMajor(majorId: number) {
  return (lookups.value.majors || []).find((item) => item.major_id === majorId)?.college_id
}

function buildPayload() {
  const payload: Record<string, any> = {}
  config.value.fields.forEach((field) => {
    if (field.uiOnly) return
    const value = form[field.prop]
    payload[field.prop] = value === '' ? null : value
  })
  return payload
}

async function save() {
  try {
    const payload = buildPayload()
    if (editingId.value) await apiPut(`/admin/${resource.value}/${editingId.value}`, payload)
    else await apiPost(`/admin/${resource.value}`, payload)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    await load()
    await loadLookups()
  } catch (error) {
    ElMessage.error((error as Error).message)
  }
}

async function remove(row: Record<string, any>) {
  try {
    await ElMessageBox.confirm('确定删除该记录吗？', '删除确认', { type: 'warning' })
    await apiDelete(`/admin/${resource.value}/${row[config.value.id]}`)
    ElMessage.success('删除成功')
    await load()
  } catch (error) {
    if (error instanceof Error && error.message !== 'cancel') ElMessage.error(error.message)
  }
}
</script>
