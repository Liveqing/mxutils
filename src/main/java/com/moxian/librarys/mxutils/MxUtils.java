package com.moxian.librarys.mxutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 获取系统和手机硬件的信息
 *
 * @author LQ
 * @date 2016年9月7日 11:16:30
 */
public class MxUtils {
    public static String TAG;
    /** DEBUG 是否调试模式 （调试模式会开启日志打印）*/
    public static boolean DEBUG = false;
    /**标识是否打开网络请求日志记录*/
    private static boolean openRequestLog = false;

    private final static String logTag = "moxian2_log";

    private final static String requestTag = "request_log_";
    /**标识是否正在删除日志文件*/
    private static boolean clearExpireFilesFlag = true;
    private static Context mApplicationContent;

    public static void initialize(Application app) {
        mApplicationContent = app.getApplicationContext();
    }


    public static void setDebug(boolean DEBUG, String TAG) {
        MxUtils.TAG = TAG;
        MxUtils.DEBUG = DEBUG;
    }


    /**
     * 日志文件缓存目录
     */
    private static String logCacheDir = Environment.getExternalStorageDirectory()
            + File.separator + logTag + File.separator;

    /**
     * @param cacheDir 日志文件缓存目录
     */
    public static void setLogCacheDir(String cacheDir) {
        logCacheDir = cacheDir;
        if (DEBUG) {
            createCacheDir();
        }
    }

    private static void createCacheDir() {
        File file = new File(logCacheDir);
        if (!file.exists()) {
            file.mkdir();
        } else {
            deleteExpireFiles(logCacheDir, requestTag, 7);
        }
    }

    /**
     * 是否为调试模式
     *
     * @return
     */
    public static boolean DEBUG() {
        return DEBUG;
    }

    /**
     * 设置是否为调试模式
     *
     * @param debug
     */
    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

	/*public static void v(String msg){
        if(DEBUG)
			Log.v(logTag, msg);
	}
	
	public static void v(String tag,String msg) {
		if (DEBUG)
			Log.v(tag, msg);
	}*/

