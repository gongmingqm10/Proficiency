package net.gongmingqm10.proficiency.network;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkUtil {

    private static Gson gson;

    public static synchronized Object call(String urlString, Class<?> clazz) {
        InputStream is = null;
        Object object = null;
        String errorMessage = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            object = getGson().fromJson(parseStringFromInputStream(is), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage().toString();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return errorMessage == null ? object : errorMessage;
        }
    }

    private static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private static String parseStringFromInputStream(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        StringBuilder builder = new StringBuilder();
        int numberRead;
        while ((numberRead = is.read(buffer)) > 0) {
            builder.append(new String(buffer, 0, numberRead));
        }
        return builder.toString();
    }

}
