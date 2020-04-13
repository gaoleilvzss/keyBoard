/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */

package com.parkingwang.vehiclekeyboard.demo;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parkingwang.keyboard.KeyboardInputController;
import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.engine.KeyboardEntry;
import com.parkingwang.keyboard.view.InputView;
import com.parkingwang.keyboard.view.OnKeyboardChangedListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private InputView mInputView;
    private EditText mProvinceView;

    private final List<String> mTestNumber = new ArrayList<>();

    private PopupKeyboard mPopupKeyboard;
    private long mTestIndex = 0;

    private boolean mHideOKKey = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInputView = findViewById(R.id.input_view);

        final Button lockTypeButton = findViewById(R.id.lock_type);

        mTestNumber.add("粤A12345");
        mTestNumber.add("粤BD12345");
        mTestNumber.add("粤Z1234港");
        mTestNumber.add("WJ粤12345");
        mTestNumber.add("WJ粤1234X");
        mTestNumber.add("NA00001");
        mTestNumber.add("123456使");
        mTestNumber.add("使123456");
        mTestNumber.add("粤A1234领");
        mTestNumber.add("粤12345领");
        mTestNumber.add("民航12345");
        mTestNumber.add("粤C0");
        mTestNumber.add("粤");
        mTestNumber.add("WJ粤12");
        mTestNumber.add("湘E123456");

        // 创建弹出键盘
        mPopupKeyboard = new PopupKeyboard(this);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(mInputView, this);
        // 隐藏确定按钮
        mPopupKeyboard.getKeyboardEngine().setHideOKKey(mHideOKKey);
        mPopupKeyboard.getKeyboardView().addKeyboardChangedListener(new OnKeyboardChangedListener() {
            @Override
            public void onTextKey(String text) {

            }

            @Override
            public void onDeleteKey() {
                mInputView.get9Button().setVisibility(View.VISIBLE);
                mInputView.get9Button().setText("+\r\n新能源");
                mInputView.set8thVisibility(false);
                KeyboardInputController.mLockedOnNewEnergyType = false;
            }

            @Override
            public void onConfirmKey() {
                if(mPopupKeyboard.isShown()){
                    mPopupKeyboard.dismiss(MainActivity.this);
                }
            }

            @Override
            public void onKeyboardChanged(KeyboardEntry keyboard) {

            }
        });
        // KeyboardInputController提供一个默认实现的新能源车牌锁定按钮
        mPopupKeyboard.getController()
                .setDebugEnabled(true)
                .bindLockTypeProxy(new KeyboardInputController.ButtonProxyImpl(mInputView.get9Button()) {
                    @Override
                    public void onNumberTypeChanged(boolean isNewEnergyType) {
                        super.onNumberTypeChanged(isNewEnergyType);
                        if (isNewEnergyType) {
                            lockTypeButton.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                        } else {
                            lockTypeButton.setTextColor(getResources().getColor(android.R.color.black));
                        }
                    }
                });

    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mPopupKeyboard!=null){
            if(!isInEditText(mPopupKeyboard.getKeyboardView(),ev)){
                if(mPopupKeyboard.isShown()){
                    mPopupKeyboard.dismiss(MainActivity.this);
                }
            }
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }
    private boolean isInEditText(View v, MotionEvent event) {
        Rect frame = new Rect();
        v.getHitRect(frame);
        float eventX = event.getX();
        float eventY = event.getY();
        return frame.contains((int) eventX, (int) eventY);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 默认选中第一个车牌号码输入框
        mInputView.performFirstFieldView();
    }

    public void onClick(View view) {
        int id = view.getId();
        // 切换键盘类型
    }
}
