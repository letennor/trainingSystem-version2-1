
const state = {
    userId: '',
    studentId: '',
    schoolName: '',
    major: '',
    email: '',
}

const mutations = {
    TEST(state, id) {
        state.userId = id
    }
}

const actions = {

}

const getters = {}

export default {
    state,
    mutations,
    actions,
    getters
}