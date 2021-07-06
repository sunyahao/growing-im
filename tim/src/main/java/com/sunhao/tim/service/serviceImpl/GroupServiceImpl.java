package com.sunhao.tim.service.serviceImpl;

import com.sunhao.tim.dao.GroupDao;
import com.sunhao.tim.dao.UserGroupDao;
import com.sunhao.tim.entity.Grouz;
import com.sunhao.tim.entity.UserGroup;
import com.sunhao.tim.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  19:01
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserGroupDao userGroupDao;

    @Override
    public List<Grouz> findGroupsByUserId(String userId) {
        List<String> groups = userGroupDao.findGroupIdByUserId(userId);
        List<Grouz> groupList = new ArrayList<>();
        for (String str : groups) {
            List<Grouz> g = groupDao.findAllByGroupId(str);
            if (g.size() == 0) {
                return null;
            }
            groupList.add(g.get(0));
        }
        return groupList;
    }

    public String findOwnerByGroupId(String group_id) {
        return groupDao.findOwnerByGroupId(group_id);
    }

    @Override
    public List<Grouz> findGrouzByPartGroupId(String groupId) {
        return groupDao.findGrouzByPartGroupId(groupId);
    }

    @Override
    public Boolean findGrouzByName(String name) {
        return groupDao.findGrouzByName(name) != null;
    }

    @Override
    public Boolean createGroup(Grouz grouz) {
        groupDao.save(grouz);
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(grouz.getGroupId());
        userGroup.setUserId(grouz.getOwner());
        userGroupDao.save(userGroup);
        return true;
    }

    @Override
    public Grouz findGroupByGroupId(String groupId) {
        return groupDao.findGroupByGroupId(groupId);
    }

    @Override
    public Boolean judgeOwner(String userId, String groupId) {
        return groupDao.findGroupByOwnerIdAndGroupId(userId, groupId) != null;
    }
}
