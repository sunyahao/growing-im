<template>
  <view-page class="edit-sign" title="编辑签名">
    <van-form @submit="onSubmit">
      <van-field v-model="sign" label="签名" :rules="[{ required: true, message: '请填写签名' }]" />
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
  name: 'edit-sign',
  components: {},
  props: {},
  data() {
    return {
      sign: ''
    };
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  watch: {},
  created() {
    var user = localStorage.getItem("userInfo");
    var userInfo = JSON.parse(user);
    this.sign = userInfo.sign;
  },
  methods: {
    onSubmit() {
      if (!this.sign) {
        return;
      }
      var userInfo = JSON.parse(localStorage.getItem("userInfo"));
      userInfo.sign = this.sign;
      this.$http.post(this.$urls.restful.userInfo, {
        userInfo : userInfo
      })
              .then(data => {
                this.sign = data.data.userInfo1.sign;
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
.edit-sign {
  .van-field__label {
    width: 50px;
  }
}
</style>
