package com.sunhao.tim.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunhao.tim.entity.Conversation;
import com.sunhao.tim.entity.Grouz;
import com.sunhao.tim.entity.Message;
import com.sunhao.tim.entity.User;
import com.sunhao.tim.service.ConversationService;
import com.sunhao.tim.service.GroupService;
import com.sunhao.tim.service.MessageService;
import com.sunhao.tim.service.UserService;
import com.sunhao.tim.util.SnowflakeIdWorker;
import com.sunhao.tim.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  9:41
 */
@RestController
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @RequestMapping("/api/v1/conversations")
    public JSONObject getConversation(@RequestBody Map map) {
        String userId = (String) map.get("userId");
        JSONObject jsonObject = new JSONObject();
        JSONArray conversations = new JSONArray();
        List<Conversation> conversationList = conversationService.findConversationByFid(userId);
        for(Conversation conversation : conversationList) {
            conversations.add(conversation);
        }
        jsonObject.put("conversations", conversations);
        return jsonObject;
    }

    // 返回私聊窗口的消息
    @RequestMapping("/api/v1/conversations/active")
    public JSONObject getConversationMessage(@RequestBody Map map) {
        String userId = (String) map.get("userId");
        String conversationId = map.get("conversationId")+"";
        String toId = (String) map.get("toId");
        JSONObject jsonObject = new JSONObject();
        JSONArray messages = new JSONArray();
        List<Message> messageList = messageService.findMessagesByFIdAndTId(userId, toId);
        // 目标用户发给我的
        List<Message> otherMessageList = messageService.findMessagesByFIdAndTId(toId, userId);
        List<MessageVO> allMessageList = new ArrayList<>();
        for (Message message:messageList) {
            User user = userService.findUserByUserId(userId);
            String nick = user.getNick();
            String photo = user.getAvatar();
            MessageVO messageVO = new MessageVO(message, nick, photo, 0);
            allMessageList.add(messageVO);
        }
        for (Message message:otherMessageList) {
            User user = userService.findUserByUserId(toId);
            String nick = user.getNick();
            String photo = user.getAvatar();
            MessageVO messageVO = new MessageVO(message, nick, photo, 1);
            allMessageList.add(messageVO);
        }
        Collections.sort(allMessageList, new Comparator<MessageVO>() {
            @Override
            public int compare(MessageVO o1, MessageVO o2) {
                return o1.getSendTime().compareTo(o2.getSendTime());
            }
        });
        for (MessageVO messageVO : allMessageList) {
            messages.add(messageVO);
        }
        jsonObject.put("messages", messages);
        jsonObject.put("conversationId", conversationId);
        return jsonObject;
    }

    // 返回群聊窗口的消息
    @RequestMapping("/api/v1/conversations/groupActive")
    public JSONObject getConversationGroupMessage(@RequestBody Map map) {
        String userId = (String) map.get("userId");
        Integer conversationId = (Integer) map.get("conversationId");
        String toId = (String) map.get("toId")+"";
        JSONObject jsonObject = new JSONObject();
        JSONArray messages = new JSONArray();
        Conversation groupConversation = conversationService.findConversationById(conversationId.longValue());
        // toId是群号，userId是我的号
        List<Message> messageList = messageService.findMessagesByFIdAndTIdAndChatType(userId, toId);
        // 群众其他成员所发的消息
        List<Message> otherMessageList = messageService.findMessagesByTIdAndChatType(userId, toId);
        List<MessageVO> allMessageList = new ArrayList<>();
        for (Message message:messageList) {
            User user = userService.findUserByUserId(message.getFId());
            String nick = user.getNick();
            String photo = user.getAvatar();
            MessageVO messageVO = new MessageVO(message, nick, photo, 0);
            allMessageList.add(messageVO);
        }
        for (Message message:otherMessageList) {
            User user = userService.findUserByUserId(message.getFId());
            String nick = user.getNick();
            String photo = user.getAvatar();
            MessageVO messageVO = new MessageVO(message, nick, photo, 1);
            allMessageList.add(messageVO);
        }
        Collections.sort(allMessageList, new Comparator<MessageVO>() {
            @Override
            public int compare(MessageVO o1, MessageVO o2) {
                return o1.getSendTime().compareTo(o2.getSendTime());
            }
        });
        for (MessageVO messageVO : allMessageList) {
            messages.add(messageVO);
        }
        jsonObject.put("messages", messages);
        jsonObject.put("groupConversation", groupConversation);
        return jsonObject;
    }

    // 添加私聊会话窗口
    @RequestMapping("/api/v1/add/person/conversation")
    public JSONObject add(@RequestBody Map map) {
        String fromId = (String) map.get("fromId");
        String toId = (String) map.get("toId");
        String type = (String) map.get("type");
        JSONObject jsonObject = new JSONObject();
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(4);
        Boolean flag = conversationService.findConversationByFIdAndTId(fromId, toId);
        if (flag) {
            jsonObject.put("status", 400);
        } else{
            Conversation conversation = new Conversation();
            conversation.setLastMsgTime(new Date());
            if (type.equals("user")) {
                conversation.setChatType(0);
                User user = userService.findUserByUserId(toId);
                if (user != null) {
                    conversation.setName(user.getNick());
                    conversation.setAvatar(user.getAvatar());
                } else{
                    conversation.setAvatar("");
                    conversation.setName("");
                }
            } else {
                conversation.setChatType(1);
                Grouz group = groupService.findGroupByGroupId(toId);
                if(group != null) {
                    conversation.setAvatar(group.getAvatar());
                    conversation.setName(group.getName());
                } else{
                    conversation.setAvatar("");
                    conversation.setName("");
                }
            }
            conversation.setFId(fromId);
            conversation.setTId(toId);
            // 创建新对话
            conversationService.save(conversation);
            Conversation conversation1 = conversationService.findConversationByFromIdAndToId(fromId, toId);
            jsonObject.put("conversation", conversation1);
        }
        return jsonObject;
    }

    // 通过fromId和toId查到conversationId
    @RequestMapping("/api/v1/search/conversation")
    public JSONObject searchConversation(@RequestBody Map map) {
        String fromId = (String) map.get("fromId");
        String toId = (String) map.get("toId");
        JSONObject jsonObject = new JSONObject();
        Conversation conversation = conversationService.selectConversationByFromIdAndToId(fromId, toId);
        jsonObject.put("conversationId", conversation.getId());
        return jsonObject;
    }
}
