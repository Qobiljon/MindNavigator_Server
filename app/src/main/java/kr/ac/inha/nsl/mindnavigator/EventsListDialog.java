package kr.ac.inha.nsl.mindnavigator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class EventsListDialog extends DialogFragment {

    //region Variables
    private Calendar selectedDay;
    private Activity activity;
    private ViewGroup root;
    //endregion

    @SuppressLint("ValidFragment")
    public EventsListDialog(Activity a, Calendar cal) {
        selectedDay = cal;
        activity = a;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.dialog_events_list, container, true);
        init();
        return root;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        TextView dateTxt = root.findViewById(R.id.cell_date);

        String dayName = selectedDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        String month = selectedDay.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        int day = selectedDay.get(Calendar.DAY_OF_MONTH);
        int year = selectedDay.get(Calendar.YEAR);
        @SuppressLint("DefaultLocale") String dateFormat = (String.format("%s, %02d %s %02d", dayName, day, month, year));
        dateTxt.setText(dateFormat);

        ArrayList<Event> dayEvents = Event.getOneDayEvents(selectedDay);
        for (Event event : dayEvents) {
            activity.getLayoutInflater().inflate(R.layout.event_element_for_view, root);
            TextView titleText = root.findViewById(R.id.event_title_text_view);
            TextView dateText = root.findViewById(R.id.event_date_text_view);
            TextView stressLevel = root.findViewById(R.id.stress_lvl_box);
            stressLevel.setText(String.valueOf(event.getStressLevel()));
            stressLevel.setBackgroundColor(event.getStressColor());
            titleText.setText(event.getTitle());

            @SuppressLint("DefaultLocale") String startDateFormat = (String.format("%02d:%02d - %02d:%02d", event.getStartTime().get(Calendar.HOUR), event.getStartTime().get(Calendar.MINUTE),
                    event.getEndTime().get(Calendar.HOUR), event.getEndTime().get(Calendar.HOUR)));
            dateText.setText(startDateFormat);
        }

        //Inflating a "Close" button to the end of dialog
        activity.getLayoutInflater().inflate(R.layout.button_close, root);
        Button btnClose = root.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }
}
