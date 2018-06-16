package com.example.administrator.controler_demo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 爱我请给我二十块 on 2018/4/21.
 */

public class mainlayout extends LinearLayout implements AdapterView.OnItemClickListener{
    public static MainActivity con;
    public ListView device_list;
    private String[] namelist={"客厅","厨房","卧室","饭厅"};
    private int[] backImage={R.mipmap.livingrom_1,R.mipmap.kitchen_1,R.mipmap.bedrom_1,R.mipmap.livingrom_1};
    private int[] imageId1={R.mipmap.livingrom_fan,R.mipmap.livingrom_fan,R.mipmap.livingrom_fan,R.mipmap.livingrom_fan};
    private int[] imageId2={R.mipmap.livingrom_light,R.mipmap.livingrom_light,R.mipmap.livingrom_light,R.mipmap.livingrom_light};

    public mainlayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.mainlayout,this);
        device_list=(ListView)findViewById(R.id.device_list);
        List<String> name=new ArrayList<>();
        for(int i=0;i<imageId1.length;i++){
            name.add(namelist[i]);
        }
        CustomAdapter adapter=new CustomAdapter(name,imageId1,imageId2,backImage,context);
        device_list.setAdapter(adapter);
        device_list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        con=MainActivity.mactivity;
        FragmentTransaction transaction0=con.mFragmentManager.beginTransaction();
        switch (position){
            case 0:

                //判断Fragment是否存在
                if(con.fragmentNow==null){
                   transaction0.show(con.livingromlayout);
                }else{
                    transaction0.hide(con.fragmentNow).show(con.livingromlayout);
                }
                transaction0.commit();
                con.fragmentNow=con.livingromlayout;
                break;
            case 1:
                Toast.makeText(MainActivity.mactivity, "ListView点击事件",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
