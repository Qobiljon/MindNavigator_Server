package kr.ac.inha.nsl.mindnavigator;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class EventsListDialog extends DialogFragment {

    //region Variables
    TextView dateTxt;
    ImageButton addEvent;
    //endregion


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_events_list, container, true);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        dateTxt = view.findViewById(R.id.cell_date);
        addEvent = view.findViewById(R.id.btn_add);
    }
}
