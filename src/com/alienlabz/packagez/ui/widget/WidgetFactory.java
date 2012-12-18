package com.alienlabz.packagez.ui.widget;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Pack;
import com.alienlabz.packagez.util.CursorList;
import com.alienlabz.packagez.util.DateUtil;
import com.alienlabz.packagez.util.Strings;

public class WidgetFactory implements RemoteViewsFactory {
	private Context context;
	private CursorList<Pack> packages;

	public WidgetFactory(Context context, Intent intent) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return packages.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	// FIXME Duplicado em PackageAdapter.java
	private String calculateDays(final Pack pack) {
		final String d = context.getResources().getString(R.string.days);
		String result = "";

		if (pack.delivered && !Strings.isEmpty(pack.lastDate) && !Strings.isEmpty(pack.firstDate)) {
			result = String.valueOf(DateUtil.calculateDays(DateUtil.parse(pack.firstDate),
					DateUtil.parse(pack.lastDate)))
					+ " " + d;
		} else if (!Strings.isEmpty(pack.lastDate)) {
			result = String.valueOf(DateUtil.calculateDays(DateUtil.parse(pack.lastDate), new Date())) + " " + d;
		} else {
			result = "-";
		}

		return result;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_package_list_item);

		row.setTextViewText(R.id.package_line_item_textview_code, packages.get(position).code);
		row.setTextViewText(R.id.package_line_item_textview_description, packages.get(position).description);
		row.setTextViewText(R.id.package_line_item_textview_status,
				packages.get(position).status.getTranslatedStatus(context));
		row.setTextViewText(R.id.package_line_item_textview_days, calculateDays(packages.get(position)));
		row.setTextViewText(R.id.package_line_item_textview_time, DateUtil.format(packages.get(position).lastDate));

		Intent i = new Intent();
		Bundle extras = new Bundle();

		// FIXME
		extras.putString("EXTRA_WORD", packages.get(position).code);
		i.putExtras(extras);
		row.setOnClickFillInIntent(android.R.id.text1, i);

		return (row);
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDataSetChanged() {
		packages = Pack.findAll();
	}

	@Override
	public void onDestroy() {
	}

}
