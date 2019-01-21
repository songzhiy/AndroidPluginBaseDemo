package com.szy.plugintestproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by songzhiyang on 2019/1/21.
 *
 * @author songzhiyang
 */
public class StubService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
