package com.imooc.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserBO {
    @NotNull
    private String userID;
    private String face;
    private String nickname;
    private String sex;

    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern="yyyy-MM-dd")
    private LocalDate birthday;

    @Email
    private String Email;

    private String position;

    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern="yyyy-MM-dd")
    private LocalDate StartWorkDate;

}
