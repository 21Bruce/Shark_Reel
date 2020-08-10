package com.dc.shark_reel_t5.ui.main;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dc.shark_reel_t5.R;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {

    private FragmentTransaction currTransaction = null;
    private ArrayList<String> mTitles = new ArrayList<String>();
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private ArrayList<Integer> mFragmentIDs = new ArrayList<Integer>();


    public SectionsPagerAdapter(FragmentManager fm, Lifecycle lc) {
        super(fm, lc);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Nullable
    public CharSequence getPageTitle(int position) { return (CharSequence) mTitles.get(position); }


    public String getTitle(int position) {
        return mTitles.get(position);
    }

    //Dynamic(runtime) methods:

    //On call: adds a hook to the end of the list in UI(DOES NOT CHANGE BACK END VARIABLES YET)
    public void addHookFrag(){
        mTitles.add("Hook " + (mFragments.size() + 1));
        PlaceholderFragment currFrag = PlaceholderFragment.newInstance(mFragments.size() + 1);
        mFragmentIDs.add(currFrag.getId());
        mFragments.add(currFrag);
        notifyItemInserted(mFragments.size()-1);
    }

    public void delHookFrag(int position){
        mFragments.remove(position);
        mFragmentIDs.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }


    @Override
    public int getItemCount() {
        return mFragments.size();
    }

    @Override
    public long getItemId(int position){
        if(position >= getItemCount() || position < 0)
            return RecyclerView.NO_ID;

        return mFragmentIDs.get(position);
    }

    @Override
    public boolean containsItem(long itemId){
        return mFragmentIDs.contains((int)itemId);
    }

}