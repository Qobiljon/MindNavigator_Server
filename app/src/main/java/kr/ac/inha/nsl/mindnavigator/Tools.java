package kr.ac.inha.nsl.mindnavigator;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.view.ViewGroup;
import android.widget.TextView;

public class Tools {
    // region Variables
    private static int cellWidth, cellHeight;
    // endregion

    public static void setCellSize(int width, int height) {
        cellWidth = width;
        cellHeight = height;
    }

    public static ViewGroup emptyCell(Activity activity, ViewGroup parent) {
        ViewGroup res = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.date_cell, parent, false);
        res.getLayoutParams().width = cellWidth;
        res.getLayoutParams().height = cellHeight;
        return res;
    }

    public static void addEvent(Activity activity, ViewGroup parent, Event event) {
        activity.getLayoutInflater().inflate(R.layout.event_element, parent);
        TextView res = (TextView) parent.getChildAt(parent.getChildCount() - 1);
        res.setBackgroundColor(event.getStressColor());
        res.setText(event.getTitle());
    }
}

class Event {
    Event(String title, int stressLevel) {
        setTitle(title);
        setStressLevel(stressLevel);
        id = System.currentTimeMillis() / 1000;
    }

    public static void init(Activity activity) {
        stressColors[0] = activity.getColor(R.color.slvl0_color);
        stressColors[1] = activity.getColor(R.color.slvl1_color);
        stressColors[2] = activity.getColor(R.color.slvl2_color);
        stressColors[3] = activity.getColor(R.color.slvl3_color);
        stressColors[4] = activity.getColor(R.color.slvl4_color);
    }

    //region Variables
    @ColorInt
    private static int[] stressColors = new int[5];

    private long id;
    private int stressLevel;
    private String title;
    //endregion

    private void setStressLevel(int stressLevel) {
        this.stressLevel = stressLevel;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getEventId() {
        return id;
    }

    public int getStressColor() {
        return stressColors[stressLevel];
    }

    public String getTitle() {
        return title;
    }
}