package com.hugo.myrxjava.rxjava;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/11.
 * 版本：v1.0
 * 描述：
 */
public class RxJavaPlugin {


    public static void main(String[] arg){

        test();
        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void test(){
        RxJavaPlugins.setOnObservableSubscribe(new BiFunction<Observable, Observer, Observer>() {
            @Override
            public Observer apply(Observable observable, Observer observer) throws Exception {
                return new ObservableSubscribeHooker(observer);
            }
        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                System.out.println("emitter 1");
                emitter.onNext(2);
                System.out.println("emitter 2");
                emitter.onNext(3);
                System.out.println("emitter 3");
                emitter.onComplete();
            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("aaaaaaaaaaa"+integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
//                .subscribe(new Observer<Integer>() {
//
////                    @Override
////                    public void onSubscribe(Subscription s) {
////                        s.request(100);
////                        System.out.println("onSubscribe ");
////                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        System.out.println("onSubscribe ");
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        System.out.println("onNext "+integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        System.out.println("onError "+t.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        System.out.println("onComplete ");
//                    }
//                });
    }

    static class ObservableSubscribeHooker<T> implements Observer<T> {
        private Observer<T>  observer;

        ObservableSubscribeHooker(Observer<T>  observer){
            this.observer = observer;
        }



        @Override
        public void onSubscribe(Disposable d) {
            System.out.println("这是插件 onSubscribe");
        }

        @Override
        public void onNext(T t) {
            System.out.println("这是插件  onNext  "+t.toString());
            observer.onNext(t);
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("这是插件 onError  "+e.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("这是插件 onComplete");
            observer.onComplete();
        }
    }
}
