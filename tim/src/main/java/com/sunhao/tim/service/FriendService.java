package com.sunhao.tim.service;

import com.sunhao.tim.entity.Friends;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  14:25
 */
public interface FriendService {

    // 查找出好友列表
    public List<String> getFriendList(String userId);

    public Boolean addFriend(Friends friends);

    //查看好友关系是否已存在
    public Boolean findFriendsByUserIdAndAndFriendId(String userId, String friendId);
}
