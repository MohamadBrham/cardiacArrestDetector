package ps.wecare.cardiacarrestdetector;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import ps.wecare.cardiacarrestdetector.db.User;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class App extends Application {

    public static final String TAG = App.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private myDbAdapter helper;
    private ImageLoader mImageLoader;

    private SharedPreferences mSharedPreferences;


    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public myDbAdapter getDbHelper() {
        if (helper == null) {
            helper = new myDbAdapter(this);
        }
        return helper;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    // Shared Preferences
    public final String IS_LOGGED_IN = "IS_LOGGED_IN";
    public final String NAME = "NAME";
    public final String PHONE = "PhoneNumber";
    public final String PASS = "Password";
    public final String AGE = "Age";
    public final String ID = "id";
    public final String SHOW_GUIDE_AGAIN = "SHOW_GUIDE_AGAIN";



    public SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null)
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return mSharedPreferences;
    }

    public String getPhoneNumber() {
        return getSharedPreferences().getString(PHONE, "");
    }

    public void logIn(User user) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(NAME, user.getName());
        editor.putString(PHONE, user.getPhone());
        editor.putString(PASS, user.getPassword());
        editor.putString(AGE, user.getAge());
        editor.putLong(ID, user.getId());

        editor.apply();
    }
    public void setWithoutGuide(){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(SHOW_GUIDE_AGAIN, false);
        editor.apply();
    }
    public void logOut() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.putString(PHONE, "");
        editor.putString(PASS, "");
        editor.putString(NAME, "");
        editor.putString(AGE, "");
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getSharedPreferences().getBoolean(IS_LOGGED_IN, false);
    }
    public boolean showGuide(){return getSharedPreferences().getBoolean(SHOW_GUIDE_AGAIN, true);}
    public long getUserId(){return getSharedPreferences().getLong(ID,0);}
    public String getUserName(){return getSharedPreferences().getString(NAME,"Anonymous");}





}
