package com.multipz.atmiyalawlab.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.multipz.atmiyalawlab.Adapter.JudgementAdapter;
import com.multipz.atmiyalawlab.Model.ModelJudgement;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;

import java.util.ArrayList;

public class JudgementActivity extends AppCompatActivity {

    private RelativeLayout rel_root;
    private ImageView img_back;
    private RecyclerView listviewjudgement;
    private ArrayList<ModelJudgement> jlist;
    private JudgementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement);
        reference();
        init();

    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        img_back = (ImageView) findViewById(R.id.img_back);
        listviewjudgement = (RecyclerView) findViewById(R.id.listviewjudgement);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        jlist = new ArrayList<>();
        jlist.add(new ModelJudgement("Judgement Title", "08-Nov-2018"));
        jlist.add(new ModelJudgement("Judgement Title", "08-Nov-2018"));
        jlist.add(new ModelJudgement("Judgement Title", "08-Nov-2018"));
        jlist.add(new ModelJudgement("Judgement Title", "08-Nov-2018"));
        jlist.add(new ModelJudgement("Judgement Title", "08-Nov-2018"));

        /*adapter = new JudgementAdapter(getApplicationContext(), jlist);
        listviewjudgement.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listviewjudgement.setAdapter(adapter);*/

    }


}
