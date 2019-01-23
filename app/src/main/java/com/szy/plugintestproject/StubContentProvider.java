package com.szy.plugintestproject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.szy.plugininterfacesmodule.Constants;

/**
 * Created by songzhiyang on 2019/1/23.
 *
 * @author songzhiyang
 */
public class StubContentProvider extends ContentProvider{
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e("------","走了中转的StubContentProvider");
        String uriStr = uri.toString();
        String authority = uri.getAuthority();
        String newUriStr = uriStr.replaceAll(authority, Constants.ContentProviderConstants.CONTENT_PROVIDER_PLUGIN_A_SCHEMA);
        Uri newUri = Uri.parse(newUriStr);
        return getContext().getContentResolver().delete(newUri,selection,selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
