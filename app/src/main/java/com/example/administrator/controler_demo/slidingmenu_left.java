package com.example.administrator.controler_demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by 爱我请给我二十块 on 2018/4/9.
 */

public class slidingmenu_left extends LinearLayout implements View.OnClickListener {

    public static MainActivity con1;
    public RelativeLayout control;
    public RelativeLayout favotite;
    public ImageButton slidingmenu_back;
    public RelativeLayout devices;
    public byte[] livingRom_light=          {0x06,0x3A,0x00,0x01,0x02,0x39,0x23};
    public byte[] livingRom_humidity=       {0x06,0x3A,0x00,0x03,0x02,0x39,0x23};
    public byte[] livingRom_temperature=    {0x06,0x3A,0x00,0x02,0x02,0x39,0x23};
    public byte[] livingRom_smoke=          {0x06,0x3A,0x00,0x04,0x02,0x39,0x23};
    public byte[] livingRom_lighttransducer={0x06,0x3A,0x00,0x06,0x02,0x39,0x23};
    public byte[] livingRom_appliances=     {0x06,0x3A,0x00,0x07,0x02,0x39,0x23};
    public byte[] livingRom_humaninduction= {0x06,0x3A,0x00,0x05,0x02,0x39,0x23};

    public slidingmenu_left(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.slidingmenu,this);
        devices=(RelativeLayout)findViewById(R.id.devices);
        control=(RelativeLayout)findViewById(R.id.control);
        favotite=(RelativeLayout)findViewById(R.id.favorite);
        slidingmenu_back=(ImageButton)findViewById(R.id.slidingmenu_back);

        devices.setOnClickListener(this);
        control.setOnClickListener(this);
        favotite.setOnClickListener(this);
        slidingmenu_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.devices:
                con1=MainActivity.mactivity;
                con1.mydrawer.closeDrawer(Gravity.LEFT);
                if(con1.isconnect){
                    Intent intent=new Intent("receiver of messagedevices");
                    FragmentTransaction transaction1=con1.mFragmentManager.beginTransaction();
                    if(con1.message_devices.isAdded()){
                        transaction1.hide(con1.fragmentNow).show(con1.message_devices);
                    }else{
                        transaction1.hide(con1.fragmentNow).add(R.id.framelayout,con1.message_devices);
                    }
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                    con1.fragmentNow=con1.message_devices;
                    for(int i=0;i<con1.num;i++){
                        for(int j=0;j<con1.jieDian[i];j++){
                            if(con1.addrNum[i][j].equals("01")){
                                intent.putExtra("light","1");
                            }
                            if(con1.addrNum[i][j].equals("05")){
                                intent.putExtra("human","1");
                            }
                            if(con1.addrNum[i][j].equals("02")){
                                Log.i("编号","1");
                                intent.putExtra("temperature","1");
                            }
                            if(con1.addrNum[i][j].equals("03")){
                                intent.putExtra("humidity","1");
                            }
                            if(con1.addrNum[i][j].equals("04")){
                                intent.putExtra("smoke","1");
                            }
                            if(con1.addrNum[i][j].equals("06")){
                                intent.putExtra("lightTranceor","1");
                            }
                            if(con1.addrNum[i][j].equals("07")){
                                intent.putExtra("application","1");
                            }
                        }
                    }
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                }else{
                    Toast.makeText(MainActivity.mactivity, "尚未连接服务器！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.control:
                Toast.makeText(MainActivity.mactivity, "You clicked back button!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.favorite:
                Toast.makeText(MainActivity.mactivity, "You clicked back button!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.slidingmenu_back:
                (MainActivity.mactivity).mydrawer.closeDrawer(Gravity.LEFT);
                break;
        }

    }
}
