package com.sunhao.tim.service;

import com.sunhao.tim.entity.Conversation;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  14:35
 */
public interface ConversationService {

    public Conversation selectConversationByFromIdAndToId(String fromId, String toId);

    public Conversation selectConversationByFromIdAndToIdGroupType(String fromId, String toId);

    public Boolean save(Conversation conversation);

    // 检查会话是否已存在
    public Boolean findConversationByFIdAndTId(String fId, String tId);

    public List<Conversation> findConversationByFid(String fId);

    public Conversation findConversationById(Long id);

    public Conversation findConversationByFromIdAndToId(String fId, String tId);
}
