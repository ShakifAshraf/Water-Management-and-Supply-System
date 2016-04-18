package app.test;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This demonstrates the use of action bar tabs and how they interact
 * with other action bar features.
 */
public class Test extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
    }

    public void onAddTab(View v) {
        final ActionBar bar = getActionBar();
        final int tabCount = bar.getTabCount();
        final String text = "Tab " + tabCount;
        bar.addTab(bar.newTab()
                .setText(text)
                .setTabListener(new TabListener(new TabContentFragment(text))));
    }

    public void onRemoveTab(View v) {
        final ActionBar bar = getActionBar();
        bar.removeTabAt(bar.getTabCount() - 1);
    }

    public void onToggleTabs(View v) {
        final ActionBar bar = getActionBar();

        if (bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
        } else {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        }
    }

    public void onRemoveAllTabs(View v) {
        getActionBar().removeAllTabs();
    }
    private class TabListener implements ActionBar.TabListener {
        private TabContentFragment mFragment;

        public TabListener(TabContentFragment fragment) {
            mFragment = fragment;
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.add(R.id.fragment_content, mFragment, mFragment.getText());
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(mFragment);
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Toast.makeText(Test.this, "Reselected!", Toast.LENGTH_SHORT).show();
        }

    }

    private class TabContentFragment extends Fragment {
        private String mText;

        public TabContentFragment(String text) {
            mText = text;
        }

        public String getText() {
            return mText;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View fragView = inflater.inflate(R.layout.row, container, false);

            TextView text = (TextView) fragView.findViewById(R.id.text);
            text.setText(mText);

            return fragView;
        }

    }
}
package net.javalib.flickr.search.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * Utility class used to deal with SD card cache.
 */
public class CacheUtil {
  /** Cache root */
  private static File root;
  
  private CacheUtil() {}
  
  static {
    root = Environment.getExternalStorageDirectory();
    root = new File(root,"FlickrSearch/cache");
  }
  
  public static File file(String cacheKey) {
    return new File(root,cacheKey);
  }
  
  public static void deleteCache() {
    deleteDir(root);
  }
  
  public static void deleteDir(File dir) {
    if (!dir.isDirectory())
      return;
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isDirectory())
        deleteDir(file);
      else {
        file.delete();
      }
    }
  }
  
  public static boolean exists(String cacheKey) {
    return file(cacheKey).exists();
  }
  
  public static Bitmap getBitmap(String cacheKey) {
    if (!exists(cacheKey))
      return null;
    File file = file(cacheKey);
    InputStream in = null;
    try {
      in = new FileInputStream(file);
      return BitmapFactory.decodeStream(in);
    } catch (Exception e) {
      return null;
    } finally {
      if (in != null) try { in.close(); } catch (Exception e) {}
    }
  }
  
}
import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;

class Util {
  public static String getNewName(String originalName, Context ctx) {
    if(null == originalName)
    {
      originalName = "tmp";
      System.out.println("ERROR: Why did i get null orginalName?");
    }
        int MAX_ATTEMPTS = 10;
        int attempts = 0;
        
        String result = getFotolabDir(ctx);// + Constants.APP_NAME + "/";
        while(attempts++ < MAX_ATTEMPTS) {
              String picName = originalName.substring(originalName.lastIndexOf("/")+1);
              //remove the extension such as .jpg, .png etc
              picName = picName.replaceAll("\\..*", "");
              
              picName = result + picName + System.currentTimeMillis() + ".png";
              
              File file = new File(picName);
              if(!file.exists())
                    return picName;
        }
        
        throw new RuntimeException("couldn't get a name");
  }
  
