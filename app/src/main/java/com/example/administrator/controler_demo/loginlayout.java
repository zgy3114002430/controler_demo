package com.example.administrator.controler_demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;


/**
 * Created by 爱我请给我二十块 on 2018/4/16.
 */

public class loginlayout extends Fragment implements View.OnClickListener {


    private String ip;
    private int port;
    Handler update=null;
    public EditText socket_ip;
    public EditText socket_port;
    private TextView receviemessage;
    public Button socket_connect;
    private View view;
    private String str2="";
    private String str="1";
    private  String mistakeMessage = "";
    public static MainActivity login;
    public byte[] recev_livingrom_humidity_num=    {0x06,0x3A,0x00,0x01,0x03,0x38,0x23};
    public byte[] recev_livingrom_temperature_num={0x06,0x3A,0x00,0x02,0x03,0x38,0x23};
    public byte[] recev_livingrom_light_num=    {0x06,0x3A,0x00,0x03,0x03,0x38,0x23};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.loginlayout,container,false);
        login=MainActivity.mactivity;
        initView(view);
        return view;
    }
    private void initView(View view){
        update=new Handler();
        receviemessage=(TextView)view.findViewById(R.id.connect_message);
        socket_ip=(EditText)view.findViewById(R.id.socket_ip);
        socket_port=(EditText)view.findViewById(R.id.socket_port);
        socket_connect=(Button)view.findViewById(R.id.socket_connect);

        socket_connect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try{
            if(login.isconnect){
                login.isconnect=false;
                try{
                    if (login.mSocketClient!=null){
                        login.mSocketClient.close();
                        login.mSocketClient=null;
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
                login.mThreadClient.interrupt();
            }
            else {
                login.isconnect=true;
                ip=socket_ip.getText().toString();
                port=Integer.parseInt(socket_port.getText().toString());
                new Thread(startSocket).start();
            }
        } catch(Exception e){
            mistakeMessage="步骤三";
            Message msg=new Message();
            mHandler.sendMessage(msg);
        }
    }

    //线程,连接socket
    private Runnable startSocket=new Runnable() {
        @Override
        public void run() {
            try{

                //连接中控
                login.mSocketClient=new Socket(ip,port);
                //获取输出流
                login.outputStreamClient=new DataOutputStream(login.mSocketClient.getOutputStream());
                login.inputStreamClient=new DataInputStream(login.mSocketClient.getInputStream());
                Log.i("情况","成功！");
                mistakeMessage="连接成功";
                Message msg1=new Message();
                mHandler.sendMessage(msg1);
            }catch (Exception e){
                e.printStackTrace();
                Log.i("情况","失败！");
                mistakeMessage="连接异常";
                Message msg=new Message();
                mHandler.sendMessage(msg);
            }
            login.mListener=new Thread(login.listener_recev);
            login.mListener.start();
        }
    };


    Handler mHandler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Toast.makeText(login,mistakeMessage,Toast.LENGTH_SHORT).show();
        }
    };

    public void startHandler(){
        login.mhandler=new Handler();
        login.mhandler.post(login.sendMsg);
    }

}
