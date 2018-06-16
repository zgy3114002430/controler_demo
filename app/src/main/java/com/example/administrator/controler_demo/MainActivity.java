package com.example.administrator.controler_demo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    //输入、输出流
    public DataInputStream inputStreamClient=null;
    public DataOutputStream outputStreamClient=null;
    //获取输入框ip和端口号
    public String IP,port;
    //
    public Thread mThreadClient=null;
    //监听线程
    public Thread mListener=null;
    public Socket mSocketClient=null;
    //判断Socket是否建立通讯
    public boolean isconnect=false;
    public static MainActivity mactivity;
    public DrawerLayout mydrawer;
    //Fragment碎片
    public Fragment mainlayout,fragmentNow,livingromlayout,loginlayout,mainlayoutfragment,message_devices;
    //Fragment管理器
    public FragmentManager mFragmentManager;
    public getMsg getmsg=null;
    public String func;
    public String data;
    public Handler mhandler;
    //节点数量
    public int num;
    //存放节点设备编号
    public String[][] addrNum;
    //存放节点地址
    public String[] addr;
    //存放指令
    public byte[] command1;
    public byte[] command2;
    public byte[] command3;
    public int[] jieDian;
    public byte[][] request;
    //警报声
    public SoundPool smoke;
    public SoundPool somebody;
    public boolean flag=true;
    //存放感应器数据
    public String[] deviceData;
    //记录灯的序号
    public String[] flag_light={"7","7"};
    //记录温度的序号
    public String[] flag_tempwrature={"7","7"};
    //记录湿度的序号
    public String[] flag_humidity={"7","7"};
    //记录烟雾传感器的序号
    public String[] flag_smoke={"7","7"};
    //记录人体感应模块的序号
    public String[] flag_human={"7","7"};
    //记录光敏传感器的序号
    public String[] flag_lightTraceor={"7","7"};
    //记录继电器的序号
    public String[] flag_application={"7","7"};
    //保存返回地址
    String strforrecev=null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置标题
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        mactivity=this;
        initView();
        initFragment();
    }
    //退出程序时释放内存
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    //控件初始化
    private void initView(){
        mydrawer=(DrawerLayout)findViewById(R.id.mydrawer);
        livingromlayout=new livingromlayout();
        loginlayout=new loginlayout();
        message_devices=new message_devices();
        mainlayoutfragment=new mainlayoutfragment();
        mFragmentManager=getSupportFragmentManager();
        //加载音频资源
        smoke=new SoundPool(10, AudioManager.STREAM_ALARM,5);
        smoke.load(getApplication(),R.raw.alarm,1);
        somebody=new SoundPool(10,AudioManager.STREAM_ALARM,5);
        somebody.load(getApplication(),R.raw.somebody,1);
    }

    public Runnable listener_recev=new Runnable() {
        @Override
        public void run() {
            while (true){
                try{
                    byte[] recev=new byte[12];
                    int i=0;
                    if((i=inputStreamClient.read(recev))>0){
                        strforrecev=BinaryToHexString(recev);
                        getmsg=new getMsg(recev);
                        func=getmsg.getfunc();
                        judgeFunc();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    };

    //定时发送请求
    public Runnable sendMsg=new Runnable() {
        @Override
        public void run() {
            while(true){
                try{
                    for(int i=0;i<num;i++){
                        outputStreamClient.write(request[i]);
                        outputStreamClient.flush();
                        Thread.sleep(10*1000);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    };
    //初始化Fragment碎片
    private void initFragment(){
        FragmentTransaction fragmentTransaction0=mFragmentManager.beginTransaction();
        fragmentTransaction0.add(R.id.framelayout,mainlayoutfragment);
        fragmentTransaction0.commit();
        fragmentNow=mainlayoutfragment;
    }
    //十六进制转化成字符串
    public static String BinaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex = "";
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result +=hex+" ";
        }
        return result;
    }

    //判断功能
    public void judgeFunc() {
        if (func.equals("01")) {
            Log.i("返回功能1的协议报文",strforrecev);
            //节点数量
            num = getmsg.getJiedianNum();
            request=new byte[num][7];
            addrNum =new String[num][6];
            //每个节点的设备数量
            jieDian=new int[getmsg.getDeviceJiedianNum()];
            if (num == 1) {
                addr = new String[1];
                addr[0] = getmsg.getDevice1();
                Log.i("addr",addr[0]);
                command1 = hexStringToBytes("063A" + addr[0] + "023923");
                request[0] = hexStringToBytes("063A"+addr[0]+"023923");
                Log.i("command1",BinaryToHexString(command1));
                try {
                    outputStreamClient.write(command1);
                    outputStreamClient.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (num == 2) {
                addr = new String[2];
                String address1 = getmsg.getDevice1();
                String address2 = getmsg.getDevice2();

                addr[0] = address1;
                addr[1] = address2;

                command1 = hexStringToBytes("063A" + address1 + "023923");
                command2 = hexStringToBytes("063A" + address2 + "023923");

                request[0]=hexStringToBytes("063A"+address1+"033823");
                request[1]=hexStringToBytes("063A"+address2+"033823");
                try {
                    outputStreamClient.write(command1);
                    outputStreamClient.flush();
                    Thread.sleep(3*1000);

                    outputStreamClient.write(command2);
                    outputStreamClient.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            } else if (num == 3) {
                addr = new String[3];
                String address1 = getmsg.getDevice1();
                String address2 = getmsg.getDevice2();
                String address3 = getmsg.getDevice3();

                addr[0] = address1;
                addr[1] = address2;
                addr[2] = address3;

                command1 = hexStringToBytes("063A" + address1 + "023923");
                command2 = hexStringToBytes("063A" + address2 + "023923");
                command3 = hexStringToBytes("063A" + address3 + "023923");

                request[0]=hexStringToBytes("063A"+address1+"033823");
                request[1]=hexStringToBytes("063A"+address2+"033823");
                request[2]=hexStringToBytes("063A"+address3+"033823");
                try {
                    outputStreamClient.write(command1);
                    outputStreamClient.flush();

                    Thread.sleep(3*1000);
                    outputStreamClient.write(command2);
                    outputStreamClient.flush();

                    Thread.sleep(3*1000);
                    outputStreamClient.write(command3);
                    outputStreamClient.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            new Thread(sendMsg).start();
        }else if(func.equals("02")){
            Log.i("返回功能2的协议报文",strforrecev);
            for(int i=0;i<num;i++){
                if(getmsg.getJiedianAddr().equals(addr[i])){
                    jieDian[i]=getmsg.getDeviceJiedianNum();
                    addrNum[i]=getmsg.getDeviceNum();
                }
            }
            for(int i=0;i<num;i++){
                for(int j=0;j<jieDian[i];j++){
                    Log.i("编号",addrNum[i][j]);
                }
            }
        }else if(func.equals("03")){
            Log.i("返回数据请求协议报文",strforrecev);
            Intent intent=new Intent("receiver of livingRom");
            if(flag){
                for(int i=0;i<num;i++){
                    for(int j=0;j<jieDian[i];j++){
                        if(addrNum[i][j].equals("01")){
                            flag_light[0]=String.valueOf(i);
                            flag_light[1]=String.valueOf(j);
                        }else if(addrNum[i][j].equals("02")){
                            flag_tempwrature[0]=String.valueOf(i);
                            flag_tempwrature[1]=String.valueOf(j);
                        }else if(addrNum[i][j].equals("03")){
                            flag_humidity[0]=String.valueOf(i);
                            flag_humidity[1]=String.valueOf(j);
                        }else if(addrNum[i][j].equals("04")){
                            flag_smoke[0]=String.valueOf(i);
                            flag_smoke[1]=String.valueOf(j);
                        }else if(addrNum[i][j].equals("05")){
                            flag_human[0]=String.valueOf(i);
                            flag_human[1]=String.valueOf(j);
                        }else if(addrNum[i][j].equals("06")){
                            flag_lightTraceor[0]=String.valueOf(i);
                            flag_lightTraceor[1]=String.valueOf(j);
                        }else if(addrNum[i][j].equals("07")){
                            flag_application[0]=String.valueOf(i);
                            flag_application[1]=String.valueOf(j);
                        }
                    }
                }
                flag=false;
            }
            int s=0;
            int index=0;
            while(s<num){
                if(getmsg.getJiedianAddr().equals(addr[s])){
                    if(flag_light[0].equals(String.valueOf(s))){
                        String data=getmsg.getData(Integer.parseInt(flag_light[1]));
                        Log.i("灯",data);
                        intent.putExtra("light",data);
                    }
                    if(flag_tempwrature[0].equals(String.valueOf(s))){
                        String data=getmsg.getData(Integer.parseInt(flag_tempwrature[1]));
                        Log.i("温度",data);
                        intent.putExtra("temperature",data);
                    }
                    if(flag_humidity[0].equals(String.valueOf(s))){
                        String data=getmsg.getData(Integer.parseInt(flag_humidity[1]));
                        Log.i("湿度",data);
                        intent.putExtra("humidity",data);
                    }
                    if(flag_smoke[0].equals(String.valueOf(s))){
                        String data=getmsg.getData(Integer.parseInt(flag_smoke[1]));
                        Log.i("烟感",data);
                        intent.putExtra("smoke",data);
                    }
                    if(flag_human[0].equals(String.valueOf(s))){
                        String data=getmsg.getData(Integer.parseInt(flag_human[1]));
                        Log.i("人体感应",data);
                        intent.putExtra("human",data);
                    }
                    if(flag_lightTraceor[0].equals(String.valueOf(s))){
                        String data=getmsg.getData(Integer.parseInt(flag_lightTraceor[1]));
                        Log.i("光敏",data);
                        intent.putExtra("lightTranceor",data);
                    }
                    if(flag_application[0].equals(String.valueOf(s))){
                        String data=getmsg.getData(Integer.parseInt(flag_application[1]));
                        Log.i("继电器",data);
                        intent.putExtra("application",data);
                    }
                    /*for(int k=0;k<deviceData.length;k++){
                        Log.i("输出的数据",deviceData[k]);
                    }*/
                    LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent);
                }
                s++;
            }
        }
    }
    //十六进制字符串转化字符数组
    public byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte)(charToByte(hexChars[pos]) << 4 |charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private byte charToByte(char c) {
        return (byte)"0123456789ABCDEF".indexOf(c);
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
