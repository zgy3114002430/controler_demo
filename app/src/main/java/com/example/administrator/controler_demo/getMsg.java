package com.example.administrator.controler_demo;

import android.util.Log;

/**
 * Created by 爱我请给我二十块 on 2018/5/5.
 */

public class getMsg {

    public byte[] msg=new byte[10];
    public int length;
    public getMsg(byte[] msg) {
        this.msg=msg;
    }

    //获取功能码
    public String getfunc(){
        byte[] str=new byte[1];
            str[0]=msg[4];
        String str3=bytesToString(str);
        return str3;
    }

    //获取节点地址
    public String getJiedianAddr(){
        byte[] addr=new byte[2];
        int j=0;
        for(int i=2;i<4;i++){
            addr[j++]=msg[i];
        }
        String strAddr=bytesToString(addr);
        return strAddr;
    }

    //查询节点设备编号
    public String[] getDeviceNum(){
        byte[] len=new byte[1];
        len[0]=msg[0];
        int intLen=Integer.parseInt(bytesToString(len),16);
        int num=intLen-6;
        String[] flags=new String[num];
        int j=0;
        for(int i=5;i<5+num;i++){
            byte[] inde=new byte[1];
            inde[0]=msg[i];
            flags[j++]=bytesToString(inde);
        }
        return flags;
    }

    //获取节点数量
    public int getJiedianNum(){
        byte[] len=new byte[1];
        len[0]=msg[0];
        int intLen=Integer.parseInt(bytesToString(len),16);
        int num=(intLen-6)/2;
        return num;
    }
    //获取该节点的设备数量
    public int getDeviceJiedianNum(){
        byte[] len=new byte[1];
        len[0]=msg[0];
        int intLen=Integer.parseInt(bytesToString(len),16);
        int num=intLen-6;
        return num;
    }

    //获取第一个节点地址
    public String getDevice1(){
        byte[] address=new byte[2];
        int j=0;
        for(int i=5;i<7;i++){
            address[j++]=msg[i];
        }
        String strToAddress=bytesToString(address);
        Log.i("节点地址1:",strToAddress);
        return strToAddress;
    }

    //获取第二个节点地址
    public String getDevice2(){
        byte[] address=new byte[2];
        int j=0;
        for(int i=7;i<9;i++){
            address[j++]=msg[i];
        }
        String strToAddress=bytesToString(address);
        Log.i("节点地址2:",strToAddress);
        return strToAddress;
    }

    //获取第三个节点地址
    public String getDevice3(){
        byte[] address=new byte[2];
        int j=0;
        for(int i=9;i<11;i++){
            address[j++]=msg[i];
        }
        String strToAddress=bytesToString(address);
        return strToAddress;
    }

    //当功能码为03时
    public String getFirstData(){
        byte[] data=new byte[1];
        data[0]=msg[5];
        String strData=String.valueOf(Integer.parseInt(bytesToString(data),16));
       // Log.i("first",strData);
        return strData;
    }
    public String getSecondData(){
        byte[] data=new byte[1];
        data[0]=msg[6];
        String strData=String.valueOf(Integer.parseInt(bytesToString(data),16));
        //Log.i("second",strData);
        return strData;
    }
    public String getThirstData(){
        byte[] data=new byte[1];
        data[0]=msg[7];
        String strData=String.valueOf(Integer.parseInt(bytesToString(data),16));
        //Log.i("thirst",strData);
        return strData;
    }
    public String getFourData(){
        byte[] data=new byte[1];
        data[0]=msg[8];
        String strData=String.valueOf(Integer.parseInt(bytesToString(data),16));
        //Log.i("four",strData);
        return strData;
    }
    //根据每个节点设备数量来获取数据
    public String getData(int i){
        if(i==0){
            return getFirstData();
        } else if(i==1){
            return getSecondData();
        }else if(i==2){
            return getThirstData();
        }else{
            return getFourData();
        }
    }

    //十六进制转成字符串
    public static String bytesToString(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] CHAR = {'0', '1', '2', '3', '4', '5',
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

            buf[index++] = CHAR[a / 16];
            buf[index++] = CHAR[a % 16];
        }
        return new String(buf);
    }

    //十六进制字节数组转成十进制数组
    public static byte[] hex2byte(byte[] b) {
        String HEX_CHAR = "0123456789ABCDEF";
        if(b.length%2 != 0) {
            throw new IllegalArgumentException("byte array length is not even!");
        }
        int length = b.length >> 1;
        byte[] b2 = new byte[length];
        int pos;
        for(int i=0; i<length; i++) {
            pos = i << 1;
            b2[i] = (byte) (HEX_CHAR.indexOf( b[pos] ) << 4 | HEX_CHAR.indexOf( b[pos+1] ) );
        }
        return b2;
    }
}
