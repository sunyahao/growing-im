package com.sunhao.tim.service.serviceImpl;

import com.sunhao.tim.dao.UserGroupDao;
import com.sunhao.tim.entity.UserGroup;
import com.sunhao.tim.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  22:42
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupDao userGroupDao;

    public Boolean addMember(UserGroup userGroup) {
        return userGroupDao.save(userGroup) != null;
    }

    @Override
    public Boolean findUserGroupByGroupIdAndAndUserId(String groupId, String userId) {
        return userGroupDao.findUserGroupByGroupIdAndAndUserId(groupId, userId) != null;
    }

    @Override
    public List<String> findUserIdByGroupId(String groupId) {
        return userGroupDao.findUserIdByGroupId(groupId);
    }
}
