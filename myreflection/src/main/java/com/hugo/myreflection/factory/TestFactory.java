package com.hugo.myreflection.factory;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/25.
 * 版本：v1.0
 * 描述：反射  + 工厂模式
 */
public class TestFactory {

    public static void main(String[] aa){
        // 1. 通过调用工厂类的静态方法（反射原理），从而动态创建产品类实例
        // 需传入  Class对象
        Product product = Factory.getInstance(ProductB.class);
        product.show();
        // 1. 通过调用工厂类的静态方法（反射原理），从而动态创建产品类实例
        // 需传入完整的类名 & 包名
        Product product1 = Factory.getInstance("com.hugo.myreflection.factory.ProductA");
        product1.show();
    }

}
