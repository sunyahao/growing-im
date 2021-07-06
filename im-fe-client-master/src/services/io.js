import http from '@/common/http';
import io from 'socket.io-client';
import store from '@/store';
import urls from '@/common/urls';
import config from '../../config/config';
import robotHead from '@/assets/images/robot.png';

// 处理消息体
const handleMessage = message => {
  // 在入口处直接添加isMyself
  message.isMyself = localStorage.getItem("userId") === message.fromId;
};

const IoService = {
  socket: null,
  scroll: null,
  async getSocket() {
    const userId = store.getters.userId;
    this.socket = io(config.ioHost, {
      query: {
        // scene: 'im',
        userId: localStorage.getItem("userId")
      }
    });

    this.socket.on('connect', (message) => {
      console.log('socket连接成功！');
      console.log(message);
      getConversationList();
    });

    // 有新消息
    this.socket.on('/v1/im/new-message', message => {
      handleMessage(message);
      message.shouldScroll = true;
      store.commit('im/newMessage', { message });
    });
  },
  connect() {
    if (this.socket) {
      this.socket.connect();
    } else {
      this.getSocket();
    }
  },
  disconnect() {
    if (this.socket) {
      this.socket.disconnect();
      console.log('socket断开成功！');
    }
  },
  //接收消息
  receiveMessage() {
    const message = {};
    this.socket.on('/v1/im/new-messages1', function (data) {
      debugger;
      message.id = data.id;
      message.isMyself = false;
      message.createAt = data.sendTime;
      message.fromId = data.fid;
      message.toId = data.tid;
      var body = {};
      if (data.msgType == 0) {
        body.type = "text";
        body.msg = data.content;
      } else if(data.msgType == 1) {
        body.type = "loc";
        body.addr = data.content;
      } else if(data.msgType == 2) {
        body.type = "image";
        body.url = data.content;
      } else if(data.msgType == 3) {
        body.type = "video";
        body.url = data.content;
      }
      message.body = body;
      message.conversationId = data.conversationId;
  })
    return message;
  },
  // 发送消息
  sendMessage(message) {
    this.socket.emit('/v1/im/new-message1', message);
  },
  // 发送消息
  sendGroupMessage(message) {
    this.socket.emit('/v1/im/new-group-message1', message);
  },
  // 请求聊天记录
  getMessageList({ conversationId, fromId, toId, init }) {
    // debugger;
    this.socket.emit(
      '/v1/im/get-messages',
      JSON.stringify({
        conversationId: conversationId,
        fromId: fromId,
        toId: toId
      })
    );
    this.socket.on('/v1/im/get-messages', function (data) {
      var messageList = data;
      var count = messageList.length;
      var messages = [];
      // 后端格式到前端的转化
      for(var i=0;i<count;i++) {
        var message = {};
        message.id = messageList[i].id;
        message.isMyself = messageList[i].fid;
        message.createAt = messageList[i].sendTime;
        message.fromId = messageList[i].fid;
        message.toId = messageList[i].tid;
        var body = {};
        if (messageList[i].msgType == 0) {
          body.type = "text";
          body.msg = messageList[i].content;
        } else if(messageList[i].msgType == 1) {
          body.type = "loc";
          body.addr = messageList[i].content;
        } else if(messageList[i].msgType == 2) {
          body.type = "image";
          body.url = messageList[i].content;
        } else if(messageList[i].msgType == 3) {
          body.type = "video";
          body.url = messageList[i].content;
        }
        message.body = body;
        message.conversationId = data.conversationId;
        messages.push(message);
      }
      // if (!init && messages[0]) {
      //   messages[0].shouldScroll = true;
      // }
      store.commit('im/newMessages', { conversationId: data.conversationId, messages, messageCount: count });
    });
  },
  // join
  join(conversationId) {
    this.socket.emit('/v1/im/join', {
      conversationId
    });
  }
};

const getConversationList = () => {
  http
    .post(urls.restful.conversations, {
      userId : localStorage.getItem("userId")
    })
    .then(data => {
      debugger;
      var conversationList1 = data.data.conversations;
      var conversationList = [];
      for (var i=0;i<conversationList1.length;i++) {
        var c = {};
        c.id = conversationList1[i].id;
        c.info = {};
        c.info.name = conversationList1[i].name;
        c.info.photo = conversationList1[i].avatar;
        c.info.id = conversationList1[i].id;
        c.updatedAt = conversationList1[i].lastMsgTime;
        c.type = conversationList1[i].chatType == 0 ? "user" : "groupChat";
        c.fromId = conversationList1[i].fid;
        c.toId = conversationList1[i].tid;
        conversationList.push(c);
      }
      for (const iterator of conversationList) {
        iterator.isActive = false;
        // iterator.info = {
        //   name: 'aaa'
        // };
        iterator.messageList = [];
        iterator.pageNumber = 2;
        iterator.pageSize = 10;
      }
      for (const conversation of conversationList) {
        IoService.getMessageList({
          conversationId: conversation.id,
          fromId: conversation.fromId,
          toId: conversation.toId,
          init: true
        });
      }

      // 不要机器人了
      // conversationList.unshift({
      //   info: {
      //     id: 'robot',
      //     name: 'robot',
      //     photo: robotHead
      //   },
      //   id: 'robot',
      //   isActive: false,
      //   type: 'robot',
      //   photo: '',
      //   messageList: [
      //     {
      //       body: { msg: '你好，我是智能机器人，我们聊天吧', type: 'text' },
      //       conversationId: 'robot',
      //       createdAt: '2020-07-17 15:11:04',
      //       fromId: 'robot',
      //       id: 0,
      //       toId: 2
      //     }
      //   ]
      // });
      // 把他更新到im文件中的state的conversationList中
      store.commit('im/updateConversationList', conversationList);
    })
    .catch(error => {
      console.log(error.errorMessage);
    });
};

export default IoService;
