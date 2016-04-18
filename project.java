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
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
class Utils 
{
  public static void showToastMessage(Context ctx, String msg, int duration )
  {
    CharSequence text = msg;
        if (duration == -1)
        {
          duration = Toast.LENGTH_SHORT;
        }         
        Toast toast = Toast.makeText(ctx, text, duration);
        toast.show();
  }
  
  public static void showToastMessage(Context ctx, String msg)
  {
    showToastMessage(ctx, msg, -1);
  }
  
    public static boolean isConnectedToInternet(Context c) {
      ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo ni = cm.getActiveNetworkInfo(); 
      return (ni != null)&&(ni.isConnected());
   }   
}
package app.test;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
  private int mAudioBufferSize;
  private int mAudioBufferSampleSize;
  private AudioRecord mAudioRecord;
  private boolean inRecordMode = false;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initAudioRecord();
  }
  @Override
  public void onResume() {
    super.onResume();
    inRecordMode = true;
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        getSamples();
      }
    });
    t.start();
  }

  protected void onPause() {
    inRecordMode = false;
    super.onPause();
  }
  @Override
  protected void onDestroy() {
    if(mAudioRecord != null) {
      mAudioRecord.release();
    }
    super.onDestroy();
  }

  private void initAudioRecord() {
    try {
      int sampleRate = 8000;
      int channelConfig = AudioFormat.CHANNEL_IN_MONO;
      int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
      mAudioBufferSize = 2 * AudioRecord.getMinBufferSize(sampleRate,
          channelConfig, audioFormat);
      mAudioBufferSampleSize = mAudioBufferSize / 2;
      mAudioRecord = new AudioRecord(
          MediaRecorder.AudioSource.MIC,
          sampleRate,
          channelConfig,
          audioFormat,
          mAudioBufferSize);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    
    int audioRecordState = mAudioRecord.getState();
    if(audioRecordState != AudioRecord.STATE_INITIALIZED) {
      finish();
    }
  }
    
  private void getSamples() {
    if(mAudioRecord == null) return;

    short[] audioBuffer = new short[mAudioBufferSampleSize];

    mAudioRecord.startRecording();

    int audioRecordingState = mAudioRecord.getRecordingState();
    if(audioRecordingState != AudioRecord.RECORDSTATE_RECORDING) {
      finish();
    }
    while(inRecordMode) {
        int samplesRead = mAudioRecord.read(audioBuffer, 0, mAudioBufferSampleSize);
    }
    mAudioRecord.stop();
  }
}
package app.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;

