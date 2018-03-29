package com.multipz.atmiyalawlab.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.SpinnerAdapter;
import com.multipz.atmiyalawlab.ImageLibrary.GetPathFromURI;
import com.multipz.atmiyalawlab.ImageLibrary.PermissionUtil;
import com.multipz.atmiyalawlab.Model.SpinnerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityDashboardUser;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityContactInformation extends AppCompatActivity {

    private RelativeLayout rel_root, rel_submit, rel_progress;
    private EditText edt_address_line_1, edt_address_line_2, edt_pincode, edt_gst_no;
    private Spinner sp_state, sp_city, sp_proof;
    private TextView btn_Add_more_co;
    private LinearLayout lnr_upload_img;
    private ImageView img_back;
    private String name = "", email = "", pass = "", c_pass = "", dob = "", gender = "",
            enrol_no = "", date_enrol_no = "", w_no = "", p_court = "", specilization = "", university = "", pass_year = "", bar_counsiling = "", year = "", qua = "", month = "", degree = "", state = "", city = "", proof = "", mobile = "";
    String add_1 = "", add_2 = "", pin = "", gst_no = "", doc = "";
    private ProgressDialog dialog;
    private String param = "";
    private ImageView imageupload;
    private ArrayList<SpinnerModel> object_state, object_city, object_proof;
    Context context;
    Shared shared;
    private Uri pictureUrl;
    private Uri cameraUrl = null;
    String path = "";
    public static int CHOOSE_CAPTURE_PHOTO_INTENT = 101;
    public static int CHOOSE_PHOTO_INTENT = 102;
    public static int SELECT_PICTURE_CAMERA = 103;
    public static int SELECT_PHOTO_CAMERA = 104;
    TextView txt_submit;
    private CircularProgressView progressBar1;
    private LinearLayout lnr_useraddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        rel_submit = (RelativeLayout) findViewById(R.id.rel_submit);

        edt_address_line_1 = (EditText) findViewById(R.id.edt_address_line_1);
        edt_address_line_2 = (EditText) findViewById(R.id.edt_address_line_2);
        edt_pincode = (EditText) findViewById(R.id.edt_pincode);
        edt_gst_no = (EditText) findViewById(R.id.edt_gst_no);

        sp_state = (Spinner) findViewById(R.id.sp_state);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        sp_proof = (Spinner) findViewById(R.id.sp_proof);
        lnr_useraddress = (LinearLayout) findViewById(R.id.lnr_useraddress);
        txt_submit = (TextView) findViewById(R.id.txt_submit);
        imageupload = (ImageView) findViewById(R.id.imageupload);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

//        btn_Add_more_co = (TextView) findViewById(R.id.btn_Add_more_co);
        lnr_upload_img = (LinearLayout) findViewById(R.id.lnr_upload_img);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, ActivityContactInformation.this);


        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow state = (android.widget.ListPopupWindow) popup.get(sp_state);
            android.widget.ListPopupWindow city = (android.widget.ListPopupWindow) popup.get(sp_city);
            android.widget.ListPopupWindow proof = (android.widget.ListPopupWindow) popup.get(sp_proof);
            // state.setHeight(Config.spinner_height);
           // city.setHeight(Config.spinner_height);
           // proof.setHeight(Config.spinner_height);

        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        txt_submit.setVisibility(View.GONE);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("password");
        c_pass = getIntent().getStringExtra("confirm_password");
        dob = getIntent().getStringExtra("dob");
        gender = getIntent().getStringExtra("gender");
        enrol_no = getIntent().getStringExtra("enrollment");
        date_enrol_no = getIntent().getStringExtra("date_enrolment");
        w_no = getIntent().getStringExtra("walfare_no");
        p_court = getIntent().getStringExtra("practise_court");
        specilization = getIntent().getStringExtra("specilization");
        university = getIntent().getStringExtra("university");
        pass_year = getIntent().getStringExtra("passing_year");
        bar_counsiling = getIntent().getStringExtra("bar_counsiling");
        year = getIntent().getStringExtra("year");
        qua = getIntent().getStringExtra("qualification");
        month = getIntent().getStringExtra("month");
        degree = getIntent().getStringExtra("degree");
        mobile = getIntent().getStringExtra("mobile");


        getstate();
        getCity();
        getProof();


    }


    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (shared.getUsertype().contentEquals("L")) {
            lnr_useraddress.setVisibility(View.VISIBLE);
        } else if (shared.getUsertype().contentEquals("U")) {
            lnr_useraddress.setVisibility(View.GONE);
        }

        rel_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                add_1 = edt_address_line_1.getText().toString();
                add_2 = edt_address_line_2.getText().toString();
                pin = edt_pincode.getText().toString();
                gst_no = edt_gst_no.getText().toString();
                state = object_state.get(sp_state.getSelectedItemPosition()).getid();
                city = object_city.get(sp_city.getSelectedItemPosition()).getid();
                proof = object_proof.get(sp_proof.getSelectedItemPosition()).getid();


                if (shared.getUsertype().contentEquals("L")) {
                    if (add_1.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.emptyAdd1, Toast.LENGTH_SHORT).show();
                    } else if (add_2.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.emptyAdd2, Toast.LENGTH_SHORT).show();
                    } else if (state.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.selectState, Toast.LENGTH_SHORT).show();
                    } else if (city.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.selectCity, Toast.LENGTH_SHORT).show();
                    } else if (pin.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.Pincode, Toast.LENGTH_SHORT).show();
                    } else if (pin.toString().length() <= 5) {
                        Toast.makeText(context, ErrorMessage.validPincode, Toast.LENGTH_SHORT).show();
                    } else if (gst_no.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.gstno, Toast.LENGTH_SHORT).show();
                    } else if (doc.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.doc, Toast.LENGTH_SHORT).show();
                    } else if (path.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.doc, Toast.LENGTH_SHORT).show();
                    } else if (Constant_method.checkConn(context)) {
                        createPost();
                    } else {
                        Toast.makeText(context, ErrorMessage.NoInternet, Toast.LENGTH_SHORT).show();
                    }
                } else if (shared.getUsertype().contentEquals("U")) {
                    if (add_1.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.emptyAdd1, Toast.LENGTH_SHORT).show();
                    } else if (add_2.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.emptyAdd2, Toast.LENGTH_SHORT).show();
                    } else if (state.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.selectState, Toast.LENGTH_SHORT).show();
                    } else if (city.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.selectCity, Toast.LENGTH_SHORT).show();
                    } else if (pin.contentEquals("")) {
                        Toast.makeText(context, ErrorMessage.Pincode, Toast.LENGTH_SHORT).show();
                    } else if (pin.toString().length() <= 5) {
                        Toast.makeText(context, ErrorMessage.validPincode, Toast.LENGTH_SHORT).show();
                    } else if (Constant_method.checkConn(context)) {
                        createPost();
                    } else {
                        Toast.makeText(context, ErrorMessage.NoInternet, Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUpload(path);

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
        sp_proof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!object_proof.get(i).getName().contentEquals("Document")) {
                    doc = object_proof.get(i).getid();
                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                } else {

                    textView.setTextColor(getResources().getColor(R.color.atimiya_law_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doc = object_proof.get(sp_proof.getSelectedItemPosition()).getid();
                if (doc.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.doc);
                } else {
                    selectImage();
                }
            }
        });
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

    private void getProof() {
        object_proof = new ArrayList<SpinnerModel>();
        object_proof.add(new SpinnerModel("", "Select Proof"));
        try {
            JSONArray array = new JSONArray(shared.getString(Config.document, "[{}]"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject state = array.getJSONObject(i);
                SpinnerModel model = new SpinnerModel();

                model.setid(state.getString("documentid"));
                model.setName(state.getString("document_type"));
                object_proof.add(model);
            }
            sp_proof.setAdapter(new com.multipz.atmiyalawlab.Adapter.SpinnerAdapter(this, object_proof));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createPost() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(ActivityContactInformation.this);
        dialog.setMessage("Loading...");
        //  dialog.show();
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    String msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);

                        JSONArray jsonArray = object.getJSONArray("data");
                        shared.putString(Config.Logindata, jsonArray.toString());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String user_id = obj.getString("ah_users_id");
                            shared.setUserId(user_id);
                            String m_no = obj.getString("mobile_number");
                            String f_name = obj.getString("name");
                            shared.setUserName(f_name);
                            String email = obj.getString("email");
                            shared.setUserEmail(email);
                            String gender = obj.getString("gender");
                            String type = obj.getString("type");
                            shared.setlogin(true);
                            if (type.contentEquals("L")) {
                                Intent intent = new Intent(ActivityContactInformation.this, ActivityDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else if (type.contentEquals("U")) {
                                Intent intent = new Intent(ActivityContactInformation.this, ActivityDashboardUser.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                        //Toaster.getToast(getApplicationContext(), "" + msg);
                    } else if (status.contentEquals("0")) {
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(getApplicationContext(), "" + msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //    Toaster.getToast(getApplicationContext(), "" + error);
                //dialog.dismiss();
                rel_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                if (shared.getUsertype().contentEquals("L")) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("action", Config.createProfile);
                        JSONObject body = new JSONObject();
                        body.put("type", shared.getUsertype());
                        body.put("ah_users_id", shared.getUserId());
                        body.put("mobile_number", mobile);
                        /***********************************************/
                        JSONObject personali_info = new JSONObject();
                        personali_info.put("name", name);
                        personali_info.put("email", email);
                        personali_info.put("gender", gender);
                        personali_info.put("password", pass);
                        personali_info.put("date_of_birth", dob);
                        body.put("personalinfo", personali_info);


                        /***********************************************/

                        JSONObject qualification = new JSONObject();
                        qualification.put("barcouncilid", bar_counsiling);
                        qualification.put("enrollmentno", enrol_no);
                        qualification.put("enrollmentdate", date_enrol_no);
                        qualification.put("welfareno", w_no);
                        qualification.put("experienceinyear", year);
                        qualification.put("experienceinmonth", month);
                        qualification.put("practicecourt", shared.getListCourt());
                        qualification.put("specialization", shared.getListSpecialization());

                        qualification.put("graduation", shared.getListQualification());
                        body.put("qualificationinfo", qualification);
                        /*****************************************/
                        JSONObject contactinfo = new JSONObject();
                        contactinfo.put("address1", add_1);
                        contactinfo.put("address2", add_2);
                        contactinfo.put("ah_city_id", city);
                        contactinfo.put("pincode", pin);
                        contactinfo.put("gstno", gst_no);
                        body.put("contactinfo", contactinfo);

                        object.put("body", body);
                        param = object.toString().replaceAll("\\\\", "").replaceAll("\"\\[", "[").replaceAll("\\]\"", "]");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (shared.getUsertype().contentEquals("U")) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("action", Config.createProfile);
                        JSONObject body = new JSONObject();
                        body.put("type", shared.getUsertype());
                        body.put("ah_users_id", shared.getUserId());
                        body.put("mobile_number", mobile);
                        /***********************************************/
                        JSONObject personali_info = new JSONObject();
                        personali_info.put("name", name);
                        personali_info.put("email", email);
                        personali_info.put("gender", gender);
                        personali_info.put("password", pass);
                        personali_info.put("date_of_birth", dob);
                        body.put("personalinfo", personali_info);
                        /***********************************************/
                        JSONObject qualification = new JSONObject();
                        qualification.put("barcouncilid", "");
                        qualification.put("enrollmentno", "");
                        qualification.put("enrollmentdate", "");
                        qualification.put("welfareno", "");
                        qualification.put("experienceinyear", "");
                        qualification.put("experienceinmonth", "");
                        qualification.put("practicecourt", "");
                        qualification.put("specialization", "");
                        qualification.put("graduation", "");
                        body.put("qualificationinfo", qualification);
                        /*****************************************/
                        JSONObject contactinfo = new JSONObject();
                        contactinfo.put("address1", add_1);
                        contactinfo.put("address2", add_2);
                        contactinfo.put("ah_city_id", city);
                        contactinfo.put("pincode", pin);
                        contactinfo.put("gstno", "");
                        body.put("contactinfo", contactinfo);
                        object.put("body", body);
                        param = object.toString().replaceAll("\\\\", "").replaceAll("\"\\[", "[").replaceAll("\\]\"", "]");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

//                String param = "{\"action\":\"" + Config.getcountry + "\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_PHOTO_INTENT) {
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();

                    try {
                        path = GetPathFromURI.getPath(ActivityContactInformation.this, uri);
//                        imageUpload(path);

                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        imageupload.setImageBitmap(bitmap);
                        txt_submit.setVisibility(View.VISIBLE);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }
            } else if (requestCode == CHOOSE_CAPTURE_PHOTO_INTENT) {
                try {
                    pictureUrl = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
                    try {
                        path = GetPathFromURI.getPath(context, pictureUrl);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
//                    imageUpload(path);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imageupload.setImageBitmap(bitmap);
                    txt_submit.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void imageUpload(final String imagePath) {
        dialog = new ProgressDialog(ActivityContactInformation.this);
        dialog.setTitle("Upload Image");
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();

        String paramURL = Config.Upload_doc_img;
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST,
                paramURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status;
                        String msg = null;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getInt("status");
                            dialog.dismiss();
                            JSONObject object = jsonObject.getJSONObject("body");
                            msg = object.getString("msg");
                            if (status == 1) {
                                Toaster.getToast(context, msg);
                                imageupload.setImageDrawable(getResources().getDrawable(R.drawable.upload));
                                txt_submit.setVisibility(View.GONE);
                            } else {
                                Toaster.getToast(context, "" + msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        smr.addFile(shared.getUserId() + "," + doc, imagePath);
        smr.setFixedStreamingMode(true);
        smr.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(smr);

    }

    /*******************************************************************************************/
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContactInformation.this);
        builder.setTitle("Add Profile Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    initCatureImage();
                } else if (options[item].equals("Choose from Gallery")) {
                    initCaturePickupCemara();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void setCaptureCemara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI;
        if (Build.VERSION.SDK_INT > 23) {
//                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName(), new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
//                                photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", UtilPicture.getTempFile());

        } else {
            photoURI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, CHOOSE_CAPTURE_PHOTO_INTENT);
    }

    private void PickUpGallary() {
        final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("*/*");
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, galleryIntent);
        startActivityForResult(chooserIntent, CHOOSE_PHOTO_INTENT);
    }

    private void initCatureImage() {
        PermissionUtil permissionUtil = new PermissionUtil();
        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(context, permissionUtil.getCameraPermissions()) && permissionUtil.verifyPermissions(context, permissionUtil.getGalleryPermissions()))
                setCaptureCemara();
            else {
                ActivityCompat.requestPermissions((Activity) context, permissionUtil.getCameraPermissions(), SELECT_PICTURE_CAMERA);
            }
        } else {
            setCaptureCemara();
        }
    }

    private void initCaturePickupCemara() {
        PermissionUtil permissionUtil = new PermissionUtil();

        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(context, permissionUtil.getCameraPermissions()) && permissionUtil.verifyPermissions(context, permissionUtil.getGalleryPermissions()))
                PickUpGallary();
            else {
                ActivityCompat.requestPermissions((Activity) context, permissionUtil.getCameraPermissions(), SELECT_PHOTO_CAMERA);
            }
        } else {
            PickUpGallary();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SELECT_PICTURE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCatureImage();
            }
        } else if (requestCode == SELECT_PHOTO_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCaturePickupCemara();
            }
        }
    }


}
