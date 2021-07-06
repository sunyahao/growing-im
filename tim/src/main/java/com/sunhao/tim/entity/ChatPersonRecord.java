package com.sunhao.tim.entity;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  19:34
 */

import javax.persistence.*;
import java.util.Date;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  18:26
 */
@Entity
@Table(name = "chat_person_record")
public class ChatPersonRecord {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "f_id")
    private String fId;

    @Column(name = "t_id")
    private String tId;

    // 其中0表示文字，1表示语言，2表示文件路径，3表示表情
    @Column(name = "msg_type")
    private Integer msgType;

    @Column(name = "send_time")
    private Date sendTime;

    @Column(name = "content")
    private String content;

    @Column(name = "receive_time")
    private Date receiveTime;
}

