package com.sunhao.tim.dao;

import com.sunhao.tim.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/26  15:10
 */
public interface MessageDao extends JpaRepository<Message, Integer>, JpaSpecificationExecutor<Message> {

    @Query(value = "select * from message where f_id = ?1 and t_id = ?2", nativeQuery = true)
    public List<Message> findMessagesByFIdAndTId(String fId, String tId);

    public Message save(Message message);

    @Query(value = "select * from message where f_id = ?1 and t_id = ?2 and chat_type = 1", nativeQuery = true)
    public List<Message> findMessagesByFIdAndTIdAndChatType(String fId, String tId);

    @Query(value = "select * from message where f_id != ?1 and t_id = ?2 and chat_type = 1", nativeQuery = true)
    public List<Message> findMessagesByTIdAndChatType(String fId, String tId);
}
