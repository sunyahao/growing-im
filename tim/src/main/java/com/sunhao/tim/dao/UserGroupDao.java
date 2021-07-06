package com.sunhao.tim.dao;

import com.sunhao.tim.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  19:21
 */
public interface UserGroupDao extends JpaRepository<UserGroup, Integer>, JpaSpecificationExecutor<UserGroup> {

    @Query(value = "select group_id from user_group where user_id = ?1", nativeQuery = true)
    public List<String> findGroupIdByUserId(String userId);

    public UserGroup save(UserGroup userGroup);

    public UserGroup findUserGroupByGroupIdAndAndUserId(String groupId, String userId);

    @Query(value = "select user_id from user_group where group_id = ?1", nativeQuery = true)
    public List<String> findUserIdByGroupId(String groupId);
}
