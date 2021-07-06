import AddFriend from '@/views/add/AddFriend.vue';
import AddGroup from '@/views/add/AddGroup.vue';
import CreateGroup from '@/views/add/CreateGroup.vue';
import ShareGroup from '@/views/add/ShareGroup.vue';
export default [
  {
    path: '/add-friend',
    component: AddFriend
  },
  {
    path: '/add-group',
    component: AddGroup
  },
  {
    path: '/create-group',
    component: CreateGroup
  },
  {
    path: '/share-group',
    component: ShareGroup
  }
];
