package net.gongmingqm10.proficiency.network;

import android.os.Handler;
import android.os.Message;

import net.gongmingqm10.proficiency.api.AbsApi;
import net.gongmingqm10.proficiency.api.ApiCallResponse;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetworkMgr {

    private static final int API_CALL_FINISHED = 100;
    private static NetworkMgr instance;
    private final int corePoolSize = 2;
    private final int maximumPoolSize = 5;
    private final int keepAliveTime = 60;
    private ThreadPoolExecutor threadPoolExecutor;
    private ConcurrentLinkedQueue<OnApiCallFinishListener> listeners;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == API_CALL_FINISHED && msg.obj instanceof ApiCallResponse) {
                for (OnApiCallFinishListener listener : listeners) {
                    listener.onApiCallFinish((ApiCallResponse<?>) msg.obj);
                }
            }
        }
    };

    private NetworkMgr() {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        listeners = new ConcurrentLinkedQueue<OnApiCallFinishListener>();
    }

    public static synchronized NetworkMgr getInstance() {
        if (instance == null) instance = new NetworkMgr();
        return instance;
    }

    public void addOnApiCallFinishListener(OnApiCallFinishListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeOnApiCallFinishListener(OnApiCallFinishListener listener) {
        listeners.remove(listener);
    }

    public void startSync(final AbsApi<?> absApi) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiCallResponse<?> response = absApi.call();
                handler.sendMessage(handler.obtainMessage(API_CALL_FINISHED, response));
            }
        });
    }

    public interface OnApiCallFinishListener {
        public void onApiCallFinish(ApiCallResponse<?> data);
    }

}
