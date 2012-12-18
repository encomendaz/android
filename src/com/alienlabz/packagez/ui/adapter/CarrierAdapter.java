package com.alienlabz.packagez.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Carrier;

/**
 * Adapter for Carriers.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class CarrierAdapter extends BaseAdapter {
	private List<Carrier> list;
	private Context context;

	public CarrierAdapter(final Context context, final List<Carrier> list) {
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
		return list.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.carrier_list_item, null);
		}

		final TextView textView = (TextView) convertView.findViewById(R.id.carrier_item_text1);
		textView.setText(((Carrier) getItem(position)).name);

		return convertView;
	}

}
