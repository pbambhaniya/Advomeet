package com.multipz.atmiyalawlab.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.multipz.atmiyalawlab.Adapter.CourtAdapter;
import com.multipz.atmiyalawlab.Adapter.RegSpecailizationAdapter;
import com.multipz.atmiyalawlab.Adapter.SpinnerAdapter;
import com.multipz.atmiyalawlab.Model.ModelCourt;
import com.multipz.atmiyalawlab.Model.ModelSpecialization;
import com.multipz.atmiyalawlab.Model.SpinnerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ActivityRegQualification extends AppCompatActivity {

    private RelativeLayout rel_root, btn_next_qualification, lnr_date_on_enroll;
    private ImageView img_back;
    private Spinner sp_bar_council, sp_year_of_exp, sp_month, sp_graduate, sp_degree;
    private EditText edt_enroll_no, edt_date_on_enroll, edt_welfare_no, edt_Add_practise_court, edt_Add_spacialization, edt_university, edt_passing_year;
    private LinearLayout btn_Add_more;
    private String name = "", email = "", pass = "", c_pass = "", dob = "", gender = "";
    private ArrayList<SpinnerModel> object_bar_counciling, object_year, object_month, object_qualification, object_degree;
    private String enrol_no = "", date_enrol_no = "", w_no = "", p_court = "", specilization = "", university = "", pass_year = "", mobile = "";
    String bar_counsiling, year, qua, month, degree;
    private int mYear, mMonth, mDay;
    Context context;
    private Shared shared;
    private CourtAdapter courtAdapter;
    String last;
    private RegSpecailizationAdapter specailizationAdapter;
    private ArrayList<ModelCourt> courtsList;
    private ArrayList<ModelSpecialization> specailizationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_qualification);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        lnr_date_on_enroll = (RelativeLayout) findViewById(R.id.lnr_date_on_enroll);
        btn_next_qualification = (RelativeLayout) findViewById(R.id.btn_next_qualification);

        img_back = (ImageView) findViewById(R.id.img_back);

        sp_bar_council = (Spinner) findViewById(R.id.sp_bar_council);
        sp_year_of_exp = (Spinner) findViewById(R.id.sp_year_of_exp);
        sp_month = (Spinner) findViewById(R.id.sp_month);


        sp_graduate = (Spinner) findViewById(R.id.sp_graduate);
        sp_degree = (Spinner) findViewById(R.id.sp_degree);

        edt_enroll_no = (EditText) findViewById(R.id.edt_enroll_no);
        edt_date_on_enroll = (EditText) findViewById(R.id.edt_date_on_enroll);
        edt_welfare_no = (EditText) findViewById(R.id.edt_welfare_no);
        edt_Add_practise_court = (EditText) findViewById(R.id.edt_Add_practise_court);
        edt_Add_spacialization = (EditText) findViewById(R.id.edt_Add_spacialization);
        edt_university = (EditText) findViewById(R.id.edt_university);
        edt_passing_year = (EditText) findViewById(R.id.edt_passing_year);

        btn_Add_more = (LinearLayout) findViewById(R.id.btn_Add_more);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, ActivityRegQualification.this);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupbarcouncil = (android.widget.ListPopupWindow) popup.get(sp_bar_council);
            android.widget.ListPopupWindow popupYear = (android.widget.ListPopupWindow) popup.get(sp_year_of_exp);
            android.widget.ListPopupWindow popupmonth = (android.widget.ListPopupWindow) popup.get(sp_month);
            // popupbarcouncil.setHeight(Config.spinner_height);
            popupYear.setHeight(Config.spinner_height);
            popupmonth.setHeight(Config.spinner_height);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("password");
        c_pass = getIntent().getStringExtra("confirm_password");
        dob = getIntent().getStringExtra("birthdate");
        gender = getIntent().getStringExtra("gender");
        mobile = getIntent().getStringExtra("mobile");


        getBarCounsilingData();
        getYear();
        getMonth();
        getQualification();
        getDegree();

    }


    private void init() {

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edt_date_on_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_date_on_enroll.getText().toString().contentEquals("")) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    String[] s = edt_date_on_enroll.getText().toString().split("-");
                    mYear = Integer.parseInt(s[0]);
                    mMonth = Integer.parseInt(s[1]) - 1;
                    mDay = Integer.parseInt(s[2]);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityRegQualification.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String day = "";
                                if (dayOfMonth > 0 && dayOfMonth <= 9) {
                                    day = "0" + dayOfMonth;
                                } else {
                                    day = "" + dayOfMonth;
                                }
                                int month = monthOfYear + 1;
                                if (month > 0 && month <= 9) {
                                    edt_date_on_enroll.setText(year + "-0" + (monthOfYear + 1) + "-" + day);
                                } else {
                                    edt_date_on_enroll.setText(year + "-" + (monthOfYear + 1) + "-" + day);
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        edt_Add_practise_court.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupCourt();


            }

        });
        edt_Add_spacialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupSpecailization();
            }
        });

        btn_next_qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enrol_no = edt_enroll_no.getText().toString();
                date_enrol_no = edt_date_on_enroll.getText().toString();
                w_no = edt_welfare_no.getText().toString();
                p_court = edt_Add_practise_court.getText().toString();
                specilization = edt_Add_spacialization.getText().toString();
                university = edt_university.getText().toString();
                pass_year = edt_passing_year.getText().toString();
                bar_counsiling = object_bar_counciling.get(sp_bar_council.getSelectedItemPosition()).getid();
                year = object_year.get(sp_year_of_exp.getSelectedItemPosition()).getid();
                qua = object_qualification.get(sp_graduate.getSelectedItemPosition()).getid();
                month = object_month.get(sp_month.getSelectedItemPosition()).getid();
                degree = object_degree.get(sp_degree.getSelectedItemPosition()).getid();

                if (bar_counsiling.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.barCounsil);
                } else if (enrol_no.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.enrollmentno);
                } else if (date_enrol_no.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.enrollmentdate);

                } else if (w_no.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.welfareNo);
                } else if (year.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.year);

                } else if (month.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.month);
                } else if (p_court.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.practiceArea);

                } else if (specilization.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.specialtion);
                } else {

                    Intent i = new Intent(ActivityRegQualification.this, ActivityContactInformation.class);
                    i.putExtra("name", name);
                    i.putExtra("email", email);
                    i.putExtra("password", pass);
                    i.putExtra("confirm_password", c_pass);
                    i.putExtra("dob", dob);
                    i.putExtra("gender", gender);
                    i.putExtra("enrollment", enrol_no);
                    i.putExtra("date_enrolment", date_enrol_no);
                    i.putExtra("walfare_no", w_no);
                    i.putExtra("practise_court", p_court);
                    i.putExtra("specilization", specilization);
                    i.putExtra("university", university);
                    i.putExtra("mobile", mobile);
                    //i.putExtra("passing_year", pass_year);
                    i.putExtra("bar_counsiling", bar_counsiling);
                    //i.putExtra("year", year);
                    // i.putExtra("qualification", qua);
                    i.putExtra("month", month);
                    // i.putExtra("degree", degree);
                    startActivity(i);
                }
            }
        });


        sp_bar_council.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!object_bar_counciling.get(i).getName().contentEquals("Bar Council")) {
                    //daysID = ObjectDays.get(i).getid();
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_year_of_exp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!object_year.get(i).getName().contentEquals("Year")) {
                    //daysID = ObjectDays.get(i).getid();
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!object_month.get(i).getName().contentEquals("Month")) {
                    //daysID = ObjectDays.get(i).getid();
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_graduate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!object_qualification.get(i).getName().contentEquals("Qualification")) {
                    //daysID = ObjectDays.get(i).getid();
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
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
                    //daysID = ObjectDays.get(i).getid();
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getBarCounsilingData() {
        object_bar_counciling = new ArrayList<SpinnerModel>();
        object_bar_counciling.add(new SpinnerModel("", "Bar Council"));

        try {
            JSONArray array = new JSONArray(shared.getString(Config.State, "[{}]"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject state = array.getJSONObject(i);
                SpinnerModel model = new SpinnerModel();
                model.setid(state.getString("ah_state_id"));
                model.setName(state.getString("state_name"));
                object_bar_counciling.add(model);
            }
            sp_bar_council.setAdapter(new SpinnerAdapter(this, object_bar_counciling));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getYear() {
        object_year = new ArrayList<SpinnerModel>();
        object_year.add(new SpinnerModel("", "Year"));
        object_year.add(new SpinnerModel("1", "1"));
        object_year.add(new SpinnerModel("2", "2"));
        object_year.add(new SpinnerModel("3", "3"));
        object_year.add(new SpinnerModel("4", "4"));
        object_year.add(new SpinnerModel("5", "5"));
        object_year.add(new SpinnerModel("6", "6"));
        object_year.add(new SpinnerModel("7", "7"));
        object_year.add(new SpinnerModel("8", "8"));
        object_year.add(new SpinnerModel("9", "9"));
        object_year.add(new SpinnerModel("10", "10"));
        sp_year_of_exp.setAdapter(new SpinnerAdapter(this, object_year));
    }

    private void getQualification() {
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
            sp_graduate.setAdapter(new SpinnerAdapter(this, object_qualification));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void getMonth() {
        object_month = new ArrayList<SpinnerModel>();
        object_month.add(new SpinnerModel("", "Month"));
        object_month.add(new SpinnerModel("1", "1"));
        object_month.add(new SpinnerModel("2", "2"));
        object_month.add(new SpinnerModel("3", "3"));
        object_month.add(new SpinnerModel("4", "4"));
        object_month.add(new SpinnerModel("5", "5"));
        object_month.add(new SpinnerModel("6", "6"));
        object_month.add(new SpinnerModel("7", "7"));
        object_month.add(new SpinnerModel("8", "8"));
        object_month.add(new SpinnerModel("9", "9"));
        object_month.add(new SpinnerModel("10", "10"));
        object_month.add(new SpinnerModel("11", "11"));
        object_month.add(new SpinnerModel("12", "12"));
        sp_month.setAdapter(new SpinnerAdapter(this, object_month));
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
            sp_degree.setAdapter(new SpinnerAdapter(this, object_degree));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void PopupCourt() {

        LayoutInflater inflater = ActivityRegQualification.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.list_item_specailization, null);

        LinearLayout rel_root = (LinearLayout) c.findViewById(R.id.rel_root);
        RecyclerView listcourt = (RecyclerView) c.findViewById(R.id.listcourt);

        Button btnCancel = (Button) c.findViewById(R.id.btnCancel);
        Button btnOk = (Button) c.findViewById(R.id.btnOk);
        Application.setFontDefault((LinearLayout) c.findViewById(R.id.rel_root));


        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRegQualification.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        /****************************************************CourtList*************************/
        courtsList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(shared.getString(Config.court, "[{}]"));

            for (int i = 0; i < array.length(); i++) {
                JSONObject state = array.getJSONObject(i);
                ModelCourt model = new ModelCourt();
                model.setAh_court_id(state.getInt("ah_court_id"));
                model.setCourt_name(state.getString("court_name"));
                model.setIs_selected(state.getBoolean("is_selected"));
                courtsList.add(model);
            }

            courtAdapter = new CourtAdapter(courtsList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            listcourt.setLayoutManager(mLayoutManager);
            listcourt.setItemAnimator(new DefaultItemAnimator());
            listcourt.setAdapter(courtAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        /****************************************************End CourtList*************************/

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_Add_practise_court.setText("");
                if (courtAdapter.arrCourt != null) {
                    if (courtAdapter.arrCourt.size() > 0) {
                        String arrCourt = new Gson().toJson(courtAdapter.arrCourt);
                        shared.setListCourt(arrCourt);
                    } else {
                        courtAdapter.arrCourt.clear();
                        String arrCourt = new Gson().toJson(courtAdapter.arrCourt);
                        shared.setListCourt(arrCourt);
                        Toaster.getToast(getApplicationContext(), "Please Select Court");
                    }
                    String Coursdata = "";
                    for (int i = 0; i < courtAdapter.arrCourt.size(); i++) {
                        Coursdata = courtAdapter.arrCourt.get(i).getCourt_name() + ", ";
                        edt_Add_practise_court.append(Coursdata);
                    }

                    if (!Coursdata.contentEquals("")) {
                        edt_Add_practise_court.setText(edt_Add_practise_court.getText().toString().substring(0, edt_Add_practise_court.getText().toString().length() - 2));
                    } else {
                        //   Toaster.getToast(getApplicationContext(), "Please Select Court");
                    }


                }
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void PopupSpecailization() {
        LayoutInflater inflater = ActivityRegQualification.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.list_item_reg_specailization, null);

        LinearLayout rel_root = (LinearLayout) c.findViewById(R.id.rel_root);
        RecyclerView listspecilization = (RecyclerView) c.findViewById(R.id.listspecilization);

        Button btnCancel = (Button) c.findViewById(R.id.btnCancel);
        Button btnOk = (Button) c.findViewById(R.id.btnOk);
        Application.setFontDefault((LinearLayout) c.findViewById(R.id.rel_root));
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRegQualification.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        /****************************************************Specialization*************************/

        specailizationList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(shared.getString(Config.specialization, "[{}]"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject state = array.getJSONObject(i);
                ModelSpecialization model = new ModelSpecialization();
                model.setAh_lawyer_type_id(state.getString("ah_lawyer_type_id"));
                model.setLawyer_type(state.getString("lawyer_type"));
                model.setIs_selected(state.getBoolean("is_selected"));
                specailizationList.add(model);
            }
            specailizationAdapter = new RegSpecailizationAdapter(specailizationList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            listspecilization.setLayoutManager(mLayoutManager);
            listspecilization.setItemAnimator(new DefaultItemAnimator());
            listspecilization.setAdapter(specailizationAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /****************************************************End Specialization*************************/

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_Add_spacialization.setText("");
                if (specailizationAdapter.arrSpecialization != null) {
                    if (specailizationAdapter.arrSpecialization.size() > 0) {
                        String arrSpe = new Gson().toJson(specailizationAdapter.arrSpecialization);
                        shared.setListSpecialization(arrSpe);
                    } else {
                        specailizationAdapter.arrSpecialization.clear();
                        String arrSpe = new Gson().toJson(specailizationAdapter.arrSpecialization);
                        shared.setListSpecialization(arrSpe);
                        Toaster.getToast(getApplicationContext(), "Please select Specialization");
                    }
                    String spacilaization = "";
                    for (int i = 0; i < specailizationAdapter.arrSpecialization.size(); i++) {
                        spacilaization = specailizationAdapter.arrSpecialization.get(i).getLawyer_type() + ", ";
                        edt_Add_spacialization.append(spacilaization);

                    }
                    if (!spacilaization.contentEquals("")) {
                        edt_Add_spacialization.setText(edt_Add_spacialization.getText().toString().substring(0, edt_Add_spacialization.getText().toString().length() - 2));
                    } else {

                        //  Toaster.getToast(getApplicationContext(), "Please select Specialization");
                    }

                }
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
