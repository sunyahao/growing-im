<template>
  <view-page class="user-info">
    <van-cell title="头像" is-link class="avatar-wrap" to="/mine/edit-avatar">
      <template #default>
        <div class="avatar">
          <img :src="avatar" />
        </div>
      </template>
    </van-cell>
    <van-cell title="昵称" is-link :value="userInfo.nickname" to="/mine/edit-nickname" />
    <van-cell title="签名" is-link :value="userInfo.sign" to="/mine/edit-sign" />
    <van-cell title="用户ID" is-link :value="userId" class="user-id"/>
    <!--<div class="mine">用户ID：{{userId}}</div>-->
  </view-page>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'UserInfo',
  components: {},
  props: {},
  data() {
    return {
      userInfo : Object,
      userId: localStorage.getItem("userId")
    };
  },
  computed: {
    ...mapGetters(['userInfo']),
    avatar() {
      if(this.userInfo.photo) {
        return this.userInfo.photo;
      } else {
        return'/public/images/1.jpeg';
      }
    }
  },
  mounted() {
    this.$http.post('/api/v1/currentUser', {
      userId: localStorage.getItem("userId")
    }).then(data => {
      this.userInfo = data.data.userInfo
    }).catch(error => {
      alert('获取头像出错');
    })
  },
  watch: {},
  created() {

  },
  methods: {}
};
</script>

<style lang="scss">
.user-info {
  .avatar-wrap {
    line-height: 44px;
    .van-cell__left-icon,
    .van-cell__right-icon {
      height: 44px;
      line-height: 44px;
    }
    .avatar {
      width: 40px;
      height: 40px;
      position: absolute;
      right: 5px;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
  }
}
.mine {
  width: 100%;
  height: 100%;
  overflow-y: auto;
}
  .user-id {
    .van-cell__right-icon {
      display: none;
    }
  }
</style>
