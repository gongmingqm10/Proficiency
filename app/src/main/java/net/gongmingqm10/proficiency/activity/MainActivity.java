package net.gongmingqm10.proficiency.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.gongmingqm10.proficiency.R;
import net.gongmingqm10.proficiency.adapter.FactsAdapter;
import net.gongmingqm10.proficiency.api.ApiCallResponse;
import net.gongmingqm10.proficiency.api.CanadaFactsApi;
import net.gongmingqm10.proficiency.cache.CacheData;
import net.gongmingqm10.proficiency.cache.CacheManager;
import net.gongmingqm10.proficiency.cache.CacheUtils;
import net.gongmingqm10.proficiency.model.Facts;
import net.gongmingqm10.proficiency.network.NetworkMgr;
import net.gongmingqm10.proficiency.network.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity implements NetworkMgr.OnApiCallFinishListener {

    private ListView listView;
    private CanadaFactsApi factsApi;
    private FactsAdapter factsAdapter;
    private MenuItem refreshMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        factsApi = new CanadaFactsApi();
        factsAdapter = new FactsAdapter(getApplicationContext());

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(factsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        NetworkMgr.getInstance().addOnApiCallFinishListener(this);
        loadFacts();

    }

    private void loadFacts() {
        CacheData<Facts> cacheFacts = CacheManager.getInstance().getCache(CacheUtils.CACHE_FACTS);
        if (cacheFacts != null) {
            Facts facts = cacheFacts.getData();
            refreshUI(facts);
        } else {
            startSync();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetworkMgr.getInstance().removeOnApiCallFinishListener(this);
    }

    private void startSync() {
        setRefreshActionButtonState(true);
        NetworkMgr.getInstance().startSync(factsApi);
    }

    public void setRefreshActionButtonState(boolean isRefreshing) {
        if (refreshMenuItem != null) {
            if (isRefreshing) {
                refreshMenuItem.setActionView(R.layout.indeterminate_progress);
            } else {
                refreshMenuItem.setActionView(null);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.refreshMenuItem = menu.findItem(R.id.action_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            startSync();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onApiCallFinish(ApiCallResponse<?> data) {
        if (data != null && data.getAbsApi() == factsApi) {
            Facts facts;
            if (data.isSuccess()) {
                facts = (Facts) data.getData();
            } else {
                Toast.makeText(this, data.getErrorMessage(), Toast.LENGTH_SHORT).show();
                // If the facts URL is not available, we use facts.json in assets.
                facts = loadFactsFromAssets();
            }
            if (facts == null) return;
            CacheData<Facts> cacheFacts = new CacheData<Facts>(CacheUtils.CACHE_FACTS, facts);
            CacheManager.getInstance().addCache(cacheFacts);
            refreshUI(facts);
        }
    }

    private void refreshUI(Facts facts) {
        setRefreshActionButtonState(false);
        if (facts.getTitle() != null)
            getSupportActionBar().setTitle(facts.getTitle());
        if (facts.getRows() != null)
            factsAdapter.setItems(facts.getRows());
    }


    private Facts loadFactsFromAssets() {
        Facts facts = null;
        try {
            InputStream is = getAssets().open("facts.json");
            facts = new Gson().fromJson(NetworkUtil.parseStringFromInputStream(is), Facts.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return facts;
    }

}
