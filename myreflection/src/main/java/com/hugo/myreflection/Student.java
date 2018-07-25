package com.hugo.myreflection;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/25.
 * 版本：v1.0
 * 描述：反射  测试类
 */
public class Student {

    public Student (){
        System.out.println("创建了一个Student 实例");
    }

    public Student (String string){
        System.out.println("创建了一个有参 Student 实例");
    }

    public void setName1(){
        System.out.println("调用了公共无参方法setName1()");
    }

    public void setName2(String name){
        System.out.println("调用了公共有参方法 setName2(String name)"+name);
    }

    private void setName3(String name){
        System.out.println("调用了私有有参方法 setName3(String name)"+name);
    }
    private String name;
}
