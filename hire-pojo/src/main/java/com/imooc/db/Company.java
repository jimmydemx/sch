package com.imooc.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    private Long id;
    private String company_name;
    private String short_name;

    private String logo;

    private String biz_license;

    private String country;

    private String province;
    private String city;

    private String address;
    private String district;

    private String nature;
    private String people_size;

    private String industry;

    private String financ_stage;

    private String work_time;

    private String introduction;

    private String advantage;

    private String benefits;

    private String bonus;
    private String subsidy;

    private LocalDate build_date;

    private String regist_capital;

    private String legal_representative;

    private Integer review_status;

    private String review_replay;

    private String auth_letter;

    private Long commit_user_id;
    private Long commit_user_mobile;

    private LocalDate commit_date;

    private Integer is_vip;
    private LocalDate vip_expire_date;
    private LocalDateTime created_time;
    private LocalDateTime updated_time;

}
