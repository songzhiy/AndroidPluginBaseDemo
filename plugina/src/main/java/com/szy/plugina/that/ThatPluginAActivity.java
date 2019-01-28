package com.szy.plugina.that;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.szy.plugina.R;
import com.szy.plugininterfacesmodule.Constants;

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
                Intent intent = null;
                try {
                    intent = new Intent(that, that.getClassLoader().loadClass("com.szy.plugintestproject.that.ActivityProxy"));
                    intent.putExtra(Constants.ThatConstants.THAT_INTENT_PLUGIN_NAME,"plugina.apk");
                    intent.putExtra(Constants.ThatConstants.THAT_INTENT_PLUGIN_ACTIVITY_CLASS,"com.szy.plugina.that.ThatPluginAActivity");
                    intent.putExtra(Constants.ThatConstants.THAT_INTENT_ACTIVITY_LAUNCH_MODE,Constants.ThatConstants.THAT_INTENT_ACTIVITY_LAUNCH_MODE_SINGLE_TOP);
                    that.startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
