package es.moncadaisla.eurideudas.activities;


import es.moncadaisla.eurideudas.R;
import es.moncadaisla.eurideudas.R.xml;
import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class PrefsActivity extends PreferenceActivity{
 
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   ActionBar actionBar = getActionBar();
   actionBar.setDisplayHomeAsUpEnabled(true);
   actionBar.setHomeButtonEnabled(true);
   addPreferencesFromResource(R.xml.prefs);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}
}