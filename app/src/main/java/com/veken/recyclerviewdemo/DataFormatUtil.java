package com.veken.recyclerviewdemo;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Veken on 2018/8/30 15:18
 *
 * @desc
 */

public class DataFormatUtil {

    public static String countDownTime(long countDownTime){
        long days = countDownTime / (1000 * 60 * 60 * 24);
        long hours = (countDownTime-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (countDownTime-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        long second = (countDownTime-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*(1000*60))/1000;
        //并保存在商品time这个属性内
        String finaltime = days + "天" + hours + "时" + minutes + "分" + second + "秒";
        return finaltime;
    }
}
