package com.hugo.myrxjava.rxjava;


import com.hugo.myrxjava.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/10.
 * 版本：v1.0
 * 描述：
 *
 * 关于内存溢出的问题
 *
 * 一是从数量上进行治理, 减少发送进队列里的事件
    二是从速度上进行治理, 减缓事件发送进队列的速度

 */
public class RxJavA {

    /**
     * 这个会出现内存溢出的情况
     */
    public static void test(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int i=0;
                while (true){
                    i++;
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("accept",integer+"");
                    }
                });
    }

    /***
     * 关于test的优化
     *
     * filter 这个是拦截器  只有返回true时
     * 观察者才能收到 被观察者发送的信息
     */
    public static void test1(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0 ; ; i++){
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                //拦截器判断
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {

                        return integer % 10 == 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("accept",integer+"");
                    }
                });
    }


    /***
     * 这个是test1的优化版
     *
     * 用sample 操作符  来取样   我这个是每2秒钟取一个事件给观察者
     *
     */
    public static void test3(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0 ; ; i++){
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                //sample 取样  每2秒取一个事件给订阅者
                .sample(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        LogUtil.e("accept",integer+"");
                    }
                });
    }
}
