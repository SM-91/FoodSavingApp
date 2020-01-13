package com.example.foodsharingapplication.authentication.AuthemnticationFragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodsharingapplication.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileHomeFragment extends Fragment {


    private TabLayout tabLayout;
    private TabItem profileTab, updateProfileTab, addExtraInfoTab;
    private PageAdapter pageAdapter;
    private ViewPager viewPager;
    private Fragment fragment=null;
    private Context context;
    public ProfileHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_home, container, false);
        tabLayout = view.findViewById(R.id.tabLayoutProfile);
       /* profileTab = view.findViewById(R.id.profileViewTab);
        updateProfileTab = view.findViewById(R.id.editProfileTab);
        addExtraInfoTab = view.findViewById(R.id.addMoreInfoTab);*/
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Update Profile"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        pageAdapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayoutClick();
        return view;
    }
    public void tabLayoutClick(){
        /*tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
        public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
