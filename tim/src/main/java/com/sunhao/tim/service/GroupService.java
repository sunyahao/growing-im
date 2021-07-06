package com.sunhao.tim.service;

import com.sunhao.tim.entity.Grouz;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  19:00
 */
public interface GroupService {

    List<Grouz> findGroupsByUserId(String userId);

    public String findOwnerByGroupId(String group_id);

    public List<Grouz> findGrouzByPartGroupId(String groupId);

    // 查找有无同名的群,true为有同名
    public Boolean findGrouzByName(String name);

    public Boolean createGroup(Grouz grouz);

    public Grouz findGroupByGroupId(String groupId);

    public Boolean judgeOwner(String userId, String groupId);
}
