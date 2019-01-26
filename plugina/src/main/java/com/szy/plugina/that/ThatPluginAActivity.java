package com.szy.plugina.that;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.szy.plugina.R;

/**
 * Created by songzhiyang on 2019/1/26.
 *
 * @author songzhiyang
 */
public class ThatPluginAActivity extends BaseThatActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        that.setContentView(R.layout.activity_that);

        that.findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(that,that.getString(R.string.plugin_name),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
