<template>
  <van-form @submit="onSubmit" class="sign-up">
    <van-field v-model="groupName" label="群名" placeholder="请填写群名" :rules="usernameRules" />
      <input class="upload"   type="file" id="groupFile">  
    <div style="margin: 16px;">
      <van-button round block type="info" native-type="submit" >创建</van-button>
    </div>
  </van-form>
</template>

<script>
  import { mapMutations } from 'vuex';

  export default {
    name: 'CreateGroup',
    components: {},
    props: {},
    data() {
      return {
        groupName: '',
        groupAvatar: '',
        imgData: {
          accept: 'image/gif, image/jpeg, image/png, image/jpg',
        },
        usernameRules: [
          { required: true, message: '请填写群名' },
          { pattern: /\w{2,10}/, message: '群名长度(2-20)位' }
        ]
      };
    },
    computed: {},
    watch: {},
    created() {},
    methods: {
      onSubmit() {
        const fileInput = document.querySelector('#groupFile');
        let reader =new FileReader();
        let img1=fileInput.files[0];
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
        var uri = '';
        let form = new FormData();
        form.append('file',img1,img1.name);
        form.append('userId', localStorage.getItem('userId'))//设置groupId
        form.append('groupName', this.groupName);
        // this.$http.post('/api/v1/uploadGroup',form,{
        //   headers:{'Content-Type':'multipart/form-data'}
        // }).then(response => {
        //   debugger;
        //   console.log(response.data);
        //   if (response.data.status == 400) {
        //     this.$toast('上传出错');
        //   } else{
        //     uri = response.data.url;
        //     reader.readAsDataURL(img1);
        //     if (localStorage.getItem("groupPhoto")) {
        //       localStorage.removeItem("groupPhoto");
        //     }
        //     localStorage.setItem("groupPhoto", uri);
        //   }
        // }).catch(error => {
        //   alert('上传图片出错！');
        // })

        this.$http
                .post(this.$urls.add.createGroup, form, {
                  headers:{'Content-Type':'multipart/form-data'}
                })
                .then(data => {
                  if (data.data.status == 400) {
                    this.$toast.success('群名已存在');
                  } else{
                    this.$toast.success('创建成功');
                    uri = data.data.url;
                    if (localStorage.getItem("groupPhoto")) {
                      localStorage.removeItem("groupPhoto");
                    }
                    localStorage.setItem("groupPhoto", uri);
                    this.$router.back();
                  }
                })
                .catch(error => {
                  this.$toast("异常错误");
                });
      },
    }
  };
</script>

<style lang="scss">
  .sign-up {
  }
</style>
