package kr.ac.inha.nsl.mindnavigator;

import android.graphics.PorterDuff;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            bar.setTitle("Event");
        init();
    }

    //region Variables
    TextView eventTitle, eventNote;
    TextView startDate, startTime, endDate, endTime;
    SeekBar stressLvl;
    Button saveBtn, cancelBtn;

    //endregion
    private void init() {
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

        stressLvl.getProgressDrawable().setColorFilter(ResourcesCompat.getColor(getResources(), R.color.slvl0_color, null), PorterDuff.Mode.SRC_IN);
        stressLvl.getThumb().setColorFilter(ResourcesCompat.getColor(getResources(), R.color.slvl0_color, null), PorterDuff.Mode.SRC_IN);
        stressLvl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        int slvl0Col = ResourcesCompat.getColor(getResources(), R.color.slvl0_color, null);
                        stressLvl.getProgressDrawable().setColorFilter(slvl0Col, PorterDuff.Mode.SRC_IN);
                        stressLvl.getThumb().setColorFilter(slvl0Col, PorterDuff.Mode.SRC_IN);
                        break;
                    case 1:
                        int slvl1Col = ResourcesCompat.getColor(getResources(), R.color.slvl1_color, null);
                        stressLvl.getProgressDrawable().setColorFilter(slvl1Col, PorterDuff.Mode.SRC_IN);
                        stressLvl.getThumb().setColorFilter(slvl1Col, PorterDuff.Mode.SRC_IN);
                        break;
                    case 2:
                        int slvl2Col = ResourcesCompat.getColor(getResources(), R.color.slvl2_color, null);
                        stressLvl.getProgressDrawable().setColorFilter(slvl2Col, PorterDuff.Mode.SRC_IN);
                        stressLvl.getThumb().setColorFilter(slvl2Col, PorterDuff.Mode.SRC_IN);
                        break;
                    case 3:
                        int slvl3Col = ResourcesCompat.getColor(getResources(), R.color.slvl3_color, null);
                        stressLvl.getProgressDrawable().setColorFilter(slvl3Col, PorterDuff.Mode.SRC_IN);
                        stressLvl.getThumb().setColorFilter(slvl3Col, PorterDuff.Mode.SRC_IN);
                        break;
                    case 4:
                        int slvl4Col = ResourcesCompat.getColor(getResources(), R.color.slvl4_color, null);
                        stressLvl.getProgressDrawable().setColorFilter(slvl4Col, PorterDuff.Mode.SRC_IN);
                        stressLvl.getThumb().setColorFilter(slvl4Col, PorterDuff.Mode.SRC_IN);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast,
                        (ViewGroup) findViewById(R.id.toast_layout_root));

                ImageView image = layout.findViewById(R.id.image);
                image.setImageResource(R.drawable.ic_launcher_foreground);

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        });
    }
}
