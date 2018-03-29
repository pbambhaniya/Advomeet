package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.SpinnerAdapter;
import com.multipz.atmiyalawlab.Model.SpinnerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.misc.AsyncTask.init;

public class LawyerProfileActivity extends AppCompatActivity {

    private RelativeLayout rel_root, rel_progress;
    private CircleImageView img_registration;
    private EditText edt_name, edt_email, edt_address_line_1, edt_address_line_2, edt_pincode;
    private Spinner sp_state, sp_city;
    private Button btnProfileUpdate;
    ProgressDialog dialog;
    Context context;
    String param;
    Shared shared;
    ArrayList<SpinnerModel> object_state, object_city;
    String l_name, l_email, l_address1, l_address2, l_city, l_pincode;

    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_profile2);

        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        img_registration = (CircleImageView) findViewById(R.id.img_registration);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_address_line_1 = (EditText) findViewById(R.id.edt_address_line_1);
        edt_address_line_2 = (EditText) findViewById(R.id.edt_address_line_2);
        edt_pincode = (EditText) findViewById(R.id.edt_pincode);

        sp_state = (Spinner) findViewById(R.id.sp_state);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        btnProfileUpdate = (Button) findViewById(R.id.btnProfileUpdate);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();


        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        HideSoftKeyboard.setupUI(rel_root, LawyerProfileActivity.this);


    }


    private void updateProfile() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;

        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String msg = "", status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");

                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);
                        JSONArray array = object.getJSONArray("data");
                        shared.putString(Config.Logindata, array.toString());
                        Toaster.getToast(getApplicationContext(), msg);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String id = obj.getString("ah_users_id");
                            String name = obj.getString("name");
                            String email = obj.getString("email");
                            String address1 = obj.getString("address1");
                            String address2 = obj.getString("address2");
                            String ah_city_id = obj.getString("ah_city_id");
                            String city_name = obj.getString("city_name");
                            String ah_state_id = obj.getString("ah_state_id");
                            String state_name = obj.getString("state_name");
                            String pincode = obj.getString("pincode");
                            String profile_img = obj.getString("profile_img");
                            String type = obj.getString("type");
                            Intent intent = new Intent(LawyerProfileActivity.this, ActivityDashboard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    } else if (status.contentEquals("0")) {
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(context, "" + msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                rel_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.updateProfile);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("name", l_name);
                    body.put("email", l_email);
                    body.put("address1", l_address1);
                    body.put("address2", l_address2);
                    body.put("ah_city_id", l_city);
                    body.put("pincode", l_pincode);
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void init() {
        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!object_state.get(i).getName().contentEquals("State")) {
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
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!object_city.get(i).getName().contentEquals("City")) {
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
        img_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectImage();
            }
        });

        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                l_name = edt_name.getText().toString();
                l_email = edt_email.getText().toString();
                l_address1 = edt_address_line_1.getText().toString();
                l_address2 = edt_address_line_2.getText().toString();
                l_pincode = edt_pincode.getText().toString();
                l_city = object_city.get(sp_city.getSelectedItemPosition()).getid();
                if (Constant_method.checkConn(context)) {
                    updateProfile();
                } else {
                    Toaster.getToast(context, ErrorMessage.NoInternet);
                }

            }
        });

        getMyProfile();
        getstate();
        getCity();
    }

    private void getMyProfile() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        //dialog.show();
        rel_progress.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int status;
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status == 1) {
                        rel_progress.setVisibility(View.VISIBLE);

                        //Toaster.getToast(getApplicationContext(), msg);
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String ah_users_id = obj.getString("ah_users_id");
                            String name = obj.getString("name");
                            String email = obj.getString("email");
                            String address1 = obj.getString("address1");
                            String address2 = obj.getString("address2");
                            String ah_city_id = obj.getString("ah_city_id");
                            String city_name = obj.getString("city_name");
                            String ah_state_id = obj.getString("ah_state_id");
                            String state_name = obj.getString("state_name");
                            String pincode = obj.getString("pincode");
                            String profile_img = obj.getString("profile_img");
                            String type = obj.getString("type");
                            edt_name.setText(name);
                            edt_email.setText(email);
                            edt_address_line_1.setText(address1);
                            edt_address_line_2.setText(address2);
                            edt_pincode.setText(pincode);
                            setSpinner(sp_state, object_state, state_name);
                            setSpinner(sp_city, object_city, city_name);
                            Picasso.with(context).load(profile_img).placeholder(R.drawable.user).into(img_registration);
                        }
                    } else {
                        rel_progress.setVisibility(View.VISIBLE);

                        Toaster.getToast(context, "" + msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                rel_progress.setVisibility(View.VISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getProfile);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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

        sp_state.setAdapter(new SpinnerAdapter(this, object_state));

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


    private void setSpinner(Spinner asset_id, ArrayList<SpinnerModel> objects_company, String assetsName) {
        for (int i = 0; i < objects_company.size(); i++) {
            if (assetsName.contentEquals(objects_company.get(i).getName())) {
                asset_id.setSelection(i);
                break;
            }
        }
    }


}
