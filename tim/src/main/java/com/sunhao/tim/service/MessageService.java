package com.sunhao.tim.service;

import com.sunhao.tim.entity.Message;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/26  15:12
 */
public interface MessageService {

    public List<Message> findMessagesByFIdAndTId(String fId, String tId);

    public Boolean addMessage(Message message);

    public List<Message> findMessagesByFIdAndTIdAndChatType(String fId, String tId);

    // 找到所有成员发给群里的消息
    public List<Message> findMessagesByTIdAndChatType(String fId, String tId);
}
