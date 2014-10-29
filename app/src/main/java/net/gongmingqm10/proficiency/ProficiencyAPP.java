package net.gongmingqm10.proficiency;

import android.app.Application;

import net.gongmingqm10.proficiency.cache.CacheManager;
import net.gongmingqm10.proficiency.imageloader.ImageLoader;

public class ProficiencyAPP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CacheManager.init(getApplicationContext());
        ImageLoader.init(getApplicationContext());
    }
}
