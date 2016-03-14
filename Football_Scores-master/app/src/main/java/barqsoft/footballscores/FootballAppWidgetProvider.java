package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.ScoreWidgetService;

/**
 * Created by geetthaker on 1/10/16.
 */
public class FootballAppWidgetProvider extends AppWidgetProvider {

    private static String NEXT_ACTION = "next_action";
    private static String PREVIOUS_ACTION = "previous_action";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.football_appwidget);
            final Intent intent = new Intent(context, ScoreWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(appWidgetId, R.id.adapterViewFlipper, intent);

            views.setOnClickPendingIntent(R.id.next_button, getPendingIntent(context, appWidgetId, NEXT_ACTION));
            views.setOnClickPendingIntent(R.id.previous_button, getPendingIntent(context, appWidgetId, PREVIOUS_ACTION));

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private PendingIntent getPendingIntent(Context context, int widgetId, String actionStr) {
        Intent intent = new Intent(context, getClass());
        intent.putExtra("widgetId", widgetId);
        intent.setAction(actionStr);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.football_appwidget);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(NEXT_ACTION)) {
            int widgetId = intent.getIntExtra("widgetId", -1);
            views.showNext(R.id.adapterViewFlipper);
            appWidgetManager.updateAppWidget(new ComponentName(context, getClass()), views);
        } else if (intent.getAction().equals(PREVIOUS_ACTION)) {
            views.showPrevious(R.id.adapterViewFlipper);
            appWidgetManager.updateAppWidget(new ComponentName(context, getClass()), views);
        }
        super.onReceive(context, intent);
    }
}
