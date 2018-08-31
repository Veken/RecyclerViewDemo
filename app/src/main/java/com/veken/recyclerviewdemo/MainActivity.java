package com.veken.recyclerviewdemo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private CountDownAdapter adapter;
    private long currentTime;
    private List<CountDownBean> mList;
    private Random random;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //局部刷新
                    adapter.notifyText();
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        refreshLayout = findViewById(R.id.refreshLayout);
        initData();
        Date date = new Date();
        //设置日期格式
        currentTime = date.getTime();
        adapter = new CountDownAdapter(R.layout.recycler_item_countdown,mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        CountDownThread countDownThread = new CountDownThread();
        new Thread(countDownThread).start();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        //设置 Header 为 默认 样式
        refreshLayout.setRefreshHeader(new ClassicsHeader(this).setEnableLastTime(true));
        //设置 Footer 为默认 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                initData();
                adapter.notifyDataSetChanged();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore();
                refreshLayout.finishLoadMore();
            }
        });

    }

    /**
     * 加载更多
     */
    private void loadMore() {
        List<CountDownBean> list = new ArrayList<>();
        for(int i = 0;i<10;i++) {
            CountDownBean countDownBean = new CountDownBean();
            countDownBean.setStartTime(currentTime - 1000*random.nextInt(100));
            countDownBean.setEndTime(currentTime +  1000*random.nextInt(100));
            countDownBean.setCountDownTime(countDownBean.getEndTime() - countDownBean.getStartTime());
            list.add(countDownBean);
        }
        mList.addAll(list);
        adapter.notifyLoadMoreToLoading();
    }


    private void initData() {
        if(mList==null){
            mList = new ArrayList<>();
        }
        random = new Random();
        for(int i=0;i<20;i++){
            CountDownBean countDownBean = new CountDownBean();
            countDownBean.setStartTime(currentTime-1000*random.nextInt(10));
            countDownBean.setEndTime(currentTime+1000*random.nextInt(20));
            countDownBean.setCountDownTime(countDownBean.getEndTime()-countDownBean.getStartTime());
            mList.add(countDownBean);
        }
    }

    private class CountDownThread implements Runnable{
        boolean stopThread = false;
        int count;
        @Override
        public void run() {
            while (!stopThread) {
                try {
                    if(count==mList.size()){
                        stopThread = true;
                        return;
                    }
                    Thread.sleep(1000);
                    long countDownTime = 0;
                    for (int i = 0; i < mList.size(); i++) {
                        if(mList.get(i).getCountDownTime()==0)continue;
                        countDownTime = mList.get(i).getCountDownTime();
                        if (countDownTime >= 1000) {
                            mList.get(i).setCountDownTime(countDownTime - 1000);
                            if(mList.get(i).getCountDownTime()==0){
                                count++;
                            }
                        }
                    }
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
