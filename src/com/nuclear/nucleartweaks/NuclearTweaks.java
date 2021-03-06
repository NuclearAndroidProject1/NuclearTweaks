package com.nuclear.nucleartweaks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.provider.Settings;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.graphics.drawable.Drawable;
import com.android.internal.logging.MetricsLogger;

import com.android.internal.util.slim.DeviceUtils;
import com.nuclear.nucleartweaks.tabs.GeneralUI;
import com.nuclear.nucleartweaks.tabs.StatusBar;
import com.nuclear.nucleartweaks.tabs.Navigation;
import com.nuclear.nucleartweaks.tabs.Recents;
import com.nuclear.nucleartweaks.tabs.System;
import com.nuclear.nucleartweaks.tabs.LockScreen;
import com.nuclear.nucleartweaks.PagerSlidingTabStrip;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import java.util.ArrayList;
import java.util.List;
import com.android.settings.Utils;
import android.util.SettingConfirmationHelper;

public class NuclearTweaks extends SettingsPreferenceFragment {

    private static final int MENU_HELP  = 0;

    ViewPager mViewPager;
    String titleString[];
    ViewGroup mContainer;
    PagerSlidingTabStrip mTabs;

    Context context;

    static Bundle mSavedState;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainer = container;

        View view = inflater.inflate(R.layout.preference_generalui, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        StatusBarAdapter StatusBarAdapter = new StatusBarAdapter(getFragmentManager());
        mViewPager.setAdapter(StatusBarAdapter);
        mTabs.setViewPager(mViewPager);

        ViewGroup tabVG = (ViewGroup) mTabs.getChildAt(0);
        int children = tabVG.getChildCount();
        for(int ii=0;ii<children;ii++) {
    		View vv = tabVG.getChildAt(ii);
   			vv.setLongClickable(true);
    		vv.setOnLongClickListener(new View.OnLongClickListener() {
        		@Override
       			public boolean onLongClick(View v) {
            		Toast.makeText(getActivity(),
                	(R.string.nucleartweaks_dialog_toast),
               		 Toast.LENGTH_LONG).show();
            		return false;
       			}           
    		});
		}		

        setHasOptionsMenu(true);
        Resources res = getResources();
 
        SettingConfirmationHelper.request(
            getActivity(),
            Settings.System.PERFORMANCE_APP,
            res.getString(R.string.performance_app_title),
            res.getString(R.string.performance_app_message),
            res.getString(R.string.kernel_adiutor_title),
            res.getString(R.string.synapse_title),
            null,
            null
        );
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.NUCLEARTWEAKS;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!DeviceUtils.isTablet(getActivity())) {
            mContainer.setPadding(30, 30, 30, 30);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_HELP, 0, R.string.nucleartweaks_dialog_title)
                .setIcon(R.drawable.ic_help)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_HELP:
                showDialogInner(MENU_HELP);
                Toast.makeText(getActivity(),
                (R.string.nucleartweaks_dialog_toast),
                Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }
    }

    private void showDialogInner(int id) {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(id);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "dialog " + id);
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int id) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            frag.setArguments(args);
            return frag;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            switch (id) {
                case MENU_HELP:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.nucleartweaks_dialog_title)
                    .setMessage(R.string.nucleartweaks_dialog_message)
                    .setCancelable(false)
                    .setNegativeButton(R.string.dlg_ok,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create();
            }
            throw new IllegalArgumentException("unknown id " + id);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    class StatusBarAdapter extends FragmentPagerAdapter {
        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];
        final int[] icons = new int[]{R.drawable.ic_nuclear_misc, R.drawable.ic_settings_statusbar, R.drawable.ic_nap_buttons_exposed ,R.drawable.ic_xd_recents, R.drawable.ic_settings_security, R.drawable.ic_settings_supersu_tile};

        public StatusBarAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new GeneralUI();
            frags[1] = new StatusBar();
            frags[2] = new Navigation();
            frags[3] = new Recents();
            frags[4] = new LockScreen();
            frags[5] = new System();
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	return titles[position];
    
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }


    }

    private String[] getTitles() {
        String titleString[];
        if (!DeviceUtils.isPhone(getActivity())) {
        titleString = new String[]{
                    getString(R.string.generalui_category),
                    getString(R.string.statusbar_category),
                    getString(R.string.navigation_category),
                    getString(R.string.multitasking_category),
                    getString(R.string.lockscreen_category),
                    getString(R.string.system_category)};
        } else {
        titleString = new String[]{
                    getString(R.string.generalui_category),
                    getString(R.string.statusbar_category),
                    getString(R.string.navigation_category),
                    getString(R.string.multitasking_category),
                    getString(R.string.lockscreen_category),
                    getString(R.string.system_category)};
        }
        return titleString;
    }
}