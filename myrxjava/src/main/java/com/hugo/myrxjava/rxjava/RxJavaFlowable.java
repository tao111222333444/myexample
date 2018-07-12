package com.hugo.myrxjava.rxjava;

import com.hugo.myrxjava.LogUtil;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/10.
 * 版本：v1.0
 * 描述：Flowable 和 Subscriber的使用
 *
 * BackpressureStrategy.ERROR   ：这个策略事件超出默认有128大小的缓存队列时会报错
 *  BackpressureStrategy.BUFFER ：这个策略和Observable一样 但是效率没有Observable高
 * BackpressureStrategy.DROP    ：这个策略 是丢弃超出128大小缓存的事件
 * BackpressureStrategy.LATEST  ：这个策略是总能获取到最后一个事件
 * BackpressureStrategy.MISSING : 这个策略是强制下游处理 事件
 *
 * 当使用interval符创建Flowable时  可以使用
 *      onBackpressureBuffer()
 *      onBackpressureDrop()
 *      onBackpressureLatest()
 *      这几个方法来设置策略
 */
public class RxJavaFlowable {
    private static String TAG = "RxJavaFlowable";

    public static void test(){
        //Flowable 的创建比Observable多了一个参数
        // 这个是用来设置当发出事件和接收事件不一致的 处理策略
        //Flowable 在异步的时候会有一个默认 128大小的事件存储队列
        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                LogUtil.e(TAG, "emitter 1");
                emitter.onNext(1);
                LogUtil.e(TAG, "emitter 2");
                emitter.onNext(2);
                LogUtil.e(TAG, "emitter 3");
                emitter.onNext(3);
                LogUtil.e(TAG, "emitter 4");
                emitter.onNext(4);
                LogUtil.e(TAG, "emitter onComplete1");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                LogUtil.e(TAG,"onSubscribe");
                //设置订阅者  事件处理能力  这个是必须设置的不是会受不到  上游发送的事件
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                LogUtil.e(TAG,"onNext   "+integer);
            }

            @Override
            public void onError(Throwable t) {
                LogUtil.e(TAG,"onError   "+t.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtil.e(TAG,"onComplete");
            }
        };
        flowable.subscribe(subscriber);
    }

    private static Subscription subscription;
    public static void setSubscription(int i){
        //设置订阅者  事件处理能力
        subscription.request(i);
    }
    public static void test1(){
      Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                LogUtil.e(TAG, "emitter 1");
                emitter.onNext(1);
                LogUtil.e(TAG, "emitter 2");
                emitter.onNext(2);
                LogUtil.e(TAG, "emitter 3");
                emitter.onNext(3);
                LogUtil.e(TAG, "emitter 4");
                emitter.onNext(4);
                LogUtil.e(TAG, "emitter onComplete1");
                emitter.onComplete();
            }
        },BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.e(TAG,"onSubscribe");
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.e(TAG,"onNext   "+integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.e(TAG,"onError   "+t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG,"onComplete");
                    }
                });
    }


    /***
     *  创建Flowable 是  设置队列缓存策略为BackpressureStrategy.BUFFER时
     *  Flowable就和Observable一样了
     */
    public static void test2(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                LogUtil.e(TAG, "emitter 1");
                emitter.onNext(1);
                LogUtil.e(TAG, "emitter 2");
                emitter.onNext(2);
                LogUtil.e(TAG, "emitter 3");
                emitter.onNext(3);
                LogUtil.e(TAG, "emitter 4");
                emitter.onNext(4);
                LogUtil.e(TAG, "emitter onComplete1");
                emitter.onComplete();
            }
        },BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.e(TAG,"onSubscribe");
                        //设置订阅者  事件处理能力
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.e(TAG,"onNext   "+integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.e(TAG,"onError   "+t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG,"onComplete");
                    }
                });
    }

    /**
     * 可以用emitter.requested()获取下游的处理事件能力
     * s.request(2);设置下游的处理事件能力
     *
     * 如果上游和下游是异步的  会出现下游不管设置处理事件值为多少
     * 上游emitter.requested()获取到的数都会是128
     * 因为异步的情况下  上游获取到的数是RxJava自动设置了   当下游处理了96个事件
     * 上游的emitter.requested()这个值就会增加96个
     *
     *
     *
     */
    public static void test3(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                LogUtil.e(TAG, "emitter 1 "+emitter.requested());
                emitter.onNext(1);
                LogUtil.e(TAG, "emitter 2 "+emitter.requested());
                emitter.onNext(2);
                LogUtil.e(TAG, "emitter 3 "+emitter.requested());
                emitter.onNext(3);
                LogUtil.e(TAG, "emitter 4 "+emitter.requested());
                emitter.onNext(4);
                LogUtil.e(TAG, "emitter onComplete1");
                emitter.onComplete();
            }
        },BackpressureStrategy.ERROR)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtil.e(TAG,"onSubscribe");
                        //设置订阅者  事件处理能力
                        s.request(2);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.e(TAG,"onNext   "+integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.e(TAG,"onError   "+t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG,"onComplete");
                    }
                });
    }
}
