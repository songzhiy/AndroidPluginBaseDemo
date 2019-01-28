package com.szy.plugintestproject.that;

import android.util.Log;

import com.szy.plugininterfacesmodule.Constants;
import com.szy.plugininterfacesmodule.that.IActivityStackRecorder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by songzhiyang on 2019/1/28.
 *
 * @author songzhiyang
 */
public class ActivityStackRecorder {

    private static List<WeakReference<IActivityStackRecorder>> mActivityRecordList = new ArrayList<>();

    public static void dealActivityStack(IActivityStackRecorder iActivityStackRecorder) {
        mActivityRecordList.add(new WeakReference<IActivityStackRecorder>(iActivityStackRecorder));

        if (mActivityRecordList.size() == 1) {
            return;
        }

        int index = -1;
        switch (iActivityStackRecorder.getLaunchMode()) {
            case Constants.ThatConstants.THAT_INTENT_ACTIVITY_LAUNCH_MODE_SINGLE_TOP:
                Log.e("------","single top");
                WeakReference<IActivityStackRecorder> iActivityStackRecorderWeakReference = mActivityRecordList.get(mActivityRecordList.size() - 2);
                if (iActivityStackRecorderWeakReference.get() == null) {
                    mActivityRecordList.remove(iActivityStackRecorderWeakReference);
                    return;
                }
                if (iActivityStackRecorder.getClassName().equals(iActivityStackRecorderWeakReference.get().getClassName())) {
                    //上一个是相同的activity
                    mActivityRecordList.remove(iActivityStackRecorderWeakReference);
                    iActivityStackRecorderWeakReference.get().finish();
                    return;
                }
                break;
            case Constants.ThatConstants.THAT_INTENT_ACTIVITY_LAUNCH_MODE_SINGLE_TASK:
                Log.e("------","single task");
                for (int i = 0; i < mActivityRecordList.size() - 1; i++) {
                    if (mActivityRecordList.get(i).get() != null && iActivityStackRecorder.getClassName().equals(mActivityRecordList.get(i).get().getClassName())) {
                        index = i;
                    }
                }
                if (index != -1) {
                    WeakReference<IActivityStackRecorder> oldIActivityRecorder = mActivityRecordList.remove(index);
                    oldIActivityRecorder.get().finish();
                    return;
                }
                break;
            case Constants.ThatConstants.THAT_INTENT_ACTIVITY_LAUNCH_MODE_SINGLE_INSTANCE:
                Log.e("------","single instance");
                for (int i = 0; i < mActivityRecordList.size() - 1; i++) {
                    if (mActivityRecordList.get(i).get() != null && iActivityStackRecorder.getClassName().equals(mActivityRecordList.get(i).get().getClassName())) {
                        index = i;
                    }
                }
                for (int i = index; i < mActivityRecordList.size() - 1; i++) {
                    WeakReference<IActivityStackRecorder> oldIActivityRecorder = mActivityRecordList.remove(i);
                    oldIActivityRecorder.get().finish();
                }
                break;
            default:
                //standard
                Log.e("------","standard");
        }

    }

}
