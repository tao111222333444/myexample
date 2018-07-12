package com.hugo.myrxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hugo.myrxjava.rxjava.Main;

/**
 * @author 作者：hugo
 * @date 时间：2018/7/9.
 * 版本：v1.0
 * 描述：
 */
public class MainAcitivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Main.test3();
        Main.test3();
    }
}
