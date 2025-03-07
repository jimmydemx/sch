
//	此资源由 58学课资源站 收集整理
//	想要获取完整课件资料 请访问：58xueke.com
//	百万资源 畅享学习
package com.imooc.exceptions;

import com.imooc.grace.result.ResponseStatusEnum;

/**
 * 优雅的处理异常，统一进行封装
 */
public class GraceException {

    public static void display(ResponseStatusEnum statusEnum) {
        throw new MyCustomException(statusEnum);
    }

}
