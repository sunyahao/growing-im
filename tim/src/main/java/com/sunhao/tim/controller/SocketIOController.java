package com.sunhao.tim.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunhao.tim.util.FileUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/29  16:34
 */
@RestController
public class SocketIOController {

    // 批量存储文件并返回文件地址列表
    @RequestMapping("/api/v1/socketIO/upload")
    public JSONObject getWindowPicture(MultipartFile[] file) {
        JSONObject jsonObject = new JSONObject();
        JSONArray urls = new JSONArray();
        try {
            String uploadDir = "C:\\Users\\10234\\Desktop\\gitrepository\\tim\\src\\main\\resources\\public\\images\\";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            // 这些urls每条url就是message对应的content
            for(int i=0;i<file.length;i++) {
                if(file[i] != null) {
                    String url = FileUtil.executeUpload(uploadDir, file[i]);
                    urls.add(url);
                }
            }
            jsonObject.put("urls", urls);
            jsonObject.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("status",400);
        }
        return jsonObject;
    }
}
