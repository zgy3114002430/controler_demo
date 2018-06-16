package com.example.administrator.controler_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 爱我请给我二十块 on 2018/3/14.
 */

public class CustomAdapter extends BaseAdapter{

    private List<String> name=null;
    private int[] imageId1=null;
    private int[] imageId2=null;
    private int[] itembackground=null;
    private LayoutInflater myLayoutInflater=null;
    private Context context=null;

    public CustomAdapter(List<String> name, int[] imageId1, int[] imageId2, int[] itembackground, Context context){
        this.context=context;
        this.name=name;
        this.imageId1=imageId1;
        this.imageId2=imageId2;
        this.itembackground=itembackground;
        this.myLayoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int count=0;
        if(imageId1==null||name==null||imageId2==null||itembackground==null){
            return count;
        }
        else
            return name.size();
    }

    @Override
    public Object getItem(int position) {

        return position;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder=null;
        if(view==null){
            holder=new ViewHolder();
            view=myLayoutInflater.inflate(R.layout.item_livingrom,null);
            holder.image1=(ImageView)view.findViewById(R.id.device_fan);
            holder.image2=(ImageView)view.findViewById(R.id.livingrom_light);
            holder.textview=(TextView)view.findViewById(R.id.name_device);
            holder.layout=(RelativeLayout)view.findViewById(R.id.device_background);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        holder.image1.setImageResource(imageId1[position]);
        holder.image2.setImageResource(imageId2[position]);
        holder.textview.setText(name.get(position));
        holder.layout.setBackgroundResource(itembackground[position]);
        return view;
    }

    private static class ViewHolder{
        public TextView textview=null;
        public ImageView image1=null;
        public ImageView image2=null;
        public RelativeLayout layout=null;
    }
}
