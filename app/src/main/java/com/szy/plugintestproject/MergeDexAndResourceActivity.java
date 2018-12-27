package com.szy.plugintestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.szy.plugininterfacesmodule.IPluginConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * Created by songzhiyang on 2018/12/24.
 *
 * @author songzhiyang
 */
public class MergeDexAndResourceActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_merge_dex_and_resource);

        findViewById(R.id.btn_load_apk_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //该方案为合并dex，将插件中的dex直接合并到宿主的dex中进行调用
                File pluginApkPath = new File(getFilesDir(),"plugina.apk");
                //这个貌似是插件dex的输出路径
                File outputPluginDexFile = new File(getFilesDir(),"plugina.dex");
                if (pluginApkPath.exists()) {
                    //如果插件存在的情况
                    PathClassLoader pathClassLoader = (PathClassLoader) getClassLoader();

                    Field pathListField = null;
                    Object pathListObj = null;
                    Field elementsFiled = null;
                    Object[] elements = null;
                    try {
                        //反射pathList  这里需要注意getDeclaredFiled和getField的区别 前者是只要声明了就能拿到 后者是只能拿public的
                        pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
                        pathListField.setAccessible(true);
                        pathListObj = pathListField.get(pathClassLoader);

                        //获取DexPathList中的Elements
                        Class dexPathListClazz = Class.forName("dalvik.system.DexPathList");
                        elementsFiled = dexPathListClazz.getDeclaredField("dexElements");
                        elementsFiled.setAccessible(true);
                        elements = (Object[]) elementsFiled.get(pathListObj);

                        //构建新的elements对象
//                        public Element(File dir, boolean isDirectory, File zip, DexFile dexFile)
                        Class elementClazz = elements.getClass().getComponentType();//获取数组内的对象class类型
                        Class[] paramsTypeClass = new Class[]{File.class,boolean.class,File.class, DexFile.class};
                        Object[] params = new Object[]{pluginApkPath,false,pluginApkPath,DexFile.loadDex(pluginApkPath.getAbsolutePath(),outputPluginDexFile.getAbsolutePath(),0)};

                        Object newElement = elementClazz.getConstructor(paramsTypeClass).newInstance(params);
                        Object[] newObjects = new Object[]{newElement};

                        //copy elements
                        Object[] newElements = (Object[]) Array.newInstance(elementClazz,elements.length+1);
                        System.arraycopy(elements,0,newElements,0,elements.length);
                        System.arraycopy(newObjects,0,newElements,elements.length,newObjects.length);
                        //将新的elements放置回pathlist中
                        elementsFiled.set(pathListObj,newElements);

                        //测试加载插件中合并过后的dex
                        IPluginConfig pluginAConfig = (IPluginConfig) getClassLoader().loadClass("com.szy.plugina.PluginAManager").newInstance();
                        Log.e("------",pluginAConfig.getPluginName());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("------","插件不存在，请稍后再试");
                }
            }
        });

        findViewById(R.id.btn_load_host_apk_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
