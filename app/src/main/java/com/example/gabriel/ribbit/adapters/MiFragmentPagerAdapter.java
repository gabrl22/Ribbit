package com.example.gabriel.ribbit.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.gabriel.ribbit.ui.FriendsFragment;
import com.example.gabriel.ribbit.ui.InboxFragment;
import com.example.gabriel.ribbit.R;

/**
 * Created by Gabriel on 8/27/15.
 */

//Adapatador para los tabs
public class MiFragmentPagerAdapter extends FragmentPagerAdapter {
    
    protected Context mContext;

    final int PAGE_COUNT = 2;//El numero de tabs que habra
    private String mTabTitles[];//Los titulos de los tabs
    private int mIcons[];

    public MiFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        //Le tengo que pasar el contexto a la aplicacion para poder usar los resources
        super(fragmentManager);
        mContext = context;
        mTabTitles = new String[] { mContext.getString(R.string.inbox_tab), mContext.getString(R.string.friends_tab)};
        mIcons = new int[]{R.drawable.ic_tab_inbox, R.drawable.ic_tab_friends};
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
        /*Drawable image = mContext.getResources().getDrawable(mIcons[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;*/
    }

}