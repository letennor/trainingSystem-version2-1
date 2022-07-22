export default
 [
   {
    path:'/',
    component: () =>import('../components/webhome')
 },
{
    path:'/login',
    component: () =>import('../components/login')
},
   {
     path: '/register',
     component: () =>import('../components/register')
   }

]
