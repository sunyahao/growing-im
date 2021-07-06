import http from '@/common/http';
import urls from '@/common/urls';
import Vue from 'vue';

const state = {
  // 申请数量
  applyCount: 0,
  mailList: []
};

// getters
const getters = {
  mailList(state) {
    return state.mailList;
  },
  applyCount(state) {
    return state.applyCount;
  }
};

// mutations
const mutations = {
  updateMailList(state, mailList) {
    state.mailList = mailList;
  },
  updateApplyCount(state, applyCount) {
    state.applyCount = applyCount;
  }
};

// actions
// mailList就是data
const actions = {
  getMailList({ commit }) {
    http.post(urls.mailList.mailList, {
      userId: localStorage.getItem("userId")
    }).then(data => {
      var mailList1 = data.data.mails;
      var mail = [];
      var mailList = {};
      for (var i=0;i<mailList1.length;i++) {
        var c = {};
        var userInfo = {};
        var conversation = {};
        userInfo.nickname = mailList1[i].user.nick;
        // userInfo.photo = mailList1[i].user.avatar;
        userInfo.userId = mailList1[i].user.userId;
        userInfo.photo = mailList1[i].user.avatar;
        if (mailList1[i].conversation != null) {
          conversation.id = mailList1[i].conversation.id;
          // 0是私聊，1是群聊
          conversation.type = mailList1[i].conversation.chatType == 0 ? 'chat' : 'groupChat';
          conversation.updateAt = mailList1[i].conversation.lastMsgTime;
        }
        c.userInfo = userInfo;
        c.conversation = conversation;
        mail.push(c);
      }
      // mailList.mail = mail;
      commit('updateMailList', mail);
    });
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
