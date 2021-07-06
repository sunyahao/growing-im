package com.sunhao.tim.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/27  20:40
 */

public class FileUtil {

    // 返回文件地址
    public static String executeUpload(String uploadDir, MultipartFile file) throws Exception {
        //文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //文件随机名
        String filename = UUID.randomUUID() + suffix;
        //创建文件对象
        File serverFile = new File(uploadDir + filename);
        //转储文件
        file.transferTo(serverFile);
        return "/public/images/" + filename;
    }
}
