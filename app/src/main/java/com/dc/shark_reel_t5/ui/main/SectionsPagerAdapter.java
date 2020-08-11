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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {

    private int idGen = 0;
    private ArrayList<PlaceholderFragment> mFragments = new ArrayList<PlaceholderFragment>();
    private ArrayList<Long> mFragmentIDs = new ArrayList<Long>();


    public SectionsPagerAdapter(FragmentManager fm, Lifecycle lc) {
        super(fm, lc);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Nullable
    public CharSequence getPageTitle(int position) { return "Hook #" + (position+1); }


    public String getTitle(int position) {
        return "Hook #" + (position+1);
    }

    //Dynamic(runtime) methods:

    //On call: adds a hook to the end of the list in UI(DOES NOT CHANGE BACK END VARIABLES YET)
    public void addHookFrag() {

        idGen++;
        PlaceholderFragment currFrag = PlaceholderFragment.newInstance(getItemCount()+1);
        mFragmentIDs.add((long)idGen);
        mFragments.add(currFrag);

        notifyItemInserted(mFragments.size()-1);
    }


    public void delHookFrag(int position, TabLayout tabs) {

        mFragments.remove(position);
        mFragmentIDs.remove(position);
        tabs.removeTabAt(position);
        notifyItemRangeChanged(position, getItemCount() - position);

        //Updates the section number for all affected fragments
        for(int i = position; i < getItemCount(); i++){
            mFragments.get(i).updatePosition(i + 1);
        }


    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }

    @Override
    public long getItemId(int position) {
        return (long)mFragmentIDs.get(position);
    }

    @Override
    public boolean containsItem(long itemId){
        return mFragmentIDs.contains((int)itemId);
    }

}