package com.szy.plugintestproject;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.szy.plugininterfacesmodule.IPluginConfig;
import com.szy.plugininterfacesmodule.IPluginSkinConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * Created by songzhiyang on 2018/12/24.
 *
 * @author songzhiyang
 */
public class MergeDexAndResourceActivity extends BaseActivity{

    private FrameLayout mFlPluginContainer = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_merge_dex_and_resource);

        mFlPluginContainer = findViewById(R.id.fl_container);

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

                try {
                    String pluginApkFilePath = getDexPath("plugina.apk");
                    //构造AssetManager
                    AssetManager newAssetManager = AssetManager.class.newInstance();
                    Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
                    //将插件的资源路径加入到AssetManager
                    addAssetPathMethod.invoke(newAssetManager,pluginApkFilePath);
                    //将宿主的资源路径加入到AssetManager中
                    addAssetPathMethod.invoke(newAssetManager,getPackageResourcePath());
                    //构造Resource对象
                    Resources newResource = new Resources(newAssetManager,getResources().getDisplayMetrics(),getResources().getConfiguration());
//                    mResources = newResource;
                    //存储newResource对象
                    //替换ContextImpl中的Resource对象
                    Field contextImplResourceField = getBaseContext().getClass().getDeclaredField("mResources");
                    contextImplResourceField.setAccessible(true);
                    contextImplResourceField.set(getBaseContext(),newResource);
                    //替换ContextImpl中的mPackageInfo(LoadedApk)对象的Resource对象
                    Field packageInfoField = getBaseContext().getClass().getDeclaredField("mPackageInfo");
                    packageInfoField.setAccessible(true);
                    Object packageInfoObj = packageInfoField.get(getBaseContext());
                    Field packageInfoResourceFiled = packageInfoObj.getClass().getDeclaredField("mResources");
                    packageInfoResourceFiled.setAccessible(true);
                    packageInfoResourceFiled.set(packageInfoObj,newResource);

                    // TODO: 2018/12/28 书上说 如果不把ContextImpl中的Theme设置为null，会有问题 需要测试一下
                    Field contextImplThemeField = getBaseContext().getClass().getDeclaredField("mTheme");
                    contextImplThemeField.setAccessible(true);
                    contextImplThemeField.set(getBaseContext(),null);

                    //测试代码 加载pluginA里面的皮肤的东西
                    IPluginSkinConfig pluginAConfig = (IPluginSkinConfig) getClassLoader().loadClass("com.szy.plugina.PluginASkinImpl").newInstance();
                    //这里如果要使用Activity的context的话，需要hook掉activity内的resources对象
                    Log.e("------",pluginAConfig.getPluginName(getBaseContext()));
                    ImageView pluginImageView = new ImageView(getBaseContext());
                    pluginImageView.setBackgroundDrawable(pluginAConfig.getPluginIcon(getBaseContext()));
                    mFlPluginContainer.addView(pluginImageView);
                    //这里发生了资源冲突的问题 导致了加载错布局的情况 其实就是和R文件生成的id有关 当id一样的时候 就会发生资源冲突的情况 导致加载错布局
                    //这里的解决方案是修改了aapt 这里是基于buildTools 26.0.2进行了修改 
                    // Q: 2019/1/3 尚未解决的问题 为什么getBaseContext的时候无效
                    // A: 因为Inflater里面使用的并非是传递进去的context，所以需要hook掉，采用替换context的方式为clone一个LayoutInflater
                    mFlPluginContainer.addView(pluginAConfig.getPluginLayoutView(getBaseContext()));

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_load_host_apk_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

//    private Resources mResources = null;
//
//    @Override
//    public Resources getResources() {
//        if (mResources != null) {
//            return mResources;
//        }
//        return super.getResources();
//    }
}
