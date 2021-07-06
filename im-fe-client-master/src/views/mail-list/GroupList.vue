<template>
  <view-page class="group-list">
    <group-item v-for="group of groupList" :group="group" :key="group.id" @click.native="onCLick(group)"></group-item>
  </view-page>
</template>

<script>
import GroupItem from './compenents/GroupItem';
import { mapMutations } from 'vuex';
import { dateFormat } from '@/utils/format.js';

export default {
  name: 'group-list',
  components: {
    GroupItem
  },
  props: {},
  data() {
    return {
      groupList: []
    };
  },
  computed: {},
  watch: {},
  created() {
    this.getGroupList();
  },
  methods: {
    ...mapMutations('im', ['addConversation', 'activateConversation']),
    onCLick(group) {
      // 在服务器端添加会话
      this.$http
              .post(this.$urls.add.addConversation, {
                fromId: localStorage.getItem("userId"),
                toId: group.group.groupId,
                type: "group"
              })
              .then(data => {
                debugger;
                  if (data.data.status != 400 && group.conversation == undefined) {
                    if (group.hasOwnProperty('conversation')) {
                      var conversation = {};
                      conversation.id = data.data.conversation.id;
                      group.conversation = conversation;
                    } else {
                      group.conversation.id = data.data.conversation.id;
                    }
                    // 在客户端添加会话
                    this.addConversation({
                      type: 'groupchat',
                      id: group.conversation.id,
                      isActive: false,
                      info: {
                        name: data.data.conversation.name
                      },
                      target: group,
                      updatedAt: dateFormat('YYYY-mm-dd HH:MM:SS', new Date()),
                      messageCount: 0,
                      messageList: [],
                      pageNumber: 2,
                      pageSize: 10,
                      refreshing: false,
                      loading: false,
                      finished: false
                    });
                  }

                var a = group.conversation;
                this.activateConversation({
                  conversationId: a.id
                });
                this.$router.push({
                  path: '/groupChat',
                  query: {
                    toId: group.group.groupId,
                    conversationId: group.conversation.id
                  }
                });
              })
              .catch(error => {
                this.$toast("异常错误");
              });

    },
    getGroupList() {
      this.$http
        .post(this.$urls.restful.groups, {
          userId: localStorage.getItem("userId")
        })
        .then(data => {
          var groupList1 = data.data.groupList;
          var groupList = [];
          for(var i = 0;i<groupList1.length;i++) {
            var g = {};
            var conversation = {};
            var group = {};
            conversation = groupList1[i].conversation;
            group = groupList1[i].group;
            group.photo = groupList1[i].avatar;
            g.conversation = conversation;
            g.group = group;
            groupList.push(g);
          }
          this.groupList = groupList;
        })
        .catch(error => {
          this.$toast(error.errorMessage);
        });
    }
  }
};
</script>

<style lang="scss">
.group-list {
}
</style>
