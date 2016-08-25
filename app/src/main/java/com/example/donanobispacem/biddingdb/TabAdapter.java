package com.example.donanobispacem.biddingdb;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by donanobispacem on 8/22/16.
 */
public class TabAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Shopping", "Public Bidding", "Small Value Procurement" };
    private Context context;
    private List<List<Bid>> grandList;
    private List<Bid> shoppingList;
    private List<Bid> biddingList;
    private List<Bid> svpList;

    public TabAdapter(List<List<Bid>> grandList, FragmentManager fm, Context context ) {
        super(fm);
        this.grandList = grandList;
        this.shoppingList = this.grandList.get(0);
        this.biddingList = this.grandList.get(1);
        this.svpList = this.grandList.get(2);
        this.context = context;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                Bundle shoppingArgs = new Bundle();
                shoppingArgs.putSerializable("shoppingList", (ArrayList<Bid>) shoppingList);

                ShoppingFragment shoppingFragment = new ShoppingFragment();
                shoppingFragment.setArguments(shoppingArgs);
                return shoppingFragment;
            case 1:
                Bundle biddingArgs = new Bundle();
                biddingArgs.putSerializable("biddingList", (ArrayList<Bid>) biddingList);

                BiddingFragment biddingFragment = new BiddingFragment();
                biddingFragment.setArguments(biddingArgs);
                return biddingFragment;
            case 2:
                Bundle svpArgs = new Bundle();
                svpArgs.putSerializable("svpList", (ArrayList<Bid>) svpList);

                ProcurementFragment procurementFragment = new ProcurementFragment();
                procurementFragment.setArguments(svpArgs);
                return procurementFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
