package com.szy.plugina;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.szy.plugininterfacesmodule.IPluginSkinConfig;

/**
 * Created by songzhiyang on 2018/12/19.
 *
 * @author songzhiyang
 */
public class PluginASkinImpl implements IPluginSkinConfig{
    @Override
    public String getPluginName(Context context) {
        if (context == null) {
            return "";
        }
        return context.getResources().getString(R.string.plugin_name);
    }

    @Override
    public Drawable getPluginIcon(Context context) {
        if (context == null) {
            return null;
        }
        return context.getResources().getDrawable(R.drawable.ic_plugin_logo);
    }

    @Override
    public View getPluginLayoutView(final Context context) {
        View pluginView = LayoutInflater.from(context).cloneInContext(context).inflate(R.layout.layout_plugin_a,null,false);
//        pluginView.findViewById(R.id.btn_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"first",Toast.LENGTH_SHORT).show();
//            }
//        });
        Log.e("-----",pluginView.toString());
        return pluginView;
    }
}
