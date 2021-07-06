package com.sunhao.tim.dao;

import com.sunhao.tim.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  14:18
 */
public interface UserFriendsDao extends JpaRepository<Friends, Integer>, JpaSpecificationExecutor<Friends> {

    // 查出userId的好友列表
    @Query(value = "select friend_id from friends where user_id = ?1 union select user_id from friends where friend_id = ?1",nativeQuery = true)
    public List<String> getFriendsId(String userId);

    public Friends save(Friends friends);

    public Friends findFriendsByUserIdAndAndFriendId(String userId, String friendId);

    public Friends findFriendsByFriendIdAndUserId(String friendId, String userId);
}

