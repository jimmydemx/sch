package com.imooc.test;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestUsers {

    private static final long serialVersionUID = 1L;

    private String id;
    private String mobile;
    private String nickname;
    private String realName;
    private Integer showWhichName;
    private LocalDate birthday;

}
