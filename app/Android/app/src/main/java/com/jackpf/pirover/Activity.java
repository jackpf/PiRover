package com.jackpf.pirover;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public abstract class Activity extends android.app.Activity
{
    /**
     * Prefs manager instance
     */
    protected SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        onCreate(savedInstanceState, true);
    }

    protected void onCreate(Bundle savedInstanceState, boolean homeAsUp)
    {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(homeAsUp);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                try {
                    NavUtils.navigateUpFromSameTask(this);
                } catch (IllegalArgumentException e) {
                    finish();
                }
                return true;
            case R.id.action_settings:
                startActivity(SettingsActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Launch blank activity
     */
    protected void startActivity(Class<?> c)
    {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
