package com.sunhao.tim.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunhao.tim.entity.Conversation;
import com.sunhao.tim.entity.User;
import com.sunhao.tim.service.ApplyService;
import com.sunhao.tim.service.ConversationService;
import com.sunhao.tim.service.FriendService;
import com.sunhao.tim.service.UserService;
import com.sunhao.tim.vo.MailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  14:14
 */
@RestController
public class FriendsController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplyService applyService;

    @RequestMapping("/api/v1/friend")
    public JSONObject findFriendList(@RequestBody Map map){
        String userId = (String) map.get("userId");
        List<String> friendList = friendService.getFriendList(userId);
        JSONObject jsonObject = new JSONObject();
        JSONObject mall = new JSONObject();
        JSONArray mails = new JSONArray();
        for (String toId : friendList) {
            User user = userService.findUserByUserId(toId);
            Conversation conversation = conversationService.selectConversationByFromIdAndToId(userId, toId);
            MailVo mailVo = new MailVo(user, conversation);
            mails.add(mailVo);
        }
        jsonObject.put("mails", mails);
        return jsonObject;
    }

    @RequestMapping("/api/v1/add/searchFriend")
    public JSONObject addFriend(@RequestBody Map map) {
        // 目标用户的userId
        String searchValue = (String) map.get("searchValue");
        JSONArray users = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        List<User> userList = userService.findUserByPartUserId(searchValue);
        for(User u : userList) {
            JSONObject user = new JSONObject();
            user.put("type", "user");
            user.put("photo", u.getAvatar());
            user.put("sign", u.getSign());
            user.put("nickname", u.getNick());
            user.put("id", u.getUserId());
            users.add(user);
        }
        jsonObject.put("users", users);
        return jsonObject;
    }
}
