package com.nuclear.nucleartweaks.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SeekBarPreference;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Display;
import android.view.Window;
import android.widget.Toast;
import com.android.internal.logging.MetricsLogger;


import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;

import java.io.File;
import java.io.IOException;

public class System extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "System";

    private static final String KEY_SYNAPSE = "key_synapse";
    private static final String KEY_KERNEL_ADIUTOR = "key_kernel_adiutor";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.system);
        PreferenceScreen prefSet = getPreferenceScreen();

        ContentResolver resolver = getActivity().getContentResolver();

        Boolean checkPerformance = Settings.System.getInt(getContentResolver(), Settings.System.PERFORMANCE_APP, 0) == 1;

        if(checkPerformance) {
           PreferenceScreen synapse = (PreferenceScreen) findPreference(KEY_SYNAPSE);
           getPreferenceScreen().removePreference(synapse);
        } else {
           PreferenceScreen kernelAdiutor = (PreferenceScreen) findPreference(KEY_KERNEL_ADIUTOR);
           getPreferenceScreen().removePreference(kernelAdiutor);
        }
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.NUCLEARTWEAKS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals(KEY_SYNAPSE)) {
             final String appPackageName = "com.af.synapse";
             try {
                 getActivity().getPackageManager().getPackageInfo(appPackageName, 0);
             } catch (PackageManager.NameNotFoundException e) {
                 try {
                     getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                 } catch (ActivityNotFoundException ex) {
                     getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                 }
                 return true;
             }
             return false;
         } else if (preference.getKey().equals(KEY_KERNEL_ADIUTOR)) {
             final String appPackageName = "com.grarak.kerneladiutor";
             try {
                 getActivity().getPackageManager().getPackageInfo(appPackageName, 0);
             } catch (PackageManager.NameNotFoundException e) {
                 try {
                     getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                 } catch (ActivityNotFoundException ex) {
                     getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                 }
                 return true;

             }
             return false;
         }
         return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return true;
    }

}