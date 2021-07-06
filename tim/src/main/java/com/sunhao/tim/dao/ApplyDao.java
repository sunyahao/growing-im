package com.sunhao.tim.dao;

import com.sunhao.tim.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  16:51
 */
public interface ApplyDao extends JpaRepository<Apply, Integer>, JpaSpecificationExecutor<Apply> {

//    // 查询某个user未处理的申请数量
//    @Query(value = "select count(*) from apply where status = 4 and t_id = ?1", nativeQuery = true)
//    public Integer getUnReadApply(String userId);

      @Query(value = "select * from apply where t_id = ?1 and status = 0", nativeQuery = true)
      public List<Apply> getApplies(String userId);

      public Apply findApplyById(Integer id);

      @Transactional
      @Modifying
      @Query(value = "update apply set status = 1 where id = ?1", nativeQuery = true)
      public int updateStatusByApplyId(Integer id);

      @Transactional
      @Modifying
      @Query(value = "update apply set status = 2 where id = ?1", nativeQuery = true)
      public int updateStatusByApplyId2(Integer id);

      // 查出所有type为1的数据的apply
      @Query(value = "select * from apply where apply_type = 1 and status = 0",nativeQuery = true)
      public List<Apply> findGroupIds();

      public Apply save(Apply apply);
}
