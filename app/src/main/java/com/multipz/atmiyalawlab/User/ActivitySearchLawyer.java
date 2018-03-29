package com.multipz.atmiyalawlab.User;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Adapter.SpinnerAdapter;
import com.multipz.atmiyalawlab.Model.SpinnerModel;

import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivitySearchLawyer extends AppCompatActivity {

    Spinner sp_state, sp_city, sp_lawyer;
    Context context;
    Shared shared;
    private ArrayList<SpinnerModel> object_state, object_city, object_specilization;
    String city, lawyer, state, city_id, state_id, lawyer_id;
    Button btn_search;
    ImageView img_back;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lawyer);
        context = this;
        shared = new Shared(context);
        ref();
        init();
    }

    private void init() {
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                city = object_city.get(sp_city.getSelectedItemPosition()).getName();
                lawyer = object_specilization.get(sp_lawyer.getSelectedItemPosition()).getName();
                state = object_state.get(sp_state.getSelectedItemPosition()).getName();

                city_id = object_city.get(sp_city.getSelectedItemPosition()).getid();
                lawyer_id = object_specilization.get(sp_lawyer.getSelectedItemPosition()).getid();
                state_id = object_state.get(sp_state.getSelectedItemPosition()).getid();

                if (state.contentEquals("State")) {
                    Toaster.getToast(context, "Enter State");
                } else if (city.contentEquals("City")) {
                    Toaster.getToast(context, "Enter City Name");
                } else if (lawyer.contentEquals("Splecilization")) {
                    Toaster.getToast(context, "Enter Specialization");
                } else {

                    Intent intent = new Intent(ActivitySearchLawyer.this, ActivitySearch.class);
                    intent.putExtra("city", city);
                    intent.putExtra("lawyer", lawyer);
                    intent.putExtra("state", state);
                    intent.putExtra("city_id", city_id);
                    intent.putExtra("lawyer_id", lawyer_id);


                    startActivity(intent);
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getstate();
        getCity();
        getSpecilization();


    }

    private void ref() {

        sp_state = (Spinner) findViewById(R.id.sp_state);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        sp_lawyer = (Spinner) findViewById(R.id.sp_lawyer);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_search = (Button) findViewById(R.id.btn_search);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, ActivitySearchLawyer.this);


    }

    private void getstate() {
        object_state = new ArrayList<SpinnerModel>();
        object_state.add(new SpinnerModel("", "State"));

        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.State, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject state = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(state.getString("ah_state_id"));
                spinnerModel.setName(state.getString("state_name"));
                object_state.add(spinnerModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sp_state.setPrompt(getResources().getString(R.string.state));
        sp_state.setAdapter(new SpinnerAdapter(this, object_state));
        //sp_state.setSelection(new SpinnerAdapter(this, object_state).getCount());
        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCity() {
        object_city = new ArrayList<SpinnerModel>();
        object_city.add(new SpinnerModel("", "City"));

        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.City, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject state = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();

                spinnerModel.setid(state.getString("ah_city_id"));
                spinnerModel.setName(state.getString("city_name"));
                object_city.add(spinnerModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sp_city.setAdapter(new SpinnerAdapter(this, object_city));

    }


    private void getSpecilization() {
        object_specilization = new ArrayList<SpinnerModel>();
        object_specilization.add(new SpinnerModel("", "Splecilization"));

        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.specialization, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject state = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();


                spinnerModel.setid(state.getString("ah_lawyer_type_id"));
                spinnerModel.setName(state.getString("lawyer_type"));
                object_specilization.add(spinnerModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sp_lawyer.setAdapter(new SpinnerAdapter(this, object_specilization));

    }
}
