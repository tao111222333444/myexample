package com.hugo.myexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hugo.myrxjava.rxjava.Main;
import com.hugo.myrxjava.rxjava.RxJavA;
import com.hugo.myrxjava.rxjava.RxJavaFlowable;
import com.hugo.myrxjava.rxjava.RxJavaZip;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_aaa).setOnClickListener(this);
//        RxJavaMap.testMap();
//        RxJavaMap.testFlatMap();
//        RxJavaMap.testConcatMap();
//        Main.test4();
//        RxJavaZip.test();
//        RxJavA.test();
//        RxJavA.test3();
//        RxJavaFlowable.test();
//        RxJavaFlowable.test1();
        RxJavaFlowable.test3();
    }

    @Override
    public void onClick(View view) {
        RxJavaFlowable.setSubscription(1);
    }
}
