package com.sunhao.tim.service.serviceImpl;

import com.sunhao.tim.dao.ConversationDao;
import com.sunhao.tim.entity.Conversation;
import com.sunhao.tim.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  14:35
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationDao conversationDao;

    @Override
    public Conversation selectConversationByFromIdAndToId(String fromId, String toId) {
        Conversation conversation = conversationDao.findConversationByFIdAndTId(fromId, toId);
        return conversation;
    }

    @Override
    public Conversation selectConversationByFromIdAndToIdGroupType(String fromId, String toId) {
        Conversation conversation = conversationDao.selectConversationByFromIdAndToIdGroupType(fromId, toId);
        return conversation;
    }

    @Override
    public Boolean save(Conversation conversation) {
        return conversationDao.save(conversation) != null;
    }

    public Boolean findConversationByFIdAndTId(String fId, String tId) {
        return conversationDao.findConversationByFIdAndTId(fId, tId) != null;
    }

    public List<Conversation> findConversationByFid(String fId) {
        return conversationDao.findConversationByFId(fId);
    }

    @Override
    public Conversation findConversationById(Long id) {
        return conversationDao.findConversationById(id);
    }

    @Override
    public Conversation findConversationByFromIdAndToId(String fId, String tId) {
        return conversationDao.findConversationByFIdAndTId(fId, tId);
    }
}
