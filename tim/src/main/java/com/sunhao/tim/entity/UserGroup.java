package com.sunhao.tim.entity;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  18:24
 */

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_group")
@Getter
@Setter
public class UserGroup {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "group_id")
    private String groupId;
}
