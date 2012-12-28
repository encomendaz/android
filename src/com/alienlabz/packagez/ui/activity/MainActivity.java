package com.alienlabz.packagez.ui.activity;

import java.util.Date;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alienlabz.packagez.App;
import com.alienlabz.packagez.GCMIntentService;
import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Carrier;
import com.alienlabz.packagez.model.Category;
import com.alienlabz.packagez.model.Pack;
import com.alienlabz.packagez.model.Status;
import com.alienlabz.packagez.service.CheckStatusService;
import com.alienlabz.packagez.ui.adapter.ActionBarAdapter;
import com.alienlabz.packagez.ui.adapter.SlidingMenuAdapter;
import com.alienlabz.packagez.ui.dialog.PackageDialog;
import com.alienlabz.packagez.ui.fragment.PackagesListFragment;
import com.alienlabz.packagez.ui.fragment.PackagesListFragment.PackageListener;
import com.alienlabz.packagez.ui.task.GCMRegisterTask;
import com.alienlabz.packagez.ui.widget.WidgetService;
import com.alienlabz.packagez.util.CursorList;
import com.alienlabz.packagez.util.DateUtil;
import com.alienlabz.packagez.util.Strings;
import com.google.android.gcm.GCMRegistrar;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

/**
 * Responsible to show application's main screen.
 * 
 * We're using SlidingActivity from SlidingMenu Project to be able to show a nice sliding menu! :-)
 * https://github.com/jfeinstein10/SlidingMenu
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class MainActivity extends SlidingActivity implements PackageDialog.PackageDialogListener, PackageListener {

	/** Messenger responsible to receive messages from status checker service **/
	private Messenger clientMessenger = new Messenger(new StatusIncomingHandler());

	/** Fragment showing the package listing. **/
	private PackagesListFragment packagesListFragment;

	private PackageDialog dialogEdit;
	private MenuItem refreshItem;
	private ListView listViewSlidingMenu;
	private TextView lastSync;
	private TextView packsCount;
	private int count = 0;
	private CursorList<Pack> lastPacks = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.sliding_menu);

		lastSync = (TextView) getSlidingMenu().findViewById(R.id.sm_last_sync);
		listViewSlidingMenu = (ListView) getSlidingMenu().findViewById(R.id.sm_listview_categories);

		configureActionBar();

		if (packagesListFragment == null) {
			packagesListFragment = new PackagesListFragment();
		}

		getFragmentManager().beginTransaction().add(android.R.id.content, packagesListFragment).commit();

		configureGCM();
	}

	/**
	 * Configuring Google Cloud Messaging.
	 */
	private void configureGCM() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		String regId = GCMRegistrar.getRegistrationId(this);

		if (Strings.isEmpty(regId)) {
			GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
		} else {
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				Toast.makeText(getApplicationContext(), "Already registered with GCM!", Toast.LENGTH_LONG).show();
			} else {

				// Calling our server.
				new GCMRegisterTask(this) {

					protected void onPostExecute(Boolean result) {
						Toast.makeText(mContext, "N‹o foi poss’vel realizar o registro!", Toast.LENGTH_LONG).show();
					}

				}.execute("Marlon Silva Carvalho", "marlon.carvalho@gmail.com", regId);

			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_add) {
			if (dialogEdit == null) {
				dialogEdit = new PackageDialog();
			}

			dialogEdit.show(getFragmentManager(), "fragment_edit_package");
		} else if (item.getItemId() == android.R.id.home) {
			toggle();
		} else if (item.getItemId() == R.id.menu_refresh) {
			callCheckStatusService("");
		} else if (item.getItemId() == R.id.menu_register) {

		} else if (item.getItemId() == R.id.menu_unregister) {
			GCMRegistrar.unregister(this);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		try {
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processNFCIntent(getIntent());
		}

		configureAndPopulateSlidingMenu();
		configureAndPopulateNavigationList();

		final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
		lastSync.setText(shared.getString(App.PREF_LAST_SYNC, getResources().getString(R.string.never)));
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	private void configurePackagesCount() {
		packsCount.setText(String.valueOf(count));
	}

	private void processNFCIntent(Intent intent) {
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage message = (NdefMessage) rawMsgs[0];
		Toast.makeText(this, String.valueOf(message.getRecords()[0].getPayload()), Toast.LENGTH_LONG).show();
	}

	/**
	 * Configuring ActionBar.
	 */
	private void configureActionBar() {
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar_customview);
		packsCount = (TextView) getActionBar().getCustomView().findViewById(R.id.actionbar_customview_count_packz);
	}

	/**
	 * Configuring Sliding Menu.
	 */
	private void configureAndPopulateSlidingMenu() {
		final SlidingMenu sm = getSlidingMenu();

		sm.setShadowDrawable(R.drawable.shadow);
		sm.setShadowWidth(15);
		sm.setBehindOffset(150);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		listViewSlidingMenu.setAdapter(new SlidingMenuAdapter(this, Status.all()));
		listViewSlidingMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				final Status status = (Status) listViewSlidingMenu.getItemAtPosition(position);
				setPackages(Pack.findByStatus(status));
				toggle();
			}

		});
	}

	/**
	 * Configure Navigation List.
	 */
	private void configureAndPopulateNavigationList() {
		final ActionBarAdapter adapter = new ActionBarAdapter(this, Category.findAll(this));
		getActionBar().setListNavigationCallbacks(adapter, new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				final Category category = (Category) adapter.getItem(itemPosition);
				setPackages(Pack.findByCategory(category));
				return true;
			}

		});
	}

	private void setPackages(CursorList<Pack> packs) {
		if (packs == null) {
			packs = lastPacks;
			if (packs == null) {
				packs = Pack.findAll();
			}
		}

		packagesListFragment.setPackages(packs);
		count = packs.size();
		configurePackagesCount();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		this.refreshItem = menu.findItem(R.id.menu_refresh);
		MenuItem search = menu.findItem(R.id.menu_search);
		SearchView searchView = (SearchView) search.getActionView();
		searchView.setSoundEffectsEnabled(true);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				packagesListFragment.setPackages(Pack.findPackages(newText));
				return true;
			}

		});
		return true;
	}

	@Override
	public void savePackage(final String code, final String description, final Long carrier, final Category category) {

		long count = Pack.countByCode(this, code);
		if (count > 0) {
			Toast.makeText(this, R.string.package_already_exists, Toast.LENGTH_SHORT).show();
			return;
		}

		final Pack pack = new Pack();
		pack.code = code;
		pack.description = description;
		pack.carrier = Carrier.findById(carrier.longValue());
		pack.delivered = false;
		pack.category = category;
		pack.insert();

		dialogEdit.clearFields();

		callCheckStatusService(code);

		setPackages(null);
		configureAndPopulateSlidingMenu();
		configureAndPopulateNavigationList();

		dialogEdit.dismiss();
	}

	@Override
	public void packageSelected(final String code) {
		final Intent intent = new Intent(this, TrackingsActivity.class);
		intent.putExtra(CheckStatusService.EXTRA_PACKAGE_CODE, code);
		startActivity(intent);
		overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
	}

	@Override
	public void refreshPackagesList() {
		callCheckStatusService("");
	}

	private void callCheckStatusService(final String code) {
		this.refreshItem.setActionView(R.layout.progress_action_item);
		this.refreshItem.setEnabled(false);

		final Intent intent = new Intent(this, CheckStatusService.class);
		intent.putExtra(CheckStatusService.EXTRA_PACKAGE_CODE, code);
		intent.putExtra(CheckStatusService.EXTRA_CLIENT_MESSENGER, clientMessenger);
		startService(intent);
	}

	@Override
	public void forceDelete() {
		Pack.forceDelete();
		configureAndPopulateNavigationList();
	}

	@Override
	public void deletePackages(final Set<Pack> packs) {
		for (Pack pack : packs) {
			Pack.delete(pack.code);
		}

		packs.clear();

		setPackages(null);
		configureAndPopulateSlidingMenu();
		configureAndPopulateNavigationList();
		WidgetService.updateWidget(this);
	}

	@Override
	public void restorePackages() {
		Pack.undelete();
		setPackages(null);
	}

	/**
	 * Handler responsible to receive messages from Check Status Service.
	 * 
	 * @author Marlon Silva Carvalho
	 * @since 1.0.0
	 */
	@SuppressLint("HandlerLeak")
	class StatusIncomingHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			setPackages(null);

			refreshItem.setActionView(null);
			refreshItem.setEnabled(true);
			packagesListFragment.refreshEnded();
			configureAndPopulateSlidingMenu();

			final String dateLastSync = DateUtil.format(new Date(), MainActivity.this);
			lastSync.setText(dateLastSync);

			final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
			shared.edit().putString(App.PREF_LAST_SYNC, dateLastSync).commit();
			WidgetService.updateWidget(MainActivity.this);
		}

	}

	@Override
	public void archive(Set<Pack> packages) {
		for (Pack pack : packages) {
			pack.category = Category.findById(Category.CATEGORY_ARCHIVED);
			pack.update();
		}

		packages.clear();
		setPackages(null);
		configureAndPopulateSlidingMenu();
		configureAndPopulateNavigationList();
		WidgetService.updateWidget(this);
	}

}
