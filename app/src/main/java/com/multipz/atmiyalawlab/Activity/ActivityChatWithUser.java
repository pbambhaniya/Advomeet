package com.multipz.atmiyalawlab.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Adapter.ChatListAdapter;
import com.multipz.atmiyalawlab.Model.ChatListModelClass;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.SocketManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class ActivityChatWithUser extends AppCompatActivity implements ItemClickListener, Emitter.Listener {
    RecyclerView chatListView;
    private ArrayList<ChatListModelClass> chatlist;
    ChatListAdapter adapter;
    Context context;
    ImageView img_back, img_setting;
    RelativeLayout img_notification, rel_root;
    LinearLayoutManager layoutManager;
    private SocketManager socketManager;
    private Shared shared;
    private TextView txt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_user);
        context = this;
        shared = new Shared(context);
        chatlist = new ArrayList<>();
        socketManager = SocketManager.getInstance(getApplicationContext());
        reference();
        init();
    }

    private void reference() {
        chatListView = (RecyclerView) findViewById(R.id.recyclerview);
        img_back = (ImageView) findViewById(R.id.img_back);
        // img_notification = (RelativeLayout) findViewById(R.id.img_notification);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        txt_name = (TextView) findViewById(R.id.txt_name);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        if (shared.getUsertype().contentEquals("L")) {
            txt_name.setText("Chat With User");
        } else if (shared.getUsertype().contentEquals("U")) {
            txt_name.setText("Chat With Lawyer");
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        socketManager.connect();
        SocketConnection();
        getChatUserList();
    }

    private void SocketConnection() {
        final JSONObject objdata = new JSONObject();
        try {
            objdata.put("ah_users_id", shared.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("action", Config.Connection);
            obj.put("data", objdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketManager.sendEmit("request", obj);
    }


    private void getChatUserList() {
        // send userdata
        final JSONObject objdata = new JSONObject();
        try {
            objdata.put("ah_users_id", shared.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject obj = new JSONObject();
        try {
            obj.put("action", Config.GetChatUserList);
            obj.put("data", objdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketManager.sendEmit("request", obj);
        socketManager.addListener("response", this);
    }

    @Override
    public void itemClicked(View View, int position) {
        try {
            ChatListModelClass m = chatlist.get(position);
            int recevierID = m.getAh_users_id();
            String rec_id = String.valueOf(recevierID);
            String name = m.getFull_name();
            Intent i = new Intent(ActivityChatWithUser.this, ActivityChatting.class);
            i.putExtra("name", name);
            i.putExtra("recevierID", rec_id);
            i.putExtra("image", m.getProfile_img());
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void call(final Object... args) {
        ActivityChatWithUser.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                String res, user_id, name, img, phone_no, msg_status, login_status, create_date, update_date, action, typeuserid, typesendid, typestatus;
                try {
                    action = data.getString("action");
                    res = data.getString("status");
                    if (action.equals(Config.GetChatUserList)) {
                        if (res.matches("1")) {
                            JSONArray array = data.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                ChatListModelClass m = new ChatListModelClass();
                                m.setAh_users_id(object.getInt("ah_users_id"));
                                m.setFull_name(object.getString("full_name"));
                                m.setMobile_number(object.getString("mobile_number"));
                                m.setEmail(object.getString("email"));
                                m.setProfile_img(object.getString("profile_img"));
                                m.setLast_msg(object.getString("last_msg"));
                                m.setMsg_type(object.getString("msg_type"));
                                m.setCreate_date(object.getString("create_date"));

                                chatlist.add(m);
                            }
                            adapter = new ChatListAdapter(getApplicationContext(), chatlist);
                            layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            chatListView.setLayoutManager(layoutManager);
                            chatListView.setAdapter(adapter);
                            adapter.setClickListener(ActivityChatWithUser.this);
                        }
                    } /*else if (action.matches("Typing")) {
                        JSONObject jsonObject = data.getJSONObject("data");
                        typeuserid = jsonObject.getString("typeuserid");
                        typesendid = jsonObject.getString("typesendid");
                        typestatus = jsonObject.getString("typestatus");
                        ChatListModelClass m = new ChatListModelClass();
                        m.setTypeuserid(typeuserid);
                        m.setTypesendid(typesendid);
                        m.setTypestatus(typestatus);

                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
