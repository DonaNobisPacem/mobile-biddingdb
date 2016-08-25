package com.example.donanobispacem.biddingdb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity {

    private static final String TAG_RESPONSE = "response";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTRACTOR = "contractor";
    private static final String TAG_MODE = "mode";
    private static final String url = "http://bidding.up-ovpd.ph/api/1/archive";

    private String userToken;
    private String userId;
    private SharedPreferences mPreferences;

    private ViewPager viewPager;
    private TabAdapter mAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPreferences.contains("AuthToken") && mPreferences.contains("UserID")) {
            getSupportActionBar().setTitle("Archived Projects");

            userToken = mPreferences.getString("AuthToken", "");
            userId = mPreferences.getString("UserID", "");
            new CallAPI().execute(url, userToken, userId );
        } else {
            Intent intent = new Intent(ArchiveActivity.this, LoginActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_archive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new CallAPI().execute(url, userToken, userId );
            return true;
        }
        if (id == R.id.action_active) {
            Intent intent = new Intent(ArchiveActivity.this, BidActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (id == R.id.action_sign_out) {
            mPreferences.edit().clear().commit();
            Intent intent = new Intent(ArchiveActivity.this, LoginActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CallAPI extends AsyncTask<String, String, List<List<Bid>>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ArchiveActivity.this);
            pDialog.setMessage("Getting Archive List ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected  List<List<Bid>> doInBackground(String... params) {
            List<List<Bid>> result = new ArrayList<>();
            List<Bid> shopping = new ArrayList<>();
            List<Bid> bidding = new ArrayList<>();
            List<Bid> svp = new ArrayList<>();

            try {
                URL u = new URL(params[0]);
                String userToken = new String(params[1]);
                String userId = new String(params[2]);

                String basicAuth = "Token token=\"" + userToken + "\", user_id=\"" + userId + "\"";

                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", basicAuth);

                conn.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String JSONResp;
                while ((JSONResp = br.readLine()) != null) {
                    sb.append(JSONResp);
                }
                br.close();
                JSONResp = sb.toString();

                JSONObject jObj = new JSONObject(JSONResp);
                JSONArray jArr = jObj.getJSONArray(TAG_RESPONSE);

                for (int i=0; i < jArr.length(); i++) {
                    Bid holder = convertBid(jArr.getJSONObject(i));
                    if( holder.getMode().equals("Shopping") ) { shopping.add(holder); }
                    else if( holder.getMode().equals("Public Bidding") ) { bidding.add(holder); }
                    else if( holder.getMode().equals("Small Value Procurement")) { svp.add(holder); }
                    else {} // do nothing
                }

                result.add(shopping);
                result.add(bidding);
                result.add(svp);
                return result;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<List<Bid>> result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            mAdapter = new TabAdapter(result, getSupportFragmentManager(), ArchiveActivity.this);
            viewPager.setAdapter(mAdapter);

            tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);
        }

        private Bid convertBid(JSONObject obj) throws JSONException {
            String _idString = obj.getString(TAG_ID);
            int _id = Integer.parseInt(_idString);
            String title = obj.getString(TAG_TITLE);
            String contractor = obj.getString(TAG_CONTRACTOR);
            String mode = obj.getString(TAG_MODE);

            return new Bid( _id, title, contractor, mode);
        }
    }
}
