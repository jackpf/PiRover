package com.jackpf.pirover;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.jackpf.pirover.Model.UI;

import java.util.HashMap;
import java.util.Map;

public abstract class Activity extends android.app.Activity
{
    /**
     * Prefs manager instance
     */
    protected SharedPreferences preferences;

    /**
     * UIs
     */
    private Map<Class, UI> uis = new HashMap<Class, UI>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        onCreate(savedInstanceState, true);
    }

    protected void onCreate(Bundle savedInstanceState, boolean homeAsUp)
    {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(homeAsUp);
        }

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

    /**
     * Initialise user interface
     *
     * @param uis
     */
    protected void initialiseUI(UI... uis)
    {
        for (UI ui : uis) {
            this.uis.put(ui.getClass(), ui);

            ui.initialise();
        }
    }

    /**
     * Get user interface instance
     *
     * @param c
     * @return
     * @throws UINotFoundException
     */
    protected UI getUI(Class c) throws UINotFoundException
    {
        if (uis.containsKey(c)) {
            return uis.get(c);
        } else {
            throw new UINotFoundException(c.getName() + " was not found or has not been initialised");
        }
    }

    /**
     * User interface not found exception
     */
    public class UINotFoundException extends RuntimeException
    {
        public UINotFoundException(String message)
        {
            super(message);
        }

        public UINotFoundException(String message, Exception e)
        {
            super(message, e);
        }
    }
}
