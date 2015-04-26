package com.engstuff.coloriphornia.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

import java.util.Set;

public class FragmentAppSettings extends PreferenceFragment {

    Preference emailPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        emailPref = findPreference(getString(R.string.prefs_user_saved_emails));

        emailPref.setSummary(savedEmails() + "\n" + getString(R.string.prers_summary));

        findPreference(getString(R.string.prefs_user_saved_emails))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {

                        new DialogFragmentSavedEmails().show(getFragmentManager(), null);
                        return true;
                    }
                });
    }

    private String savedEmails() {

        Set<String> strings = PrefsHelper.readFromPrefsAll(getActivity(), Cv.SAVED_EMAILS).keySet();

        StringBuilder sb = new StringBuilder();

        for (String s : strings) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }
}