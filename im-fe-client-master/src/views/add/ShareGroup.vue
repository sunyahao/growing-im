<template>
  <van-form @submit="onSubmit" class="sign-up">
      <van-field v-model="groupId" label="群id" />
      <div class="qrcode" ref="qrCodeUrl"></div>
      <div style="margin: 16px;">
        <van-button round block type="info" native-type="submit" >创建</van-button>
      </div>
  </van-form>
</template>

<script>
  import { mapMutations } from 'vuex';
  import QRCode from 'qrcodejs2';

  export default {
    name: 'ShareGroup',
    components: {},
    props: {},
    data() {
      return {
        groupName: '',
        groupAvatar: '',
        imgData: {
          accept: 'image/gif, image/jpeg, image/png, image/jpg',
        },
        query: String,
        groupId: String
      };
    },
    computed: {},
    watch: {},
    created() {

    },
    methods: {
      onSubmit() {
        this.$http
                .post('/api/v1/judgeOwner', {
                  userId: localStorage.getItem("userId"),
                  groupId: this.groupId
                })
                .then(data => {
                  if (data.data.status == 400) {
                    this.$toast.success('你不是该群群主，不能进行分享');
                  } else{
                    this.creatQrCode();
                  }
                })
                .catch(error => {
                  this.$toast("异常错误");
                });
      },
      creatQrCode() {
        var qrcode = new QRCode(this.$refs.qrCodeUrl, {
          text: this.groupId, // 需要转换为二维码的内容
          width: 100,
          height: 100,
          colorDark: '#000000',
          colorLight: '#ffffff',
          correctLevel: QRCode.CorrectLevel.H
        })
      },
    },
    mounted() {
      this.query = this.$route.query;
      var a = this.query;
      debugger;
      this.groupId = this.query.toId;
    }
  };
</script>

<style lang="scss">
  .sign-up {
  }
</style>
