package com.multipz.atmiyalawlab.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.DictionaryModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;

import java.util.ArrayList;

public class DictionaryDetail extends AppCompatActivity {

    private ImageView img_back;
    private RelativeLayout rel_root;
    private TextView txt_word_name, txtdescrption;
    private LinearLayout btn_previous_dict, btn_next_dict;
    String title, des;
    ArrayList<DictionaryModel> alist;
    int currentindex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_detail);
        alist = new ArrayList<>();
        reference();
        init();
    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        txt_word_name = (TextView) findViewById(R.id.txt_word_name);
        txtdescrption = (TextView) findViewById(R.id.txtdescrption);
        btn_previous_dict = (LinearLayout) findViewById(R.id.btn_previous_dict);
        btn_next_dict = (LinearLayout) findViewById(R.id.btn_next_dict);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        des = getIntent().getStringExtra("des");
        title = getIntent().getStringExtra("title");
        currentindex = getIntent().getIntExtra("pos", 0);
        alist = (ArrayList<DictionaryModel>) getIntent().getSerializableExtra("searchText");
        txt_word_name.setText(title);
        txtdescrption.setText(des);


    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_next_dict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentindex = currentindex + 1;
                if (currentindex < alist.size()) {
                    txtdescrption.setText(alist.get(currentindex).getDescription());
                    txt_word_name.setText(alist.get(currentindex).getTitle());
                } else {
                    currentindex = currentindex - 1;
                }
            }
        });

        btn_previous_dict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentindex = currentindex - 1;
                if (currentindex >= 0) {
                    txtdescrption.setText(alist.get(currentindex).getDescription());
                    txt_word_name.setText(alist.get(currentindex).getTitle());
                } else {
                    currentindex = currentindex + 1;
                }
            }
        });
    }
}

