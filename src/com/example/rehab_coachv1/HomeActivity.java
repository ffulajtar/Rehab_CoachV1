package com.example.rehab_coachv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeActivity extends Activity {

	String[] values = new String[] { "Go to grocery store", "Hang out with friends", "Visit family", "Go to work", "Go to school", "Go to laundry", "Go to bank", "Go to church", "Spend time with spouse", "Spend time with best friend", "Go out to lunch", "Go for a run", "Go to the gym", "Use Lumosity", "Go to the library", "Meet with support group", "Play instrument", "Draw a picture", "Go to doctor", "Go see a movie"};
	int theme = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		theme = getIntent().getIntExtra("theme", 0);
		if (theme == 0)
		{
			setTheme(android.R.style.Theme_Holo_Light);
		}
		else
		{
			setTheme(android.R.style.Theme_Holo);
		}
		setContentView(R.layout.activity_home);
		ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.home_list_layout, values);
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent remind = new Intent (HomeActivity.this, ReminderActivity.class);
				remind.putExtra("act", values[position]);
				remind.putExtra("theme", theme);
				startActivity(remind);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (theme == 1)
		{
			getMenuInflater().inflate(R.menu.dark, menu);
		}
		else
		{
			getMenuInflater().inflate(R.menu.light, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {

	        case R.id.home_screen:
	            openHome();
	            return true;
	        case R.id.profile_screen:
	            openProfile();
	            return true;
	        case R.id.help_screen:
	            openHelp();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void openHome() {
		Intent remind = new Intent (this, HomeActivity.class);
		remind.putExtra("theme", theme);
		startActivity(remind);
		
	}

	public void openProfile() {
		Intent remind = new Intent (this, ProfileActivity.class);
		remind.putExtra("theme", theme);
		startActivity(remind);
		
	}

	public void openHelp() {
		Intent remind = new Intent (this, HelpActivity.class);
		remind.putExtra("theme", theme);
		startActivity(remind);	
	}

	public void changetheme(View view)
	{
		if (theme == 0)
		{
			theme = 1;
			Intent remind = new Intent (this, HomeActivity.class);
			remind.putExtra("theme", theme);
			startActivity(remind);
		}
		else
		{
			theme = 0;
			Intent remind = new Intent (this, HomeActivity.class);
			remind.putExtra("theme", theme);
			startActivity(remind);
		}
	}

}
