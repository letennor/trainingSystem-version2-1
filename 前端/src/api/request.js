//对axios进行二次封装
import axios from 'axios'

//1.利用axios对象的方法create，去创建一个axios实例
//2.request就是axios，只不过稍微配置一下
const requests = axios.create({
    //配置对象
    // baseURL: "http://localhost:8085",
    // baseURL: "http://localhost:8000",// express服务器
    baseURL: "http://localhost:8990",
    // baseURL:"http://124.221.161.162:8770/tomcats"
    // baseURL: 'http://tomcats'
    // baseURL: "http://124.221.161.162:8990",// 打包时所用服务器
    // baseURL:"http://121.4.32.234:8990"
    //代表请求超时的事件 5s
    // timeout: 5000,
})
//请求拦截器：在发请求之前，请求拦截器可以检测到，可以在请求发出去之前做一些事情
requests.interceptors.request.use((config) => {
    //config：配置对象，里面有一个属性很重要，headers请求头
    // 将token写入请求头
    const token = sessionStorage.getItem('token')
    if (token) {
        config.headers['token'] = token
    }
    return config
})

//响应拦截器
requests.interceptors.response.use((res) => {
    //成功的回调函数：服务器相应数据回来以后，响应拦截器都可以检测到，可以做一些事情
    // console.log(res);
    // console.log(res.data);
    // console.log(res);
    return res.data
}, (error) => {
    //响应失败的回调函数
    return Promise.reject(new Error(error))
})

//对外报楼
export default requests