package net.gongmingqm10.proficiency.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import net.gongmingqm10.proficiency.R;
import net.gongmingqm10.proficiency.adapter.FactsAdapter;
import net.gongmingqm10.proficiency.api.ApiCallResponse;
import net.gongmingqm10.proficiency.api.CanadaFactsApi;
import net.gongmingqm10.proficiency.model.Facts;
import net.gongmingqm10.proficiency.network.NetworkMgr;


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
        startSync();
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
            if (data.isSuccess()) {
                Facts facts = (Facts) data.getData();
                if (facts.getRows() != null)
                    factsAdapter.setItems(facts.getRows());
            } else {
                Toast.makeText(this, data.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
            setRefreshActionButtonState(false);
        }
    }
}
