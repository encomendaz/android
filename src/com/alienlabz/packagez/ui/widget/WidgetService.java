package com.alienlabz.packagez.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
	public static final String EXTRA_UPDATE_FROM_ACTIVITY = "com.alienlabz.packagez.EXTRA_UPDATE_FROM_ACTIVITY";

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return (new WidgetFactory(this.getApplicationContext(), intent));
	}

	public static void updateWidget(Context context) {
		int widgetIDs[] = AppWidgetManager.getInstance(context.getApplicationContext()).getAppWidgetIds(
				new ComponentName(context.getApplicationContext(), WidgetProvider.class));

		Intent intent = new Intent(context, WidgetProvider.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIDs);
		intent.putExtra(EXTRA_UPDATE_FROM_ACTIVITY, true);
		context.sendBroadcast(intent);
	}

}
