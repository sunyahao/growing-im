package com.sunhao.tim.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author SunYaHao
 * @version 1.0
 * @date 2021/5/25  16:21
 */
@Entity
@Table(name = "apply")
@Getter
@Setter
public class Apply {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "f_id")
    private String fId;

    @Column(name = "t_id")
    private String tId;

    //0表示个人申请，1表示群申请
    @Column(name = "apply_type")
    private Integer applyType;

    @Column(name = "info")
    private String info;

    // 0表示未处理，1表示同意，2表示拒绝，3表示未读
    @Column(name = "status")
    private Integer status;
}
