package net.gongmingqm10.proficiency.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import net.gongmingqm10.proficiency.R;
import net.gongmingqm10.proficiency.api.ApiCallResponse;
import net.gongmingqm10.proficiency.api.CanadaFactsApi;
import net.gongmingqm10.proficiency.model.Facts;
import net.gongmingqm10.proficiency.network.NetworkMgr;


public class MainActivity extends ActionBarActivity implements NetworkMgr.OnApiCallFinishListener{


    private ListView listView ;
    private CanadaFactsApi factsApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        factsApi = new CanadaFactsApi();
        getSupportActionBar().setTitle("About Canada");
        listView = (ListView) findViewById(R.id.listView);
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
        NetworkMgr.getInstance().startSync(factsApi);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onApiCallFinish(ApiCallResponse<?> data) {
        if (data != null && data.getAbsApi() == factsApi) {
            if (data.isSuccess()) {
                Facts facts = (Facts) data.getData();
                Log.i("gongmingqm10", String.valueOf(facts.getRows()));
            } else {
                Toast.makeText(this, data.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
