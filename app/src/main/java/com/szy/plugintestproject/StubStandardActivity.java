package com.szy.plugintestproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by songzhiyang on 2019/1/9.
 *
 * @author songzhiyang
 */
public class StubStandardActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stub_standard);
    }
}
