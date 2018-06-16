package com.example.administrator.controler_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by 爱我请给我二十块 on 2018/5/8.
 */
public class message_devices extends Fragment {

    public TextView show_light;
    public TextView show_humidity;
    public TextView show_temperature;
    public TextView show_humanInduction;
    public TextView show_smoke;
    public TextView show_appliances;
    public TextView show_lightTransducer;
    public String address=null;
    public String recev=null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播
        LocalBroadcastManager broadcastManager=LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("receiver of messagedevices");
        BroadcastReceiver mItemViewlistClickReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String light;
                String temperature;
                String humidity;
                String smoke;
                String human;
                String lightTranceor;
                String appliances;

                if((light=intent.getStringExtra("light"))!=null){
                    if(light.equals("1")){
                        show_light.setText("连接中");
                        show_light.setTextColor(Color.RED);
                    }
                }
                if((temperature=intent.getStringExtra("temperature"))!=null){
                    if(temperature.equals("1")){
                        show_temperature.setText("连接中");
                        show_temperature.setTextColor(Color.RED);
                    }
                }
                if((humidity=intent.getStringExtra("humidity"))!=null){
                    if(humidity.equals("1")){
                        show_humidity.setText("连接中");
                        show_humidity.setTextColor(Color.RED);
                    }
                }
                if((smoke=intent.getStringExtra("smoke"))!=null){
                    if(smoke.equals("1")){
                        show_smoke.setText("连接中");
                        show_smoke.setTextColor(Color.RED);
                    }
                }
                if((human=intent.getStringExtra("human"))!=null){
                    if(human.equals("1")){
                        show_humanInduction.setText("连接中");
                        show_humanInduction.setTextColor(Color.RED);
                    }
                }
                if((lightTranceor=intent.getStringExtra("lightTranceor"))!=null){
                    if(lightTranceor.equals("1")){
                        show_lightTransducer.setText("连接中");
                        show_lightTransducer.setTextColor(Color.RED);
                    }
                }
                if((appliances=intent.getStringExtra("application"))!=null){
                    if(appliances.equals("1")){
                        show_appliances.setText("连接中");
                        show_appliances.setTextColor(Color.RED);
                    }
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewlistClickReceiver,intentFilter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.message_devices,container,false);
        initView(view);
        return view;
    }

    public void initView(View view){
        show_light=(TextView)view.findViewById(R.id.show_light);
        show_humidity=(TextView)view.findViewById(R.id.show_humidity);
        show_temperature=(TextView)view.findViewById(R.id.show_temperature);
        show_humanInduction=(TextView)view.findViewById(R.id.show_humanInduction);
        show_smoke=(TextView)view.findViewById(R.id.show_smoke);
        show_appliances=(TextView)view.findViewById(R.id.show_appliances);
        show_lightTransducer=(TextView)view.findViewById(R.id.show_lightTransducer);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void updateUI(){
    }
}
