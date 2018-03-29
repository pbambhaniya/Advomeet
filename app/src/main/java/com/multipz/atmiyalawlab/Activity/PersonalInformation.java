package com.multipz.atmiyalawlab.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.multipz.atmiyalawlab.Adapter.SpinnerAdapter;
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
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInformation extends AppCompatActivity {

    private RelativeLayout rel_root, btn_next, rel_dob;
    private ImageView img_back;
    private CircleImageView img_registration;
    private EditText edt_per_name, edt_per_email, edt_per_confirm_password, edt_dob, edt_per_password;
    private Spinner sp_gender;
    private ProgressDialog dialog;
    private Context context;
    private String name = "", email = "", pass = "", c_pass = "", dob = "", gender = "", mobile = "";
    private ArrayList<SpinnerModel> object_gender;
    private int mYear, mMonth, mDay;
    Shared shared;

    private Uri pictureUrl;
    private Uri cameraUrl = null;
    public static int CHOOSE_CAPTURE_PHOTO_INTENT = 101;
    public static int CHOOSE_PHOTO_INTENT = 102;
    public static int SELECT_PICTURE_CAMERA = 103;
    public static int SELECT_PHOTO_CAMERA = 104;
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_information);
        context = this;
        shared = new Shared(context);
        refernce();
        init();
    }

    private void refernce() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        btn_next = (RelativeLayout) findViewById(R.id.btn_next);
        rel_dob = (RelativeLayout) findViewById(R.id.rel_dob);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_registration = (CircleImageView) findViewById(R.id.img_registration);
        edt_per_name = (EditText) findViewById(R.id.edt_per_name);
        edt_per_email = (EditText) findViewById(R.id.edt_per_email);
        edt_dob = (EditText) findViewById(R.id.edt_dob);
        edt_per_password = (EditText) findViewById(R.id.edt_per_password);
        edt_per_confirm_password = (EditText) findViewById(R.id.edt_per_confirm_password);
        edt_per_name = (EditText) findViewById(R.id.edt_per_name);
        sp_gender = (Spinner) findViewById(R.id.sp_gender);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI((RelativeLayout) findViewById(R.id.rel_root), PersonalInformation.this);

       /* try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow gender = (android.widget.ListPopupWindow) popup.get(sp_gender);
            gender.setHeight(280);
           *//* sp_gender.setHeight(Config.spinner_height);
            popupmonth.setHeight(Config.spinner_height);*//*
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }*/
        getGenderData();
    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                if (edt_dob.getText().toString().contentEquals("")) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    String[] s = edt_dob.getText().toString().split("-");
                    mYear = Integer.parseInt(s[0]);
                    mMonth = Integer.parseInt(s[1]) - 1;
                    mDay = Integer.parseInt(s[2]);
                }

                final DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalInformation.this,
                        R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
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
                            edt_dob.setText(year + "-0" + (monthOfYear + 1) + "-" + day);
                        } else {
                            edt_dob.setText(year + "-" + (monthOfYear + 1) + "-" + day);
                        }


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.company);
                if (!object_gender.get(i).getName().contentEquals("Gender")) {
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

        btn_next.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                name = edt_per_name.getText().toString();
                email = edt_per_email.getText().toString();
                pass = edt_per_password.getText().toString();
                c_pass = edt_per_confirm_password.getText().toString();
                dob = edt_dob.getText().toString();
                gender = object_gender.get(sp_gender.getSelectedItemPosition()).getName();

                if (name.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.enterName);
                } else if (email.trim().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterEmail);
                } else if (!Constant_method.isValidEmail(email)) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterValidEmail);
                } else if (pass.contentEquals("")) {
                    Toast.makeText(context, ErrorMessage.enterPassword, Toast.LENGTH_SHORT).show();
                } else if (!pass.contentEquals(c_pass)) {
                    Toast.makeText(context, ErrorMessage.entervalidpassword, Toast.LENGTH_SHORT).show();
                } else if (gender.contentEquals("Gender")) {
                    Toaster.getToast(context, ErrorMessage.gender);

                } else if (dob.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.dob);
                } else {
                    pass = c_pass;
                   /* Intent i = new Intent(PersonalInformation.this, ActivityRegQualification.class);
                    i.putExtra("name", name);
                    i.putExtra("email", email);
                    i.putExtra("password", pass);
                    i.putExtra("confirm_password", c_pass);
                    i.putExtra("birthdate", dob);
                    i.putExtra("gender", gender);

                    startActivity(i);*/

                    if (shared.getUsertype().contentEquals("U")) {
                        Intent i = new Intent(PersonalInformation.this, ActivityContactInformation.class);
                        i.putExtra("name", name);
                        i.putExtra("email", email);
                        i.putExtra("password", pass);
                        i.putExtra("confirm_password", c_pass);
                        i.putExtra("birthdate", dob);
                        i.putExtra("gender", gender);
                        i.putExtra("mobile", mobile);
                        startActivity(i);

                    } else if (shared.getUsertype().contentEquals("L")) {
                        Intent i = new Intent(PersonalInformation.this, ActivityQualification.class);
                        i.putExtra("name", name);
                        i.putExtra("email", email);
                        i.putExtra("password", pass);
                        i.putExtra("confirm_password", c_pass);
                        i.putExtra("birthdate", dob);
                        i.putExtra("gender", gender);
                        i.putExtra("mobile", mobile);
                        startActivity(i);
                    }


                }
            }
        });


        img_registration.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                selectImage();

            }
        });
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
                        path = GetPathFromURI.getPath(PersonalInformation.this, uri);
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
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;

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

    private void imageUpload(final String imagePath) {
        dialog = new ProgressDialog(PersonalInformation.this);
        dialog.setTitle("Upload Image");
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();

        String paramURL = Config.Upload_profile_img;

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST,
                paramURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg = "", status = "";
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getString("status");
                            dialog.dismiss();

                            if (status.contentEquals("1")) {
                                JSONObject object = jsonObject.getJSONObject("body");
                                msg = object.getString("msg");
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

    private void getGenderData() {
        object_gender = new ArrayList<SpinnerModel>();
        object_gender.add(new SpinnerModel("", "Gender"));
        object_gender.add(new SpinnerModel("Male", "Male"));
        object_gender.add(new SpinnerModel("Female", "Female"));
        sp_gender.setAdapter(new SpinnerAdapter(this, object_gender));

    }

    /**************************************************Select Image***********************************/


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformation.this);
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


}
