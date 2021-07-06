package com.sunhao.tim.dao;

import com.sunhao.tim.entity.Grouz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  18:57
 */
public interface GroupDao extends JpaRepository<Grouz, Integer>, JpaSpecificationExecutor<Grouz> {

    public List<Grouz> findAllByGroupId(String groupId);

    @Query(value = "select owner from grouz where group_id = ?1",nativeQuery = true)
    public String findOwnerByGroupId(String group_id);

    @Query(value = "select * from grouz where group_id like ?1%", nativeQuery = true)
    public List<Grouz> findGrouzByPartGroupId(String groupId);

    public Grouz findGrouzByName(String name);

    public Grouz save(Grouz grouz);

    @Query(value = "select * from grouz where group_id = ?1",nativeQuery = true)
    public Grouz findGroupByGroupId(String groupId);

    @Query(value = "select * from grouz where owner = ?1 and group_id = ?2", nativeQuery = true)
    public Grouz findGroupByOwnerIdAndGroupId(String userId, String groupId);
}
