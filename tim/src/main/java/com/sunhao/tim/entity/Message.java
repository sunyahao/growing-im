package com.sunhao.tim.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  18:21
 */
@Entity
@Table(name = "message")
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "f_id")
    private String fId;

    @Column(name = "t_id")
    private String tId;

    // 其中0表示私聊，1表示群聊
    @Column(name = "chat_type")
    private Integer chatType;

    // 其中0表示文字，1表示语言，2表示图片，3表示视频
    @Column(name = "msg_type")
    private Integer msgType;

    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "GMT+8")
    @Column(name = "send_time")
    private Date sendTime;

    @Column(name = "content")
    private String content;
}
