<template>
    <view-page class="user-info">
      <ul class="clearfix">  
                    <li v-if="imgs.length>0" v-for='(item ,index ) in imgs'>  
                        <img :src="item">  
                    </li>  
                    <li style="position:relative" v-if="change">  
                        <img :src="avatar">
                    </li>  
        <input class="upload" @change='add_img'  type="file">  
      </ul>
    </view-page>
</template>

<script>
import { mapGetters } from 'vuex';
import { VueCropper } from 'vue-cropper';
import { getUserInfo, updateUserInfo } from '@/services/edit.js';

export default {
  name: 'EditAvatar',
  components: {
    VueCropper
  },
  props: {},
  data() {
    return {
      imgs: [],
      imgData: {
        accept: 'image/gif, image/jpeg, image/png, image/jpg',
      },
      userId: localStorage.getItem("userId"),
      change: true
    };
  },
  computed: {
    ...mapGetters(['userInfo']),
    avatar() {
      var a = JSON.parse(localStorage.getItem("userInfo"));
      if(a.photo) {
        return a.photo;
      } else {
        return '/public/images/1.jpeg';
      }
    }
  },
  watch: {},
  created() {
    this.img = this.userInfo.photo;
  },
  methods: {
    add_img(event){
      let reader =new FileReader();
      let img1=event.target.files[0];
      let type=img1.type;//文件的类型，判断是否是图片  
      let size=img1.size;//文件的大小，判断图片的大小  
      if(this.imgData.accept.indexOf(type) == -1){
        alert('请选择我们支持的图片格式！');
        return false;
      }
      if(size>3145728){
        alert('请选择3M以内的图片！');
        return false;
      }
      var uri = ''
      let form = new FormData();
      form.append('file',img1,img1.name);
      form.append('userId', localStorage.getItem('userId'))
      this.$http.post('/api/v1/upload',form,{
        headers:{'Content-Type':'multipart/form-data'}
      }).then(response => {
        debugger;
        console.log(response.data);
        if (response.data.status == 400) {
          this.$toast('上传出错');
        } else{
          uri = response.data.url;
          reader.readAsDataURL(img1);
          var that=this;
          var a = JSON.parse(localStorage.getItem("userInfo"));
          a.photo = "http://localhost:7001"+uri;
          localStorage.removeItem("userInfo");
          localStorage.setItem("userInfo", JSON.stringify(a));
          reader.onloadend=function(){
            that.change = false;
            that.imgs = [];
            that.imgs.push("http://localhost:7001"+uri);
          }
        }
      }).catch(error => {
        alert('上传图片出错！');
      })
    }, 
  }
};
</script>

<style lang="scss">
.edit-avatar {
  position: relative;
  .vue-cropper {
    width: 100%;
    height: 100%;
  }
  .bottom {
    width: 100%;
    position: absolute;
    bottom: 0;
    .van-button {
      margin-top: 10px;
    }
  }
}
</style>
