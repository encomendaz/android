package com.alienlabz.packagez.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Track;
import com.alienlabz.packagez.util.CursorList;
import com.alienlabz.packagez.util.DateUtil;
import com.alienlabz.packagez.util.Strings;

public class TrackingAdapter extends BaseAdapter {
	private Context context;
	private CursorList<Track> list;

	public TrackingAdapter(final Context context, final CursorList<Track> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		Track track = (Track) getItem(position);
		return track.id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.tracking_list_item, null);
		}

		final TextView status = (TextView) convertView.findViewById(R.id.tracking_line_item_textview_status);
		final TextView time = (TextView) convertView.findViewById(R.id.tracking_line_item_textview_time);
		final TextView description = (TextView) convertView.findViewById(R.id.tracking_line_item_textview_description);
		final ImageView image = (ImageView) convertView.findViewById(R.id.tracking_line_item_imageview_status);
		final TextView citystate = (TextView) convertView.findViewById(R.id.tracking_line_item_textview_citystate);

		final Track track = (Track) getItem(position);
		status.setText(track.status.getTranslatedStatus(context));
		time.setText(DateUtil.format(track.date));
		image.setImageResource(track.status.getBigDrawable(context));

		String strCityState = context.getResources().getString(R.string.default_empty_string);
		if (!Strings.isEmpty(track.city)) {
			strCityState = track.city + "/" + track.state;
		}
		citystate.setText(strCityState);

		String strDescription = track.description;
		if (Strings.isEmpty(strDescription)) {
			strDescription = context.getResources().getString(R.string.default_empty_string);
		}
		description.setText(strDescription);

		return convertView;
	}

}
