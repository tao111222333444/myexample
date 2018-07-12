package com.hugo.myrxjava.rxjava;

import com.hugo.myrxjava.LogUtil;

import java.io.Serializable;
import java.nio.channels.IllegalSelectorException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/9.
 * 版本：v1.0
 * 描述：
 */
public class Main {
    private static String TAG = "Main";

    public static void main(String[] arg){
        test4();
    }

    public static void test4(){
//        Observable.create(emitter -> {
//            while(!emitter.isDisposed()){
//                long time = System.currentTimeMillis();
//                emitter.onNext(time);
//                if(time % 2 != 0){
//                    emitter.onError(new IllegalStateException("Odd millisecond"));
//                }
//            }
//        }).subscribe(System.out::println,Throwable::printStackTrace);
//
//
//        Observable.just("hello world").subscribe(System.out::println);

        Observable.just(1,2,3,4).subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer ii) {
//                LogUtil.e(TAG,ii+"    onNext");
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
     * subscribeOn() 指定的是上游发送事件的线程
     * observeOn() 指定的是下游接收事件的线程.
     *  多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.
     *  多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.
     *
     *  Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
     *  Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
     *  Schedulers.newThread() 代表一个常规的新线程
     *  AndroidSchedulers.mainThread() 代表Android的主线程
     *
     *  运行结果
     *  [Main]  onSubscribe thread is main
     *  [Main]  Observable thread is RxNewThreadScheduler-1
     *  [Main]  subscribe  21
     *  [Main]  accept thread is main
     *  [Main]  onNext thread is RxCachedThreadScheduler-1
     *  [Main]  onNext  21
     *  [Main]  onNext onComplete is RxCachedThreadScheduler-1
     */
    public  static void test3(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                LogUtil.e(TAG,"Observable thread is "+ Thread.currentThread().getName());

                emitter.onNext(21);
                LogUtil.e(TAG,"subscribe  21"  );
                emitter.onComplete();
//                emitter.onError(new Exception("aaaaaa"));
            }
        })
                //subscribeOn() 指定的是上游发送事件的线程
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                //observeOn() 指定的是下游接收事件的线程.
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e(TAG,"accept thread is "+ Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtil.e(TAG,"onSubscribe thread is "+ Thread.currentThread().getName());
            }

            @Override
            public void onNext(Integer integer) {
                LogUtil.e(TAG,"onNext thread is "+ Thread.currentThread().getName());
                LogUtil.e(TAG,"onNext  "+integer  );
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG,"onNext onError is "+ Thread.currentThread().getName());

            }

            @Override
            public void onComplete() {
                LogUtil.e(TAG,"onNext onComplete is "+ Thread.currentThread().getName());

            }
        });
    }


    private static void test2(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
                emitter.onNext(4);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("accept"+integer);
            }
        });
    }


    private static void test1(){
        /**
         * 上游可以发送无限个onNext, 下游也可以接收无限个onNext.
         当上游发送了一个onComplete后, 上游onComplete之后的事件将会继续发送,
         而下游收到onComplete事件之后将不再继续接收事件.
         当上游发送了一个onError后, 上游onError之后的事件将继续发送,
         而下游收到onError事件之后将不再继续接收事件.
         上游可以不发送onComplete或onError.
         最为关键的是onComplete和onError必须唯一并且互斥,
         即不能发多个onComplete, 也不能发多个onError, 也不能先发一个onComplete, 然后再发一个onError, 反之亦然
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
//                emitter.onError(new Exception("aaaaaaaaa"));
//                emitter.onError(new Exception("bbbbbbbbb"));
                emitter.onNext(4);
                emitter.onNext(5);
            }
        }).subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
//                d.dispose();可以解绑观察者

                //CompositeDisposable 这个类是当有多个Disposable来存放的来
                // 解绑是调用aa.clear()  就可以解绑所有的观察者了
//                CompositeDisposable aa =  new  CompositeDisposable();
//                aa.add(d);
//                aa.clear();
                System.out.println("onSubscribe   "+ d.isDisposed());
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext  "+integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError  "+e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete  ");
            }
        });
    }
}
