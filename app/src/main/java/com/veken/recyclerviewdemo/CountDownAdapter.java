package com.veken.recyclerviewdemo;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Veken on 2018/8/30 14:54
 *
 * @desc
 */

public class CountDownAdapter extends BaseQuickAdapter<CountDownBean,BaseViewHolder> {

    private List<CountDownBean> mList;
    private List<BaseViewHolder> mViewHolder = new ArrayList<>();
    public CountDownAdapter(int layoutResId, @Nullable List<CountDownBean> data) {
        super(layoutResId, data);
        mList = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, CountDownBean item) {
        mViewHolder.add(holder);
        ((TextView)holder.getView(R.id.tv_count_down_time)).setText("倒计时："+DataFormatUtil.countDownTime(item.getCountDownTime()));
    }

    public void notifyText(){
        for(int i = 0;i<mViewHolder.size();i++){
            if(mViewHolder.get(i).getLayoutPosition()>=0){
                ((TextView)mViewHolder.get(i).getView(R.id.tv_count_down_time)).setText("倒计时："+DataFormatUtil.countDownTime(mList.get(mViewHolder.get(i).getLayoutPosition()).getCountDownTime()));
            }
        }
    }
}
