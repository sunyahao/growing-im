package com.sunhao.tim.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunhao.tim.entity.Apply;
import com.sunhao.tim.entity.Friends;
import com.sunhao.tim.entity.User;
import com.sunhao.tim.entity.UserGroup;
import com.sunhao.tim.service.*;
import com.sunhao.tim.util.SnowflakeIdWorker;
import com.sunhao.tim.vo.ApplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  16:57
 */
@RestController
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserGroupService userGroupService;

    // 查看申请列表
    @RequestMapping("/api/v1/applies")
    public JSONObject getUnReadApply(@RequestBody Map map){
        String userId = (String) map.get("userId");
        if (userId == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray applies = new JSONArray();
        List<Apply> applyList = applyService.getApplies(userId);
        List<Apply> applyListGroup = applyService.findApplyGroupIdsOwner(userId);
        User user = new User();
        for(Apply a : applyList) {
            if (a != null) {
                user = userService.findUserByUserId(a.getFId());
                ApplyVO applyVO = new ApplyVO(a, user);
                applies.add(applyVO);
            }
        }
        for(Apply a : applyListGroup) {
            user = userService.findUserByUserId(a.getFId());
            ApplyVO applyVO = new ApplyVO(a, user);
            applies.add(applyVO);
        }
        jsonObject.put("applies", applies);
        return jsonObject;
    }

    @RequestMapping("/api/v1/add/friend")
    public JSONObject applyFriend(@RequestBody Map map) {
        Boolean approval = (Boolean) map.get("approval");
        Integer applyId = (Integer) map.get("id");
        JSONObject jsonObject = new JSONObject();
        // 如果为false则是拒绝
        if (approval) {
            Apply apply = applyService.getApply(applyId);
            Boolean flag = applyService.applySuccess(applyId);
            User fromUser = userService.findUserByUserId(apply.getFId());
            SnowflakeIdWorker idWorker = new SnowflakeIdWorker(2);
//            Conversation conversation = new Conversation();
//            conversation.setLastMsgTime(new Date());
//            conversation.setChatType(0);
//            conversation.setFId(apply.getTId());
//            conversation.setTId(apply.getFId());
//            conversation.setName(fromUser.getNick());
//            conversation.setAvatar(fromUser.getNick());
//            conversation.setId(idWorker.nextId());
            // 创建新对话
//            conversationService.save(conversation);

            // 添加好友
            Friends friends = new Friends();
            friends.setUserId(apply.getTId());
            friends.setFriendId(apply.getFId());
            Boolean flag1 = friendService.addFriend(friends);
//            jsonObject.put("conversationId", conversation.getId());
            jsonObject.put("status", "success");
        } else {
            Boolean flag = applyService.applyRefuse(applyId);
            jsonObject.put("status", "refuse");
        }
        return jsonObject;
    }

    @RequestMapping("/api/v1/add/group")
    public JSONObject applyGroup(@RequestBody Map map) {
        Boolean approval = (Boolean) map.get("approval");
        Integer applyId = (Integer) map.get("id");
        JSONObject jsonObject = new JSONObject();
        // 如果为false则是拒绝
        if (approval) {
            Apply apply = applyService.getApply(applyId);
            // 更新状态
            Boolean flag = applyService.applySuccess(applyId);
            // 将申请人添加到群组
            UserGroup userGroup = new UserGroup();
            userGroup.setUserId(apply.getFId());
            userGroup.setGroupId(apply.getTId());
            userGroupService.addMember(userGroup);
            jsonObject.put("status", "success");
        } else {
            Boolean flag = applyService.applyRefuse(applyId);
            jsonObject.put("status", "refuse");
        }
        return jsonObject;
    }


    @RequestMapping("/api/v1/add/applies")
    public JSONObject addApply(@RequestBody Map map) {
        String type = (String) map.get("type");
        String userId = (String) map.get("userId");
        String toId = (String) map.get("toId");
        JSONObject jsonObject = new JSONObject();
        if (type.equals("group")) {
            boolean flag = userGroupService.findUserGroupByGroupIdAndAndUserId(toId, userId);
            if (flag) {
                // 你已在该群中
                jsonObject.put("status", 401);
            } else{
                Apply apply = new Apply();
                apply.setStatus(0);
                apply.setApplyType(1);
                apply.setFId(userId);
                apply.setTId(toId);
                apply.setInfo("来自"+userId+"的群申请");
                boolean flag1 = applyService.addGroupApply(apply);
                jsonObject.put("status", 201);
            }
        } else {
            boolean flag2 = friendService.findFriendsByUserIdAndAndFriendId(userId, toId);
            if (flag2) {
                // 你已是他的好友
                jsonObject.put("status", 400);
            } else{
                Apply apply = new Apply();
                apply.setStatus(0);
                apply.setApplyType(0);
                apply.setFId(userId);
                apply.setTId(toId);
                apply.setInfo("来自"+userId+"的好友申请");
                boolean flag3 = applyService.addGroupApply(apply);
                jsonObject.put("status", 200);
            }
        }
        return jsonObject;
    }
}
