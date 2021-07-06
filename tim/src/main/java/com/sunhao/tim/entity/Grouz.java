package com.sunhao.tim.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  18:20
 */
@Entity
@Table(name = "grouz")
@Getter
@Setter
public class Grouz {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "name")
    private String name;

    @Column(name = "member_count")
    private Integer memberCount;

    @Column(name = "owner")
    private String owner;
}
