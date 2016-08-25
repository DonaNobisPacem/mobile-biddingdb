package com.example.donanobispacem.biddingdb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    private CaldroidFragment caldroidFragment;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");

    private String userToken;
    private String userId;
    private SharedPreferences mPreferences;
    private BidAdapter adapter;

    private static final String url = "http://bidding.up-ovpd.ph/api/1/bids";
    private static final String TAG_RESPONSE = "response";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTRACTOR = "contractor";
    private static final String TAG_MODE = "mode";
    private static final String TAG_PREPROCUREMENT = "preprocurement";
    private static final String TAG_PREBIDDING = "prebidding";
    private static final String TAG_BIDDING = "bidding";
    private static final String TAG_POSTQUALIFICATION = "postqualification";
    private static final String TAG_NOA = "noa";
    private static final String TAG_PURCHASE = "purchase";
    private static final String TAG_NTP = "ntp";

    private HashMap<String, List<Bid>> calendarMap;
    private List<String> hashKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPreferences.contains("AuthToken") && mPreferences.contains("UserID")) {
            getSupportActionBar().setTitle("Calendar");

            adapter = new BidAdapter(new ArrayList<Bid>(), this);
            final ListView calendarList = (ListView) findViewById(R.id.calendar_list);

            caldroidFragment = new CaldroidFragment();
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);

            userToken = mPreferences.getString("AuthToken", "");
            userId = mPreferences.getString("UserID", "");

            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.calendar1, caldroidFragment);
            t.commit();

            final CaldroidListener listener = new CaldroidListener() {
                @Override
                public void onSelectDate(Date date, View view) {
                    List<Bid> bidsToday = calendarMap.get(formatter.format(date));
                    Toast.makeText(getApplicationContext(), Integer.toString( bidsToday.size() ) + " entries found.", Toast.LENGTH_SHORT).show();

                    adapter.setBidList(bidsToday);
                    adapter.notifyDataSetChanged();

                    calendarList.setAdapter( adapter );
                    calendarList.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View v,
                                                        int position, long id) {
                                    Intent i = new Intent(v.getContext(), BidDetailActivity.class);
                                    i.putExtra(TAG_ID, adapter.getBid(+position).getID());
                                    i.putExtra(TAG_TITLE, adapter.getBid(+position).getTitle());
                                    startActivity(i);
                                }
                            }
                    );
                }
            };
            caldroidFragment.setCaldroidListener(listener);
            caldroidFragment.refreshView();

            new CallAPI().execute(url, userToken, userId );
        } else {
            Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }


    private void setEventMarker( List<String> dates ) {
        for( String dateString: dates ){
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(formatter.parse(dateString));
                Date colorDate = cal.getTime();

                if (caldroidFragment != null) {
                    ColorDrawable blue = new ColorDrawable(Color.BLUE);
                    caldroidFragment.setBackgroundDrawableForDate(blue, colorDate);
                    caldroidFragment.setTextColorForDate(R.color.white, colorDate);
                }
                caldroidFragment.refreshView();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private class CallAPI extends AsyncTask<String, String, List<Bid>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CalendarActivity.this);
            pDialog.setMessage("Getting Calendar Events...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected List<Bid> doInBackground(String... params) {
            List<Bid> result = new ArrayList<>();

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

                calendarMap = new HashMap<>();

                for (int i=0; i < jArr.length(); i++) {
                    Bid holder = convertBid(jArr.getJSONObject(i));
                    calendarMap = addToMap( calendarMap, holder );
                    result.add(holder);
                }

                hashKeys = new ArrayList<>();
                for( String hashKey: calendarMap.keySet() ) {
                    hashKeys.add( hashKey );
                }

                return result;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bid> result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            setEventMarker( hashKeys );
        }

        private Bid convertBid(JSONObject obj) throws JSONException {
            String _idString = obj.getString(TAG_ID);

            Bid bid = new Bid();

            bid.setID(Integer.parseInt(_idString));
            bid.setTitle(obj.getString(TAG_TITLE));
            bid.setContractor(obj.getString(TAG_CONTRACTOR));
            bid.setMode(obj.getString(TAG_MODE));
            bid.setPreprocurement(obj.getString(TAG_PREPROCUREMENT));
            bid.setPrebidding(obj.getString(TAG_PREBIDDING));
            bid.setBidding(obj.getString(TAG_BIDDING));
            bid.setPostqualification(obj.getString(TAG_POSTQUALIFICATION));
            bid.setNoa(obj.getString(TAG_NOA));
            bid.setPurchase(obj.getString(TAG_PURCHASE));
            bid.setNtp(obj.getString(TAG_NTP));

            return bid;
        }

        private HashMap<String, List<Bid>> addToMap( HashMap<String, List<Bid>> map, Bid bidEntry ){
            List<Bid> bidHolder;

            // add to map with key = date of preprocurement
            if( map.containsKey( bidEntry.getPreprocurement())  ) {
                bidHolder = map.get(bidEntry.getPreprocurement() );
            } else {
                bidHolder = new ArrayList<>();
            }
            bidHolder.add( bidEntry );
            map.put( bidEntry.getPreprocurement(), bidHolder );

            // add to map with key = date of prebidding
            if( map.containsKey( bidEntry.getPrebidding())  ) {
                bidHolder = map.get(bidEntry.getPrebidding() );
            } else {
                bidHolder = new ArrayList<>();
            }
            bidHolder.add( bidEntry );
            map.put( bidEntry.getPrebidding(), bidHolder );

            // add to map with key = date of bidding
            if( map.containsKey( bidEntry.getBidding())  ) {
                bidHolder = map.get(bidEntry.getBidding() );
            } else {
                bidHolder = new ArrayList<>();
            }
            bidHolder.add( bidEntry );
            map.put( bidEntry.getBidding(), bidHolder );

            // add to map with key = date of postquali
            if( map.containsKey( bidEntry.getPostqualification())  ) {
                bidHolder = map.get(bidEntry.getPostqualification() );
            } else {
                bidHolder = new ArrayList<>();
            }
            bidHolder.add( bidEntry );
            map.put( bidEntry.getPostqualification(), bidHolder );

            // add to map with key = date of noa
            if( map.containsKey( bidEntry.getNoa())  ) {
                bidHolder = map.get(bidEntry.getNoa() );
            } else {
                bidHolder = new ArrayList<>();
            }
            bidHolder.add( bidEntry );
            map.put( bidEntry.getNoa(), bidHolder );

            // add to map with key = date of purchase
            if( map.containsKey( bidEntry.getPurchase())  ) {
                bidHolder = map.get(bidEntry.getPurchase() );
            } else {
                bidHolder = new ArrayList<>();
            }
            bidHolder.add( bidEntry );
            map.put( bidEntry.getPurchase(), bidHolder );

            // add to map with key = date of ntp
            if( map.containsKey( bidEntry.getNtp())  ) {
                bidHolder = map.get(bidEntry.getNtp() );
            } else {
                bidHolder = new ArrayList<>();
            }
            bidHolder.add( bidEntry );
            map.put( bidEntry.getNtp(), bidHolder );

            return map;
        }
    }
}
