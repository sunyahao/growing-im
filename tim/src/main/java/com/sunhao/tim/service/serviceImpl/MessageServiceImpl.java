package com.sunhao.tim.service.serviceImpl;

import com.sunhao.tim.dao.MessageDao;
import com.sunhao.tim.entity.Message;
import com.sunhao.tim.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/26  15:12
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public List<Message> findMessagesByFIdAndTId(String fId, String tId) {
        return messageDao.findMessagesByFIdAndTId(fId, tId);
    }

    @Override
    public Boolean addMessage(Message message) {
        return messageDao.save(message) != null;
    }

    @Override
    public List<Message> findMessagesByFIdAndTIdAndChatType(String fId, String tId) {
        return messageDao.findMessagesByFIdAndTIdAndChatType(fId, tId);
    }

    @Override
    public List<Message> findMessagesByTIdAndChatType(String fId, String tId) {
        return messageDao.findMessagesByTIdAndChatType(fId, tId);
    }
}
