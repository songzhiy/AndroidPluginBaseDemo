package com.szy.plugintestproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.szy.plugininterfacesmodule.IPluginConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        //将assets目录下的包替换到data/data/files目录下 assets目录下的文件只能open出一个inputstream
        InputStream pluginInputStream = null;
        FileOutputStream copyOutputStream = null;
        try {
            pluginInputStream = getAssets().open("plugina-release-unsigned.apk");
            File pluginAFile = new File(getFilesDir(),"plugina.apk");
            copyOutputStream = new FileOutputStream(pluginAFile);

            byte[] readArray = new byte[1024];
            int totalRead = 0;
            while ((totalRead = pluginInputStream.read(readArray)) != -1) {
                copyOutputStream.write(readArray,0,totalRead);
                copyOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pluginInputStream != null) {
                try {
                    pluginInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (copyOutputStream != null) {
                try {
                    copyOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.load_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(MainActivity.this.getFilesDir(),"plugina.apk");
                if (file.exists()) {
                    File pluginAFile = MainActivity.this.getFileStreamPath("plugina.apk");
                    String dexPath = pluginAFile.getPath();
                    File dexReleaseFile = MainActivity.this.getDir("dex",Context.MODE_PRIVATE);
                    DexClassLoader dexClassLoader = new DexClassLoader(dexPath,dexReleaseFile.getAbsolutePath(),null,getClassLoader());

                    try {
                        Class pluginAManagerClazz = dexClassLoader.loadClass("com.szy.plugina.PluginAManager");
                        IPluginConfig pluginAConfig = (IPluginConfig) pluginAManagerClazz.newInstance();
                        Toast.makeText(MainActivity.this,pluginAConfig.getPluginName(),Toast.LENGTH_SHORT).show();
                        pluginAConfig.updatePluginName("MainActivity");
                        Toast.makeText(MainActivity.this,pluginAConfig.getPluginName(),Toast.LENGTH_SHORT).show();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this,"有了 ---- " + file.length(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,"没有",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
