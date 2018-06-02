package kr.ac.inha.nsl.mindnavigator;

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

public class EventsListDialog extends DialogFragment {

    private ViewGroup root;
    //endregion

    public EventsListDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.dialog_events_list, container, true);
        init(getArguments());
        return root;
    }

    private void init(Bundle args) {
        Calendar selectedDay = Calendar.getInstance();
        selectedDay.setTimeInMillis(args.getLong("selectedDayMillis"));

        TextView dateTxt = root.findViewById(R.id.cell_date);

        dateTxt.setText(String.format(Locale.US,
                "%s, %02d %s %02d",
                selectedDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()),
                selectedDay.get(Calendar.DAY_OF_MONTH),
                selectedDay.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()),
                selectedDay.get(Calendar.YEAR)
        ));

        ArrayList<Event> dayEvents = Event.getOneDayEvents(selectedDay);
        for (Event event : dayEvents) {
            getActivity().getLayoutInflater().inflate(R.layout.event_element_for_view, root);
            ViewGroup view = (ViewGroup) root.getChildAt(root.getChildCount() - 1);
            TextView titleText = view.findViewById(R.id.event_title_text_view);
            TextView dateText = view.findViewById(R.id.event_date_text_view);
            TextView stressLevel = view.findViewById(R.id.stress_lvl_box);

            stressLevel.setText(String.valueOf(event.getStressLevel()));
            stressLevel.setBackgroundColor(event.getStressColor());
            titleText.setText(event.getTitle());

            dateText.setText(String.format(Locale.US,
                    "%02d:%02d - %02d:%02d",
                    event.getStartTime().get(Calendar.HOUR),
                    event.getStartTime().get(Calendar.MINUTE),
                    event.getEndTime().get(Calendar.HOUR),
                    event.getEndTime().get(Calendar.MINUTE))
            );
        }

        //Inflating a "Close" button to the end of dialog
        getActivity().getLayoutInflater().inflate(R.layout.button_close, root);
        Button btnClose = root.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
