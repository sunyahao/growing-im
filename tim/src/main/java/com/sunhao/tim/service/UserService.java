package com.sunhao.tim.service;

import com.sunhao.tim.entity.User;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  18:42
 */
public interface UserService {

    // 用户注册
    public Boolean addUser(User user);

    // 用户登录
    public Boolean userLogin(String username, String password);

    public User findUserByUsername(String username);

    public User findUserByUserId(String userId);

    public Boolean updateUser(User user, String userId);

    public List<User> findUserByPartUserId(String userId);

    public Boolean updateUserAvatar(String url, String userId);
}
