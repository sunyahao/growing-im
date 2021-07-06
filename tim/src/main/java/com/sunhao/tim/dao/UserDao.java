package com.sunhao.tim.dao;

import com.sunhao.tim.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  18:37
 */
public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    public List<User> findUsersByUsernameAndPassWord(String username, String password);

    public User save(User user);

    public List<User> findUsersByUsername(String username);

    public User findUserByUsername(String username);

    public User findUserByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "update User u set u.avatar = ?1, u.nick = ?2, u.sign = ?3 where user_id = ?4")
    public int updateUser(String avatar, String nick, String sign, String userId);

    @Query(value = "select * from user where user_id like ?1%", nativeQuery = true)
    public List<User> findUserByPartUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "update User u set u.avatar = ?1 where user_id = ?2")
    public int updateUserAvatar(String avatar, String userId);
}
