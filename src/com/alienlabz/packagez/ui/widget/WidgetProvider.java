package com.alienlabz.packagez.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.alienlabz.packagez.R;

public class WidgetProvider extends AppWidgetProvider {
	private Intent intent;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int size = appWidgetIds.length;

		for (int index = 0; index < size; index++) {
			int appWidgetId = appWidgetIds[index];

			Intent svcIntent = new Intent(context, WidgetService.class);
			svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

			RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.widget);
			widget.setRemoteAdapter(R.id.widget_listview, svcIntent);

			if (intent.getExtras().getBoolean(WidgetService.EXTRA_UPDATE_FROM_ACTIVITY)) {
				appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);
			}
			appWidgetManager.updateAppWidget(appWidgetId, widget);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		this.intent = intent;
		super.onReceive(context, intent);
	}

}
