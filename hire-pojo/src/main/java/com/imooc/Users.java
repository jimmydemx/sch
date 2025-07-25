package com.imooc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 对外展示名，1：真实姓名，2：昵称
     */
    private Integer showWhichName;

    /**
     * 性别，1:男 0:女 2:保密
     */
    private Integer sex;

    /**
     * 用户头像
     */
    private String face;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 介绍
     */
    private String description;

    /**
     * 我参加工作的时间
     */
    private LocalDate startWorkDate;

    /**
     * 我当前职位/职务
     */
    private String position;

    /**
     * 身份角色，1: 求职者，2: 求职者可以切换为HR进行招聘，同时拥有两个身份
     */
    private Integer role;

    /**
     * 成为HR后，认证的（绑定的）公司主键id
     */
    private String hrInWhichCompanyId;

    /**
     * 我的一句话签名
     */
    private String hrSignature;

    /**
     * 我的个性化标签
     */
    private String hrTags;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;


    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", mobile=" + mobile +
                ", nickname=" + nickname +
                ", realName=" + realName +
                ", showWhichName=" + showWhichName +
                ", sex=" + sex +
                ", face=" + face +
                ", email=" + email +
                ", birthday=" + birthday +
                ", country=" + country +
                ", province=" + province +
                ", city=" + city +
                ", district=" + district +
                ", description=" + description +
                ", startWorkDate=" + startWorkDate +
                ", position=" + position +
                ", role=" + role +
                ", hrInWhichCompanyId=" + hrInWhichCompanyId +
                ", hrSignature=" + hrSignature +
                ", hrTags=" + hrTags +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                "}";
    }
}

