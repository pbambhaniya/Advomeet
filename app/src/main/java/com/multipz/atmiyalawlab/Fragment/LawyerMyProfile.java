package com.multipz.atmiyalawlab.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LawyerMyProfile extends Fragment {

    private RelativeLayout rel_root;
    private CircleImageView img_registration;
    private EditText edt_name, edt_email, edt_address_line_1, edt_address_line_2, edt_pincode;
    private Spinner sp_state, sp_city;
    private Button btnProfileUpdate;


    public LawyerMyProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lawyer_my_profile, container, false);

        reference(view);
        init();
        return view;
    }

    private void reference(View view) {
        rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
        img_registration = (CircleImageView) view.findViewById(R.id.img_registration);
        edt_name = (EditText) view.findViewById(R.id.edt_name);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_address_line_1 = (EditText) view.findViewById(R.id.edt_address_line_1);
        edt_address_line_2 = (EditText) view.findViewById(R.id.edt_address_line_2);
        edt_pincode = (EditText) view.findViewById(R.id.edt_pincode);

        sp_state = (Spinner) view.findViewById(R.id.sp_state);
        sp_city = (Spinner) view.findViewById(R.id.sp_city);
        btnProfileUpdate = (Button) view.findViewById(R.id.btnProfileUpdate);
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));

    }

    private void init() {

    }

}
