package com.example.gabriel.ribbit;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Gabriel on 8/27/15.
 */

//Adapatador para los tabs
public class MiFragmentPagerAdapter extends FragmentPagerAdapter {
    
    protected Context mContext;

    final int PAGE_COUNT = 2;//El numero de tabs que habra
    private String mTabTitles[];//Los titulos de los tabs

    public MiFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        //Le tengo que pasar el contexto a la aplicacion para poder usar los resources
        super(fragmentManager);
        mContext = context;
        mTabTitles = new String[] { mContext.getString(R.string.inbox_tab), mContext.getString(R.string.friends_tab)};
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
        //Regresa la cantidad de tabs
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch(position) {
            case 0:
                fragment = InboxFragment.newInstance();
                break;
            case 1:
                fragment = FriendsFragment.newInstance();
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return mTabTitles[position];
    }
}