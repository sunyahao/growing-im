<template>
  <div class="chat">
    <van-nav-bar title="chat">
      <template #right>
        <van-icon name="plus" size="18" @click="onAdd" />
      </template>
    </van-nav-bar>
    <van-overlay :show="showOverlay" @click="showOverlay = false">
      <div class="chat-menu">
        <div class="menu-item" @click="onAddFriend">
          <van-icon name="add-o" size="18" />添加朋友
        </div>
        <div class="menu-item" @click="onAddGroup">
          <van-icon name="add-o" size="18" />添加群组
        </div>
        <div class="menu-item" @click="OnCreateGroup">
          <van-icon name="add-o" size="18" />创建群组
        </div>
        <div class="menu-item" @click="OnShareGroup">
          <van-icon name="add-o" size="18" />分享群组
        </div>
      </div>
    </van-overlay>
    <conversation-list></conversation-list>

  </div>
</template>

<script>
import IoService from '@/services/io.js';
import ConversationList from './components/ConversationList';

export default {
  name: 'Chat',
  components: {
    ConversationList
  },
  props: {},
  data() {
    return {
      showMenu: false,
      showOverlay: false
    };
  },
  computed: {},
  watch: {},
  created() {
    // 如果检测到store.getter.userId中有数据，那就加载它
    if (this.$checkLogin()) {
      // debugger;
      IoService.connect();
    }
  },
  beforeDestroy() {
    IoService.disconnect();
  },
  methods: {
    onAdd() {
      this.showOverlay = true;
    },
    onAddFriend() {
      this.showMenu = false;
      this.$router.push('/add-friend');
    },
    onAddGroup() {
      this.showMenu = false;
      this.$router.push('/add-group');
    },
    OnCreateGroup() {
      this.showMenu = false;
      this.$router.push('/create-group');
    },
    OnShareGroup() {
      this.showMenu = false;
      this.$router.push('/share-group');
    }
  }
};
</script>

<style lang="scss">
.chat {
  width: 100%;
  height: 100%;
  position: relative;
  .van-overlay {
    width: 600px;
    background-color: rgba(0, 0, 0, 0.2);
    .chat-menu {
      background-color: #fff;
      padding: 20px;
      position: absolute;
      right: 0;
      top: 46px;
      .menu-item {
        display: flex;
        align-items: center;
        &:not(:last-child) {
          margin-bottom: 10px;
        }
        .van-icon {
          margin-right: 10px;
        }
      }
    }
  }
}
</style>
