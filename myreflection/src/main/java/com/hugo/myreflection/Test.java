package com.hugo.myreflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/25.
 * 版本：v1.0
 * 描述：
 */
public class Test {

    public static void main(String[] aa){
        reflection3();
    }

    /**
     * 实例1：利用反射获取类的属性 & 赋值
     */
    private void reflection1(){
        //获取Class对象
        // <-- 方式1：Object.getClass() -->
        Class studentClass = new Student().getClass();
        //<-- 方式2：T.class 语法    -->
        studentClass = Student.class;
        //<-- 方式3：static method Class.forName   -->
        try {
            studentClass = Class.forName("com.hugo.myreflection.Student");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //<-- 方式4：TYPE语法  -->
        Class<Boolean> classType = Boolean.TYPE;
        Object student;
        try {
            //作用：快速地创建一个类的实例
            // 具体过程：调用默认构造器（若该类无默认构造器，则抛出异常
            // 注：若需要为构造器提供参数需使用java.lang.reflect.Constructor中的newInstance（）
            //2. 通过Class对象创建Student类的对象
            student = studentClass.newInstance();
            //3.通过Class对象获取student对象的name属性
            Field field = studentClass.getDeclaredField("name");
            //4. 设置私有访问权限
            field.setAccessible(true);
            //5. 对新创建的Student对象设置name值
            field.set(student,"hugo");
            // 6. 获取新创建Student对象的的name属性 & 输出
            System.out.println(field.get(student));

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    /**
     *  <-- 利用反射调用构造函数 -->
     */
    private static void reflection2(){

        // 1. 获取Student类的Class对象
        Class<Student> studentClass = Student.class;
        try {
            // 2.1 通过Class对象获取Constructor类对象，从而调用无参构造方法
            // 注：构造函数的调用实际上是在newInstance()，而不是在getConstructor()中调用
            Object mStudent1 = studentClass.getConstructor().newInstance();

            Object mStduent2 = studentClass.getConstructor(String.class).newInstance("hugo");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用反射调用类对象的方法
     */
    private static void reflection3(){
        //1.获取Student的class对象
        Class<Student> student = Student.class;

        try {
            //2. 通过Class对象创建Student类的对象
            Object mStudent = student.newInstance();

            // 3.1 通过Class对象获取方法setName1（）的Method对象:需传入方法名
            // 因为该方法 = 无参，所以不需要传入参数
            Method mSetName1 = student.getMethod("setName1");
            //通过Method对象调用setName1（）：需传入创建的实例
            mSetName1.invoke(mStudent);

            // 3.2 通过Class对象获取方法setName2(String name)的Method对象:需传入方法名 & 参数类型
            Method mSetName2 = student.getMethod("setName2", String.class);
            //通过Method对象调用setName2（）：需传入创建的实例 & 参数值
            mSetName2.invoke(mStudent,"hugo");

            //3.3 通过Class对象获取私有方法setName3(String name) 的Method对象：需要传入方法名& 参数类型
            Method mSetName3 = student.getDeclaredMethod("setName3", String.class);
            //4. 设置私有访问权限
            mSetName3.setAccessible(true);
            //通过Method对象调用setName3():需要传入创建的实例 & 参数值
            mSetName3.invoke(mStudent,"private hugo");

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
