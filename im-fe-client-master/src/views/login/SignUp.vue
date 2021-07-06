<template>
  <van-form @submit="onSubmit" class="sign-up">
    <van-field v-model="username" label="用户名" placeholder="请填写用户名" :rules="usernameRules" />
    <van-field v-model="password" type="password" label="密码" placeholder="请填写密码" :rules="passwordRules" />
    <van-field v-model="nick" label="昵称" placeholder="请填写昵称" :rules="nickRules" />
    <van-field v-model="sign" type="text" label="签名" placeholder="请填写签名" :rules="signRules" />
    <!--<van-field v-model="avatar" label="头像" placeholder="请选择头像" :rules="avatarRules" />-->
    <div style="margin: 16px;">
      <van-button round block type="info" native-type="submit">注册</van-button>
    </div>
  </van-form>
</template>

<script>
import { mapMutations } from 'vuex';

export default {
  name: 'SignUp',
  components: {},
  props: {},
  data() {
    return {
      username: '',
      password: '',
      nick: '',
      sign: '',
      usernameRules: [
        { required: true, message: '请填写用户名' },
        { pattern: /\w{4,10}/, message: '用户名长度(4-10)位' }
      ],
      passwordRules: [
        { required: true, message: '请填写密码' },
        { pattern: /\S{6,20}/, message: '密码长度(6-20)位' }
      ],
      nickRules: [
        { required: true, message: '请填写昵称' },
        { pattern: /\w{2,10}/, message: '昵称长度(2-10)位' }
      ],
      signRules: [
        { required: true, message: '请填写签名' },
        { pattern: /\S{0,100}/, message: '请不要超过100字' }
      ]
    };
  },
  computed: {},
  watch: {},
  created() {},
  methods: {
    ...mapMutations(['updateUserId', 'updateUserInfo']),
    onSubmit() {
      this.$http
        .post(this.$urls.login.signup, {
          username: this.username,
          password: this.password,
          nick: this.nick,
          sign: this.sign
        })
        .then(data => {
          this.$toast.success('注册成功');
          this.$router.back();
          // this.updateUserId({
          //   userId: data.id
          // });
          // this.updateUserInfo({
          //   userInfo: data.userInfo
          // });
          localStorage.setItem("userId", data.data.id);
          localStorage.setItem("userInfo", JSON.stringify(data.data.userInfo));
          setTimeout(() => {
            this.$router.replace('/');
          }, 10);
        })
        .catch(error => {
          this.$toast(error.errorMessage);
        });
    }
  }
};
</script>

<style lang="scss">
.sign-up {
}
</style>
