package com.wpf.citychoose.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.wpf.citychoose.R;

/**
 * Created by 王朋飞 on 7-19-0019.
 * 选择城市View
 */

public class ChooseCityView extends LinearLayout {

    public ChooseCityView(Context context) {
        this(context,null);
    }

    public ChooseCityView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChooseCityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.view_choose_city,this,false);

        addView(view);
    }
}
