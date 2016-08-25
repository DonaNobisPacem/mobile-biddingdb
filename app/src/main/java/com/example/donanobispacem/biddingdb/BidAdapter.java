package com.example.donanobispacem.biddingdb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by donanobispacem on 8/22/16.
 */
public class BidAdapter extends ArrayAdapter<Bid> {
    private List<Bid> bidsList;
    private Context context;

    public BidAdapter(List<Bid> bidsList, Context ctx) {
        super(ctx, R.layout.bids_list, bidsList);
        this.bidsList = bidsList;
        this.context = ctx;
    }

    public int getCount() {
        if (bidsList != null)
            return bidsList.size();
        return 0;
    }

    public Bid getBid(int position) {
        if (bidsList != null)
            return bidsList.get(position);
        return null;
    }

    public long getBidId(int position) {
        if (bidsList != null)
            return bidsList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.bids_list, null);
        }

        Bid b = bidsList.get(position);
        TextView text = (TextView) v.findViewById(R.id.title);
        text.setText(b.getTitle());

        TextView text1 = (TextView) v.findViewById(R.id.contractor);
        text1.setText(b.getContractor());

        return v;

    }

    public List<Bid> getBidList() {
        return bidsList;
    }

    public void setBidList(List<Bid> bidsList) {
        this.bidsList = bidsList;
    }
}
