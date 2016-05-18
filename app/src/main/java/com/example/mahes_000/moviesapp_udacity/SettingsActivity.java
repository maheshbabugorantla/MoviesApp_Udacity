package com.example.mahes_000.moviesapp_udacity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by mahes_000 on 5/18/2016.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Here we add the general preferences created using the XML File
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            // Adding the Resources from the settings xml file
            addPreferencesFromResource(R.xml.settings_preferences);
            new SettingsActivity().bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_choice_key)));
        }
    }

    protected void bindPreferenceSummaryToValue(Preference preference)
    {
        //Set the listener to watch for the preference changes
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener Immediately with preference's Current Value
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(),""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        String stringValue = newValue.toString();

        // For list preferences, look up the correct display value in the preference's entries list (since they have separate labels/values).
        if(preference instanceof ListPreference)
        {
            ListPreference listPreference = (ListPreference) preference;

            // finding the Index of the Selected value
            int prefIndex = listPreference.findIndexOfValue(stringValue);

            // Setting the Summary of the Selected Option on the List Preference
            if (prefIndex >= 0)
            {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }

        return true;
    }
}
