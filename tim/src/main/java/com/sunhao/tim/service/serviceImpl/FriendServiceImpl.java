package com.sunhao.tim.service.serviceImpl;

import com.sunhao.tim.dao.UserFriendsDao;
import com.sunhao.tim.entity.Friends;
import com.sunhao.tim.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  14:25
 */
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserFriendsDao userFriendsDao;

    @Override
    public List<String> getFriendList(String userId) {
        List<String> friends = userFriendsDao.getFriendsId(userId);
        return friends;
    }

    @Override
    public Boolean addFriend(Friends friends) {
        Friends friends1 = userFriendsDao.save(friends);
        return friends1 != null;
    }

    @Override
    public Boolean findFriendsByUserIdAndAndFriendId(String userId, String friendId) {
        return userFriendsDao.findFriendsByUserIdAndAndFriendId(userId, friendId) != null
                || userFriendsDao.findFriendsByFriendIdAndUserId(userId, friendId) != null;
    }
}
