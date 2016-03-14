package barqsoft.footballscores;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by geetthaker on 3/13/16.
 */
public class ScoreWidgetAdapter extends ArrayAdapter<FootBallScore> {

    private ArrayList<FootBallScore> mScores;

    public ScoreWidgetAdapter(Context context, int resource, ArrayList<FootBallScore> scores) {
        super(context, resource);
        mScores = scores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
