package com.sunhao.tim.service.serviceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.sunhao.tim.entity.Conversation;
import com.sunhao.tim.entity.Message;
import com.sunhao.tim.pojo.PersonMessage;
import com.sunhao.tim.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  9:07
 */
@Slf4j
@Service
public class SocketIOServiceImpl implements ISocketIOService {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserService userService;

    /**
     * 存放已连接的客户端
     */
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    /**
     * 自定义事件`push_data_event`,用于服务端与客户端通信
     */
    private static final String PUSH_DATA_EVENT = "/v1/im/new-message";

    private static final String PUSH_CHAT_RECORD = "/v1/im/get-messages";

    private static final String PUSH_PERSON_CHAT= "/v1/im/new-message1";

    private static final String PUSH_GROUP_CHAT = "/v1/im/new-group-message1";

    // 私聊：客户端监听的别人发送消息事件
    private static final String ON_CLIENT_PERSON_OTHER_CHAT = "/v1/im/person-other-messages1";

    // 私聊：客户端监听自己发送的消息的事件
    private static final String ON_CLIENT_PERSON_OWN_CHAT = "/v1/im/person-own-messages1";

    // 群聊：客户端监听的别人发送消息事件
    private static final String ON_CLIENT_GROUP_OTHER_CHAT = "/v1/im/group-other-messages1";

    // 群聊：客户端监听自己发送的消息的事件
    private static final String ON_CLIENT_GROUP_OWN_CHAT = "/v1/im/group-own-messages1";

    @Autowired
    private SocketIOServer socketIOServer;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     */
    @PostConstruct
    private void autoStartup() {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     */
    @PreDestroy
    private void autoStop() {
        stop();
    }

    @Override
    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            log.info("************ 客户端： " + getIpByClient(client) + " 已连接 ************");
            // 自定义事件`connected` -> 与客户端通信  （也可以使用内置事件，如：Socket.EVENT_CONNECT）
            client.sendEvent("connect", "你成功连接上了哦...");
            String userId = getParamsByClient(client);
            if (userId != null) {
                clientMap.put(userId, client);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String clientIp = getIpByClient(client);
//            log.debug(clientIp + " *********************** " + "客户端已断开连接");
            String userId = getParamsByClient(client);
            if (userId != null) {
                clientMap.remove(userId);
                client.disconnect();
            }
        });

        /*
         * 添加监听事件，监听客户端的事件
         * 1.第一个参数eventName需要与客户端的事件要一致
         * 2.第二个参数eventClase是传输的数据类型
         * 3.第三个参数listener是用于接收客户端传的数据，数据类型需要与eventClass一致
         */

        // 自定义事件`client_info_event` -> 监听客户端消息
        socketIOServer.addEventListener(PUSH_DATA_EVENT, String.class, (client, data, ackSender) -> {
            // 客户端推送`client_info_event`事件时，onData接受数据，这里是string类型的json数据，还可以为Byte[],object其他类型
            String clientIp = getIpByClient(client);
//            log.debug(clientIp + " ************ 客户端：" + data);
        });

        // 自定义事件`client_info_event` -> 监听客户端消息
        socketIOServer.addEventListener(PUSH_CHAT_RECORD, String.class, (client, data, ackSender) -> {

            JSONObject gpsData = (JSONObject) JSONObject.parse(data);
            String conversationId = gpsData.get("conversationId")+"";
            String fromId = gpsData.get("fromId")+"";
            String toId = gpsData.get("toId")+"";
            JSONObject jsonObject = new JSONObject();
            JSONArray messages = new JSONArray();
            // 找到消息的历史记录
            List<Message> messageList = messageService.findMessagesByFIdAndTId(fromId, toId);
            for (Message message:messageList) {
                messages.add(message);
            }
            jsonObject.put("messages", messages);
            jsonObject.put("conversationId", conversationId);
            client.sendEvent(PUSH_CHAT_RECORD, jsonObject);
        });

        // 自定义事件`PUSH_PERSON_CHAT` -> 监听客户端新消息
        // 参数中的client是指发送给服务端的client
        socketIOServer.addEventListener(PUSH_PERSON_CHAT, PersonMessage.class, (client, data, ackSender) -> {
//            JSONObject gpsData = (JSONObject) JSONObject.parse(data);
//            String conversationId = gpsData.get("conversationId")+"";
//            String fromId = gpsData.get("fromId")+"";
//            String toId = gpsData.get("toId")+"";
//            Object body = gpsData.get("body")+"";
            String conversationId = data.getConversationId();
            String fromId = data.getFromId();
            String toId = data.getToId();
            Map<String, String> body = (Map<String, String>) data.getBody();
            JSONObject jsonObject = new JSONObject();
            Conversation reverseConversation = conversationService.selectConversationByFromIdAndToId(toId, fromId);
            String reverseConversationId = reverseConversation == null ? "" : String.valueOf(reverseConversation.getId());
            Message message = new Message();
            message.setChatType(0);
            if (body.get("type").equals("text")) {
                message.setMsgType(0);
                message.setContent(body.get("msg"));
            } else if (body.get("type").equals("loc")) {
                message.setMsgType(1);
                JSONObject loc = new JSONObject();
                loc.put("addr", body.get("addr"));
                loc.put("lat", body.get("lat"));
                loc.put("lng", body.get("lng"));
                // 将这些信息设置为json字符串格式
                message.setContent(loc.toJSONString());
            } else if (body.get("type").equals("image")) {
                message.setMsgType(2);
                message.setContent(body.get("url"));
            } else if (body.get("type").equals("video")) {
                message.setMsgType(3);
                message.setContent(body.get("url"));
            }
            message.setSendTime(new Date());
            message.setFId(fromId);
            message.setTId(toId);
            messageService.addMessage(message);
            String photo = userService.findUserByUserId(fromId).getAvatar();
            String nickname = userService.findUserByUserId(fromId).getNick();
            jsonObject.put("message", message);
            jsonObject.put("conversationId", conversationId);
            jsonObject.put("reverseConversationId", reverseConversationId);
            jsonObject.put("photo", photo);
            jsonObject.put("nickname", nickname);
            // 如果目标用户没上线。。就啥都干不了
            if (!clientMap.containsKey(toId)) {
            } else {
                SocketIOClient toClient = clientMap.get(toId);
                toClient.sendEvent(ON_CLIENT_PERSON_OTHER_CHAT, jsonObject);
            }
            // 发给自己
            if (!clientMap.containsKey(fromId)) {
                return ;
            } else {
                SocketIOClient toClient = clientMap.get(fromId);
                toClient.sendEvent(ON_CLIENT_PERSON_OWN_CHAT, jsonObject);
            }
        });

