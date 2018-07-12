package com.hugo.myrxjava.rxjava;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.FileReader;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/10.
 * 版本：v1.0
 * 描述：
 */
public class RxJavaTest {

    public static void main(String [] arg){
        printlce();
        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Subscription subscription;
    public static void printlce(){
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                try {


                FileReader fileReader = new FileReader("G:\\myExample\\myexample\\myrxjava\\src\\main\\java\\com\\hugo\\myrxjava\\rxjava\\text.txt");
                BufferedReader br = new BufferedReader(fileReader);
                System.out.println(br.readLine());
                String str;
                while ((str = br.readLine())!=null && !emitter.isCancelled()){
                    while(emitter.requested() == 0){
                        if(emitter.isCancelled()){
                            break;
                        }
                    }
                    emitter.onNext(str);
                }
                br.close();
                fileReader.close();

                emitter.onComplete();
                }catch (Exception e){
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                        try {
                            Thread.sleep(1000);
                            subscription.request(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError "+t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

}