public class Test extends Activity implements SensorEventListener {
    private WakeLock mWakelock;
    private PowerManager mPwrMgr;
    private WakeLock mTurnBackOn = null;
    Handler handler = new Handler();
  private SensorManager mMgr;
    private Sensor mAccel;
    private BufferedWriter mLog;
  final private SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss - ");
  private int mSavedTimeout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mMgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        mAccel = mMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        try {
            String filename = Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/accel.log";
            mLog = new BufferedWriter(new FileWriter(filename, true));
        }
        catch(Exception e) {
          e.printStackTrace();
          finish();
        }
        mPwrMgr = (PowerManager) this.getSystemService(POWER_SERVICE);
        mWakelock = mPwrMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Accel");
        mWakelock.acquire();
        try {
          mSavedTimeout = Settings.System.getInt(getContentResolver(), 
              Settings.System.SCREEN_OFF_TIMEOUT);
        }
        catch(Exception e) {
          mSavedTimeout = 120000;  // 2 minutes
        }
        Settings.System.putInt(getContentResolver(), 
            Settings.System.SCREEN_OFF_TIMEOUT, -1);  // always on
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                handler.post(new Runnable() {
          public void run() {
            if(mTurnBackOn != null)
              mTurnBackOn.release();
                    mTurnBackOn = mPwrMgr.newWakeLock(
                            PowerManager.SCREEN_DIM_WAKE_LOCK |
                                    PowerManager.ACQUIRE_CAUSES_WAKEUP,
                            "AccelOn");
                    mTurnBackOn.acquire();
          }});
            }
        }
    };

    @Override
    protected void onStart() {
        mMgr.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
      super.onStart();
    }

    @Override
    protected void onStop() {
        mMgr.unregisterListener(this, mAccel);
        unregisterReceiver(mReceiver);
        try {
      mLog.flush();
    } catch (IOException e) {
    }
      super.onStop();
    }

    @Override
    protected void onDestroy() {
      try {
          mLog.flush();
          mLog.close();
      }
      catch(Exception e) {
      }
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, mSavedTimeout);
        mWakelock.release();
        if(mTurnBackOn != null)
            mTurnBackOn.release();
        super.onDestroy();
    }
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }
  public void onSensorChanged(SensorEvent event) {
    writeLog("Got a sensor event: " + event.values[0] + ", " +
        event.values[1] + ", " + event.values[2]);
  }
  private void writeLog(String str) {
    try {
        Date now = new Date();
        mLog.write(mTimeFormat.format(now));
      mLog.write(str);
      mLog.write("\n");
    }
    catch(IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;


class OauthWebConfirm {
  private static HttpClient httpClient = new CHttpClient();
  public static String email = "";
  public static String pwd = "";

  public static void confirm(String url) {
    httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
    try {

      // login
      HttpPost loginpost = new HttpPost("http://www.douban.com/login");

      String loginentity = "redir=&form_email=" + URLEncoder.encode(email, "UTF-8") + "&form_password=" + URLEncoder.encode(pwd, "UTF-8") +"&remember=on&user_login=%E8%BF%9B%E5%85%A5";

      StringEntity reqEntity = new StringEntity(loginentity);
      reqEntity.setContentType("application/x-www-form-urlencoded");
      loginpost.setEntity(reqEntity);
      loginpost.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");
      httpClient.execute(loginpost);
      
      // agree page
      HttpGet get = new HttpGet(url);
      get.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");
      HttpResponse res2 = httpClient.execute(get);
      String restring2 = convertStreamToString(res2.getEntity().getContent());

      // agree action
      HttpPost agreepost = new HttpPost(url);
      
      StringBuilder stringBuilder = new StringBuilder();

      stringBuilder.append("oauth_token=").append(getFromValue(restring2, "oauth_token"));
      stringBuilder.append("&oauth_callback=").append(getFromValue(restring2, "oauth_callback"));
      stringBuilder.append("&ssid=").append(getFromValue(restring2, "ssid"));
      stringBuilder.append("&confirm=").append(getFromValue(restring2, "confirm"));

      StringEntity agreeEntity = new StringEntity(stringBuilder.toString());
      agreeEntity.setContentType("application/x-www-form-urlencoded");
      agreepost.setEntity(agreeEntity);
      agreepost.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");
      httpClient.execute(agreepost);
      // HttpResponse res3 = httpClient.execute(agreepost);
      
      // String restring3 = convertStreamToString(res3.getEntity().getContent());
      
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String convertStreamToString(InputStream is) throws IOException {
    if (is != null) {
      StringBuilder sb = new StringBuilder();
      String line;

      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while ((line = reader.readLine()) != null) {
          sb.append(line).append("\n");
        }
      } finally {
        is.close();
      }
      return sb.toString();
    } else {
      return "";
    }
  }
  
  public static String getFromValue(String html, String name) {
    
    Pattern p = Pattern.compile("name=\\\"" + name + "\\\" value=\\\".+\\\"");
    Matcher m = p.matcher(html);
    String ex = "";
    if(m.find()) ex = m.group(0);
    else return "";
    String value = "";
        try {
          value = URLEncoder.encode(ex.split("value=\"")[1].replace("\"", ""), "UTF-8");
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
    return value;
  }
}



class CHttpClient extends DefaultHttpClient {
  @Override
  protected ClientConnectionManager createClientConnectionManager() {
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
    
    return new ThreadSafeClientConnManager(this.getParams(), registry);
  }
}

