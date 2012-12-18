package com.alienlabz.packagez.ui.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Pack;
import com.alienlabz.packagez.util.CursorList;
import com.alienlabz.packagez.util.DateUtil;
import com.alienlabz.packagez.util.FontUtil;
import com.alienlabz.packagez.util.Strings;

public class PackageAdapter extends BaseAdapter {
	private Context context;
	private List<Pack> packages;
	private PackagesAdapterListener listener;

	public PackageAdapter(final Context context, final CursorList<Pack> packages) {
		this.context = context;
		this.packages = packages;
	}

	public void setListener(PackagesAdapterListener lis) {
		this.listener = lis;
	}

	@Override
	public int getCount() {
		return packages.size();
	}

	@Override
	public Object getItem(int position) {
		return packages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return packages.get(position).id;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final Pack pack = (Pack) getItem(position);

		if (convertView == null) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.package_list_item, null);
		}

		final View resultView = convertView;

		final TextView type = (TextView) convertView.findViewById(R.id.package_line_item_textview_incoming);
		final ImageView carrier = (ImageView) convertView.findViewById(R.id.package_line_item_imageview_carrier);
		final TextView days = (TextView) convertView.findViewById(R.id.package_line_item_textview_days);
		final TextView code = (TextView) convertView.findViewById(R.id.package_line_item_textview_code);
		final TextView description = (TextView) convertView.findViewById(R.id.package_line_item_textview_description);
		final TextView status = (TextView) convertView.findViewById(R.id.package_line_item_textview_status);
		final TextView date = (TextView) convertView.findViewById(R.id.package_line_item_textview_time);
		final CheckBox check = (CheckBox) convertView.findViewById(R.id.package_line_item_checkbox);

		FontUtil.setFutura(context.getAssets(), code, description, status);

		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				listener.itemChecked(position, pack, resultView, isChecked);
			}

		});

		calculateDays(pack, days);
		setCategory(pack, type);
		setCarrier(pack, carrier);
		date.setText(DateUtil.format(pack.lastDate));
		code.setText(pack.code);
		status.setText(Strings.firstInUpperCase(pack.status.getTranslatedStatus(context)));
		setDescription(description, pack);

		return convertView;
	}

	private void setDescription(final TextView description, final Pack pack) {
		String strDescription = pack.description;
		if (Strings.isEmpty(strDescription)) {
			strDescription = context.getResources().getString(R.string.default_empty_string);
		}
		description.setText(strDescription);
	}

	private void setCategory(Pack pack, TextView type) {
		type.setText(pack.category.name);
	}

	private void setCarrier(Pack pack, ImageView carrier) {
		carrier.setImageResource(pack.carrier.logoResource);
	}

	private void calculateDays(final Pack pack, final TextView days) {
		final String d = context.getResources().getString(R.string.days);

		if (pack.delivered && !Strings.isEmpty(pack.lastDate) && !Strings.isEmpty(pack.firstDate)) {

			days.setText(String.valueOf(DateUtil.calculateDays(DateUtil.parse(pack.firstDate),
					DateUtil.parse(pack.lastDate)))
					+ " " + d);

		} else if (!Strings.isEmpty(pack.firstDate)) {
			days.setText(String.valueOf(DateUtil.calculateDays(DateUtil.parse(pack.firstDate), new Date())) + " " + d);
		} else {
			days.setText("-");
		}
	}

	/**
	 * Listen to Adapter specific events.
	 * 
	 * @author Marlon Silva Carvalho
	 * @since 1.0.0
	 */
	public interface PackagesAdapterListener {
		void itemChecked(int position, Pack pack, View view, boolean checked);
	}

}