        // 监听客户端发来的群聊消息，其中fromId是客户端发送者ID，toId是群ID，要做的是将找到所有在线的群成员，将消息广播给他们(不包括发送者)
        // 同时也将消息发给发送者自己(事件不同)
        socketIOServer.addEventListener(PUSH_GROUP_CHAT, PersonMessage.class, (client, data, ackSender) -> {
            String conversationId = data.getConversationId();
            String fromId = data.getFromId();
            String toId = data.getToId();
            Map<String, String> body = (Map<String, String>) data.getBody();
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            Message message = new Message();
            message.setChatType(1);
            if (body.get("type").equals("text")) {
                message.setMsgType(0);
                message.setContent(body.get("msg"));
            } else if (body.get("type").equals("loc")) {
                message.setMsgType(1);
                JSONObject loc = new JSONObject();
                loc.put("addr", body.get("addr"));
                loc.put("lat", body.get("lat"));
                loc.put("lng", body.get("lng"));
                // 将这些信息设置为json字符串格式
                message.setContent(loc.toJSONString());
            } else if (body.get("type").equals("image")) {
                message.setMsgType(2);
                message.setContent(body.get("url"));
            } else if (body.get("type").equals("video")) {
                message.setMsgType(3);
                message.setContent(body.get("url"));
            }
            message.setSendTime(new Date());
            message.setFId(fromId);
            message.setTId(toId);

            messageService.addMessage(message);
            String photo = userService.findUserByUserId(fromId).getAvatar();
            String nickname = userService.findUserByUserId(fromId).getNick();
            jsonObject.put("message", message);
            jsonObject.put("photo", photo);
            jsonObject1.put("message", message);
            jsonObject1.put("photo", photo);
            jsonObject.put("nickname", nickname);
            jsonObject1.put("nickname", nickname);
            List<String> userIds = userGroupService.findUserIdByGroupId(toId);
            for (String str : userIds) {
                // 如果是发送者自己就跳过这个循环
                if (str.equals(fromId)) {
                    continue ;
                }
                // 如果目标用户没上线。。就跳过这个循环
                if (!clientMap.containsKey(str)) {
                    //TODO
                    continue ;
                } else {
                    SocketIOClient toClient = clientMap.get(str);
                    // 找到其他用户和群的对话id
                    Conversation conversation = conversationService.selectConversationByFromIdAndToIdGroupType(str, toId);
                    String conversationId1 = conversation == null ? "" : String.valueOf(conversation.getId());
                    jsonObject.put("conversationId", conversationId1);
                    toClient.sendEvent(ON_CLIENT_GROUP_OTHER_CHAT, jsonObject);
                }
            }
                // 发给自己
                if (!clientMap.containsKey(fromId)) {
                    return ;
                } else {
                    SocketIOClient toClient = clientMap.get(fromId);
                    jsonObject1.put("conversationId", conversationId);
                    toClient.sendEvent(ON_CLIENT_GROUP_OWN_CHAT, jsonObject1);
                }
        });

        // 启动服务
        socketIOServer.start();

        // broadcast: 默认是向所有的socket连接进行广播，但是不包括发送者自身，如果自己也打算接收消息的话，需要给自己单独发送。
        new Thread(() -> {
            int i = 0;
            while (true) {
                try {
                    // 每3秒发送一次广播消息
                    Thread.sleep(3000);
                    socketIOServer.getBroadcastOperations().sendEvent("myBroadcast", "广播消息 ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    @Override
    public void pushMessageToUser(String userId, String msgContent) {
        SocketIOClient client = clientMap.get(userId);
        if (client != null) {
            client.sendEvent(PUSH_DATA_EVENT, msgContent);
        }
    }

    /**
     * 获取客户端url中的userId参数（这里根据个人需求和客户端对应修改即可）
     *
     * @param client: 客户端
     * @return: java.lang.String
     */
    private String getParamsByClient(SocketIOClient client) {
        // 获取客户端url参数（这里的userId是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> userIdList = params.get("userId");
        if (!CollectionUtils.isEmpty(userIdList)) {
            return userIdList.get(0);
        }
        return null;
    }

    /**
     * 获取连接的客户端ip地址
     *
     * @param client: 客户端
     * @return: java.lang.String
     */
    private String getIpByClient(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":"));
        return clientIp;
    }
}

