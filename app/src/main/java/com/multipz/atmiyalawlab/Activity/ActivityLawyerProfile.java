package com.multipz.atmiyalawlab.Activity;

import android.Manifest;
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
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.SpinnerAdapter;
import com.multipz.atmiyalawlab.BuildConfig;
import com.multipz.atmiyalawlab.ImageLibrary.FileUtil;
import com.multipz.atmiyalawlab.ImageLibrary.GetPathFromURI;
import com.multipz.atmiyalawlab.ImageLibrary.PermissionUtil;
import com.multipz.atmiyalawlab.Model.SpinnerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.ImageFile;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ActivityLawyerProfile extends AppCompatActivity {

    private RelativeLayout rel_root, btnProfileUpdate, rel_progress;
    private CircleImageView img_registration;
    private EditText edt_name, edt_email, edt_address_line_1, edt_address_line_2, edt_pincode;
    private Spinner sp_state, sp_city;
    private ImageView img_back;
    ProgressDialog dialog;
    Context context;
    String param;
    Shared shared;
    ArrayList<SpinnerModel> object_state, object_city;
    String l_name = "", l_email = "", l_address1 = "", l_address2 = "", l_city = "", l_pincode = "";
    private String mCurrentPhotoPath = "";
    private ImageFile mImageFile;

    private Uri pictureUrl;
    private Uri cameraUrl = null;
    private CircularProgressView progressBar1;
    public static int CHOOSE_CAPTURE_PHOTO_INTENT = 101;
    public static int CHOOSE_PHOTO_INTENT = 102;
    public static int SELECT_PICTURE_CAMERA = 103;
    public static int SELECT_PHOTO_CAMERA = 104;
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_profile);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context = this;
        shared = new Shared(context);
        reference();

        init();

    }

    private void init() {
        if (Constant_method.checkConn(context)) {
            getMyProfile();
        } else {
            Toast.makeText(context, ErrorMessage.NoInternet, Toast.LENGTH_SHORT).show();
        }
        getstate();
        getCity();


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
                selectImage();
                //requestPhotoPermissions();
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
                    Toast.makeText(context, ErrorMessage.NoInternet, Toast.LENGTH_SHORT).show();
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

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_registration = (CircleImageView) findViewById(R.id.img_registration);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_address_line_1 = (EditText) findViewById(R.id.edt_address_line_1);
        edt_address_line_2 = (EditText) findViewById(R.id.edt_address_line_2);
        edt_pincode = (EditText) findViewById(R.id.edt_pincode);

        sp_state = (Spinner) findViewById(R.id.sp_state);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        btnProfileUpdate = (RelativeLayout) findViewById(R.id.btnProfileUpdate);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI((RelativeLayout) findViewById(R.id.rel_root), ActivityLawyerProfile.this);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow gender = (android.widget.ListPopupWindow) popup.get(sp_city);
            gender.setHeight(300);
           /* sp_gender.setHeight(Config.spinner_height);
            popupmonth.setHeight(Config.spinner_height);*/
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

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
//                        Toaster.getToast(getApplicationContext(), msg);
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
                            popUpAlertLogout(msg);
                        }
                    } else {
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


    private void getMyProfile() {
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
                        //dialog.dismiss();
                        rel_progress.setVisibility(View.GONE);
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
                            if (profile_img.contentEquals("")) {
                                Glide.with(context).load(R.drawable.user).into(img_registration);
                            } else {
                                Glide.with(context).load(profile_img).into(img_registration);
                            }
                        }
                    } else {
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_PHOTO_INTENT) {
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();

                    try {
                        path = GetPathFromURI.getPath(ActivityLawyerProfile.this, uri);
                        imageUpload(path);

                        Bitmap bitmap = getCompressedBitmap(path);
                        img_registration.setImageBitmap(bitmap);

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
                    imageUpload(path);
                    Bitmap bitmap = getCompressedBitmap(path);
                    img_registration.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

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

    private void imageUpload(final String imagePath) {
        dialog = new ProgressDialog(ActivityLawyerProfile.this);
        dialog.setTitle("Upload Image");
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        //dialog.show();
        rel_progress.setVisibility(View.VISIBLE);

        String paramURL = Config.Upload_profile_img;
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST,
                paramURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status;
                        String msg = null;
                        rel_progress.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getInt("status");
                            dialog.dismiss();
                            JSONObject object = jsonObject.getJSONObject("body");
                            msg = object.getString("msg");
                            if (status == 1) {
                                Toaster.getToast(context, msg);
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
                rel_progress.setVisibility(View.GONE);
                Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        smr.addFile(shared.getUserId(), imagePath);
        smr.setFixedStreamingMode(true);
        smr.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(smr);

    }

    /**************************************************Select Image***********************************/


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLawyerProfile.this);
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
        galleryIntent.setType("image/*");
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

    public Bitmap getCompressedBitmap(String imagePath) {
        float maxHeight = 1920.0f;
        float maxWidth = 1080.0f;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);

        byte[] byteArray = out.toByteArray();

        Bitmap updatedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        return updatedBitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public void popUpAlertLogout(String msg) {
        LayoutInflater inflater = ActivityLawyerProfile.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_submit, null);
        TextView txt_des = (TextView) c.findViewById(R.id.txt_des);
        LinearLayout lnr_ok = (LinearLayout) c.findViewById(R.id.lnr_ok);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityLawyerProfile.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_des.setText(msg);
        lnr_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityLawyerProfile.this, ActivityDashboard.class);
                startActivity(intent);

            }
        });

    }
}
