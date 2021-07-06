import ChatWindow from '@/views/chat/ChatWindow.vue';
import Position from '@/views/chat/Position.vue';
import GroupChatWindow from '@/views/chat/GroupChatWindow.vue';
export default [
  {
    path: '/chat',
    component: ChatWindow
  },
  {
    path: '/position',
    component: Position
  },
  {
    path: '/groupChat',
    component: GroupChatWindow
  }
];
