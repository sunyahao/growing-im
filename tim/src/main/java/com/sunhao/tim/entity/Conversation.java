package com.sunhao.tim.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  18:29
 */
@Entity
@Table(name = "conversation")
@Getter
@Setter
public class Conversation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "avatar")
    private String avatar;

    // 0表示私聊，1表示群聊
    @Column(name = "chat_type")
    private Integer chatType;

    @Column(name = "active_time")
    private Date activeTime;

    // 0表示false，1表示true
    @Column(name = "active")
    private String active;

    @Column(name = "f_id")
    private String fId;

    @Column(name = "t_id")
    private String tId;

//    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "GMT+8")
    @Column(name = "last_msg_time")
    private Date lastMsgTime;

    @Column(name = "name")
    private String name;
}
