package com.imooc.db;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("father_id")
    private Long fatherId;
    @JsonProperty("is_leaf")
    private Integer isLeaf;
    private Integer level;
}
