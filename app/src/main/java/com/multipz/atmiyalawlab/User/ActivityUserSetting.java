package com.multipz.atmiyalawlab.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.multipz.atmiyalawlab.Activity.ActivityChangePassword;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;

public class ActivityUserSetting extends AppCompatActivity {
    private LinearLayout lnr_change_con_no, lnr_change_pass;
    private ImageView img_back;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        reference();
        init();

    }

    private void reference() {
        lnr_change_con_no = (LinearLayout) findViewById(R.id.lnr_change_con_no);
        lnr_change_pass = (LinearLayout) findViewById(R.id.lnr_change_pass);
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        lnr_change_con_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityUserSetting.this, ActivityChangeContactNo.class);
                startActivity(i);
            }
        });
        lnr_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityUserSetting.this, ActivityChangePassword.class);
                startActivity(i);
            }
        });

    }
}
