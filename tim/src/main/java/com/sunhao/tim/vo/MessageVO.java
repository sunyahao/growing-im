package com.sunhao.tim.vo;

import com.sunhao.tim.entity.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/26  17:52
 */
@Getter
@Setter
public class MessageVO {

    private Integer id;

    private String fId;

    private String tId;

    private Integer chatType;

    private Integer msgType;

    private Date sendTime;

    private String content;

    private String nick;

    private String photo;

    private Integer isMyself;

    public MessageVO(Message message, String nick, String photo, Integer isMyself) {
        this.chatType = message.getChatType();
        this.content = message.getContent();
        this.fId = message.getFId();
        this.id = message.getId();
        this.msgType = message.getMsgType();
        this.sendTime = message.getSendTime();
        this.tId = message.getTId();
        this.nick = nick;
        this.isMyself = isMyself;
        this.photo = photo;
    }
}
