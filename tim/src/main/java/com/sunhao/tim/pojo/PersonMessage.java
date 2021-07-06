package com.sunhao.tim.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/26  20:47
 */
@Getter
@Setter
public class PersonMessage {
    private String type;
    private String toId;
    private String fromId;
    private String conversationId;
    private Object body;
}
