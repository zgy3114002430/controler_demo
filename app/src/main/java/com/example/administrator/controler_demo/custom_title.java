package com.example.administrator.controler_demo;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * Created by 爱我请给我二十块 on 2018/4/9.
 */

public class custom_title extends RelativeLayout implements View.OnClickListener{

    public ImageButton menu_left;
    public ImageButton setting;
    public static MainActivity con1;

    public custom_title(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_title,this);
        menu_left=(ImageButton)findViewById(R.id.menu_left);
        setting=(ImageButton)findViewById(R.id.setting);

        menu_left.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_left:
                (MainActivity.mactivity).mydrawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.setting:
                con1=MainActivity.mactivity;
                FragmentTransaction transaction1=con1.mFragmentManager.beginTransaction();
                if(con1.loginlayout.isAdded()){
                    transaction1.hide(con1.fragmentNow).show(con1.loginlayout);
                }else{
                    transaction1.hide(con1.fragmentNow).add(R.id.framelayout,con1.loginlayout);
                }
                transaction1.addToBackStack(null);
                transaction1.commit();
                con1.fragmentNow=con1.loginlayout;
                /*if(con1.fragmentNow==null) {
                    transaction1.show(con1.loginlayout);
                }else{
                    transaction1.hide(con1.fragmentNow).show(con1.loginlayout);
                }
                transaction1.commit();
                con1.fragmentNow= con1.loginlayout;*/
                break;
                /*if(con1.loginlayout.isAdded()){
                    transaction1.hide(con1.fragmentNow).show(con1.loginlayout);
                }else{
                    transaction1.hide(con1.fragmentNow).add(R.id.framelayout,con1.loginlayout);
                    transaction1.addToBackStack(null);
                }
                con1.fragmentNow=con1.loginlayout;
                transaction1.commit();*/
        }

    }
}
