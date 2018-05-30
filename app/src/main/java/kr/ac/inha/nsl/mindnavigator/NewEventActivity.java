package kr.ac.inha.nsl.mindnavigator;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class NewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            bar.setTitle("Sign up");
        init();
    }

    //region Variables
    TextView eventTitle, eventNote;
    TextView startDate, startTime, endDate, endTime;
    SeekBar stressLvl;
    Button saveBtn, cancelBtn;

    //endregion
    private void init(){
        //region Assign UI variables
        eventTitle = findViewById(R.id.txt_event_title);
        eventNote = findViewById(R.id.txt_event_note);
        startDate = findViewById(R.id.txt_event_start_date);
        startTime = findViewById(R.id.txt_event_start_time);
        endDate = findViewById(R.id.txt_event_end_date);
        endTime = findViewById(R.id.txt_event_end_time);
        saveBtn = findViewById(R.id.btn_save);
        cancelBtn = findViewById(R.id.btn_cancel);
        stressLvl = findViewById(R.id.stressLvl);
        //endregion

        stressLvl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(NewEventActivity.this, String.valueOf(progress), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
