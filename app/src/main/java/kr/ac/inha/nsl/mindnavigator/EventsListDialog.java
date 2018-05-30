package kr.ac.inha.nsl.mindnavigator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class EventsListDialog extends DialogFragment {

    //region Variables
    TextView dateTxt;
    ImageButton addEvent;
    //endregion

    @SuppressLint("ValidFragment")
    public EventsListDialog(Activity activity, Calendar cal){
        Toast.makeText(activity, "Here", Toast.LENGTH_SHORT).show();
    }

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
