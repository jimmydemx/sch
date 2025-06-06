package com.imooc.aspects.operation_log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OperationOutput {
    String title;
    String businessType;
    String method;
    String username;
    String result;
    String[] params;
    String duration;
    boolean success;
}
