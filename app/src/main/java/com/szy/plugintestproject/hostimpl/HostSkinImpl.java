package com.szy.plugintestproject.hostimpl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.szy.plugininterfacesmodule.IPluginSkinConfig;
import com.szy.plugintestproject.R;

/**
 * Created by songzhiyang on 2019/1/3.
 *
 * @author songzhiyang
 */
public class HostSkinImpl implements IPluginSkinConfig{
    @Override
    public String getPluginName(Context context) {
        return context.getString(R.string.plugin_name);
    }

    @Override
    public Drawable getPluginIcon(Context context) {
        return context.getResources().getDrawable(R.drawable.ic_plugin_logo);
    }

    @Override
    public View getPluginLayoutView(final Context context) {
        View view = LayoutInflater.from(context).cloneInContext(context).inflate(R.layout.layout_host_skin_view,null,false);
        view.findViewById(R.id.btn_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"first",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
