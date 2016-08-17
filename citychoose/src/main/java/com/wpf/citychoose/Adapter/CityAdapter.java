package com.wpf.citychoose.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.wpf.citychoose.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王朋飞 on 7-19-0019.
 * 城市适配器
 */


public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> implements
        StickyRecyclerHeadersAdapter<CityAdapter.MyHeaderViewHolder> {
    private List<String> cityList = new ArrayList<>();
    public CityAdapter(List<String> cityList) {
        this.cityList = cityList;
    }

    @Override
    public long getHeaderId(int position) {
        return cityList.get(position).split(",")[0].charAt(0);
    }

    @Override
    public MyHeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new MyHeaderViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_header,parent,false));
    }

    @Override
    public void onBindHeaderViewHolder(MyHeaderViewHolder holder, int position) {
        holder.textView_Header.setText(cityList.get(position).split(",")[0]);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView_Item.setText(cityList.get(position).split(",")[1]);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_Item;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView_Item = (TextView) itemView.findViewById(R.id.textView_item);
        }
    }

    class MyHeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_Header;
        public MyHeaderViewHolder(View itemView) {
            super(itemView);
            textView_Header = (TextView) itemView.findViewById(R.id.textView_header);
        }
    }
}
