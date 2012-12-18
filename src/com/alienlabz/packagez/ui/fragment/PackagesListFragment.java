package com.alienlabz.packagez.ui.fragment;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Pack;
import com.alienlabz.packagez.ui.adapter.PackageAdapter;
import com.alienlabz.packagez.ui.adapter.PackageAdapter.PackagesAdapterListener;
import com.alienlabz.packagez.ui.listener.SwipeListViewTouchListener;
import com.alienlabz.packagez.util.CursorList;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Showing and handling Packages Listing.
 * 
 * @author Marlon Silva Carvalho
 * @version 1.0.0
 */
public class PackagesListFragment extends Fragment {
	private PullToRefreshListView listView;
	private MultiChoiceModeListener actionModeCallback;
	private Set<Pack> checked = new HashSet<Pack>();
	private ActionMode actionMode;
	private PackageListener callback;
	private ShareActionProvider shareActionProvider;
	private CursorList<Pack> lastPackList;
	private Button undoButton;
	private RelativeLayout undoPanel;
	private TextView undoText;
	private int deleteCount = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_packages_list, null);

		undoText = (TextView) view.findViewById(R.id.undo_count);
		undoButton = (Button) view.findViewById(R.id.undo_button);
		listView = (PullToRefreshListView) view.findViewById(R.id.listview_packages);
		undoPanel = (RelativeLayout) view.findViewById(R.id.undo_panel);

		undoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.restorePackages();
				undoPanel.setVisibility(View.GONE);
			}

		});

		SwipeListViewTouchListener touchListener = new SwipeListViewTouchListener(listView.getRefreshableView(),
				new SwipeListViewTouchListener.OnSwipeCallback() {

					@Override
					public void onSwipeLeft(ListView listView, int[] reverseSortedPositions) {
						swipeToDelete(listView, reverseSortedPositions);
					}

					@Override
					public void onSwipeRight(ListView listView, int[] reverseSortedPositions) {
						swipeToDelete(listView, reverseSortedPositions);
					}

				}, true, true);

		listView.getRefreshableView().setOnTouchListener(touchListener);
		listView.getRefreshableView().setOnScrollListener(touchListener.makeScrollListener());
		configureActionMode();
		configureListView();

		return view;
	}

	private void swipeToDelete(final ListView listView, final int[] reverseSortedPositions) {
		Set<Pack> codes = new HashSet<Pack>();
		for (int pos : reverseSortedPositions) {
			Pack pack = (Pack) listView.getItemAtPosition(pos);
			codes.add(pack);
		}
		delete(codes);
	}

	private void configureListView() {
		listView.getRefreshableView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
				callback.refreshPackagesList();
			}

		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				final Pack pack = (Pack) listView.getRefreshableView().getAdapter().getItem(position);
				callback.packageSelected(pack.code);
			}

		});
	}

	private void archive() {
		callback.archive(checked);
	}

	private void delete(Set<Pack> codes) {
		deleteCount++;
		Set<Pack> delete = codes;

		if (codes == null) {
			delete = checked;
		}

		undoText.setText(delete.size() + " " + getActivity().getResources().getString(R.string.deleted));
		callback.deletePackages(delete);
		undoPanel.setVisibility(View.VISIBLE);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (deleteCount == 1) {
					undoPanel.setVisibility(View.GONE);
					callback.forceDelete();
				}
				deleteCount--;
			}

		}, 5000);
	}

	private void configureActionMode() {
		actionModeCallback = new MultiChoiceModeListener() {

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				if (item.getItemId() == 1) {
					delete(null);
				} else if (item.getItemId() == 2) {
					archive();
				}
				mode.finish();
				return true;
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				menu.add(0, 1, 0, R.string.action_delete);
				menu.add(0, 2, 1, R.string.action_archive);
				MenuItem share = menu.add(0, 3, 2, R.string.action_share);

				shareActionProvider = new ShareActionProvider(getActivity());

				share.setActionProvider(shareActionProvider);
				share.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("text/plain");
				shareActionProvider.setShareIntent(intent);

				actionMode = mode;
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				actionMode = null;
				checked.clear();
				setPackages(lastPackList);
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
			}

		};
	}

	public void setPackages(final CursorList<Pack> list) {
		this.lastPackList = list;

		PackagesAdapterListener listener = new PackagesAdapterListener() {

			@Override
			public void itemChecked(int position, Pack pack, View resultView, boolean isChecked) {
				if (isChecked) {
					checked.add(pack);
					resultView.setBackgroundColor(getActivity().getResources().getColor(
							R.color.package_line_item_color_selected));

					if (actionMode == null) {
						getActivity().startActionMode(actionModeCallback);
					}
				} else {
					resultView.setBackgroundColor(0);
					checked.remove(pack);
					if (checked.size() <= 0) {
						actionMode.finish();
					}
				}
			}
		};

		PackageAdapter adapter = new PackageAdapter(getActivity(), list);
		adapter.setListener(listener);
		listView.setAdapter(adapter);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (PackageListener) activity;
	}

	public void refreshEnded() {
		listView.onRefreshComplete();
	}

	/**
	 * All Activities that uses this Fragment must implement this interface to handle
	 * events from it.
	 * 
	 * @author Marlon Silva Carvalho
	 * @since 1.0.0
	 */
	public interface PackageListener {
		void packageSelected(String code);

		void restorePackages();

		void forceDelete();

		void deletePackages(Set<Pack> packages);

		void refreshPackagesList();

		void archive(Set<Pack> packages);
	}

}
