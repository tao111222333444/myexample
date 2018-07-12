package com.hugo.myrxjava.rxjava;


import com.hugo.myrxjava.LogUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/10.
 * 版本：v1.0
 * 描述：
 */
public class RxJavaZip {
    private static String TAG = "RxJavaZip";

    /***
     * 不设置运行线程   两个observable都是在同一个线程里运行的
     * 所以会出现先运行完observable  在运行另一个observable1的情况
     *
     * 设置了运行线程  两个observable就好同时运行了
     */
    public static void test(){
        Observable<Integer> observable  = Observable.create(
                new ObservableOnSubscribe<Integer>() {
                               @Override
                               public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                   LogUtil.e(TAG, "emitter 1");
                                   emitter.onNext(1);
                                   Thread.sleep(1000);
                                   LogUtil.e(TAG, "emitter 2");
                                   emitter.onNext(2);
                                   Thread.sleep(1000);
                                   LogUtil.e(TAG, "emitter 3");
                                   emitter.onNext(3);
                                   Thread.sleep(1000);
                                   LogUtil.e(TAG, "emitter 4");
                                   emitter.onNext(4);
                                   LogUtil.e(TAG, "emitter onComplete1");
                                   emitter.onComplete();
                               }
                            }
                         ).subscribeOn(Schedulers.io());

        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
                                                               @Override
                 public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                     LogUtil.e(TAG, "emitter A");
                     emitter.onNext("A");
                     Thread.sleep(1000);
                     LogUtil.e(TAG,"emitter B");
                     emitter.onNext("B");
                                                                   Thread.sleep(1000);
                     LogUtil.e(TAG,"emitter C");
                     emitter.onNext("C");
                                                                   Thread.sleep(1000);
                     LogUtil.e(TAG,"emitter D");
                     emitter.onNext("D");
                     LogUtil.e(TAG,"emitter E");
                     emitter.onNext("E");
                     LogUtil.e(TAG,"emitter onComplete2");
                     emitter.onComplete();
                 }

        }).subscribeOn(Schedulers.io());


        Observable.zip(observable, observable1, new BiFunction<Integer, String ,String >() {
            @Override
            public String apply(Integer integer, String s) {
                LogUtil.e(TAG,"apply    "+integer+s);
                return integer+s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtil.e(TAG,"onSubscribe");
            }

            @Override
            public void onNext(String s) {
                LogUtil.e(TAG,s);
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtil.e(TAG,"onComplete");
            }
        });
    }
}
