package com.imooc.reflection.LogExecutionTime;


import java.lang.reflect.Method;

public class LogExecutionTimeProcessor<T> {

    public static <T> Object getExecutionTime(Class<T> cls,Object ...args){
        try {
            T instance = cls.getDeclaredConstructor().newInstance();
            for(Method method: cls.getDeclaredMethods()){
                if(method.isAnnotationPresent(LogExecutionTime.class)){
                    System.out.print("method name:"+ method.getName());

                    // before
                    long start = System.currentTimeMillis();

                    // execute
                    Object result = method.invoke(instance,args);
                    long end = System.currentTimeMillis();
                    System.out.println("方法"+method.getName()+" 执行时间："+(end-start)+"ms");
                    return  result;
                   }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
