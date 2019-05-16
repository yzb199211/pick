package com.yyy.pick;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.yyy.pick.pick.builder.TimePickerBuilder;
import com.yyy.pick.pick.listener.OnTimeSelectChangeListener;
import com.yyy.pick.pick.listener.OnTimeSelectListener;
import com.yyy.pick.pick.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PickActivity extends AppCompatActivity {

    @BindView(R.id.btn_date)
    Button btnDate;
    @BindView(R.id.btn_time)
    Button btnTime;
    @BindView(R.id.btn_other)
    Button btnOther;

    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        ButterKnife.bind(this);
        initTimePicker();
    }

    private void initTimePicker() {//Dialog 模式下，在底部弹出

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Toast.makeText(PickActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                Log.i("pvTime", "onTimeSelect");
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                }).setContentTextSize(18).setBgColor(0xFFFFFFFF)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
                //当显示只有一列是需要设置window宽度，防止两边有空隙；
                WindowManager.LayoutParams winParams;
                winParams = dialogWindow.getAttributes();
                winParams.width =WindowManager.LayoutParams.MATCH_PARENT;
                dialogWindow.setAttributes(winParams);
            }
        }
    }

    @OnClick({R.id.btn_date, R.id.btn_time, R.id.btn_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_date:

                break;
            case R.id.btn_time:
//                pvTime.setDate( Calendar.getInstance());
                pvTime.show(view);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
                break;
            case R.id.btn_other:
                break;
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }
}
