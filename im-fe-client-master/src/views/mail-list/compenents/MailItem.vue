<template>
  <div class="mail-item" @click="enter">
    <div class="avatar">
      <img :src="head" />
    </div>
    <div class="name">{{mail.userInfo.nickname}}</div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex';
import defaultHead from '@/assets/images/head.png';
import { dateFormat } from '@/utils/format.js';

export default {
  name: 'mail-item',
  components: {},
  props: {
    mail: Object,
    conversation: Object
  },
  data() {
    return {
      defaultHead: defaultHead
    };
  },
  computed: {
    head() {
      return this.mail.userInfo.photo || '/public/images/1.jpeg';
    }
  },
  watch: {},
  created() {},
  methods: {
    ...mapMutations('im', ['addConversation', 'activateConversation']),
    enter() {
      this.$http
              .post(this.$urls.add.addConversation, {
                fromId: localStorage.getItem("userId"),
                toId: this.mail.userInfo.userId,
                type: "user"
              })
              .then(data => {
                debugger;
                var c = this.mail;
                if (data.data.status != 400) {
                  debugger;
                  if (this.mail.hasOwnProperty('conversation')) {
                    var conversation = {};
                    conversation.id = data.data.conversation.id;
                    this.mail.conversation = conversation;
                  }
                  this.addConversation({
                    type: "user",
                    id: this.mail.conversation.id,
                    isActive: false,
                    info: {
                      name: data.data.conversation.name
                    },
                    target: this.mail.userInfo,
                    updatedAt: dateFormat('YYYY-mm-dd HH:MM:SS', new Date()),
                    messageCount: 0,
                    messageList: [],
                    pageNumber: 2,
                    pageSize: 10,
                    refreshing: false,
                    loading: false,
                    finished: false
                  });
                }

                this.activateConversation({
                  conversationId: this.mail.conversation.id
                });
                var a = this.mail;
                debugger;
                this.$router.push({
                  path: '/chat',
                  query: {
                    toId: this.mail.userInfo.userId,
                    conversationId: this.mail.conversation.id
                  }
                });
              })
              .catch(error => {
                this.$toast(error.errorMessage);
              });

    }
  }
};
</script>

<style lang="scss">
.mail-item {
  background-color: $color-white;
  display: flex;
  align-items: center;
  height: 60px;
  .avatar {
    margin-left: 10px;
    width: 40px;
    height: 40px;
    flex-shrink: 0;
    img {
      background-color: #fff;
      width: 100%;
      height: 100%;
      border-radius: 50%;
    }
  }
  .name {
    flex-grow: 2;
    position: relative;
    height: 100%;
    line-height: 60px;
    margin-left: 10px;
    &::after {
      position: absolute;
      box-sizing: border-box;
      content: ' ';
      pointer-events: none;
      top: -50%;
      right: -50%;
      bottom: -50%;
      left: -50%;
      border: 0 solid #ebedf0;
      -webkit-transform: scale(0.5);
      transform: scale(0.5);
      border-bottom-width: 1px;
    }
  }
}
</style>
