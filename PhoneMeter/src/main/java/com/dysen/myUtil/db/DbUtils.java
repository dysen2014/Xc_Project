package com.dysen.myUtil.db;

import com.dysen.afinalUtil.tBook;

import net.tsz.afinal.FinalDb;

/**
 * 作者：沈迪 [dysen] on 2016-04-08 11:02.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class DbUtils {

    /**
     * 检查数据库数据是否为空
     * @param db
     * @return
     */
    public static boolean checkTableIsEmpty(FinalDb db){
        return db.findAll(tBook.class).size() >0 ? true : false;
    }

    /**
     * 检查数据库数据大小
     * @param db
     * @return
     */
    public static long checkTableSize(FinalDb db){
        return db.findAll(tBook.class).size();
    }
}