    public static void i(String msg) {
        if (DEBUG)
            Log.i(logTag, msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void w(String msg) {
        if (DEBUG)
            Log.w(logTag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static void e(String msg) {
        if (DEBUG)
            Log.e(logTag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void d(String msg) {
        if (DEBUG)
            Log.d(logTag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    /**
     * 写日志文件
     *
     * @param tag
     * @param text
     */
    public static void writeLogtoFile(String tag, String text) {
        if (DEBUG) {
            System.out.println(text);
            createCacheDir();
            String time = MxTimeUtils.getCurrentTime(MxTimeUtils.TimeFormatType
                    .TIME_FOEMAT_STANDARD);
            String needWriteMessage = time + "    " + tag + "    " + text;
            File file = new File(logCacheDir, "mx_log_" + MxTimeUtils.getCurrentTime(MxTimeUtils
                    .TimeFormatType.TIME_FOEMAT_Y_M_D) + ".txt");
            try {
                FileWriter filerWriter = new FileWriter(file, true);
                BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write(needWriteMessage);
                bufWriter.newLine();
                bufWriter.close();
                filerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return void
     * @Title: writeFluxLogtoFile
     * @param:
     * @Description: 流量监听日志
     */
    public static void writeFluxLogtoFile(String text) {
        if (DEBUG) {
            System.out.println(text);
            createCacheDir();
            String needWriteMessage = text + "\r\n";
            File file = new File(logCacheDir, "mxflux.txt");
            if (file.exists() && file.length() > 200000) {
                file.delete();
            }
            try {
                FileWriter filerWriter = new FileWriter(file, true);
                BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write(needWriteMessage);
                bufWriter.newLine();
                bufWriter.close();
                filerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeCommonLogtoFile(String text) {
        if (DEBUG) {
            System.out.println(text);
            createCacheDir();
            String needWriteMessage = text + "\r\n";
            File file = new File(logCacheDir, "mxcommon.txt");
            if (file.exists() && file.length() > 200000) {
                file.delete();
            }
            try {
                FileWriter filerWriter = new FileWriter(file, true);
                BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write(needWriteMessage);
                bufWriter.newLine();
                bufWriter.close();
                filerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写日志文件
     *
     * @param text
     */
    public static void writeRequestLogtoFile(String text) {
        if (DEBUG) {
            System.out.println(text);
            if (openRequestLog) {
                createCacheDir();
                String needWriteMessage = text + "\r\n";
                File file = new File(logCacheDir, requestTag + MxTimeUtils.getCurrentTime
                        (MxTimeUtils
                        .TimeFormatType.TIME_FOEMAT_Y_M_D) + ".txt");
                try {
                    FileWriter filerWriter = new FileWriter(file, true);
                    BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                    bufWriter.write(needWriteMessage);
                    bufWriter.newLine();
                    bufWriter.close();
                    filerWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void deleteExpireFiles(final String dir, final String tag, final int timeLen) {
        if (clearExpireFilesFlag) {
            clearExpireFilesFlag = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    MxFileUtils.deleteFilesInDirectory(dir, tag, timeLen);
                }

            }).start();
        }
    }

    /**
     * dp转px
     */
    public static int dip2px(float dpValue) {
        final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * px转dp
     */
    public static int px2dip(float pxValue) {
        final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
        return dm.heightPixels - getStatusBarHeight();
    }

    /**
     * 取屏幕高度包含状态栏高度
     *
     * @return
     */
    public static int getScreenHeightWithStatusBar() {
        DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 取导航栏高度
     *
     * @return
     */
    public static int getNavigationBarHeight() {
        int result = 0;
        int resourceId = mApplicationContent.getResources().getIdentifier
                ("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mApplicationContent.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = mApplicationContent.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = mApplicationContent.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getActionBarHeight() {
        int actionBarHeight = 0;

        final TypedValue tv = new TypedValue();
        if (mApplicationContent.getTheme()
                .resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, mApplicationContent.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }


    /**
     * 关闭输入法
     *
     * @param act
     */
    public static void closeInputMethod(Activity act) {
        View view = act.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) mApplicationContent.getSystemService(Context
                    .INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager
                            .HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 判断应用是否处于后台状态
     *
     * @return
     */
    public static boolean isBackground() {
        ActivityManager am = (ActivityManager) mApplicationContent.getSystemService(Context
                .ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mApplicationContent.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 复制文本到剪贴板
     *
     * @param text
     */
    public static void copyToClipboard(String text) {
        if (Build.VERSION.SDK_INT >= 11) {
            ClipboardManager cbm = (ClipboardManager) mApplicationContent.getSystemService
                    (Activity.CLIPBOARD_SERVICE);
            cbm.setPrimaryClip(ClipData.newPlainText(mApplicationContent.getPackageName(), text));
        } else {
            android.text.ClipboardManager cbm = (android.text.ClipboardManager)
                    mApplicationContent.getSystemService(Activity.CLIPBOARD_SERVICE);
            cbm.setText(text);
        }
    }

    /**
     * 经纬度测距
     *
     * @param jingdu1
     * @param weidu1
     * @param jingdu2
     * @param weidu2
     * @return
     */
    public static double distance(double jingdu1, double weidu1, double jingdu2, double weidu2) {
        double a, b, R;
        R = 6378137; // 地球半径  
        weidu1 = weidu1 * Math.PI / 180.0;
        weidu2 = weidu2 * Math.PI / 180.0;
        a = weidu1 - weidu2;
        b = (jingdu1 - jingdu2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * R
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(weidu1)
                * Math.cos(weidu2) * sb2 * sb2));
        return d;
    }

    /**
     * 获取手机卡的IMEI
     *
     * @return
     */
    public static String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) mApplicationContent
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        MxUtils.i(TAG, " IMEI：" + imei);
        return imei;
    }

    /**
     * 获取用户手机号国家简码
     *
     * @return String
     * @Title: GetCountryID
     * @param:
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static String GetCountryID() {
        String CountryID = "";
        TelephonyManager manager = (TelephonyManager) mApplicationContent.getSystemService
                (Context.TELEPHONY_SERVICE);
        if (manager != null && manager.getSimCountryIso() != null) {
            CountryID = manager.getSimCountryIso().toUpperCase();
        }
        return CountryID;
    }

    /**
     * 取APP版本号
     *
     * @return
     */
    public static int getAppVersionCode() {
        try {
            PackageManager mPackageManager = mApplicationContent.getPackageManager();
            PackageInfo _info = mPackageManager.getPackageInfo(mApplicationContent.getPackageName
                    (), 0);
            return _info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 取APP版本名
     *
     * @return
     */
    public static String getAppVersionName() {
        try {
            PackageManager mPackageManager = mApplicationContent.getPackageManager();
            PackageInfo _info = mPackageManager.getPackageInfo(mApplicationContent.getPackageName
                    (), 0);
            return _info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }


    public static String MD5(byte[] data) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(data);
        byte[] m = md5.digest();//加密
        return Base64.encodeToString(m, Base64.DEFAULT);
    }

    public static Uri getUriFromRes(int id) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + mApplicationContent.getResources().getResourcePackageName(id) + "/"
                + mApplicationContent.getResources().getResourceTypeName(id) + "/"
                + mApplicationContent.getResources().getResourceEntryName(id));
    }

}
