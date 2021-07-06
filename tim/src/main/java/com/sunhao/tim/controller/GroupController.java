package com.sunhao.tim.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunhao.tim.entity.Conversation;
import com.sunhao.tim.entity.Grouz;
import com.sunhao.tim.service.ConversationService;
import com.sunhao.tim.service.GroupService;
import com.sunhao.tim.util.FileUtil;
import com.sunhao.tim.util.SnowflakeIdWorker;
import com.sunhao.tim.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  19:02
 */
@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private ConversationService conversationService;

    @RequestMapping("/api/v1/groups")
    public JSONObject getGroups(@RequestBody Map map) {
        String userId = (String) map.get("userId");
        JSONObject jsonObject = new JSONObject();
        List<Grouz> groupList = groupService.findGroupsByUserId(userId);
        JSONArray groups = new JSONArray();
        for(Grouz g : groupList) {
            Conversation conversation = conversationService.selectConversationByFromIdAndToIdGroupType(userId, g.getGroupId());
            GroupVO groupVO = new GroupVO(g, conversation);
            groups.add(groupVO);
        }
        jsonObject.put("groupList", groups);
        return jsonObject;
    }

    @RequestMapping("/api/v1/add/searchGroup")
    public JSONObject addGroup(@RequestBody Map map) {
        // 目标用户的userId
        String searchValue = (String) map.get("searchValue");
        JSONArray groups = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        List<Grouz> groupList = groupService.findGrouzByPartGroupId(searchValue);
        for(Grouz g : groupList) {
            JSONObject group = new JSONObject();
            group.put("photo", g.getAvatar());
            group.put("name", g.getName());
            group.put("id", g.getGroupId());
            group.put("introduction", "");
            groups.add(group);
        }
        jsonObject.put("groups", groups);
        return jsonObject;
    }

    @RequestMapping("/api/v1/create/group")
    public JSONObject createGroup(MultipartFile file, String userId, String groupName) {
        // 目标用户的userId
        Boolean flag1 = groupService.findGrouzByName(groupName);
        JSONObject jsonObject = new JSONObject();
        if (flag1) {
            jsonObject.put("status", 400);
        } else{
            try {
                String uploadDir = "C:\\Users\\10234\\Desktop\\gitrepository\\tim\\src\\main\\resources\\public\\images\\";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                String url = FileUtil.executeUpload(uploadDir, file);
                Grouz grouz = new Grouz();
                SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(3);
                long groupId = snowflakeIdWorker.nextId();
                grouz.setGroupId(String.valueOf(groupId));
                grouz.setName(groupName);
                grouz.setOwner(userId);
                grouz.setAvatar(url);
                groupService.createGroup(grouz);
                jsonObject.put("url",url);
                jsonObject.put("status", 200);
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.put("status", 400);
            }
        }
        return jsonObject;
    }

    @RequestMapping("/api/v1/getGroupPhoto")
    public JSONObject getGroupPhoto(@RequestBody Map map) {
        JSONObject jsonObject = new JSONObject();
        String groupId = (String) map.get("groupId");
        Grouz grouz = groupService.findGroupByGroupId(groupId);
        if(grouz.getAvatar() != null && !grouz.getAvatar().equals("")) {
            jsonObject.put("groupPhoto", grouz.getAvatar());
            jsonObject.put("status", 200);
        } else {
            jsonObject.put("status", 400);
        }
        return jsonObject;
    }
}
