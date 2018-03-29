package com.multipz.atmiyalawlab.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.bumptech.glide.Glide;
import com.multipz.atmiyalawlab.Adapter.ChattingAdapter;
import com.multipz.atmiyalawlab.ImageLibrary.GetPathFromURI;
import com.multipz.atmiyalawlab.ImageLibrary.PermissionUtil;
import com.multipz.atmiyalawlab.Model.ChatMessage;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ConstantChatMedia;
import com.multipz.atmiyalawlab.Util.DownloadTask;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.SocketManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;

public class ActivityChatting extends AppCompatActivity implements Emitter.Listener, ItemClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
    ImageView img_back;
    private RelativeLayout rel_root;
    private EditText messageEdit;
    private TextView btnSend;
    private RecyclerView messagesContainer;
    private ArrayList<ChatMessage> chatHistory;
    private ImageView imgmedia;
    private String recevierID, name;
    private TextView txt_profile_name, txtstatus, receiverstatus;
    private ChattingAdapter chattingAdapter;
    private SocketManager socketManager;
    String filePath;
    ProgressDialog dialog;
    private CircleImageView img_profile;
    private String msgData;
    private String SendtypeMedia;
    final MediaPlayer mediaPlayer = new MediaPlayer();
    private SeekBar seekBar;
    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            updateSeekProgress();
        }
    };
    private TextView timeplay;
    private ImageView imgplaymusicreceiver, imgpausemusicreceiver, imagesetlocaly;
    private Context context;
    public static int CHOOSE_CAPTURE_PHOTO_INTENT = 101;
    public static int CHOOSE_PHOTO_INTENT = 102;
    public static int SELECT_PICTURE_CAMERA = 103;
    public static int SELECT_PHOTO_CAMERA = 104;
    String path = "", image = "", MediaType = "";
    private Uri pictureUrl;
    private Shared shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        context = this;
        shared = new Shared(context);
        chatHistory = new ArrayList<ChatMessage>();
        socketManager = SocketManager.getInstance(getApplicationContext());
        reference();
        init();
    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        messageEdit = (EditText) findViewById(R.id.messageEdit);
        btnSend = (TextView) findViewById(R.id.btnSend);
        messagesContainer = (RecyclerView) findViewById(R.id.messagesContainer);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        img_profile = (CircleImageView) findViewById(R.id.img_profile);
        txt_profile_name = (TextView) findViewById(R.id.txt_profile_name);
        txtstatus = (TextView) findViewById(R.id.txtstatus);
        //txtreceivername = (TextView) findViewById(R.id.txtreceivername);
        // receiverstatus = (TextView) findViewById(R.id.receiverstatus);
        imgmedia = (ImageView) findViewById(R.id.imgmedia);
        imagesetlocaly = (ImageView) findViewById(R.id.imagesetlocaly);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, ActivityChatting.this);

    }


    private void init() {
        /*socketManager.connect();
        SocketConnection();*/

        Intent i = getIntent();
        recevierID = i.getStringExtra("recevierID");
        name = i.getStringExtra("name");
        image = i.getStringExtra("image");
        txt_profile_name.setText(name);
        Glide.with(context).load(Config.chatProfile + image).error(R.drawable.user).into(img_profile);
//        txtreceivername.setText(name);


        getChatMsg();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaType.contentEquals("Media")) {
                    imageUpload(path, "");
                } else if (messageEdit.getText().toString().equals("")) {
                    Toast.makeText(ActivityChatting.this, "please enter msg", Toast.LENGTH_SHORT).show();
                } else {
                    msgData = messageEdit.getText().toString();
                    //displayMessage(msgData, "Msg");
                    SendMsg(msgData, "Msg");
                    messageEdit.setText("");
                }
            }
        });

        imgmedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        messagesContainer.scrollToPosition(chatHistory.size() - 1);


        messageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (charSequence.length() > 0) {
                    SocketTyping("online");
                } else {
                    SocketTyping("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    private void SocketTyping(String type) {
        final JSONObject objdata = new JSONObject();
        try {
            objdata.put("ah_users_id", /*"1"*/shared.getUserId());
            objdata.put("ah_receiver_id", recevierID);
            objdata.put("typestatus", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject obj = new JSONObject();
        try {
            obj.put("action", Config.Typing);
            obj.put("data", objdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketManager.sendEmit("request", obj);
        socketManager.addListener("response", this);
    }

    private void SendMsgWithMedia(final String filePath, String type) {

        // imageUpload(filePath, type);
        MediaType = "Media";
        Bitmap bitmap = getCompressedBitmap(filePath);
        imagesetlocaly.setImageBitmap(bitmap);
        imagesetlocaly.setVisibility(View.VISIBLE);
        //socketManager.addListener("response", this);
    }

    private void SendMsg(String msg, String type) {
        // send userdata
        final JSONObject objdata = new JSONObject();
        final JSONObject obj = new JSONObject();
        if (type.matches("Msg")) {
            try {
                objdata.put("ah_users_id", /*"28"*/shared.getUserId());
                objdata.put("ah_receiver_id", recevierID);
                objdata.put("msg", msg);
                objdata.put("type", type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                obj.put("action", Config.SendMsg);
                obj.put("data", objdata);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.matches("Media")) {
            try {
                objdata.put("ah_users_id", /*"28"*/shared.getUserId());
                objdata.put("ah_receiver_id", recevierID);
                objdata.put("msg", msg);
                objdata.put("type", type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                obj.put("action", Config.SendMsg);
                obj.put("data", objdata);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  getMedia(msg);
        }
        socketManager.sendEmit("request", obj);
        socketManager.addListener("response", this);

    }

    private void getChatMsg() {
        // send userdata
        final JSONObject objdata = new JSONObject();
        try {
            objdata.put("ah_users_id", /*"28"*/shared.getUserId());
            objdata.put("ah_receiver_id", recevierID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject obj = new JSONObject();
        try {
            obj.put("action", Config.GetChatMsg);
            obj.put("data", objdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketManager.sendEmit("request", obj);
        socketManager.addListener("response", this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void displayMessage(final String message, final String type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (chatHistory != null) {
                    if (type.matches("Msg")) {
                        if (chatHistory.size() > 0) {
                            ChatMessage m = new ChatMessage();
                            m.setMessage(message);
                            m.setMsgType("Msg");
                            m.setUserId(shared.getUserId());
                            chatHistory.add(0, m);
                            //chattingAdapter.notifyItemInserted(chatHistory.size() - 1);
                            chattingAdapter.notifyDataSetChanged();
                            scroll();
                        } else {
                            ChatMessage m = new ChatMessage();
                            m.setMessage(message);
                            m.setMsgType("Msg");
                            m.setUserId("1");
                            chatHistory.add(m);
                            chattingAdapter = new ChattingAdapter(getApplicationContext(), chatHistory);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            messagesContainer.setLayoutManager(layoutManager);
                            messagesContainer.setAdapter(chattingAdapter);
                            chattingAdapter.notifyDataSetChanged();
                            scroll();
                        }

                    } else if (type.matches("Media")) {
                        ChatMessage m = new ChatMessage();
                        m.setMessage(message);
                        m.setMsgType("Media");
                        m.setUserId(shared.getUserId());
                        chatHistory.add(m);
                        chattingAdapter.notifyDataSetChanged();
                        scroll();
                    }

                }
            }
        });
    }

    private void scroll() {
        messagesContainer.scrollToPosition(chatHistory.size() - 1);
    }

    private void updateSeekProgress() {
        if (mediaPlayer != null) {
            int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
            seekBar.setProgress(mCurrentPosition);
        }
        int curPos = mediaPlayer.getCurrentPosition();
        int totPos = mediaPlayer.getDuration();
        timeplay.setText(getTimeString(curPos) + "/" + getTimeString(totPos));
        seekBar.postDelayed(r, 1000);
    }

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();
        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf.append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void itemClicked(View view, int position) {
        ChatMessage cmsg = chatHistory.get(position);
        String url = cmsg.getMessage();
        if (view.getId() == R.id.downloadfile) {
            new DownloadTask(context, url);
        } else if (view.getId() == R.id.layoutaudio) {
            popUpplaymusicreceiver(url);
        } else if (view.getId() == R.id.layoutaudioright) {
            popUpplaymusicreceiver(url);
        } else if (view.getId() == R.id.downloadfileright) {
            new DownloadTask(context, url);
        } else if (view.getId() == R.id.imagesenduserleft) {

            Intent intent = new Intent(ActivityChatting.this, ChattingImageActivity.class);
            intent.putExtra("msg", cmsg.getMessage());
            startActivity(intent);
        } else if (view.getId() == R.id.imagesenduserright) {
            Intent intent = new Intent(ActivityChatting.this, ChattingImageActivity.class);
            intent.putExtra("msg", cmsg.getMessage());
            startActivity(intent);
        }
    }

    @Override
    public void call(final Object... args) {
        ActivityChatting.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                String res, sender_id, msg, action, type, receiver_id, typeuserid, typesendid, typestatus;
                try {
                    res = data.getString("status");
                    action = data.getString("action");
                    if (action.equals(Config.GetChatMsg)) {
                        if (res.matches("1")) {
                            JSONArray arr = data.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject object = arr.getJSONObject(i);
                                sender_id = object.getString("ah_sender_id");
                                msg = object.getString("msg");
                                type = object.getString("msg_type");
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.setUserId(sender_id);
                                chatMessage.setMessage(msg);
                                chatMessage.setMsgType(type);
                                //
                                String a[] = msg.split("\\.");
                                try {
                                    if (ConstantChatMedia.isImage(a[a.length - 1])) {
                                        chatMessage.setSendMsgType("image");
                                    } else if (ConstantChatMedia.isAudio(a[a.length - 1])) {
                                        chatMessage.setSendMsgType("audio");
                                    } else if (ConstantChatMedia.isVideo(a[a.length - 1])) {
                                        chatMessage.setSendMsgType("video");
                                    } else {
                                        chatMessage.setSendMsgType("primary");
                                    }
                                } catch (Exception e) {
                                    chatMessage.setMsgType("Msg");
                                    chatMessage.setMessage("Message Problem");
                                }

                                chatHistory.add(chatMessage);
                            }
                            chattingAdapter = new ChattingAdapter(getApplicationContext(), chatHistory);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            messagesContainer.setLayoutManager(layoutManager);
                            messagesContainer.setAdapter(chattingAdapter);
                            scroll();
                            chattingAdapter.setClickListener(ActivityChatting.this);

                        }
                    } else if (action.equals(Config.SendMsg)) {
                        if (res.matches("1")) {
                            chatHistory.clear();
                            JSONArray arr = data.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject object = arr.getJSONObject(i);
                                sender_id = object.getString("ah_sender_id");
                                receiver_id = object.getString("ah_receiver_id");
                                msg = object.getString("msg");
                                type = object.getString("msg_type");
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.setUserId(sender_id);
                                chatMessage.setReceiver_id(receiver_id);
                                chatMessage.setMessage(msg);
                                chatMessage.setMsgType(type);
                                chatMessage.setDate(object.getString("create_date"));
                                chatHistory.add(chatMessage);
                            }
                        } else if (res.matches("0")) {
                            JSONObject object1 = data.getJSONObject("data");
                            sender_id = object1.getString("ah_sender_id");
                            //receiver_id = object1.getString("ah_receiver_id");
                            msg = object1.getString("msg");
                            type = object1.getString("msg_type");
                            ChatMessage chatMessage = new ChatMessage();
                            chatMessage.setUserId(sender_id);
                            // chatMessage.setReceiver_id(receiver_id);
                            chatMessage.setMessage(msg);
                            chatMessage.setMsgType(type);
                            chatMessage.setDate(object1.getString("create_date"));

                            String a[] = msg.split("\\.");
                            try {
                                if (ConstantChatMedia.isImage(a[a.length - 1])) {
                                    chatMessage.setSendMsgType("image");
                                } else if (ConstantChatMedia.isAudio(a[a.length - 1])) {
                                    chatMessage.setSendMsgType("audio");
                                } else if (ConstantChatMedia.isVideo(a[a.length - 1])) {
                                    chatMessage.setSendMsgType("video");
                                } else {
                                    chatMessage.setSendMsgType("primary");
                                }
                            } catch (Exception e) {
                                chatMessage.setMsgType("Msg");
                                chatMessage.setMessage("Message Problem");
                            }
                            chatHistory.add(chatMessage);
                        }
                        chattingAdapter = new ChattingAdapter(getApplicationContext(), chatHistory);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        messagesContainer.setLayoutManager(layoutManager);
                        messagesContainer.setAdapter(chattingAdapter);
                        chattingAdapter.setClickListener(ActivityChatting.this);
                        scroll();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void popUpplaymusicreceiver(String url) {
        LayoutInflater inflater = ActivityChatting.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.layout_playmusic, null);
        imgplaymusicreceiver = (ImageView) c.findViewById(R.id.imgplaymusicreceiver);
        imgpausemusicreceiver = (ImageView) c.findViewById(R.id.imgpausemusicreceiver);
        timeplay = (TextView) c.findViewById(R.id.timeplayDuration);
        seekBar = (SeekBar) c.findViewById(R.id.seektune_reciever);
        seekBar.setOnTouchListener(this);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityChatting.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        PlayAudio(url);
        imgplaymusicreceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int ProgressValue, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(ProgressValue);
                }
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(0);
            }
        });
        updateSeekProgress();
    }

    private void playAudio() {
        timeplay.setVisibility(View.VISIBLE);
        if (mediaPlayer.isPlaying()) {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                imgplaymusicreceiver.setImageResource(R.drawable.ic_playmusic);
            }
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                imgplaymusicreceiver.setImageResource(R.drawable.ic_pausemusic);

            }
        }
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void PlayAudio(String url) {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }


    /**************************************************Select Image***********************************/


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityChatting.this);
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

    private void imageUpload(final String imagePath, final String type) {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, Config.SocketUrl + "/Upload/Chat",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String status, name;
                        Log.d("Response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            status = jObj.getString("status");
                            if (status.matches("1")) {
                                imagesetlocaly.setVisibility(View.GONE);
                                //displayMessage(path, "Media");

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        smr.addFile(shared.getUserId() + "," + recevierID, imagePath);
        smr.setFixedStreamingMode(true);
        AppController.getInstance().addToRequestQueue(smr);
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
                        path = GetPathFromURI.getPath(ActivityChatting.this, uri);
                        MediaType = "Media";
                        SendMsgWithMedia(path, MediaType);
                        //  Bitmap bitmap = getCompressedBitmap(path);
                        // img_registration.setImageBitmap(bitmap);
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
                    MediaType = "Media";
                    SendMsgWithMedia(path, MediaType);
                    Bitmap bitmap = getCompressedBitmap(path);
                    //  img_registration.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
