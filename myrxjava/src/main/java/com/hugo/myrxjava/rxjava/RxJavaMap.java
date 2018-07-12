package com.hugo.myrxjava.rxjava;

import com.hugo.myrxjava.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/9.
 * 版本：v1.0
 * 描述：这个是  RxJava Map的例子
 */
public class RxJavaMap {
    private static String TAG = "RxJavaMap";

    public static void main(String[] arg){
        testFlatMap();
    }

    /**
     * 在onComplete后 map也是收不到信息的
     */
    public static void testMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                LogUtil.e(TAG,"subscribe "+Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
                emitter.onNext(3);
            }
        })      .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        LogUtil.e(TAG,"sssss "+Thread.currentThread().getName());
                        LogUtil.e(TAG,"sssss "+integer);
                        return "this is result "+integer;
                    }
                }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtil.e(TAG,"onSubscribe "+Thread.currentThread().getName());
            }

            @Override
            public void onNext(String s) {
                LogUtil.e(TAG,"onNext "+Thread.currentThread().getName());
                LogUtil.e(TAG,s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * FlatMap输出是无序的
     */
    public static void testFlatMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0;i<3;i++){
                    list.add("I am value "+integer);
                }
                //为了看出  FlatMap输出是无序的    在这添加了10秒的延迟 delay(10, TimeUnit.MILLISECONDS)

                if(integer<2) {
                    return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
                }else {
                    return Observable.fromIterable(list);
                }
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
               LogUtil.e(TAG,s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        
    }

    /**
     * concatMap是有序的
     */
    public static void testConcatMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0;i<3;i++){
                    list.add("I am value "+integer);
                }
                //为了看出  ConcatMap输出是无序的    在这添加了10秒的延迟 delay(10, TimeUnit.MILLISECONDS)

                if(integer<2) {
                    return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
                }else {
                    return Observable.fromIterable(list);
                }
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                LogUtil.e(TAG,s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
