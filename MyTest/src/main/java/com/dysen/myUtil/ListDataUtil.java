package com.dysen.myUtil;

import java.util.ArrayList;

/**
 * 作者：沈迪 [dysen] on 2016-02-24 14:49.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class ListDataUtil {
    /**
     * 添加数据
     */
    public static ArrayList<String> addData(ArrayList<String> list, String str){
        list.add(str);
        return list;
    }

    /**
     * 清除数据
     */
    public static ArrayList<String> clearData(ArrayList<String> list){
        list.clear();
        return list;
    }
}
