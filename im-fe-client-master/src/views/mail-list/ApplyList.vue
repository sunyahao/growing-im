<template>
  <view-page class="apply-list" title="新的朋友">
    <div class="wrapper">
      <apply-item :apply="apply" v-for="apply of applyList" :key="apply.id" @click="onClick(apply)"></apply-item>
    </div>
    <van-action-sheet v-model="show" :actions="actions" close-on-click-action cancel-text="取消" @select="onSelect" @cancel="onCancel" />
  </view-page>
</template>

<script>
import ApplyItem from './compenents/ApplyItem';
import { mapMutations, mapActions } from 'vuex';
import { dateFormat } from '@/utils/format.js';

export default {
  name: 'ApplyList',
  components: {
    ApplyItem
  },
  props: {},
  data() {
    return {
      show: false,
      actions: [
        { name: '通过', color: '#07c160' },
        { name: '拒绝', color: 'red' }
      ],
      activeApplyId: '',
      activeApplyType: '',
      applyList: []
    };
  },
  computed: {},
  watch: {},
  created() {
    this.getApplies();
  },
  methods: {
    ...mapMutations('mail', ['updateApplyCount']),
    ...mapMutations('im', ['addConversation', 'activateConversation']),
    ...mapActions('mail', ['getMailList']),
    onCancel() {
      this.show = false;
    },
    onSelect(item) {
      const result = item.name === '通过';
      if (this.activeApplyType === 'user') {
        this.applyFriend(result);
      } else if (this.activeApplyType) {
        this.applyGroup(result);
      }
    },
    onClick(apply) {
      this.activeApplyId = apply.id;
      this.activeApplyType = apply.type;
      this.show = true;
    },
    // approval为true就是通过，false就是拒绝
    applyFriend(approval) {
      const params = {
        id: this.activeApplyId,
        approval
      };
      this.$http
        .post(this.$urls.add.applyFriend, params)
        .then(data => {
          if (data.data.status == "success") {
            this.$toast('已同意申请');
            // this.addConversation({
            //   type: 'chat',
            //   id: data.data.conversationId,
            //   isActive: false,
            //   info: {
            //     name: ''
            //   },
            //   target: data.target,
            //   updatedAt: dateFormat('YYYY-mm-dd HH:MM:SS', new Date()),
            //   messageCount: 0,
            //   messageList: [],
            //   pageNumber: 2,
            //   pageSize: 10,
            //   refreshing: false,
            //   loading: false,
            //   finished: false
            // });
            // this.activateConversation({
            //   conversationId: data.data.conversationId
            // });
            // this.$router.replace('/chat');
          } else {
            this.$toast('已拒绝申请');
          }
        })
        .catch(error => {
          this.$toast("操作失败");
        })
        .finally(() => {
          this.getApplies();
        });
    },
    applyGroup(approval) {
      const params = {
        id: this.activeApplyId,
        approval
      };
      this.$http
        .post(this.$urls.add.applyGroup, params)
        .then(data => {
          if (data.data.status == "success") {
            this.$toast('已同意申请');
            this.$router.back();
            this.getMailList();
          } else {
            this.$toast('已拒绝申请');
            this.$router.back();
            this.getMailList();
          }

        })
        .catch(error => {
          this.$toast(error.errorMessage);
        })
        .finally(() => {
          this.getApplies();
        });
    },
    getApplies() {
      this.$http
        .post(this.$urls.add.applies, {
          userId: localStorage.getItem("userId")
        })
        .then(data => {
          var applyList1 = data.data.applies;
          this.updateApplyCount(data.data.applies.length);
          var applyList = [];
          for (var i=0;i<applyList1.length;i++) {
            var apply = {};
            var userInfo = {};
            userInfo.photo = applyList1[i].user.avatar;
            userInfo.nickname = applyList1[i].user.nick;
            apply.type = applyList1[i].apply.applyType == 0 ? "user" : "group";
            apply.id = applyList1[i].apply.id;
            apply.userInfo = userInfo;
            applyList.push(apply);
          }
          this.applyList = applyList;
        })
        .catch(error => {
          this.$toast(error.errorMessage);
        });
    }
  }
};
</script>

<style lang="scss">
.apply-list {
  .apply-item:not(:last-child) {
    &:after {
      position: absolute;
      box-sizing: border-box;
      content: ' ';
      pointer-events: none;
      top: -50%;
      right: -50%;
      bottom: -50%;
      left: -50%;
      border: 0 solid #ebedf0;
      margin-left: 10px;
      margin-right: 10px;
      -webkit-transform: scale(0.5);
      transform: scale(0.5);
      border-bottom-width: 1px;
    }
  }
}
</style>
