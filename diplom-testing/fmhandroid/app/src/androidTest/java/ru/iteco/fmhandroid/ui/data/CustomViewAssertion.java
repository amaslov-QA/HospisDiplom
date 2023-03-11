package ru.iteco.fmhandroid.ui.data;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;


import java.util.List;

public class CustomViewAssertion {


    public static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!((List<?>) tasks).isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}