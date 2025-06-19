package com.imooc.db;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Industry {

    private String name;
    private Long id;
    private Integer sort;
    private Long parentId;
    private Integer is_leaf;
    private Integer level;
}
