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
            Class productClass = Class.forName(ClassName);
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
