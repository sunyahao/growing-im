package com.sunhao.tim.service;

import com.sunhao.tim.entity.Apply;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  16:55
 */
public interface ApplyService {

//    public Integer getUnReadApply(String userId);

    List<Apply> getApplies(String userId);

    // 通过applyId查询单个apply
    Apply getApply(Integer id);

    public Boolean applySuccess(Integer id);

    public Boolean applyRefuse(Integer id);

    // 查出所有群聊申请的owner并和参数ID比对，输出与参数ID相等的applyId
    public List<Apply> findApplyGroupIdsOwner(String userId);

    // 添加好友申请
    public Boolean addPersonApply(Apply apply);

    public Boolean addGroupApply(Apply apply);
}
