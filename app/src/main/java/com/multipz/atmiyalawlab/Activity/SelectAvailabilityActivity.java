package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.SelectAvailabilityAdapter;
import com.multipz.atmiyalawlab.Model.SelectAvailabilityModel;
import com.multipz.atmiyalawlab.Model.SpinnerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.MyAsyncTask;
import com.multipz.atmiyalawlab.Util.RecyclerItemTouchHelper;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SelectAvailabilityActivity extends AppCompatActivity implements ItemClickListener, MyAsyncTask.AsyncInterface, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private Spinner spinner2;
    private TextView txt_save;
    private ArrayList<SelectAvailabilityModel> list;
    private EditText edtStartTimeMon, edtEndTimeMon;
    private String format = "";
    private SelectAvailabilityAdapter adapterlist;
    private RecyclerView listavailability;
    private Shared shared;
    private Context context;
    ArrayList<SpinnerModel> ObjectDays;
    private String param = "", daysID = "", startTime = "", endTime = "";
    private int removePos = 0;
    private ImageView img_back;
    private RelativeLayout rel_root;
    private Paint p = new Paint();
    private View view;
    ProgressDialog dialog;
    String date;
    TextView textView;
    private CircularProgressView progressBar1;
    RelativeLayout rel_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_availability);
        context = this;
        shared = new Shared(context);
        reference();
        init();


    }

    private void init() {
        if (Constant_method.checkConn(context)) {
            getDays();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }


        list = new ArrayList<>();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!ObjectDays.get(i).getName().contentEquals("days")) {
                    daysID = ObjectDays.get(i).getid();
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        edtStartTimeMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SelectAvailabilityActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //startTime = hour + ":" + minute /*+ ":" + "00"*/;
                        getTimeFormate(selectedHour, selectedMinute, view);
                        startTime = edtStartTimeMon.getText().toString();
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        edtEndTimeMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SelectAvailabilityActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int selectedMinute) {
                        //endTime = hourOfDay + ":" + selectedMinute /*+ ":" + "00"*/;
                        getTimeFormate(hourOfDay, selectedMinute, view);
                        endTime = edtEndTimeMon.getText().toString();
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (daysID.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select Day");
                } else if (edtStartTimeMon.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select Start Time");
                } else if (edtEndTimeMon.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select End Time");
                } else {
                    createlawyeravailability(daysID, startTime, endTime);
                 /*   SelectAvailabilityModel model = new SelectAvailabilityModel();
                    model.setDay_name("");
                    model.setStart_time(edtStartTimeMon.getText().toString());
                    model.setEnd_time(edtEndTimeMon.getText().toString());
                    list.add(model);

                    adapterlist = new SelectAvailabilityAdapter(getApplicationContext(), list);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    listavailability.setLayoutManager(mLayoutManager);
                    listavailability.setAdapter(adapterlist);
                    adapterlist.setClickListener(SelectAvalibilty.this);*/


                }

            }
        });


    }


    private void getDays() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;

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
                        dialog.dismiss();
                        JSONObject dataobj = object.getJSONObject("data");
                        JSONArray account_details = dataobj.getJSONArray("day");

                        for (int i = 0; i < account_details.length(); i++) {
                            JSONObject c = account_details.getJSONObject(i);
                            SpinnerModel model = new SpinnerModel();
                            model.setName(c.getString("day_name"));
                            model.setid(c.getString("ah_day_id"));
                            ObjectDays.add(model);
                        }
                        spinner2.setAdapter(new com.multipz.atmiyalawlab.Adapter.SpinnerAdapter(SelectAvailabilityActivity.this, ObjectDays));
                    } else if (status.contentEquals("0")) {
                        dialog.dismiss();
                        Toaster.getToast(context, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.config);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                String param = "{\"action\":\"getcourt\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void reference() {
        list = new ArrayList<>();
        ObjectDays = new ArrayList<>();
        txt_save = (TextView) findViewById(R.id.txt_save);
        spinner2 = (Spinner) findViewById(R.id.sp_day);
        img_back = (ImageView) findViewById(R.id.img_back);
        edtStartTimeMon = (EditText) findViewById(R.id.edtStartTimeMonnew);
        edtEndTimeMon = (EditText) findViewById(R.id.edtEndTimeMonnew);
        listavailability = (RecyclerView) findViewById(R.id.listavailability);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, SelectAvailabilityActivity.this);
        if (Constant_method.checkConn(context)) {
            getLawyerAvailabilty();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }
    }

    private void createlawyeravailability(String daysID, String startTime, String endTime) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.addLawyerAvailability);
            JSONObject user = new JSONObject();
            user.put("ah_users_id", shared.getUserId());
            user.put("ah_day_id", daysID);
            user.put("start_time", startTime);
            user.put("end_time", endTime);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SelectAvailabilityActivity.this, param, Config.API_ADD_CREATE_LAYER_AVAILABILTY);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void getTimeFormate(int hourOfDay, int selectedMinute, View view) {
        if (hourOfDay == 0) {
            hourOfDay += 12;
            format = "AM";
        } else if (hourOfDay == 12) {
            format = "PM";
        } else if (hourOfDay > 12) {
            hourOfDay -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        if (view.getId() == R.id.edtStartTimeMonnew) {
            edtStartTimeMon.setText(pad(hourOfDay) + ":" + pad(selectedMinute) + " " + format);
        } else if (view.getId() == R.id.edtEndTimeMonnew) {
            edtEndTimeMon.setText(pad(hourOfDay) + ":" + pad(selectedMinute) + " " + format);
        }

    }


    private void deletelawyercourt(String lawyer_availabilty_id) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.deletelawyeravailability);
            JSONObject user = new JSONObject();
            user.put("ah_lawyer_availability_id", lawyer_availabilty_id);
            user.put("ah_users_id", shared.getUserId());
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SelectAvailabilityActivity.this, param, Config.API_DELETE_LAWYER_AVAILABILITY);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLawyerAvailabilty() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        //  dialog.show();
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
                        dialog.dismiss();
                        rel_progress.setVisibility(View.GONE);
                        JSONObject dataobj = object.getJSONObject("data");
                        list.clear();
                        JSONArray account_details = dataobj.getJSONArray("availability");
                        for (int i = 0; i < account_details.length(); i++) {
                            JSONObject obj = account_details.getJSONObject(i);
                            SelectAvailabilityModel model = new SelectAvailabilityModel();
                            model.setAh_day_id(obj.getString("ah_day_id"));
                            model.setDay_name(obj.getString("day_name"));
                            model.setAh_lawyer_availability_id(obj.getString("ah_lawyer_availability_id"));
                            model.setStart_time(obj.getString("start_time"));
                            model.setEnd_time(obj.getString("end_time"));
                            list.add(model);
                        }
                        adapterlist = new SelectAvailabilityAdapter(getApplicationContext(), list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        listavailability.setLayoutManager(mLayoutManager);
                        listavailability.setAdapter(adapterlist);
                        //adapterlist.setClickListener(new LawyerLanguageActivity());
                        adapterlist.notifyDataSetChanged();
                        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, SelectAvailabilityActivity.this);
                        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(listavailability);
                    } else if (status.contentEquals("0")) {
//                        dialog.dismiss();
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(getApplicationContext(), msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
                rel_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getSetting);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("type", shared.getUsertype());
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

    @Override
    public void itemClicked(View view, int position) {
        removePos = position;
        SelectAvailabilityModel model = list.get(position);
       /* if (view.getId() == R.id.btnDelete) {
            String ah_lawyer_availability_id = model.getAh_lawyer_availability_id();
            deletelawyercourt(ah_lawyer_availability_id);
        }*/
    }


    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        JSONObject object = null;
        if (flag == Config.API_ADD_CREATE_LAYER_AVAILABILTY) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    edtStartTimeMon.setText("");
                    edtEndTimeMon.setText("");
                    spinner2.setSelection(0);
                    Toaster.getToast(getApplicationContext(), Message);
                    if (Constant_method.checkConn(context)) {
                        getLawyerAvailabilty();
                    } else {
                        Toaster.getToast(context, ErrorMessage.NoInternet);
                    }

                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (flag == Config.API_DELETE_LAWYER_AVAILABILITY) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), Message);
                    adapterlist.removeItem(removePos);
                    adapterlist.notifyDataSetChanged();

                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof SelectAvailabilityAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = list.get(viewHolder.getAdapterPosition()).getDay_name();
            // backup of removed item for undo purpose
            final SelectAvailabilityModel deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            String ah_lawyer_availability_id = deletedItem.getAh_lawyer_availability_id();
            deletelawyercourt(ah_lawyer_availability_id);
            // remove the item from recycler view
            //adapterlist.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
          /*  Snackbar snackbar = Snackbar
                    .make(rel_root, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    adapterlist.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();*/
        }
    }
}