  private static void alert(String msg, Context ctx)
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    builder.setMessage(msg)
           .setCancelable(false)
           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   
               }
           })           
           ;
    AlertDialog alert = builder.create();
    alert.show();
  }
  public static String getFotolabDir(Context ctx)
  {
    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;
    String state = Environment.getExternalStorageState();

    if (Environment.MEDIA_MOUNTED.equals(state)) {
        // We can read and write the media
        mExternalStorageAvailable = mExternalStorageWriteable = true;
    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        // We can only read the media
        mExternalStorageAvailable = true;
        mExternalStorageWriteable = false;
    } else {
        // Something else is wrong. It may be one of many other states, but all we need
        //  to know is we can neither read nor write
        mExternalStorageAvailable = mExternalStorageWriteable = false;
    }
    
    if(!mExternalStorageAvailable)
    {
      alert("External media is not available. App may not function correctly",ctx);
    }else if(!mExternalStorageWriteable)
    {
      alert("External media is not writable. App may not function correctly",ctx);
    }
    return Environment.getExternalStorageDirectory().getAbsolutePath()+"/fotolab/";
    
  }


}
package app.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

class CMDExecute {

  public synchronized String run(String[] cmd, String workdirectory)
      throws IOException {
    String result = "";

    try {
      ProcessBuilder builder = new ProcessBuilder(cmd);
      // set working directory
      if (workdirectory != null)
        builder.directory(new File(workdirectory));
      builder.redirectErrorStream(true);
      Process process = builder.start();
      InputStream in = process.getInputStream();
      byte[] re = new byte[1024];
      while (in.read(re) != -1) {
        System.out.println(new String(re));
        result = result + new String(re);
      }
      in.close();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

}

public class Main {
  private static StringBuffer buffer;
  public static String getSystemProperty() {
    buffer = new StringBuffer();
    initProperty("java.vendor.url", "java.vendor.url");
    initProperty("java.class.path", "java.class.path");
    initProperty("user.home", "user.home");
    initProperty("java.class.version", "java.class.version");
    initProperty("os.version", "os.version");
    initProperty("java.vendor", "java.vendor");
    initProperty("user.dir", "user.dir");
    initProperty("user.timezone", "user.timezone");
    initProperty("path.separator", "path.separator");
    initProperty(" os.name", " os.name");
    initProperty("os.arch", "os.arch");
    initProperty("line.separator", "line.separator");
    initProperty("file.separator", "file.separator");
    initProperty("user.name", "user.name");
    initProperty("java.version", "java.version");
    initProperty("java.home", "java.home");
    return buffer.toString();
  }
  private static String initProperty(String description, String propertyStr) {
    if (buffer == null) {
      buffer = new StringBuffer();
    }
    buffer.append(description).append(":");
    buffer.append(System.getProperty(propertyStr)).append("\n");
    return buffer.toString();
  }
}
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

class DistanceUtil {

  private LocationManager _lm;
  private String _provider;

  public DistanceUtil(Context context) {
    _lm = (LocationManager) context
        .getSystemService(Context.LOCATION_SERVICE);
    Criteria crit = new Criteria();
    crit.setAccuracy(Criteria.ACCURACY_COARSE);
    if (_lm != null)
      _provider = _lm.getBestProvider(crit, true);
  }

  public static double calculateDistance(double startLat, double startLong,
      double endLat, double endLong) {

    try {
      double theta = startLong - endLong;
      double dist = Math.sin(deg2rad(startLat))
          * Math.sin(deg2rad(endLat)) + Math.cos(deg2rad(startLat))
          * Math.cos(deg2rad(endLat)) * Math.cos(deg2rad(theta));
      dist = Math.acos(dist);
      dist = rad2deg(dist);
      dist = dist * 60 * 1.1515; // in miles
      dist = dist * 1.609344; // in kilometers

      return dist;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  public double calculateDistanceFromActual(double endLat, double endLong) {
    if (_lm != null && _provider != null) {
      Location loc = _lm.getLastKnownLocation(_provider);
      if (loc != null)
        return calculateDistance(loc.getLatitude(), loc.getLongitude(),
            endLat, endLong);
    }

    return 0;
  }
  private static double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  private static double rad2deg(double rad) {
    return (rad * 180.0 / Math.PI);
  }

}
