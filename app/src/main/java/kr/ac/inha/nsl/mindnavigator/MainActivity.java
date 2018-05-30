package kr.ac.inha.nsl.mindnavigator;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // region Variables
    private GridLayout event_grid;
    private ViewGroup[][] cells = new ViewGroup[7][5];
    private TextView monthName;
    private TextView year;
    private Calendar currentCal;

    private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    // region CellClick Listener
    LinearLayout.OnClickListener cellClick = new LinearLayout.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar selectedDay = Calendar.getInstance();
            selectedDay.setTimeInMillis((long) view.getTag());

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            DialogFragment dialogFragment = new EventsListDialog(MainActivity.this, selectedDay);
            dialogFragment.show(ft, "dialog");

        }
    };
    // endregion
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mTopToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        Event.init(this);
        init();

        // region Add fake events
        Calendar startTime = Calendar.getInstance(), endTime = Calendar.getInstance();

        startTime.set(Calendar.MINUTE, 0);
        endTime.set(Calendar.MINUTE, 30);
        new Event("UCL final match", 3, startTime, endTime);

        startTime.add(Calendar.HOUR, 1);
        endTime.add(Calendar.HOUR, 1);
        new Event("Movie with Debbie", 0, startTime, endTime);

        startTime.add(Calendar.DAY_OF_MONTH, 1);
        endTime.add(Calendar.DAY_OF_MONTH, 1);
        new Event("NSL meeting", 2, startTime, endTime);

        startTime.add(Calendar.DAY_OF_MONTH, 1);
        endTime.add(Calendar.DAY_OF_MONTH, 1);
        new Event("Shopping", 1, startTime, endTime);
        // endregion
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

    public void navNextWeekClick(View view) {
        currentCal.add(Calendar.MONTH, 1);
        updateCalendarView();
    }

    public void navPrevWeekClick(View view) {
        currentCal.add(Calendar.MONTH, -1);
        updateCalendarView();
    }

    @SuppressLint("CutPasteId")
    public void updateCalendarView() {
        // Update the value of year and month according to the currently selected month
        monthName.setText(months[currentCal.get(Calendar.MONTH)]);
        year.setText(String.valueOf(currentCal.get(Calendar.YEAR)));

        // First clear our the grid
        for (int row = 0; row < event_grid.getRowCount(); row++)
            for (int col = 0; col < event_grid.getColumnCount(); col++)
                Tools.cellClearOut(cells, col, row, this, event_grid, cellClick);

        // Check if displayed month contains today
        Calendar today = Calendar.getInstance();
        TextView todayText;

        if (currentCal.get(Calendar.MONTH) == today.get(Calendar.MONTH) && currentCal.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            int col = today.get(Calendar.DAY_OF_WEEK) - 1;
            int row = (today.get(Calendar.DAY_OF_MONTH) + col) / 7;

            todayText = cells[col][row].findViewById(R.id.date_text_view);
            todayText.setTextColor(Color.WHITE);
            todayText.setBackgroundResource(R.drawable.bg_today_view);
        }

        // Calculate which date of week current month starts from
        int firstDayOfMonth = getFirstWeekdayIndex(currentCal.get(Calendar.DAY_OF_MONTH), currentCal.get(Calendar.MONTH), currentCal.get(Calendar.YEAR));
        int numOfDaysCurMonth = currentCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar clone = (Calendar) currentCal.clone();
        clone.add(Calendar.MONTH, -1);
        int prevCnt = clone.getActualMaximum(Calendar.DAY_OF_MONTH) - firstDayOfMonth + 2;

        // Set dates to display
        int col = 0, row, count = 1;
        for (row = 0; row < event_grid.getRowCount(); row++) {
            if (row == 0) {
                for (col = 0; col < firstDayOfMonth - 1; col++) {
                    TextView date_text = cells[col][row].findViewById(R.id.date_text_view);
                    date_text.setText(String.valueOf(prevCnt));

                    clone.set(Calendar.DAY_OF_MONTH, prevCnt);
                    cells[col][row].setTag(clone.getTimeInMillis());

                    prevCnt++;
                }
                clone.add(Calendar.MONTH, 1);
                for (; col < event_grid.getColumnCount(); col++, count++) {
                    TextView date_text = cells[col][row].findViewById(R.id.date_text_view);
                    date_text.setText(String.valueOf(count));

                    clone.set(Calendar.DAY_OF_MONTH, count);
                    cells[col][row].setTag(clone.getTimeInMillis());
                }
            } else
                for (col = 0; count <= numOfDaysCurMonth && col < event_grid.getColumnCount(); col++, count++) {
                    TextView date_text = cells[col][row].findViewById(R.id.date_text_view);
                    date_text.setText(String.valueOf(count));

                    clone.set(Calendar.DAY_OF_MONTH, count);
                    cells[col][row].setTag(clone.getTimeInMillis());
                }
        }
        clone.add(Calendar.MONTH, 1);

        for (count = 1, row = event_grid.getRowCount() - 1; col < event_grid.getColumnCount(); col++, count++) {
            TextView date_text = cells[col][row].findViewById(R.id.date_text_view);
            date_text.setText(String.valueOf(count));

            clone.set(Calendar.DAY_OF_MONTH, count);
            cells[col][row].setTag(clone.getTimeInMillis());
        }

        for (row = 0; row < event_grid.getRowCount(); row++)
            for (col = 0; col < event_grid.getColumnCount(); col++) {
                Calendar day = Calendar.getInstance();
                day.setTimeInMillis((long) cells[col][row].getTag());
                ArrayList<Event> dayEvents = Event.getOneDayEvents(day);
                for (Event event : dayEvents) {
                    getLayoutInflater().inflate(R.layout.event_element, cells[col][row]);
                    TextView res = (TextView) cells[col][row].getChildAt(cells[col][row].getChildCount() - 1);
                    res.setBackgroundColor(event.getStressColor());
                    res.setText(event.getTitle());
                }
            }
    }

    public int getFirstWeekdayIndex(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void todayClick(MenuItem item) {
        currentCal = Calendar.getInstance();
        updateCalendarView();
    }

    public void settingsClick(MenuItem item) {

    }

    public void selectMonth(View view) {
    }

    public void selectYear(View view) {
    }

    public void onNewEventClick(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }
}
