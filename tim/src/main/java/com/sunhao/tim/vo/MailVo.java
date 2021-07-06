package com.sunhao.tim.vo;

import com.sunhao.tim.entity.Conversation;
import com.sunhao.tim.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  14:46
 */
@Getter
@Setter
public class MailVo {
    private User user;
    private Conversation conversation;

    public MailVo(User user, Conversation conversation) {
        this.user = user;
        this.conversation = conversation;
    }
}
