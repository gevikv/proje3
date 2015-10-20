package barqsoft.footballscores;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WidgetIntentService extends IntentService {


    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "barqsoft.footballscores.action.FOO";
    private static final String ACTION_BAZ = "barqsoft.footballscores.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "barqsoft.footballscores.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "barqsoft.footballscores.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WidgetIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WidgetIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        // get all widgets
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                football_widget.class));

        // Get today's date
        Date todayDate = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String date_today = mformat.format(todayDate);

        // Get today's match score from the ContentProvider
        Cursor today_data = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, new String[]{date_today}, null);


        if (today_data == null) {
            return;
        }
        if (!today_data.moveToFirst()) {
            today_data.close();
            return;
        }

        // Extract the match data from the Cursor
        String homeName = today_data.getString(scoresAdapter.COL_HOME);
        String awayName = today_data.getString(scoresAdapter.COL_AWAY);
        String score = Utilies.getScores(today_data.getInt(scoresAdapter.COL_HOME_GOALS),
                today_data.getInt(scoresAdapter.COL_AWAY_GOALS));
        String time = today_data.getString(scoresAdapter.COL_MATCHTIME);

        today_data.close();

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);

            // Add the data to the RemoteViews

            views.setTextViewText(R.id.home_name, homeName);
            views.setTextViewText(R.id.away_name, awayName);
            views.setTextViewText(R.id.result, score);
            views.setTextViewText(R.id.time, time);

            // Content Descriptions for RemoteViews were only added in ICS MR1
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {

                views.setContentDescription(R.id.home_name,"home team name is"+ homeName);
                views.setContentDescription(R.id.away_name, "away team name is "+awayName);
                views.setContentDescription(R.id.result,"the result is" + score);
                views.setContentDescription(R.id.time,"the game time is"+ time);
            }

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
           views.setOnClickPendingIntent(R.id.mywidget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
