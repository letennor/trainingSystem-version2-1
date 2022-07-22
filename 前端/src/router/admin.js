import Admin from '@/pages/Admin'
import SystemInfo from '@/pages/Admin/SystemInfo'
import HandleStuApplication from '@/pages/Admin/HandleStuApplication'
import HandleTeaApplication from '@/pages/Admin/HandleTeaApplication'
import AddTeacher from '@/pages/Admin/AddTeacher'
import HandleAccount from '@/pages/Admin/HandleAccount'
import SystemMaintain from '@/pages/Admin/SystemMaintain'
import StudentCharts from '@/pages/Train/containerMain/studyCharts'
export default [
    // 管理员主页
    {
        path: '/adminHome',
        component: Admin,
        redirect: '/adminHome/systemInfo',
        children: [
            // 系统概况模块（用户量、在线量、留存统计）
            {
                path: 'systemInfo',
                component: SystemInfo,
            },
            // 处理学生申请模块
            { 
                path: 'handleStuApplication',
                component: HandleStuApplication,
                children: [
                    {
                        name:'StudyCharts1',
                        path: 'studentCharts',
                        component: StudentCharts
                    }
                ]
            },
            // 处理老师申请模块
            {
                path: 'handleTeaApplication',
                component: HandleTeaApplication
            },
            // 添加老师账号模块
            {
                path: 'addTeacher',
                component: AddTeacher,
            },
            // 管理账号模块
            {
                path: 'handleAccount',
                component: HandleAccount
            },
            // 系统维护模块
            {
                path: 'systemMaintain',
                component: SystemMaintain
            },
        ]
    }
]