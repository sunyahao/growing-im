package com.sunhao.tim.service.serviceImpl;

import com.sunhao.tim.dao.UserDao;
import com.sunhao.tim.entity.User;
import com.sunhao.tim.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  18:42
 */
@Service
public class IUserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Boolean addUser(User user) {
        List<User> res = userDao.findUsersByUsername(user.getUsername());
        if (res.size() > 0) {
            return false;
        }
        return userDao.save(user) == null ? false : true;
    }

    @Override
    public Boolean userLogin(String username, String password) {
        List<User> res = userDao.findUsersByUsernameAndPassWord(username, password);
        return res.size() > 0 ? true : false;
    }

    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }

    public User findUserByUserId(String userId) {
        return userDao.findUserByUserId(userId);
    }

    public Boolean updateUser(User user, String userId) {
        String nick = user.getNick();
        String sign = user.getSign();
        String avatar = user.getAvatar();
        return userDao.updateUser(avatar, nick, sign, userId) > 0;
    }

    public List<User> findUserByPartUserId(String userId) {
        return userDao.findUserByPartUserId(userId);
    }

    @Override
    public Boolean updateUserAvatar(String url, String userId) {
        return userDao.updateUserAvatar(url, userId) > 0;
    }
}
