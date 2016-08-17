package com.wpf.citychoose.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wpf.citychoose.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王朋飞 on 7-20-0020.
 * 查询适配器
 */

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.MyViewHolder> {

    private List<String> cityList = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(cityList.get(position));
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView_item);
        }
    }

    public void setCityList(List<String> cityList) {
        this.cityList = cityList;
        notifyDataSetChanged();
    }
}
