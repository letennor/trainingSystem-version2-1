const state = {
  trainId: -1, // 试卷Id
  questions: [],
  userAnswer: [],
};

const mutations = {
  SET_USERANSWER(state, value) {
    for (let i = 0; i < value.length; i++) {
      state.userAnswer.splice(i, 1, {
        questionId: state.questions[i].questionId,
        content: "",
        isRight: -1,
      });
    }
  },
  SET_QUESTIONS(state, value) {
    for (let i = 0; i < value.length; i++) {
      state.questions.splice(i, 1, value[i]);
    }
  },
  SET_USERANSWER_CONTENT_FROM_SESSION(state, value) {
    state.userAnswer[value.i].content = value.content;
  },
  SET_TRAINID(state, value) {
    state.trainId = value;
  },
};

const actions = {};

const getters = {};

export default {
  state,
  mutations,
  actions,
  getters,
};
