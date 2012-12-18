package com.alienlabz.packagez.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Pack;
import com.alienlabz.packagez.model.Status;

public class SlidingMenuAdapter extends BaseAdapter {
	private Context context;
	private List<Status> data = new ArrayList<Status>();

	public SlidingMenuAdapter(final Context context, final List<Status> status) {
		this.context = context;
		this.data = status;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.sliding_menu_options_line, null);
		}

		final Status item = (Status) getItem(position);

		final TextView text = (TextView) convertView.findViewById(R.id.sm_options_text);
		text.setText(item.getTranslatedStatus(context));

		final TextView count = (TextView) convertView.findViewById(R.id.sm_options_count);
		count.setText(String.valueOf(Pack.countByStatus(item)));

		final ImageView image = (ImageView) convertView.findViewById(R.id.sm_options_img);
		image.setImageResource(item.getSmallDrawable(context));

		return convertView;
	}

}
