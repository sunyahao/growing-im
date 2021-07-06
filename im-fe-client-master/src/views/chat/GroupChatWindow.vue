<template>
  <view-page class="cs-chatbox" :groupId="query.toId">
    <div class="cs-chat" ref="wrapper">
      <div>
        <message-group-item
          ref="messages"
          v-for="message of messageList"
          :key="message.id"
          :message="message"
          :scroll="scroll"
          :conversationId="query.conversationId"
          :groupConversation="groupConversation"
        ></message-group-item>
      </div>
    </div>
    <enter-area :conversationId="query.conversationId" :toId="query.toId" ></enter-area>
  </view-page>
</template>

<script>
import MessageGroupItem from './components/MessageGroupItem';
import EnterArea from './components/EnterArea';
import { mapGetters, mapMutations } from 'vuex';
import IoService from '@/services/io.js';
import BScroll from '@better-scroll/core';
import ObserveDOM from '@better-scroll/observe-dom';
import PullDown from '@better-scroll/pull-down';

BScroll.use(ObserveDOM);
BScroll.use(PullDown);

export default {
  name: 'GroupChat',
  components: {
    MessageGroupItem,
    EnterArea
  },
  props: {},
  data() {
    return {
      topMessage: null,
      scroll: null,
      messageList: [],
      query: String,
      groupConversation: Object
    };
  },
  computed: {
    ...mapGetters('im', ['activeConversation'])
  },
  mounted() {

    // 路由参数
    this.query = this.$route.query//query包含传递的所有参数

    // 创建聊天窗口时，机器人的不触发this.activeConversation()
      this.activedConversation();


    const scroll = new BScroll(this.$refs.wrapper, {
      scrollY: true,
      click: true,
      observeDOM: true,
      pullDownRefresh: {
        threshold: -30
      }
    });

    //下拉刷新
    scroll.on('pullingDown', this.onLoad);
    this.scroll = scroll;
    setTimeout(() => {
      this.scroll.scrollTo(0, this.scroll.maxScrollY);
    }, 50);

    // 在这里监听别人的新消息
    var that = this;
    if (IoService.socket == null)  {
      IoService.connect();
    } else {
      IoService.socket.on('/v1/im/group-other-messages1', function (data) {
        debugger;
        var message = {};
        message.id = data.message.id;
        message.isMyself = false;
        message.createAt = data.message.sendTime;
        message.fromId = data.message.fid;
        message.toId = data.message.tid;
        message.photo = data.photo;
        message.nickname = data.nickname;
        var body = {};
        if (data.message.msgType == 0) {
          body.type = "text";
          body.msg = data.message.content;
        } else if(data.message.msgType == 1) {
          body.type = "loc";
          var locat = JSON.parse(data.message.content);
          if (locat) {
            body.addr = location.addr;
            body.lng = location.lng;
            body.lat = location.lat;
          }
        } else if(data.message.msgType == 2) {
          body.type = "image";
          body.url = data.message.content;
        } else if(data.message.msgType == 3) {
          body.type = "video";
          body.url = data.message.content;
        }
        message.body = body;
        if (data.conversationId == that.query.conversationId) {
          that.messageList.push(message);
        }
      })

      // 在这里监听自己的消息
      IoService.socket.on('/v1/im/group-own-messages1', function (data) {
        debugger;
        var message = {};
        message.id = data.message.id;
        message.isMyself = true;
        message.createAt = data.message.sendTime;
        message.fromId = data.message.fid;
        message.toId = data.message.tid;
        message.photo = data.photo;
        message.nickname = data.nickname;
        var body = {};
        if (data.message.msgType == 0) {
          body.type = "text";
          body.msg = data.message.content;
        } else if(data.message.msgType == 1) {
          body.type = "loc";
          var locat = JSON.parse(data.message.content);
          if (locat) {
            body.addr = location.addr;
            body.lng = location.lng;
            body.lat = location.lat;
          }
        } else if(data.message.msgType == 2) {
          body.type = "image";
          body.url = data.message.content;
        } else if(data.message.msgType == 3) {
          body.type = "video";
          body.url = data.message.content;
        }
        message.body = body;
        if (data.conversationId == that.query.conversationId) {
          that.messageList.push(message);
        }
      })
    }

  },
  destroyed() {
    this.scroll = null;
  },
  methods: {
    ...mapMutations('im', ['clearMessage', 'clearMessages', 'increaseConversationPageNumber']),
    onLoad() {
      if (this.activeConversation.messageList.length >= this.activeConversation.messageCount) {
        return;
      }
      this.increaseConversationPageNumber({
        conversationId: this.query.conversationId
      });
      this.$nextTick(() => {
        IoService.getMessageList({
          conversationId: this.query.conversationId,
          pageSize: this.activeConversation.pageSize,
          pageNumber: this.activeConversation.pageNumber
        });
      });
    },
    activedConversation() {
      this.$http.post('/api/v1/conversations/groupActive', {
        conversationId: this.query.conversationId,
        userId: localStorage.getItem("userId"),
        toId: this.query.toId,
      }).then(data => {
        var messageList = data.data.messages;
        var messages = [];
        // 后端格式到前端的转化
        for(var i=0;i<messageList.length;i++) {
          var message = {};
          message.id = messageList[i].id;
          if (messageList[i].isMyself == 0) {
            message.isMyself = true;
          } else {
            message.isMyself = false;
          }
          message.fromId = messageList[i].fid;
          message.toId = messageList[i].tid;
          message.createdAt = messageList[i].sendTime;
          message.nickname = messageList[i].nick;
          message.photo = messageList[i].photo;
          var body = {};
          if (messageList[i].msgType == 0) {
            body.type = "text";
            body.msg = messageList[i].content;
          } else if(messageList[i].msgType == 1) {
            body.type = "loc";
            var locat = JSON.parse(messageList[i].content);
            if (locat) {
              body.addr = location.addr;
              body.lng = location.lng;
              body.lat = location.lat;
            }
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
        this.messageList = messages;

        //处理会话
        var c = data.data.groupConversation;
        var groupConversation = {};
        groupConversation.type = "groupchat";
        var info = {};
        info.name = c.name;
        groupConversation.info = info;
        groupConversation.id = c.id;
        this.groupConversation = groupConversation;
        })
        .catch(error => {
          this.$toast("出现异常");
        });
    }
  }
};

</script>

<style lang="scss">
.cs-chatbox {
  height: 100%;
  .cs-chat {
    overflow: hidden;
    height: calc(100% - 90px);
    .van-list {
      min-height: 50vh;
    }
  }
  .van-nav-bar__right {
    background:black;
  }
}
</style>
