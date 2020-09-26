package com.emad.fcmtester.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.emad.fcmtester.MyApplication;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class AndroidUtils {

    private static final String TAG = "AndroidUtils";

    private static Toast mToast;
    private static SharedPreferences pref;
    private static Typeface mTypeFace;

    public static String getPref(String key) {
        checkPref();
        return pref.getString(key, Constants.EMPTY_STRING);
    }

    public static String getPref(String key, String defaultValue) {
        checkPref();
        return pref.getString(key, defaultValue);
    }

    public static int getPref(String key, int defaultValue) {
        checkPref();
        return pref.getInt(key, defaultValue);
    }

    public static long getPref(String key, long defaultValue) {
        checkPref();
        return pref.getLong(key, defaultValue);
    }

    public static boolean getPref(String key, boolean defaultValue) {
        checkPref();
        return pref.getBoolean(key, defaultValue);
    }

    public static void editPref(String key, String value) {
        checkPref();
        pref.edit().putString(key, value).apply();
    }

    public static void editPref(String key, int value) {
        checkPref();
        pref.edit().putInt(key, value).apply();
    }

    public static void editPref(String key, float value) {
        checkPref();
        pref.edit().putFloat(key, value).apply();
    }

    public static void editPref(String key, long value) {
        checkPref();
        pref.edit().putLong(key, value).apply();
    }

    public static void editPref(String key, boolean value) {
        checkPref();
        pref.edit().putBoolean(key, value).apply();
    }

    private static void checkPref() {
        if (pref == null)
            pref = MyApplication.getContext().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
    }

    public static boolean hasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void showSoftwareKeyboard(Activity activity, boolean showKeyboard) {

        final InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (activity.getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), showKeyboard ? InputMethodManager.SHOW_FORCED : InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public static int convertSpFontSize(float fontSize, Context context) {
        Resources resources = context.getResources();
        float SCREEN_DENSITY = resources.getDisplayMetrics().density;
        int spFontSize = (int) (fontSize / SCREEN_DENSITY);

        return spFontSize;
    }

    public static int convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp;
    }

    public static String miliSecToTimeFormat(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public static void openBrowser(final Context context, String url) {

        String HTTPS = "https://";
        String HTTP = "http://";

        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            url = HTTP + url;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "Choose browser"));// Choose browser is arbitrary :)

    }

    public static void setHTMLText(TextView textView, String htmlText) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(htmlText));
        }
    }

    /**
     * make vibrate
     */
    public static Pair<Boolean, String> extractIntentString(Intent intent, String key){
        if (intent == null || intent.getExtras() == null || !intent.getExtras().containsKey(key)) {
            return new Pair<>(false, Constants.EMPTY_STRING);
        } else {
            return new Pair<>(true, intent.getExtras().getString(key));
        }
    }

    public static Pair<Boolean, Serializable> extractIntentSerializable(Intent intent, String key){
        if (intent == null || intent.getExtras() == null || !intent.getExtras().containsKey(key)) {
            return new Pair<>(false, null);
        } else {
            return new Pair<>(true, intent.getExtras().getSerializable(key));
        }
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            // width and height are equal for all assets since they are ovals.
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }


    public static Typeface getTypeface() {
        return mTypeFace;
    }

    public static void setTypeface(Context context) {
        try {
            mTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/iran_sans_light.ttf");
        } catch (Exception ex) {
            Log.i(TAG, "setTypeface: " + ex);
        }
    }

    public static String convertDate(long timeStamp) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timeStamp);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);

        String formattedHours = String.format(Locale.US, "%02d", hours);
        String formattedMinutes = String.format(Locale.US, "%02d", minutes);

        return new StringBuilder().append(formattedHours).append(":").append(formattedMinutes).toString();
    }

    public static void openUrl(Context context, String URL) {
        try {
            if (!URL.startsWith("http://") && !URL.startsWith("https://"))
                URL = "http://" + URL;

            Uri uri = Uri.parse(URL); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
