package com.multipz.atmiyalawlab.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.multipz.atmiyalawlab.Adapter.AddQualificationAdapter;
import com.multipz.atmiyalawlab.Adapter.FilterDictionaryAdapter;
import com.multipz.atmiyalawlab.Adapter.SpinnerAdapter;
import com.multipz.atmiyalawlab.Model.ModelAddQualification;
import com.multipz.atmiyalawlab.Model.SpinnerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityQualification extends AppCompatActivity implements ItemClickListener {

    private RecyclerView listQualification;
    private TextView btn_Add_more;
    private Spinner sp_graduate, sp_degree;
    private EditText edt_university, edt_passing_year;
    private Shared shared;
    private ArrayList<SpinnerModel> object_qualification, object_degree;
    private Context context;
    private String param = "", qul_id = "", degree_id = "", qualification = "", degree = "";
    private String name = "", email = "", password = "", confirm_password = "", birthdate = "", gender = "", mobile = "";
    private ArrayList<ModelAddQualification> list;
    private ArrayList<ModelAddQualification> displayList;
    private AddQualificationAdapter adapter;
    private RelativeLayout btn_next_qualification;
    private ImageView img_back;
    private CollapsingToolbarLayout collapsing_toolbar;
    private RelativeLayout rel_root, lnr_university;
    private AppBarLayout.LayoutParams params;
    private AppBarLayout app_bar_layout;
    private boolean isEmpty = false;
    private CoordinatorLayout.LayoutParams appBarLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification);
        context = this;
        shared = new Shared(context);
        list = new ArrayList<>();
        displayList = new ArrayList<>();
        reference();
        init();
    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        listQualification = (RecyclerView) findViewById(R.id.listQualification);
        btn_Add_more = (TextView) findViewById(R.id.btn_Add_more);
        sp_graduate = (Spinner) findViewById(R.id.sp_graduate);
        sp_degree = (Spinner) findViewById(R.id.sp_degree);
        edt_university = (EditText) findViewById(R.id.edt_university);
        edt_passing_year = (EditText) findViewById(R.id.edt_passing_year);
        btn_next_qualification = (RelativeLayout) findViewById(R.id.btn_next_qualification);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        lnr_university = (RelativeLayout) findViewById(R.id.lnr_university);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI((RelativeLayout) findViewById(R.id.rel_root), ActivityQualification.this);

        params = (AppBarLayout.LayoutParams) collapsing_toolbar.getLayoutParams();
        appBarLayoutParams = (CoordinatorLayout.LayoutParams) app_bar_layout.getLayoutParams();


    }

    private void init() {

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        confirm_password = getIntent().getStringExtra("confirm_password");
        birthdate = getIntent().getStringExtra("birthdate");
        gender = getIntent().getStringExtra("gender");
        mobile = getIntent().getStringExtra("mobile");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setSpinner();
        btn_Add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (qul_id.contentEquals("")) {
                    Toast.makeText(context, ErrorMessage.Qualification, Toast.LENGTH_SHORT).show();
                } else if (degree_id.contentEquals("")) {
                    Toast.makeText(context, ErrorMessage.selectDegree, Toast.LENGTH_SHORT).show();
                } else if (edt_university.getText().toString().contentEquals("")) {
                    Toast.makeText(context, ErrorMessage.provideUniversity, Toast.LENGTH_SHORT).show();
                } else if (edt_passing_year.getText().toString().contentEquals("")) {
                    Toast.makeText(context, ErrorMessage.providePassingYear, Toast.LENGTH_SHORT).show();
                } else {
                    getCollapsinglayoutparamVisible();

                    String uni = edt_university.getText().toString();
                    String passing_year = edt_passing_year.getText().toString();
                    list.add(new ModelAddQualification(qul_id, degree_id, passing_year, uni));
//                    displayList.add(new ModelAddQualification(qul_id, degree_id, qualification, degree, passing_year, uni));

                    ModelAddQualification m = new ModelAddQualification();
                    m.setGraduate_id(qualification);
                    m.setDegree_id(degree);
                    m.setUniversity(uni);
                    m.setPassing_year(passing_year);
                    displayList.add(m);

                    adapter = new AddQualificationAdapter(getApplicationContext(), displayList);
                    listQualification.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    listQualification.setAdapter(adapter);
                    adapter.setClickListener(ActivityQualification.this);
                    adapter.notifyDataSetChanged();

                    sp_graduate.setSelection(0);
                    sp_degree.setSelection(0);
                    qul_id = "";
                    degree_id = "";
                    edt_university.setText("");
                    edt_passing_year.setText("");
                    isEmpty = true;
                    edt_passing_year.setFocusable(false);
                    edt_passing_year.setFocusableInTouchMode(true);

                    Toaster.getToast(getApplicationContext(), ErrorMessage.AddQualification);

                }


            }
        });
        btn_next_qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (displayList.size() > 0) {
                    if (isEmpty) {
                        shared.setListQualification(new Gson().toJson(list));
                        Intent i = new Intent(ActivityQualification.this, ActivityRegQualification.class);
                        i.putExtra("name", name);
                        i.putExtra("email", email);
                        i.putExtra("password", password);
                        i.putExtra("confirm_password", confirm_password);
                        i.putExtra("birthdate", birthdate);
                        i.putExtra("gender", gender);
                        i.putExtra("mobile", mobile);
                        startActivity(i);


                    } else {
                        if (qul_id.contentEquals("")) {
                            Toast.makeText(context, ErrorMessage.selectQualification, Toast.LENGTH_SHORT).show();
                        } else if (degree_id.contentEquals("")) {
                            Toast.makeText(context, ErrorMessage.selectDegree, Toast.LENGTH_SHORT).show();
                        } else if (edt_university.getText().toString().contentEquals("")) {
                            Toast.makeText(context, ErrorMessage.provideUniversity, Toast.LENGTH_SHORT).show();
                        } else if (edt_passing_year.getText().toString().contentEquals("")) {
                            Toast.makeText(context, ErrorMessage.providePassingYear, Toast.LENGTH_SHORT).show();
                        } else {
                            if (list.size() > 0) {
                                shared.setListQualification(new Gson().toJson(list));
                                Intent i = new Intent(ActivityQualification.this, ActivityRegQualification.class);
                                i.putExtra("name", name);
                                i.putExtra("email", email);
                                i.putExtra("password", password);
                                i.putExtra("confirm_password", confirm_password);
                                i.putExtra("birthdate", birthdate);
                                i.putExtra("gender", gender);
                                i.putExtra("mobile", mobile);
                                startActivity(i);
                            } else {
                                Toaster.getToast(getApplicationContext(), ErrorMessage.Qualification);
                            }
                        }
                    }
                } else {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.Qualification);
                }


            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setSpinner() {
        sp_graduate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!object_qualification.get(i).getName().contentEquals("Qualification")) {
                    qul_id = object_qualification.get(i).getid();
                    qualification = object_qualification.get(i).getName();
                    textView.setTextColor(context.getResources().getColor(R.color.atimiya_law_color));
                } else {
                    qul_id = "";
                    textView.setTextColor(context.getResources().getColor(R.color.atimiya_law_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_degree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!object_degree.get(i).getName().contentEquals("Degree")) {
                    degree_id = object_degree.get(i).getid();
                    degree = object_degree.get(i).getName();
                    textView.setTextColor(context.getResources().getColor(R.color.atimiya_law_color));
                } else {
                    degree_id = "";
                    textView.setTextColor(context.getResources().getColor(R.color.atimiya_law_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getDegree();
        getQualification();

    }

    private void getDegree() {
        object_degree = new ArrayList<SpinnerModel>();
        object_degree.add(new SpinnerModel("", "Degree"));
        try {
            JSONArray array = new JSONArray(shared.getString(Config.degree, "[{}]"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject state = array.getJSONObject(i);
                SpinnerModel model = new SpinnerModel();
                model.setid(state.getString("degreeid"));
                model.setName(state.getString("degree_type"));
                object_degree.add(model);
            }
            sp_degree.setAdapter(new SpinnerAdapter(context, object_degree));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getQualification() {

        object_qualification = new ArrayList<SpinnerModel>();
        object_qualification.add(new SpinnerModel("", "Qualification"));

        try {
            JSONArray array = new JSONArray(shared.getString(Config.graduation, "[{}]"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject gradation = array.getJSONObject(i);
                SpinnerModel model = new SpinnerModel();
                model.setid(gradation.getString("graduationid"));
                model.setName(gradation.getString("graduation"));
                object_qualification.add(model);
            }
            sp_graduate.setAdapter(new SpinnerAdapter(context, object_qualification));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        if (view.getId() == R.id.btn_remove) {
            adapter.removeItem(position);
            if (displayList.size() == 0) {
                params.setScrollFlags(0);
                appBarLayoutParams.setBehavior(null);
                app_bar_layout.setLayoutParams(appBarLayoutParams);
            } else {
                getCollapsinglayoutparamVisible();
            }

        }
    }

    void getCollapsinglayoutparamVisible() {
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        appBarLayoutParams.setBehavior(new AppBarLayout.Behavior());
        app_bar_layout.setLayoutParams(appBarLayoutParams);
    }
}
