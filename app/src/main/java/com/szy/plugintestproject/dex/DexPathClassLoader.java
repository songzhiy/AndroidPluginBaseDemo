package com.szy.plugintestproject.dex;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by songzhiyang on 2019/1/14.
 *
 * @author songzhiyang
 */
public class DexPathClassLoader extends PathClassLoader{

    List<DexClassLoader> pluginDexClassLoaderCache = new ArrayList<>();

    public DexPathClassLoader(String dexPath, ClassLoader parent) {
        super(dexPath, parent);
    }

    public void setDexClassLoader(DexClassLoader dexClassLoader) {
        pluginDexClassLoaderCache.add(dexClassLoader);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        try {
            // super中找到对应的class进行加载
            clazz = super.findClass(name);
        } catch (Exception e) {
        }
        if (clazz != null) {
            return clazz;
        }
        //super 没有找到 需要去DexClassLoader中加载
        for (DexClassLoader dexClassLoader : pluginDexClassLoaderCache) {
            clazz = dexClassLoader.loadClass(name);
            return clazz;
        }
        return clazz;
    }
}
