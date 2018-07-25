package com.hugo.myreflection.factory;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/25.
 * 版本：v1.0
 * 描述：
 */
public class Factory {

    public static Product getInstance(Class aa){
        Product concreteProduct = null;
        try {
            // 通过Class对象动态创建该产品类的实例
            concreteProduct = (Product) aa.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return concreteProduct;
    }

    public static Product getInstance(String  ClassName){
        Product concreteProduct = null;
        try {
            // 1. 根据 传入的产品类名 获取 产品类类型的Class对象
            Class productClass = Class.forName(ClassName);
            // 2. 通过Class对象动态创建该产品类的实例
            concreteProduct = (Product) productClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return concreteProduct;
    }

}
