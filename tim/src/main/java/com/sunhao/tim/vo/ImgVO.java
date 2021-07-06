package com.sunhao.tim.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/27  21:37
 */
@Getter
@Setter
public class ImgVO {

    private MultipartFile file;

    private String userId;
}
