package com.sunhao.tim.vo;

import com.sunhao.tim.entity.Conversation;
import com.sunhao.tim.entity.Grouz;
import lombok.Getter;
import lombok.Setter;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  19:06
 */
@Getter
@Setter
public class GroupVO {

    private Grouz group;
    private Conversation conversation;
    public GroupVO(Grouz group, Conversation conversation) {
        this.group = group;
        this.conversation = conversation;
    }
}
