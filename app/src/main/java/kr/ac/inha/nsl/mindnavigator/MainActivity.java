package kr.ac.inha.nsl.mindnavigator;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // region Variables
    private GridLayout event_grid;
    private ViewGroup[][] cells = new ViewGroup[7][5];
    private TextView monthName;
    private TextView year;
    private Calendar currentCal;

    private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        Event.init(this);
    }

    private void init() {
        currentCal = Calendar.getInstance();
        event_grid = findViewById(R.id.event_grid);
        monthName = findViewById(R.id.header_month_name);
        year = findViewById(R.id.header_year);

        event_grid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                event_grid.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Tools.setCellSize(event_grid.getWidth() / event_grid.getColumnCount(), event_grid.getHeight() / event_grid.getRowCount());
                updateCalendarView();
            }
        });
    }

    private void initEmptyGridViews() {
        event_grid.removeAllViews();

        for (int row = 0; row < event_grid.getRowCount(); row++)
            for (int col = 0; col < event_grid.getColumnCount(); col++) {
                // Populate the prepared TextView
                cells[col][row] = Tools.emptyCell(this, event_grid);
                event_grid.addView(cells[col][row]);
            }

        Calendar today = Calendar.getInstance();
        if (currentCal.get(Calendar.MONTH) == today.get(Calendar.MONTH) && currentCal.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            int col = today.get(Calendar.DAY_OF_WEEK) - 1;
            int row = (today.get(Calendar.DAY_OF_MONTH) + col) / 7;

            TextView todayText = cells[col][row].findViewById(R.id.date_text_view);
            todayText.setTextColor(Color.WHITE);
            todayText.setBackgroundResource(R.drawable.bg_today_view);
        }
    }

    private void refreshToDefaultGrid() {
        initEmptyGridViews();

        int firstDayOfMonth = getFirstWeekdayIndex(currentCal.get(Calendar.DAY_OF_MONTH), currentCal.get(Calendar.MONTH), currentCal.get(Calendar.YEAR));
        int numOfDaysCurMonth = currentCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar clone = (Calendar) currentCal.clone();
        clone.add(Calendar.MONTH, -1);
        int prevCnt = clone.getActualMaximum(Calendar.DAY_OF_MONTH) - firstDayOfMonth + 2;

        int col = 0, count = 1;
        for (int row = 0; row < event_grid.getRowCount(); row++) {
            if (row == 0) {
                for (col = 0; col < firstDayOfMonth - 1; col++)
                    ((TextView) cells[col][row].findViewById(R.id.date_text_view)).setText(String.valueOf(prevCnt++));
                for (; col < event_grid.getColumnCount(); col++)
                    ((TextView) cells[col][row].findViewById(R.id.date_text_view)).setText(String.valueOf(count++));
            } else
                for (col = 0; col < event_grid.getColumnCount(); col++) {
                    ((TextView) cells[col][row].findViewById(R.id.date_text_view)).setText(String.valueOf(count));
                    if (count++ == numOfDaysCurMonth + 1)
                        break;
                }
        }
        for (count = 1; col < event_grid.getColumnCount(); col++)
            ((TextView) cells[col][event_grid.getRowCount() - 1].findViewById(R.id.date_text_view)).setText(String.valueOf(count++));
    }

    public void navNextWeekClick(View view) {
        currentCal.add(Calendar.MONTH, 1);
        updateCalendarView();
    }

    public void navPrevWeekClick(View view) {
        currentCal.add(Calendar.MONTH, -1);
        updateCalendarView();
    }

    public void updateCalendarView() {
        refreshToDefaultGrid();
        Event events[] = new Event[]{
                new Event("UCL final match", 3),
                new Event("Movie with Debbie", 0),
                new Event("NSL meeting", 2),
                new Event("Shopping", 1)
        };
        for (int row = 0; row < event_grid.getRowCount(); row++)
            for (int col = 0; col < event_grid.getColumnCount(); col++) {
                Tools.addEvent(this, cells[col][row], events[0]);
                Tools.addEvent(this, cells[col][row], events[1]);
                Tools.addEvent(this, cells[col][row], events[2]);
                Tools.addEvent(this, cells[col][row], events[3]);

            }

        monthName.setText(months[currentCal.get(Calendar.MONTH)]);
        year.setText(String.valueOf(currentCal.get(Calendar.YEAR)));
    }

    public int getFirstWeekdayIndex(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
}
