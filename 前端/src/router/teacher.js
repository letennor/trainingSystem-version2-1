import TeacherHome from '@/pages/Teacher/TeacherHome'
import BasicInfo from '@/pages/Teacher/personalInfoManagementPages/BasicInfo.vue'
import CreateTest from '@/pages/Teacher/createTestPages/CreateTest'
import MyQuestion from '@/pages/Teacher/answerQuestionPages/MyQuestion.vue'
import UploadQuestion from '@/pages/Teacher/createQuestionPages/UploadQuestion.vue'
import StudentInfo from '@/pages/Teacher/queryStudentInfo/StudentInfo.vue'
import SpecialTest from '@/pages/Teacher/createTestPages/SpecialTest.vue'
import SetTest from '@/pages/Teacher/createTestPages/SetTest.vue'
import SelectQuestion from '@/pages/Teacher/createTestPages/SelectQuestion.vue'
import NormalTest from '@/pages/Teacher/createTestPages/NormalTest.vue'
import SelectQuestionBasicInfo from '@/pages/Teacher/createQuestionPages/SelectQuestionBasicInfo.vue'
import InputQuestionInfoType1 from '@/pages/Teacher/createQuestionPages/InputQuestionInfoType1.vue'
import InputQuestionInfoType2 from '@/pages/Teacher/createQuestionPages/InputQuestionInfoType2.vue'
import StudyCharts from '@/pages/Train/containerMain/studyCharts.vue'
import BlankPage from '@/pages/Teacher/queryStudentInfo/BlankPage.vue'
import SourceManagement from '@/pages/Teacher/sourceManagementPages/SourceManagement.vue'
import QuestionManagement from '@/pages/Teacher/sourceManagementPages/QuestionManagement.vue'
import ModifyQuestionType1 from '@/pages/Teacher/sourceManagementPages/ModifyQuestionType1.vue'
import ModifyQuestionType2 from '@/pages/Teacher/sourceManagementPages/ModifyQuestionType2.vue'
import TestSetManagement from '@/pages/Teacher/sourceManagementPages/TestSetManagement.vue'
import SpecialTestManagement from '@/pages/Teacher/sourceManagementPages/SpecialTestManagement.vue'
import ModifySpecialTest from '@/pages/Teacher/sourceManagementPages/ModifySpecialTest.vue'
import NormalTestManagement from '@/pages/Teacher/sourceManagementPages/NormalTestManagement.vue'
import ModifyNormalTest from '@/pages/Teacher/sourceManagementPages/ModifyNormalTest.vue'
import QuestionDetail from '@/pages/Teacher/sourceManagementPages/QuestionDetail.vue'
import TeacherInfo from '@/pages/Teacher/personalInfoManagementPages/TeacherInfo'

export default [
    {
        name:'TeacherHome',
        path:'/TeacherHome',
        redirect: '/TeacherHome/BasicInfo',
        components:{
            default:TeacherHome
        },
        children:[

            {
                name:'BasicInfo',
                path:'BasicInfo',
                components:{
                    mainView:BasicInfo
                },
            },
    
            {
                name:'CreateTest',
                path:'CreateTest',
                components:{
                    mainView:CreateTest
                },
            },
    
            {
                name:'SpecialTest',
                path:'SpecialTest',
                components:{
                    mainView:SpecialTest
                }
            },
    
            {
                name:'SetTest',
                path:'SetTest',
                components:{
                    mainView:SetTest
                },
            },
        
            {
                name:'MyQuestion',
                path:'MyQustion',
                components:{
                    mainView:MyQuestion
                },
            },
    
            {
                name:'SelectQuestion',
                path:'SelectQuestion',
                components:{
                    mainView:SelectQuestion
                },
            },
    
            {
                name:'UploadQuestion',
                path:'UploadQuestion',
                components:{
                    mainView:UploadQuestion
                },
            },
    
            {
                name:'StudentInfo',
                path:'StudentInfo',
                components:{
                    mainView:StudentInfo
                },
                children:[
                    {
                        name:'StudyCharts',
                        path:'StudyCharts',
                        components:{
                            studentInfoView:StudyCharts
                        }
                    },

                    {
                        name:'BlankPage',
                        path:'BlankPage',
                        components:{
                            studentInfoView:BlankPage
                        }
                    }
                ]
            },
            
            {
                name:'NormalTest',
                path:'NormalTest',
                components:{
                    mainView:NormalTest
                }
            },

            {
                name:'SelectQuestionBasicInfo',
                path:'SelectQuestionBasicInfo',
                components:{
                    mainView:SelectQuestionBasicInfo
                }
            },

            {
                name:'InputQuestionInfoType1',
                path:'InputQuestionInfoType1',
                components:{
                    mainView:InputQuestionInfoType1
                }

            },

            {
                name:'InputQuestionInfoType2',
                path:'InputQuestionInfoType2',
                components:{
                    mainView:InputQuestionInfoType2
                }
            },

            {
                name:'SourceManagement',
                path:'SourceManagement',
                components:{
                    mainView:SourceManagement
                }
            },

            {
                name:'QuestionManagement',
                path:'QuestionManagement',
                components:{
                    mainView:QuestionManagement
                }
            },

            {
                name:'ModifyQuestionType1',
                path:'ModifyQuestionType1',
                components:{
                    mainView:ModifyQuestionType1
                }
            },

            {
                name:'ModifyQuestionType2',
                path:'ModifyQuestionType2',
                components:{
                    mainView:ModifyQuestionType2
                }
            },

            {
                name:'SpecialTestManagement',
                path:'SpecialTestManagement',
                components:{
                    mainView:SpecialTestManagement
                }
            },

            {
                name:'TestSetManagement',
                path:'TestSetManagement',
                components:{
                    mainView:TestSetManagement

                }
            },


            {
                name:'ModifySpecialTest',
                path:'ModifySpecialTest',
                components:{
                    mainView:ModifySpecialTest
                }
            },


            {
                name:'NormalTestManagement',
                path:'NormalTestManagement',
                components:{
                    mainView:NormalTestManagement

                }
            },

            {
                name:'ModifyNormalTest',
                path:'ModifyNormalTest',
                components:{
                    mainView:ModifyNormalTest

                }
            },

            {
                name:'QuestionDetail',
                path:'QuestionDetail',
                components:{
                    mainView:QuestionDetail
                }
            },
            {
                name: 'TeacherInfo',
                path: 'TeacherInfo',
                components: {
                    mainView: TeacherInfo
                }
            }
            
            

        ]
    },
]