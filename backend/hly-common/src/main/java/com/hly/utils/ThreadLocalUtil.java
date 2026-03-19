package com.hly.utils;

import lombok.Data;

@Data
public class ThreadLocalUtil {
    private static Integer currentId;
    private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();
    
    public static void setCurrentIdS(Integer userId){
        THREAD_LOCAL.set(userId);
    }
    
    public static Integer getCurrentIdS(){
        return THREAD_LOCAL.get();
    }
    
    public static void clear(){THREAD_LOCAL.remove();}
    
}
