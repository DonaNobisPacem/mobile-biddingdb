package com.example.donanobispacem.biddingdb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BidDetailActivity extends AppCompatActivity {

    private static final String TAG_RESPONSE = "response";
    private static final String url = "http://bidding.up-ovpd.ph/api/1/bids/";

    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTRACTOR = "contractor";
    private static final String TAG_MODE = "mode";
    private static final String TAG_NUMBER = "number";
    private static final String TAG_BUDGET = "budget";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_PREPROCUREMENT = "preprocurement";
    private static final String TAG_PREBIDDING = "prebidding";
    private static final String TAG_BIDDING = "bidding";
    private static final String TAG_POSTQUALIFICATION = "postqualification";
    private static final String TAG_NOA = "noa";
    private static final String TAG_PURCHASE = "purchase";
    private static final String TAG_NTP = "ntp";
    private static final String TAG_MEMBERS = "members";
    private static final String TAG_ADDTL_INFO = "addtl_info";
    private static final String TAG_REMARKS = "remarks";
    private static final String TAG_ARCHIVED = "archived";

    private String userToken;
    private String userId;
    private SharedPreferences mPreferences;

    private int bid_id;

    private TextView titleTextView;
    private TextView contractorTextView;
    private TextView numberTextView;
    private TextView modeTextView;
    private TextView budgetTextView;
    private TextView amountTextView;
    private TextView preprocurementTextView;
    private TextView prebiddingTextView;
    private TextView biddingTextView;
    private TextView postqualificationTextView;
    private TextView noaTextView;
    private TextView purchaseTextView;
    private TextView ntpTextView;
    private TextView membersTextView;
    private TextView addtl_infoTextView;
    private TextView remarksTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_detail);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

        titleTextView = (TextView) findViewById(R.id.title);
        contractorTextView = (TextView) findViewById(R.id.contractor);
        numberTextView = (TextView) findViewById(R.id.number);
        modeTextView = (TextView) findViewById(R.id.mode);
        budgetTextView = (TextView) findViewById(R.id.budget);
        amountTextView = (TextView) findViewById(R.id.amount);
        preprocurementTextView = (TextView) findViewById(R.id.preprocurement);
        prebiddingTextView = (TextView) findViewById(R.id.prebidding);
        biddingTextView = (TextView) findViewById(R.id.bidding);
        postqualificationTextView = (TextView) findViewById(R.id.postqualification);
        noaTextView = (TextView) findViewById(R.id.noa);
        purchaseTextView = (TextView) findViewById(R.id.purchase);
        ntpTextView = (TextView) findViewById(R.id.ntp);
        membersTextView = (TextView) findViewById(R.id.members);
        addtl_infoTextView = (TextView) findViewById(R.id.addtl_info);
        remarksTextView = (TextView) findViewById(R.id.remarks);
    }

    @Override
    public void onResume() {
        super.onResume();
        String title;

        if (mPreferences.contains("AuthToken") && mPreferences.contains("UserID")) {

            Intent i = getIntent();
            bid_id = i.getIntExtra(TAG_ID, 0);
            title = i.getStringExtra(TAG_TITLE);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);

            userToken = mPreferences.getString("AuthToken", "");
            userId = mPreferences.getString("UserID", "");
            new CallAPI().execute((url + String.valueOf(bid_id)), userToken, userId );

        } else {
            Intent intent = new Intent(BidDetailActivity.this, LoginActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bid_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new CallAPI().execute((url + String.valueOf(bid_id)), userToken, userId );
            return true;
        }
        if (id == R.id.action_active) {
            Intent intent = new Intent(BidDetailActivity.this, BidActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (id == R.id.action_archives) {
            Intent intent = new Intent(BidDetailActivity.this, ArchiveActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (id == R.id.action_sign_out) {
            mPreferences.edit().clear().commit();
            Intent intent = new Intent(BidDetailActivity.this, LoginActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CallAPI extends AsyncTask<String, String, Bid> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BidDetailActivity.this);
            pDialog.setMessage("Getting Details ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Bid doInBackground(String... params) {
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
                JSONObject jBid = jObj.getJSONObject(TAG_RESPONSE);

                Bid result = convertBid( jBid );

                return result;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bid result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(titleTextView != null) titleTextView.setText(result.getTitle());
            if(contractorTextView != null) contractorTextView.setText(result.getContractor());
            if(numberTextView != null) numberTextView.setText(result.getNumber());
            if(modeTextView != null) modeTextView.setText(result.getMode());
            if(budgetTextView != null) budgetTextView.setText(result.getBudget());
            if(amountTextView != null) amountTextView.setText(result.getAmount());
            if(preprocurementTextView != null) preprocurementTextView.setText(result.getPreprocurement());
            if(prebiddingTextView != null) prebiddingTextView.setText(result.getPrebidding());
            if(biddingTextView != null) biddingTextView.setText(result.getBidding());
            if(postqualificationTextView != null) postqualificationTextView.setText(result.getPostqualification());
            if(noaTextView != null) noaTextView.setText(result.getNoa());
            if(purchaseTextView != null) purchaseTextView.setText(result.getPurchase());
            if(ntpTextView != null) ntpTextView.setText(result.getNtp());
            if(membersTextView != null) membersTextView.setText(result.getMembers());
            if(addtl_infoTextView != null) addtl_infoTextView.setText(result.getAddtl_info());
            if(remarksTextView != null) remarksTextView.setText(result.getRemarks());
        }

        private Bid convertBid(JSONObject obj) throws JSONException {
            String _idString = obj.getString(TAG_ID);

            Bid bid = new Bid();

            bid.setID(Integer.parseInt(_idString));
            bid.setTitle(obj.getString(TAG_TITLE));
            bid.setContractor(obj.getString(TAG_CONTRACTOR));
            bid.setNumber(obj.getString(TAG_NUMBER));
            bid.setMode(obj.getString(TAG_MODE));
            bid.setBudget(obj.getString(TAG_BUDGET));
            bid.setAmount(obj.getString(TAG_AMOUNT));
            bid.setPreprocurement(obj.getString(TAG_PREPROCUREMENT));
            bid.setPrebidding(obj.getString(TAG_PREBIDDING));
            bid.setBidding(obj.getString(TAG_BIDDING));
            bid.setPostqualification(obj.getString(TAG_POSTQUALIFICATION));
            bid.setNoa(obj.getString(TAG_NOA));
            bid.setPurchase(obj.getString(TAG_PURCHASE));
            bid.setNtp(obj.getString(TAG_NTP));
            bid.setMembers(obj.getString(TAG_MEMBERS));
            bid.setAddtl_info(obj.getString(TAG_ADDTL_INFO));
            bid.setRemarks(obj.getString(TAG_REMARKS));
            bid.setArchived(obj.getString(TAG_ARCHIVED));

            return bid;
        }

    }
}
