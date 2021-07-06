<template>
  <div class="hy-upload">
    <input ref="input" type="file" @change="changeHandler" :multiple="multiple" :accept="accept" />
  </div>
</template>

<script>
export default {
  name: 'HyUpload',
  components: {},
  props: {
    limit: Number,
    multiple: {
      type: Boolean,
      default: false
    },
    accept: {
      type: String,
      default: 'image/*'
    },
    action: {
      type: Object,
      default() {
        return {
          target: '/api/v1/socketIO/upload'
        };
      }
    }
  },
  data() {
    return {};
  },
  methods: {
    click() {
      this.$refs.input.click();
    },
    changeHandler(e) {
      const files = e.currentTarget.files;
      if (files) {
        this.uploadFiles(files);
      }
    },
    uploadFiles(files) {
      if (this.limit && files.length > this.limit) {
        return;
      }

      for (const file of files) {
        if (file.size / 1024 / 1024 > 5) {
          this.$toast('最大上传文件为5M');
          return;
        }
      }

      let postFiles = Array.prototype.slice.call(files);
      if (!this.multiple) {
        postFiles = postFiles.slice(0, 1);
      }

      if (postFiles.length === 0) {
        return;
      }
      this.upload(postFiles);
    },
    upload(files) {
      var that = this;
      this.$refs.input.value = null;
      const formData = new FormData();
      for (const file of files) {
        formData.append('file', file, file.name);
      }
      this.$http
        .post(this.action.target, formData, {
        headers:{'Content-Type':'multipart/form-data'}
      })
        .then(data => {
          // 把得到的url发送给它
          that.$emit('file-success', data.data.urls);
        })
        .catch(error => {
          console.log("图片上传出错");
        });
    }
  }
};
</script>

<style lang="scss">
.hy-upload {
}
</style>
