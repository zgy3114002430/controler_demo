package com.example.administrator.controler_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by 爱我请给我二十块 on 2018/4/11.
 */

public class livingromlayout extends Fragment implements SeekBar.OnSeekBarChangeListener,View.OnClickListener{


    public static MainActivity login=null;
    byte[] command_livingrom_light_switch_on= {0x08,0x3A,0x00,0x01,0x04,0x01,0x01,0x3F,0x23};
    byte[] command_livingrom_light_switch_off={0x08,0x3A,0x00,0x01,0x04,0x01,0x00,0x3F,0x23};
    public ImageView voice;
    public Switch livingrom_light_switch;
    public Switch livingrom_appliances_switch;
    public SeekBar livingrom_light_seekbar;
    public TextView livingrom_humidity_num;
    public TextView livingrom_light_num;
    public TextView livingrom_temperature_num;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String , String>();
    public String address;
    public String data;
    public boolean flag_light=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager broadcastManager=LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("receiver of livingRom");
        BroadcastReceiver mItemViewListClickReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String temperature;
                String light;
                final String humidity;
                String smoke;
                String human;
                String application;
                final String lightTranceor;
                //Log.i("广播开始","I AM BROADCAST！");
                if((temperature=intent.getStringExtra("temperature"))!=null){
                   // Log.i("温度",temperature);
                    new Handler().post(new Runnable(){
                        @Override
                        public void run() {
                            livingrom_temperature_num.setText(temperature);
                        }
                    });
                }
                if((light=intent.getStringExtra("light"))!=null){
                  //  Log.i("灯",light);
                }
                if((humidity=intent.getStringExtra("humidity"))!=null){
                   // Log.i("湿度",humidity);
                    new Handler().post(new Runnable(){
                        @Override
                        public void run() {
                            livingrom_humidity_num.setText(humidity);
                        }
                    });
                }
                if((smoke=intent.getStringExtra("smoke"))!=null){
                  //  Log.i("烟感",smoke);
                    if(smoke.equals("0")){
                        login.smoke.play(1,1,1,0,0,1);
                    }
                }
                if((human=intent.getStringExtra("human"))!=null){
                  //  Log.i("人体",human);
                    if(human.equals("0")){
                        login.somebody.play(1,1,1,0,0,1);
                    }
                }
                if((application=intent.getStringExtra("application"))!=null){
                   // Log.i("继电器",application);
                }
                if((lightTranceor=intent.getStringExtra("lightTranceor"))!=null){
                   // Log.i("光敏",lightTranceor);
                    if(lightTranceor.equals("0")){
                        new Handler().post(new Runnable(){
                            @Override
                            public void run() {
                                livingrom_light_num.setText("强光");
                            }
                        });
                    }else{
                        new Handler().post(new Runnable(){
                            @Override
                            public void run() {
                                livingrom_light_num.setText("弱光");
                            }
                        });
                    }
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver,intentFilter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.livingromlayout,container,false);
        login=MainActivity.mactivity;
        initView(view);
        initSpeech();
        return view;
    }

    private void initSpeech() {
        // 将“12345678”替换成您申请的 APPID，申请地址： http://www.xfyun.cn
        // 请勿在 “ =”与 appid 之间添加任务空字符或者转义符
        SpeechUtility.createUtility(getActivity(),SpeechConstant.APPID+"=5ae28ac6" );
    }

    private void initView(View view){
        voice=(ImageView)view.findViewById(R.id.voice);
        livingrom_light_switch=(Switch)view.findViewById(R.id.livingrom_light_switch);
        livingrom_appliances_switch=(Switch)view.findViewById(R.id.livingrom_appliances_switch);
        livingrom_light_seekbar=(SeekBar)view.findViewById(R.id.livingrom_light_seekbar);
        livingrom_humidity_num=(TextView)view.findViewById(R.id.livingrom_humidity_num);
        livingrom_light_num=(TextView)view.findViewById(R.id.livingrom_light_num);
        livingrom_temperature_num=(TextView)view.findViewById(R.id.livingrom_temperature_num);
        //添加继电器监听事件
        livingrom_appliances_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(login.isconnect){
                        try{
                            for(int i=0;i<login.num;i++){
                                for(int j=0;j<login.jieDian[i];j++){
                                    if(login.addrNum[i][j].equals("07")){
                                        //083A00010401013F23
                                        byte[] command=hexStringToBytes("083A"+login.addr[i]+"0407003F23");
                                        Log.i("继电器",BinaryToHexString(command));
                                        login.outputStreamClient.write(command);
                                        login.outputStreamClient.flush();
                                        flag_light=false;
                                        break;
                                    }
                                }
                            }
                            if(flag_light){
                                Toast.makeText(MainActivity.mactivity, "没有此设备",Toast.LENGTH_SHORT).show();
                                flag_light=true;
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(MainActivity.mactivity, "未接入局域网",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(login.isconnect){
                        try{
                            for(int i=0;i<login.num;i++){
                                for(int j=0;j<login.jieDian[i];j++){
                                    if(login.addrNum[i][j].equals("07")){
                                        //083A00010401013F23
                                        byte[] command=hexStringToBytes("083A"+login.addr[i]+"0407013F23");
                                        Log.i("灯",BinaryToHexString(command));
                                        login.outputStreamClient.write(command);
                                        login.outputStreamClient.flush();
                                        flag_light=false;
                                        break;
                                    }
                                }
                            }
                            if(flag_light){
                                Toast.makeText(MainActivity.mactivity, "没有此设备",Toast.LENGTH_SHORT).show();
                                flag_light=true;
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(MainActivity.mactivity, "未接入局域网",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //添加灯光监听事件
        livingrom_light_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(login.isconnect){
                        try{
                            for(int i=0;i<login.num;i++){
                                for(int j=0;j<login.jieDian[i];j++){
                                    if(login.addrNum[i][j].equals("01")){
                                        //083A00010401013F23
                                        byte[] command=hexStringToBytes("083A"+login.addr[i]+"0401003F23");
                                        Log.i("等",BinaryToHexString(command));
                                        login.outputStreamClient.write(command);
                                        login.outputStreamClient.flush();
                                        flag_light=false;
                                        break;
                                    }
                                }
                            }
                            if(flag_light){
                                Toast.makeText(MainActivity.mactivity, "没有此设备",Toast.LENGTH_SHORT).show();
                                flag_light=true;
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(MainActivity.mactivity, "未接入局域网",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(login.isconnect){
                        try{
                            for(int i=0;i<login.num;i++){
                                for(int j=0;j<login.jieDian[i];j++){
                                    if(login.addrNum[i][j].equals("01")){
                                        //083A00010401013F23
                                        byte[] command=hexStringToBytes("083A"+login.addr[i]+"0401013F23");
                                        Log.i("继电器",BinaryToHexString(command));
                                        login.outputStreamClient.write(command);
                                        login.outputStreamClient.flush();
                                        flag_light=false;
                                        break;
                                    }
                                }
                            }
                            if(flag_light){
                                Toast.makeText(MainActivity.mactivity, "没有此设备",Toast.LENGTH_SHORT).show();
                                flag_light=true;
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(MainActivity.mactivity, "未接入局域网",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        voice.setOnClickListener(this);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        startSpeechDialog();
    }

    public void startSpeechDialog(){
        //1. 创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(getActivity(), new MyInitListener()) ;
        //2. 设置accent、 language等参数
        mDialog.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
        mDialog.setParameter(SpeechConstant. ACCENT, "mandarin" );
        // 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后 onResult回调返回将是语义理解
        // 结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener( new MyRecognizerDialogListener()) ;
        //4. 显示dialog，接收语音输入
        mDialog.show() ;
        showTip("请说出您的命令");
        TextView txt=(TextView)mDialog.getWindow().getDecorView().findViewWithTag("textlink");
        txt.setText("");
    }
    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param results
         * @param isLast  是否说完了
         */
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String result = results.getResultString(); //为解析的
            showTip(result) ;
            System. out.println(" 没有解析的 :" + result);

            String text = JsonParser.parseIatResult(result) ;//解析过后的
            System. out.println(" 解析后的 :" + text);

            String sn = null;
            // 读取json结果中的 sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString()) ;
                sn = resultJson.optString("sn" );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text) ;//没有得到一句，添加到

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults .get(key));
            }

            //获取结果
        }
        @Override
        public void onError(SpeechError speechError) {
        }
    }
    class MyInitListener implements InitListener {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败 ");
            }

        }
    }
    private void showTip (String data) {
        Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show() ;
    }

    //更新Fragment的UI界面
    public void updataUI(){
            livingrom_light_num.setText(data);
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
}
