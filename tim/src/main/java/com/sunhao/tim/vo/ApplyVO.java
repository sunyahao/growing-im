package com.sunhao.tim.vo;

import com.sunhao.tim.entity.Apply;
import com.sunhao.tim.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  17:36
 */
@Getter
@Setter
public class ApplyVO {
    private Apply apply;
    private User user;

    public ApplyVO(Apply apply, User user) {
        this.apply = apply;
        this.user = user;
    }
}
