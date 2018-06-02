package kr.ac.inha.nsl.mindnavigator;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class InterventionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interventions);

        ActionBar bar = getSupportActionBar();
        if (bar != null)
            bar.setTitle("Interventions");
        init();
    }

    //region Variables
    LinearLayout layoutSelf;
    LinearLayout layoutSystem;
    LinearLayout layoutPeer;

    TextView selfTab, systemTab, peerTab;
    //endregion

    private void init() {
        layoutSelf = findViewById(R.id.layout_self);
        layoutSystem = findViewById(R.id.layout_system);
        layoutPeer = findViewById(R.id.layout_peer);
        selfTab = findViewById(R.id.tab_self);
        systemTab = findViewById(R.id.tab_system);
        peerTab = findViewById(R.id.tab_peer);
    }


    public void cleanTabs(){
        selfTab.setBackground(getDrawable(R.drawable.bg_interv_method_unchecked_view));
        systemTab.setBackground(getDrawable(R.drawable.bg_interv_method_unchecked_view));
        peerTab.setBackground(getDrawable(R.drawable.bg_interv_method_unchecked_view));
        layoutSelf.setVisibility(View.GONE);
        layoutSystem.setVisibility(View.GONE);
        layoutPeer.setVisibility(View.GONE);
    }
    public void selfTabClick(View view) {
        cleanTabs();
        selfTab.setBackground(getDrawable(R.drawable.bg_interv_method_checked_view));
        layoutSelf.setVisibility(View.VISIBLE);
    }

    public void systemTabClick(View view) {
        cleanTabs();
        systemTab.setBackground(getDrawable(R.drawable.bg_interv_method_checked_view));
        layoutSystem.setVisibility(View.VISIBLE);
    }

    public void peerTabClick(View view) {
        cleanTabs();
        peerTab.setBackground(getDrawable(R.drawable.bg_interv_method_checked_view));
        layoutPeer.setVisibility(View.VISIBLE);
    }
}
