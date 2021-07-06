package com.sunhao.tim.controller;

import com.alibaba.fastjson.JSONObject;
import com.sunhao.tim.entity.User;
import com.sunhao.tim.service.GroupService;
import com.sunhao.tim.service.UserService;
import com.sunhao.tim.util.FileUtil;
import com.sunhao.tim.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  18:49
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @RequestMapping("/api/v1/login")
    public JSONObject login(@RequestBody Map map){
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        JSONObject jsonObject = new JSONObject();
        JSONObject userInfo = new JSONObject();
        User user = userService.findUserByUsername(username);
        if (userService.userLogin(username, password)) {
            jsonObject.put("id", user.getUserId());
            userInfo.put("id", user.getUserId());
            userInfo.put("nickname", user.getNick());
            userInfo.put("sign", user.getSign());
            userInfo.put("photo", user.getAvatar());
            jsonObject.put("userInfo", userInfo);
            jsonObject.put("status", 200);
        } else {
            jsonObject.put("status", 400);
        }
        return jsonObject;
    }

    @RequestMapping("/api/v1/signup")
    public JSONObject signUP(@RequestBody Map map){
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        String nick = (String) map.get("nick");
        String sign = (String) map.get("sign");
        JSONObject jsonObject = new JSONObject();
        JSONObject userInfo = new JSONObject();
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1);
        User user = new User();
        user.setPassWord(password);
        user.setUsername(username);
        user.setSign(sign);
        user.setNick(nick);
        user.setUserId(String.valueOf(idWorker.nextId()));
        Boolean flag = userService.addUser(user);
        if (flag) {
            jsonObject.put("id", user.getUserId());
            userInfo.put("nickname", user.getNick());
            userInfo.put("sign", user.getSign());
            jsonObject.put("userInfo", userInfo);
            jsonObject.put("statusCode", 0);
        }
        return jsonObject;
    }

    @RequestMapping("/api/v1/currentUser")
    public JSONObject getCurrentUser(@RequestBody Map map){
        String userId = (String) map.get("userId");
        JSONObject jsonObject = new JSONObject();
        JSONObject userInfo = new JSONObject();
        User user = userService.findUserByUserId(userId);
        jsonObject.put("id", user.getUserId());
        userInfo.put("nickname", user.getNick());
        userInfo.put("sign", user.getSign());
        userInfo.put("photo", user.getAvatar());
        jsonObject.put("userInfo", userInfo);
        jsonObject.put("statusCode", 0);
        return jsonObject;
    }

    @RequestMapping("/api/v1/userinfo")
    public JSONObject updateUserInfo(@RequestBody Map<String, Map<String, Object>> map){
        Map<String, Object> userInfo = (Map<String, Object>) map.get("userInfo");
        String nick = (String)userInfo.get("nickname");
        String sign = (String)userInfo.get("sign");
        String avatar = (String)userInfo.get("avatar");
        String userId = (String)userInfo.get("id");
        User user = new User();
        user.setNick(nick);
        user.setAvatar(avatar);
        user.setSign(sign);
        Boolean flag = userService.updateUser(user, userId);
        JSONObject userInfo1 = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        userInfo1.put("id", user);
        userInfo1.put("nickname", nick);
        userInfo1.put("sign", sign);
        userInfo1.put("avatar", avatar);
        jsonObject.put("userInfo1", userInfo);
        return jsonObject;
    }

    @RequestMapping("/api/v1/logout")
    public JSONObject logout(@RequestBody Map map){
        return null;
    }

    @RequestMapping("/api/v1/upload")
    public JSONObject handleUploadPicture(MultipartFile file, String userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            String uploadDir = "C:\\Users\\10234\\Desktop\\gitrepository\\tim\\src\\main\\resources\\public\\images\\";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String url = FileUtil.executeUpload(uploadDir, file);
            userService.updateUserAvatar(url, userId);
            jsonObject.put("url",url);
            jsonObject.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("status",400);
        }
        return jsonObject;
    }

    @RequestMapping("/api/v1/judgeOwner")
    public JSONObject judgeOwner(@RequestBody Map map){
        String userId = (String) map.get("userId");
        String groupId = (String) map.get("groupId");
        JSONObject jsonObject = new JSONObject();
        Boolean isOwner = groupService.judgeOwner(userId, groupId);
        if (isOwner) {
            jsonObject.put("status", 200);
        } else {
            jsonObject.put("status", 400);
        }
        return jsonObject;
    }
}



