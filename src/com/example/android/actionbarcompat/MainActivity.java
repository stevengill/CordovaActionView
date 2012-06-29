/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.actionbarcompat;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.DroidGap;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.IPlugin;
import org.apache.cordova.api.LOG;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements CordovaInterface {
	private boolean mAlternateTitle = false;
	private boolean bound;
	private boolean volumeupBound;
	private boolean volumedownBound;

	String TAG = "MainActivity-ActionBarTest";
	private IPlugin activityResultCallback;
	private Object activityResultKeepRunning;
	private Object keepRunning;

	CordovaWebView mainView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mainView = (CordovaWebView) findViewById(R.id.mainView);
		mainView.loadUrl("file:///android_asset/www/index.html");

		/*
		 * findViewById(R.id.toggle_title).setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View view) { if (mAlternateTitle) {
		 * setTitle(R.string.app_name); } else {
		 * setTitle(R.string.alternate_title); } mAlternateTitle =
		 * !mAlternateTitle; } });
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

		// Calling super after populating the menu is necessary here to ensure
		// that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_refresh:
			Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT)
					.show();
			getActionBarHelper().setRefreshActionItemState(true);
			getWindow().getDecorView().postDelayed(new Runnable() {
				@Override
				public void run() {
					getActionBarHelper().setRefreshActionItemState(false);
				}
			}, 1000);
			break;

		case R.id.menu_search:
			Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_share:
			Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Override the backbutton.
	 * 
	 * @param override
	 */
	public void bindBackButton(boolean override) {
		Log.w("back button override", "override");
		this.bound = override;
	}

	/**
	 * Determine of backbutton is overridden.
	 * 
	 * @return
	 */
	public boolean isBackButtonBound() {
		return this.bound;
	}

	public void bindButton(String button, boolean override) {
		// TODO Auto-generated method stub
		if (button.compareTo("volumeup") == 0) {
			this.volumeupBound = override;
		} else if (button.compareTo("volumedown") == 0) {
			this.volumedownBound = override;
		}
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	/**
	 * Called when a message is sent to plugin.
	 * 
	 * @param id
	 *            The message id
	 * @param data
	 *            The message data
	 * @return Object or null
	 */
	public Object onMessage(String id, Object data) {
		LOG.d(TAG, "onMessage(" + id + "," + data + ")");
		if ("exit".equals(id)) {
			super.finish();
		}
		return null;
	}

	@Override
	public void setActivityResultCallback(IPlugin plugin) {
		this.activityResultCallback = plugin;
	}

	/**
	 * Launch an activity for which you would like a result when it finished.
	 * When this activity exits, your onActivityResult() method will be called.
	 * 
	 * @param command
	 *            The command object
	 * @param intent
	 *            The intent to start
	 * @param requestCode
	 *            The request code that is passed to callback to identify the
	 *            activity
	 */
	public void startActivityForResult(IPlugin command, Intent intent,
			int requestCode) {
		this.activityResultCallback = command;
		this.activityResultKeepRunning = this.keepRunning;

		// If multitasking turned on, then disable it for activities that return
		// results
		if (command != null) {
			this.keepRunning = false;
		}

		// Start activity
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void cancelLoadUrl() {
		// This is a no-op.
	}

	public void onDestroy() {
		super.onDestroy();
		if (mainView.pluginManager != null) {
			mainView.pluginManager.onDestroy();
		}
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
