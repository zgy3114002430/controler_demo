package com.example.administrator.controler_demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 爱我请给我二十块 on 2018/4/22.
 */

public class mainlayoutfragment extends Fragment implements AdapterView.OnItemClickListener {

    public static MainActivity con;
    public View view;
    public ListView device_list;
    private String[] namelist={"客厅","厨房","卧室","饭厅"};
    private int[] backImage={R.mipmap.livingrom_1,R.mipmap.kitchen_1,R.mipmap.bedrom_1,R.mipmap.livingrom_1};
    private int[] imageId1={R.mipmap.livingrom_fan,R.mipmap.livingrom_fan,R.mipmap.livingrom_fan,R.mipmap.livingrom_fan};
    private int[] imageId2={R.mipmap.livingrom_light,R.mipmap.livingrom_light,R.mipmap.livingrom_light,R.mipmap.livingrom_light};
    public byte[] search=    {0x06,0x3A,0x00,0x00,0x01,0x3B,0x23};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.mainlayout,container,false);
        initView(view);
        return view;
    }

    public void initView(View view){
        device_list=(ListView)view.findViewById(R.id.device_list);
        List<String> name=new ArrayList<>();
        for(int i=0;i<imageId1.length;i++){
            name.add(namelist[i]);
        }
        CustomAdapter adapter=new CustomAdapter(name,imageId1,imageId2,backImage,getActivity());
        device_list.setAdapter(adapter);
        device_list.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        con=MainActivity.mactivity;
        FragmentTransaction transaction0=con.mFragmentManager.beginTransaction();
        switch (position){
            case 0:
                if(con.livingromlayout.isAdded()){
                    transaction0.hide(con.fragmentNow).show(con.livingromlayout);
                }else{
                    transaction0.hide(con.fragmentNow).add(R.id.framelayout,con.livingromlayout);
                }
                transaction0.addToBackStack(null);
                transaction0.commit();
                con.fragmentNow=con.livingromlayout;
                if(con.isconnect){
                    try{
                        con.outputStreamClient.write(search);
                        con.outputStreamClient.flush();
                        Log.i("发送查询节点地址请求：",bytesToHexFun1(search));
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MainActivity.mactivity, "未连接设备!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static String bytesToHexFun1(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }
        return new String(buf);
    }
}
