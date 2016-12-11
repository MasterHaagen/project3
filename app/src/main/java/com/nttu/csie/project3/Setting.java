package com.nttu.csie.project3;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Setting extends PreferenceActivity{
    private static final String data = "DATA";
    private static final String name = "NAME";
    private static final String phone = "PHONE";
    SharedPreferences prefs;
    EditTextPreference editTextPreference_name;
    EditTextPreference editTextPreference_phone;
    Preference button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mypreference);

        prefs = getSharedPreferences(data, MODE_PRIVATE);

        editTextPreference_name=(EditTextPreference)findPreference("preference_name");
        editTextPreference_phone=(EditTextPreference)findPreference("preference_phone");
        button = (Preference)getPreferenceManager().findPreference("button");

        if(prefs != null) {
            editTextPreference_name.setSummary(prefs.getString(name, ""));
            editTextPreference_phone.setSummary(prefs.getString(phone, ""));
        } else {
            editTextPreference_name.setSummary("Setting your username");
            editTextPreference_phone.setSummary("Setting your phone");
        }

        editTextPreference_name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // TODO Auto-generated method stub
                prefs = getSharedPreferences(data,0);
                prefs.edit()
                        .putString(name,newValue.toString())
                        .commit();
                editTextPreference_name.setSummary(newValue.toString());
                editTextPreference_name.setDefaultValue(newValue);
                editTextPreference_name.setText(newValue.toString());
                return false;
            }
        });

        editTextPreference_phone.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // TODO Auto-generated method stub
                prefs = getSharedPreferences(data,0);
                prefs.edit()
                        .putString(phone,newValue.toString())
                        .commit();
                editTextPreference_phone.setSummary(newValue.toString());
                editTextPreference_phone.setDefaultValue(newValue);
                editTextPreference_phone.setText(newValue.toString());
                return false;
            }
        });

        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                prefs.edit()
                        .clear()
                        .commit();
                editTextPreference_name.setSummary("Setting your username");
                editTextPreference_name.setDefaultValue("Username");
                editTextPreference_name.setText("Username");
                editTextPreference_phone.setSummary("Setting your phone");
                editTextPreference_phone.setDefaultValue("0900000000");
                editTextPreference_phone.setText("0900000000");
                Toast.makeText(getApplicationContext(), "所有資料已清除", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
