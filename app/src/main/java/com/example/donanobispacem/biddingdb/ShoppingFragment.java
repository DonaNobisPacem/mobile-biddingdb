package com.example.donanobispacem.biddingdb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ShoppingFragment extends Fragment {
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";

    private List<Bid> shoppingList;
    private BidAdapter adapter;
    private Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingList = (List<Bid>) getArguments().getSerializable("shoppingList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        adapter = new BidAdapter(new ArrayList<Bid>(), ctx);
        adapter.setBidList( shoppingList );
        adapter.notifyDataSetChanged();

        final ListView bidList = (ListView) view.findViewById(R.id.shopping_list);
        bidList.setAdapter( adapter );
        bidList.setOnItemClickListener(
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

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }
}
