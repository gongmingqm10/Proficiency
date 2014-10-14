package net.gongmingqm10.proficiency.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import net.gongmingqm10.proficiency.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageLoader {

    private static ImageLoader instance;
    private final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private final int corePoolSize = CPU_COUNT + 1;
    private final int maximumPoolSize = CPU_COUNT * 2 + 1;
    private final int keepAliveTime = 60;
    private final int IMAGE_LOAD_MESSAGE = 200;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == IMAGE_LOAD_MESSAGE) {
                Bitmap bitmap = cachedBitmaps.get(msg.obj);
                if (bitmap != null && msg.obj.equals(target.getTag()))
                    target.setImageBitmap(bitmap);
            }

        }
    };
    private ThreadPoolExecutor threadPoolExecutor;
    private ImageView target;
    private Map<String, Bitmap> cachedBitmaps;

    private ImageLoader() {
        cachedBitmaps = new ConcurrentHashMap<String, Bitmap>();
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    public void load(final String urlString, final ImageView imageView) {
        load(urlString, R.drawable.placeholder, imageView);
    }

    public void load(final String urlString, int placeholder, final ImageView imageView) {
        imageView.setImageResource(placeholder);
        imageView.setTag(urlString);
        if (urlString == null || "".equals(urlString)) return;
        Bitmap cachedBitmap = cachedBitmaps.get(urlString);
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmaps.get(urlString));
            return;
        }

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    cachedBitmaps.put(urlString, bitmap);
                    target = imageView;
                    handler.sendMessage(handler.obtainMessage(IMAGE_LOAD_MESSAGE, urlString));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
