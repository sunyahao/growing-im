package com.sunhao.tim.dao;

import com.sunhao.tim.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  14:31
 */
public interface ConversationDao extends JpaRepository<Conversation, Integer>, JpaSpecificationExecutor<Conversation> {

    @Query(value = "select * from conversation where f_id = ?1 and t_id = ?2 and chat_type = 1",nativeQuery = true)
    public Conversation selectConversationByFromIdAndToId(String fromId, String toId);

    @Query(value = "select * from conversation where f_id = ?1 and t_id = ?2 and chat_type = 1",nativeQuery = true)
    public Conversation selectConversationByFromIdAndToIdGroupType(String fromId, String toId);

    public Conversation save(Conversation conversation);

    @Query(value = "select * from conversation where f_id = ?1 and t_id = ?2",nativeQuery = true)
    public Conversation findConversationByFIdAndTId(String fId, String tId);

    @Query(value = "select * from conversation where f_id = ?1",nativeQuery = true)
    public List<Conversation> findConversationByFId(String fId);

    public Conversation findConversationById(Long id);
}
