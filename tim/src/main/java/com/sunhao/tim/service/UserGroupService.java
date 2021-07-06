package com.sunhao.tim.service;

import com.sunhao.tim.entity.UserGroup;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  22:42
 */
public interface UserGroupService {

    // 群组新增成员
    public Boolean addMember(UserGroup userGroup);

    // 查找群组中是否已经有该成员的id
    public Boolean findUserGroupByGroupIdAndAndUserId(String groupId, String userId);

    public List<String> findUserIdByGroupId(String groupId);
}
