package com.sunhao.tim.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/24  18:23
 */
@Entity
@Table(name = "friends")
@Getter
@Setter
public class Friends {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "friend_id")
    private String friendId;
}
