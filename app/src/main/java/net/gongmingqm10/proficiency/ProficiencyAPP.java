package net.gongmingqm10.proficiency;

import android.app.Application;

import net.gongmingqm10.proficiency.cache.CacheManager;

public class ProficiencyAPP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CacheManager.init(getApplicationContext());
    }
}
