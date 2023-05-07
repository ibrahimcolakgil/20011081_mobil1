package com.example.login;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DatabaseReference databaseReference;
    private DatabaseReference logReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    private String txtFirstName, txtLastName, txtEmail, txtEducation, txtJobCity, txtJobCountry, txtJobCompany, txtSocials, txtPhoneNum;


    ImageView imageView;
    TextView firstName;
    TextView lastName;
    TextView email;

    TextView education;
    TextView job_country;
    TextView job_city;
    TextView job_company;

    TextView phoneNum;
    TextView socials;

    Button save_btn;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        firstName = (TextView) rootView.findViewById(R.id.first_name);
        lastName = (TextView) rootView.findViewById(R.id.last_name);
        email = (TextView) rootView.findViewById(R.id.email_txt);

        save_btn = rootView.findViewById(R.id.savebtn);


        education = (TextView)rootView.findViewById(R.id.education);
        job_country = (TextView)rootView.findViewById(R.id.country_et);
        job_city = (TextView)rootView.findViewById(R.id.city_et);
        job_company = (TextView)rootView.findViewById(R.id.company_et);
        socials = (TextView)rootView.findViewById(R.id.socials_et);
        phoneNum = (TextView)rootView.findViewById(R.id.phone_num_txt);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("user/").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                email.setText(user.getEmail());
                education.setText(user.getEducation());
                job_city.setText(user.getJobCity());
                job_country.setText(user.getJobCountry());
                job_company.setText(user.getJobCompany());
                phoneNum.setText(user.getPhoneNumber());
                socials.setText(user.getSocials());

                if(user.getProfilePicture().equals("default")){
                    imageView.setImageResource(R.drawable.baseline_add_a_photo_24);
                }else{
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Triggers when clicked 'Save' button on profile fragment.
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFirstName = firstName.getText().toString();
                txtLastName = lastName.getText().toString();
                txtEmail = email.getText().toString();
                txtEducation = education.getText().toString();
                txtJobCountry = job_country.getText().toString();
                txtJobCity = job_city.getText().toString();
                txtJobCompany = job_company.getText().toString();
                txtSocials = socials.getText().toString();
                txtPhoneNum = phoneNum.getText().toString();

                databaseReference.child("firstName").setValue(txtFirstName);
                databaseReference.child("lastName").setValue(txtLastName);
                databaseReference.child("email").setValue(txtEmail);
                databaseReference.child("education").setValue(txtEducation);
                databaseReference.child("jobCountry").setValue(txtJobCountry);

                databaseReference.child("jobCity").setValue(txtJobCity);
                databaseReference.child("jobCompany").setValue(txtJobCompany);
                databaseReference.child("socials").setValue(txtSocials);
                databaseReference.child("phoneNumber").setValue(txtPhoneNum);


                // We are checking if all the fields are filled or not. All fields are mandatory to fill.
                // Edit user's data in firebase.
            }
        });

        return rootView;
    }
}