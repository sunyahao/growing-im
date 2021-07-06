<template>
  <view-page class="edit-nickname" title="编辑昵称">
    <van-form @submit="onSubmit">
      <van-field v-model="nickname" label="昵称" :rules="[{ required: true, message: '请填写昵称' }]" />
      <div style="margin: 16px;">
        <van-button round block type="info" native-type="submit">保存</van-button>
      </div>
    </van-form>
  </view-page>
</template>

<script>
import { mapGetters } from 'vuex';
import { getUserInfo, updateUserInfo } from '@/services/edit.js';

export default {
  name: 'edit-nickname',
  components: {},
  props: {},
  data() {
    return {
      nickname: ''
    };
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  watch: {},
  created() {
    var user = localStorage.getItem("userInfo");
    var userInfo = JSON.parse(user);
    this.nickname = userInfo.nickname;
  },
  methods: {
    onSubmit() {
      if (!this.nickname) {
        return;
      }
      var userInfo = JSON.parse(localStorage.getItem("userInfo"));
      userInfo.nickname = this.nickname;
      this.$http.post(this.$urls.restful.userInfo, {
        userInfo : userInfo
      })
        .then(data => {
          debugger;
          this.nickname = data.data.userInfo1.nickname;
          localStorage.removeItem("userInfo");
          localStorage.setItem("userInfo", JSON.stringify(data.data.userInfo1));
          this.$toast('操作完成');
          this.$router.back();
        })
        .catch();
    }
  }
};
</script>

<style lang="scss">
.edit-nickname {
  .van-field__label {
    width: 50px;
  }
}
</style>
