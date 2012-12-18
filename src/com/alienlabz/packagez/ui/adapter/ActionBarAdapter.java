package com.alienlabz.packagez.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Category;
import com.alienlabz.packagez.util.CursorList;

public class ActionBarAdapter extends BaseAdapter {
	private Context context;
	private CursorList<Category> list;

	public ActionBarAdapter(final Context context, final CursorList<Category> list) {
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
		return position;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.actionbar_spinner_item, null);
		}

		final Category category = (Category) getItem(position);
		final TextView desc = (TextView) convertView.findViewById(R.id.actionbar_spinner_item_text1);
		desc.setText(category.name);

		return convertView;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.actionbar_spinner_navigation, null);
		}

		final Category category = (Category) getItem(position);
		final TextView desc = (TextView) convertView.findViewById(R.id.actionbar_spinner_navigation_item);
		desc.setText(category.name);

		return convertView;
	}

}
