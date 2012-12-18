package com.alienlabz.packagez.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Track;
import com.alienlabz.packagez.ui.adapter.TrackingAdapter;
import com.alienlabz.packagez.util.CursorList;

public class TrackingsListFragment extends Fragment {
	private ListView listView;
	private TextView textView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_trackings_list, null);
		listView = (ListView) view.findViewById(R.id.listview_trackings);
		textView = (TextView) view.findViewById(R.id.trackings_alert_text);
		return view;
	}

	public void loadTrackings(final CursorList<Track> list) {
		if (list.size() <= 0) {
			listView.setVisibility(View.GONE);
			textView.setVisibility(View.VISIBLE);
		} else {
			listView.setAdapter(new TrackingAdapter(getActivity(), list));
		}
	}

}
