package com.szy.plugina;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by songzhiyang on 2019/1/22.
 *
 * @author songzhiyang
 */
public class PluginABroadcastReceiver2 extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("------","PluginABroadcastReceiver2 -- 调用成功");
    }
}
