<template>
  <div class="enter-area">
    <div class="input-area">
      <van-field type="text" v-model="value" placeholder="请输入" @keypress="enter" @focus="onFocus" />
      <div class="send-btn" @click="onSend">
        <i class="iconfont iconi-sh-man-xy"></i>
      </div>
    </div>
    <div ref="tool" class="tools">
      <div class="tool-item">
        <i class="iconfont iconi-sh-car-bf-f" @click="pickVideo"></i>
        <hy-upload accept="video/*" style="display: none;" ref="videoUpload" @file-success="videoSuccess"></hy-upload>
      </div>
      <div class="tool-item">
        <i class="iconfont iconi-sh-man-dt" @click="onSendPosition"></i>
      </div>
      <div class="tool-item">
        <i class="iconfont iconi-sh-man-zp" @click="pickPicture"></i>
        <hy-upload accept="image/*" style="display: none;" ref="imageUpload" @file-success="imageSuccess"></hy-upload>
      </div>
      <div class="tool-item">
        <i class="iconfont iconi-sh-man-bq" @click="pickEmoji"></i>
      </div>
    </div>
    <emoji-picker v-if="isEmoji" @emoji-select="emojiSelect"></emoji-picker>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import HyUpload from '@/compenents/HyUpload';
import IoService from '@/services/io.js';
import EmojiPicker from './EmojiPicker';
import BMap from 'BMap';

export default {
  name: 'EnterArea',
  components: {
    HyUpload,
    EmojiPicker
  },
  props: {
    conversationId: [Number, String],
    toId: [Number, String]
  },
  data() {
    return {
      value: '',
      isEmoji: false
    };
  },
  computed: {
    ...mapGetters(['userId', 'userInfo']),
    ...mapGetters('im', ['activeConversation'])
  },
  watch: {},
  methods: {
    enter(event) {
      if (event.code === 'Enter') {
        this.onSend();
      }
    },
    pickEmoji() {
      this.isEmoji = !this.isEmoji;
    },
    onFocus() {
      this.isEmoji = false;
    },
    emojiSelect(key) {
      this.value = this.value + key;
    },
    inputFocus(event) {
      this.isEmoji = false;
      this.$emit('change-hight', '64');
      setTimeout(() => {
        document.body.scrollTop = document.body.scrollHeight;
      }, 300);
    },
    pickPicture() {
      this.$refs.imageUpload.click();
},
    pickVideo() {
      this.$refs.videoUpload.click();
    },
    imageSuccess(urls) {
      // urls是message的content
      for (var i=0;i<urls.length;i++) {
        this.send({
          url: urls[i], // 图片地址
          type: 'image'
        });
      }
    },
    videoSuccess(urls) {
      debugger;
      for (var i=0;i<urls.length;i++) {
        this.send({
          url: urls[i], // 视频地址
          type: 'video'
        });
      }
    },
    onSend() {
      if (this.value.trim() === '') {
        this.$toast('不能发送空白信息');
        return;
      }
      this.send({
        msg: this.value,
        type: 'text'
      });
      this.value = '';
    },
    onSendPosition() {
      debugger;
      var geolocation = new BMap.Geolocation();
      geolocation.getCurrentPosition(data => {
          const addr = data.address.province + data.address.city + data.address.district + data.address.street + data.address.street_number;
          debugger;
          this.send({
            type: 'loc',
            addr: addr,
            // 纬度
            lat: data.latitude,
            // 经度
            lng: data.longitude
          });

      });
    },
    send(body) {
      var a = this.activeConversation;
      if (this.activeConversation && this.activeConversation.type == "user") {
        // 处理私聊
        this.isEmoji = false;
        // IoService.sendMessage({
        //   // type为"user"或"group"
        //   type: this.activeConversation.type,
        //   // 要发送到的客户端
        //   toId: this.activeConversation.toId,
        //   fromId: localStorage.getItem("userId"),
        //   conversationId: this.activeConversation.id,
        //   body : body
        // });
        var aa = this.conversationId;
        var aaa = this.toId;
        debugger;
        if (IoService.socket == null)  {
          IoService.connect();
        } else {
          IoService.socket.emit('/v1/im/new-message1', {
            // type为"user"或"group"
            type: a.type,
            // 要发送到的客户端
            toId: this.toId,
            fromId: localStorage.getItem("userId"),
            conversationId: this.conversationId,
            body : body
          });
        }
      } else {
        // 处理群聊
        var aa = this.conversationId;
        var aaa = this.toId;
        debugger;
        this.isEmoji = false;
        // IoService.sendGroupMessage({
        //   // type为"user"或"groupchat"
        //   type: "groupchat",
        //   // 要发送到的客户端
        //   toId: this.toId,
        //   fromId: localStorage.getItem("userId"),
        //   conversationId: this.conversationId,
        //   body : body
        // });
        if (IoService.socket == null)  {
          IoService.connect();
        } else {
          IoService.socket.emit('/v1/im/new-group-message1', {
            // type为"user"或"groupchat"
            type: "groupchat",
            // 要发送到的客户端
            toId: this.toId,
            fromId: localStorage.getItem("userId"),
            conversationId: this.conversationId,
            body : body
          });
        }
      }
    }
  }
};
</script>

<style lang="scss">
.enter-area {
  background-color: #f4f5f7;
  .input-area {
    border-top: #f2f4f5 1px solid;
    display: flex;
    input {
      color: #666;
    }
    .send-btn {
      width: 40px;
      text-align: center;
      display: flex;
      align-items: center;
      justify-content: center;
      i {
        color: #1989fa;
        font-size: 22px;
      }
    }
  }
  .tools {
    display: flex;
    justify-content: space-around;
    margin-top: 5px;
    padding-bottom: 5px;
    .tool-item {
      i {
        font-size: 24px;
        color: #1989fa;
      }
    }
  }
}
</style>
