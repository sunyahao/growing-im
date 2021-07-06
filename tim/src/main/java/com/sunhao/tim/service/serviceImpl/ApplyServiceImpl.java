package com.sunhao.tim.service.serviceImpl;

import com.sunhao.tim.dao.ApplyDao;
import com.sunhao.tim.dao.GroupDao;
import com.sunhao.tim.entity.Apply;
import com.sunhao.tim.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  16:55
 */
@Service
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    private ApplyDao applyDao;

    @Autowired
    private GroupDao groupDao;

//    @Override
//    public Integer getUnReadApply(String userId) {
//        return applyDao.getUnReadApply(userId);
//    }

    public List<Apply> getApplies(String userId) {
        return applyDao.getApplies(userId);
    }

    @Override
    public Apply getApply(Integer id) {
        return applyDao.findApplyById(id);
    }

    public Boolean applySuccess(Integer id) {
        if (applyDao.updateStatusByApplyId(id) > 0) {
            return true;
        }
        return false;
    }

    public Boolean applyRefuse(Integer id) {
        if (applyDao.updateStatusByApplyId2(id) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Apply> findApplyGroupIdsOwner(String userId) {
        List<Apply> applies = applyDao.findGroupIds();
        List<Apply> res = new ArrayList<>();
        for(Apply apply : applies) {
            String owner = groupDao.findOwnerByGroupId(apply.getTId());
            if (owner.equals(userId)) {
                res.add(apply);
            }
        }
        return res;
    }

    @Override
    public Boolean addPersonApply(Apply apply) {
        return applyDao.save(apply) != null;
    }

    @Override
    public Boolean addGroupApply(Apply apply) {
        return applyDao.save(apply) != null;
    }
}
