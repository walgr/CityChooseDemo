package com.wpf.citychoose;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.wpf.citychoose.Adapter.CityAdapter;
import com.wpf.citychoose.Listener.RecyclerItemClickListener;
import com.wpf.citychoose.Location.BaiDuLocation;
import com.wpf.citychoose.View.DividerDecoration;
import com.wpf.requestpermission.RequestPermission;
import com.wpf.tabview.TabView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ChooseCityActivity extends AppCompatActivity implements
        View.OnClickListener,
        View.OnTouchListener,
        OnQuickSideBarTouchListener {

    private SearchView searchView;
    private QuickSideBarView quickSideBarView;
    private QuickSideBarTipsView quickSideBarTipsView;
    private TabView tabView_GPS,tabView_1,tabView_2,tabView_3,tabView_4;
    private RecyclerViewHeader header;
    private RecyclerView recyclerView;
    private StickyRecyclerHeadersDecoration srhd;
    private CityAdapter adapter;
    private LinearLayout quickSidBar;
    private RequestPermission requestPermission;
    private boolean isTouch;
    private List<String> cityList = new ArrayList<>();
    private HashMap<String,Integer> letters = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_choose_city);
        init();
    }

    private void init() {
        //initView
//        searchView = (SearchView) findViewById(R.id.searchView);
        tabView_GPS = (TabView) findViewById(R.id.tab_gps);
        tabView_1 = (TabView) findViewById(R.id.tab_1);
        tabView_2 = (TabView) findViewById(R.id.tab_2);
        tabView_3 = (TabView) findViewById(R.id.tab_3);
        tabView_4 = (TabView) findViewById(R.id.tab_4);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        quickSideBarView = (QuickSideBarView) findViewById(R.id.quickSideBarView);
        quickSideBarTipsView = (QuickSideBarTipsView) findViewById(R.id.quickSideBarTipsView);
        quickSidBar = (LinearLayout) findViewById(R.id.quickSidBar);
        //getData
        String[] hotCity = getHotCity();
        cityList = getCityList();
        getLetters();
//        searchView.setHint("请输入城市");
//        final SearchAdapter searchAdapter = new SearchAdapter(this, getSuggestItemList(cityList));
//        searchView.setAdapter(searchAdapter);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchAdapter.getFilter().filter(newText);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//        });

        tabView_GPS.setOnClickListener(this);
        tabView_1.setOnClickListener(this);
        tabView_2.setOnClickListener(this);
        tabView_3.setOnClickListener(this);
        tabView_4.setOnClickListener(this);

        setAllTabColor(new int[]{getResourcesColor(R.color.colorPrimary),
                getResourcesColor(R.color.colorPrimary),
                getResourcesColor(R.color.colorPrimary),
                getResourcesColor(R.color.colorPrimary),
                getResourcesColor(R.color.colorPrimary)});

        tabView_GPS.setText("正在定位");
        tabView_GPS.setImageView(getResources().getDrawable(R.drawable.ic_location));

        tabView_1.setText(hotCity[0]);
        tabView_2.setText(hotCity[1]);
        tabView_3.setText(hotCity[2]);
        tabView_4.setText(hotCity[3]);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = srhd.findHeaderPositionUnder(0,0);
                if(quickSidBar.getVisibility() == View.GONE) {
                    if(position == 0 && isTouch) quickSidBar.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    quickSidBar.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setOnTouchListener(this);
        adapter = new CityAdapter(cityList);
        recyclerView.setAdapter(adapter);
        srhd = new StickyRecyclerHeadersDecoration(adapter);

        recyclerView.addItemDecoration(new DividerDecoration(this));
        recyclerView.addItemDecoration(srhd);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                returnCityName(cityList.get(position).split(",")[1]);
            }
        }));

        header = (RecyclerViewHeader) findViewById(R.id.header);
        header.attachTo(recyclerView);
        quickSideBarView.setOnQuickSideBarTouchListener(this);
        getGps();
    }

    private void getGps() {
        requestPermission = new RequestPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION,1) {
            @Override
            public void onSuccess() {
                getLocation();
            }

            @Override
            public void onFail(String[] failList) {
                tabView_GPS.setText("定位失败,权限不足");
            }
        };
    }

    private void getLocation() {
        new BaiDuLocation(ChooseCityActivity.this) {
            @Override
            public void locationSuccess(String cityName) {
                if(cityName == null || cityName.isEmpty()) cityName = "定位失败";
                tabView_GPS.setText(cityName);
            }
        };
    }

    private List<SearchItem> getSuggestItemList(List<String> strings) {
        List<SearchItem> searchItemList = new ArrayList<>();
        for(String str : strings) {
            searchItemList.add(new SearchItem(str.split(",")[1]));
        }
        return searchItemList;
    }

    private List<String> getCityList() {
        List<String> cityList = new ArrayList<>();
        cityList.addAll(Arrays.asList(getResources().getStringArray(R.array.cityList)));
        return cityList;
    }

    private void getLetters() {
        int i = 0;
        for(String str : cityList) {
            letters.put(str.split(",")[0],i);
            i++;
        }
    }

    private int getResourcesColor(int colorId) {
        return getResources().getColor(colorId);
    }

    private void setAllTabColor(int[] colors) {
        if(colors.length < 5) return;
        tabView_GPS.setBackColor(colors[0]);
        tabView_1.setBackColor(colors[1]);
        tabView_2.setBackColor(colors[2]);
        tabView_3.setBackColor(colors[3]);
        tabView_4.setBackColor(colors[4]);
        tabView_GPS.setTextColor(Color.WHITE);
        tabView_1.setTextColor(Color.WHITE);
        tabView_2.setTextColor(Color.WHITE);
        tabView_3.setTextColor(Color.WHITE);
        tabView_4.setTextColor(Color.WHITE);
    }

    private String[] getHotCity() {
        return getResources().getStringArray(R.array.hot_city);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tab_gps || i == R.id.tab_1 || i == R.id.tab_2 || i == R.id.tab_3 || i == R.id.tab_4)
            returnCityName(((TabView)view).getText());
    }

    private void returnCityName(String cityName) {
        if("定位失败".equals(cityName))  {
            tabView_GPS.setText("正在重新定位");
            getLocation();
            return;
        } else if("正在重新定位".equals(cityName)) {
            return;
        }
        Intent intent = getIntent();
        intent.putExtra("CityName",cityName);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onLetterChanged(String letter, int position, float y) {
        quickSideBarTipsView.setText(letter, position, y);
        //有此key则获取位置并滚动到该位置
        if(letters.containsKey(letter)) {
            recyclerView.scrollToPosition(letters.get(letter));
        }
    }

    @Override
    public void onLetterTouching(boolean touching) {
        quickSideBarTipsView.setVisibility(touching? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        isTouch = true;
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        requestPermission.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
