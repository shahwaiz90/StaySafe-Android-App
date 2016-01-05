package com.devtuts.staysafe;
    
import com.devtuts.helpme.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider; 
import android.content.Context;
import android.content.Intent;  
import android.widget.RemoteViews;

public class AppWidgett extends AppWidgetProvider {
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}  

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub

		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) { 
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction(); 
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else { 
			 super.onReceive(context, intent);
		}
		 
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {  
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		 
		
		final int N = appWidgetIds.length;
		
		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i=0; i<N; i++) {
		    int appWidgetId = appWidgetIds[i]; 
		    
		    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_button);
		    
		    Intent intent = new Intent(context, WidgetService.class); 
		    intent.setAction("ONCLICK");
		    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		
		    views.setOnClickPendingIntent(R.id.widgetBtn, pendingIntent); 
		  
		    // Tell the AppWidgetManager to perform an update on the current App Widget
		    appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}
