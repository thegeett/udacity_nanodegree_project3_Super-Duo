package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.FootBallScore;
import barqsoft.footballscores.R;

/**
 * Created by geetthaker on 3/13/16.
 */
public class ScoreWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScoreRemoteViewFactory(this.getApplicationContext(), intent);
    }
}

class ScoreRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<FootBallScore> mScores;
    private int mAppWidgetId;

    public ScoreRemoteViewFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mScores = getFootballScore(mContext);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mScores.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        FootBallScore score = mScores.get(position);

        final int itemId = R.layout.score_fliper_page;
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), itemId);
        rv.setTextViewText(R.id.home_name_textview, score.getHome() + " : " + score.getHomeGoals());
        rv.setTextViewText(R.id.away_name_textview, score.getAway() + " : " + score.getAwayGoals());
        rv.setTextViewText(R.id.time_textview, score.getTime());

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private ArrayList<FootBallScore> getFootballScore(Context context) {
        Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
        String[] mProjection = {DatabaseContract.scores_table.HOME_COL, DatabaseContract.scores_table.AWAY_COL, DatabaseContract.scores_table.DATE_COL, DatabaseContract.scores_table.HOME_GOALS_COL, DatabaseContract.scores_table.AWAY_GOALS_COL, DatabaseContract.scores_table.TIME_COL};
        String mSelectionClause = DatabaseContract.scores_table.DATE_COL + "=?";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(Calendar.getInstance().getTime());
        String[] mSelectionArgs = {today};

        Cursor c = context.getContentResolver().query(uri, mProjection, mSelectionClause, mSelectionArgs, null);
        ArrayList<FootBallScore> scores = new ArrayList<FootBallScore>();
        try {
            if (c != null && c.moveToFirst()) {
                do {
                    FootBallScore score = new FootBallScore();
                    score.setHome(c.getString(c.getColumnIndex(DatabaseContract.scores_table.HOME_COL)));
                    score.setAway(c.getString(c.getColumnIndex(DatabaseContract.scores_table.AWAY_COL)));
                    score.setHomeGoals(c.getInt(c.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL)));
                    score.setAwayGoals(c.getInt(c.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL)));
                    score.setTime(c.getString(c.getColumnIndex(DatabaseContract.scores_table.TIME_COL)));
                    scores.add(score);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return scores;
    }
}
