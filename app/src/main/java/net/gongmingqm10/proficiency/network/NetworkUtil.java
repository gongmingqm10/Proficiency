package net.gongmingqm10.proficiency.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkUtil {

    private static final int CONNECT_TIME_OUT = 8 * 1000;

    public static synchronized Object call(String urlString, Class<?> clazz) {
        InputStream is = null;
        Object object = null;
        String errorMessage = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            object = new Gson().fromJson(parseStringFromInputStream(is), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Network connection timeout, please try again later";
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return errorMessage == null ? object : errorMessage;
        }
    }

    public static String parseStringFromInputStream(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        StringBuilder builder = new StringBuilder();
        int numberRead;
        while ((numberRead = is.read(buffer)) > 0) {
            builder.append(new String(buffer, 0, numberRead));
        }
        return builder.toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
