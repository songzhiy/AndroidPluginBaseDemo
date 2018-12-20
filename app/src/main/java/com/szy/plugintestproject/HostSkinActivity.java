package com.szy.plugintestproject;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.szy.plugininterfacesmodule.IPluginSkinConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by songzhiyang on 2018/12/19.
 *
 * @author songzhiyang
 */
public class HostSkinActivity extends BaseActivity {

    private Resources mPluginResource = null;

    TextView mTvPluginName = null;
    ImageView mIvPluginIcon = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_host_skin);

        mTvPluginName = findViewById(R.id.tv_plugin_name);
        mIvPluginIcon = findViewById(R.id.iv_plugin_icon);

        findViewById(R.id.btn_load_skin_plugin_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initResource("plugina.apk");
                DexClassLoader dexClassLoader = loadPluginApk("plugina.apk");
                updatePluginNameAndIcon(dexClassLoader);
            }
        });

        findViewById(R.id.btn_load_skin_plugin_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initResource("pluginb.apk");
                DexClassLoader dexClassLoader = loadPluginApk("pluginb.apk");
                updatePluginNameAndIcon(dexClassLoader);
            }
        });
    }

    private void updatePluginNameAndIcon(DexClassLoader dexClassLoader) {
        try {
            Class IPluginSkinConfigImplClazz = dexClassLoader.loadClass("com.szy.plugina.PluginASkinImpl");
            IPluginSkinConfig pluginSkinConfigAImpl = (IPluginSkinConfig) IPluginSkinConfigImplClazz.newInstance();
            String pluginName = pluginSkinConfigAImpl.getPluginName(HostSkinActivity.this);
            Drawable pluginIcon = pluginSkinConfigAImpl.getPluginIcon(HostSkinActivity.this);
            mTvPluginName.setText(pluginName);
            mIvPluginIcon.setBackgroundDrawable(pluginIcon);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void initResource(String pluginApkName) {
        try {
            //添加dexpath到AssetManager中
            AssetManager assetManager = AssetManager.class.newInstance();
            Class[] addAssetsMethodParamsClazz = new Class[]{String.class};
            Method addAssetsMethod = AssetManager.class.getMethod("addAssetPath", addAssetsMethodParamsClazz);
            String dexPath = getDexPath(pluginApkName);
            addAssetsMethod.invoke(assetManager, dexPath);
            //构造Resource对象
            Resources resources = new Resources(assetManager, super.getResources().getDisplayMetrics(), super.getResources().getConfiguration());
            mPluginResource = resources;
            Resources.Theme pluginThem = mPluginResource.newTheme();
            pluginThem.setTo(super.getTheme());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {
        if (mPluginResource == null) {
            return super.getResources();
        }
        return mPluginResource;
    }
}
