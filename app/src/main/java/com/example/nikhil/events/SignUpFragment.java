package com.example.nikhil.events;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
    private FirebaseAuth mAuth;
    private Button mSignup;
    private TextInputLayout mEmail,mPassword,mUsername;
    private AlertDialog alertDialog;
    private TextView login_text;
    SignUpListener signUpListener;

    public SignUpFragment() {
        // Required empty public constructor
    }
 public interface SignUpListener{
        public void newUsersignUpListener(FirebaseUser user);
 }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth = FirebaseAuth.getInstance();

        mSignup=(Button)v.findViewById(R.id.buttonSignup);
        mEmail=(TextInputLayout) v.findViewById(R.id.emailSignUp);
        mPassword=(TextInputLayout) v.findViewById(R.id.passwordSignup);
        mUsername=(TextInputLayout) v.findViewById(R.id.usernameSignup);
        login_text=(TextView)v.findViewById(R.id.signUp_login_text);

       alertDialog=new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage("Registering...");
        alertDialog.setCancelable(false);
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getEditText().getText().toString().trim();
                String password=mPassword.getEditText().getText().toString().trim();
                String username=mUsername.getEditText().getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    mUsername.setError("Username cannot be empty");
                }else if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                }else if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                }else{
                      attemptSignUp(email,password,username);
                      mEmail.setError(null);
                    mPassword.setError(null);
                    mUsername.setError(null);

                    alertDialog.show();
                }
            }
        });
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_contaner, new LoginFragment())
                        .commit();

            }
        });

        return v;
    }
    private void attemptSignUp(String email, String password, final String userName){

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                   FirebaseUser user=mAuth.getCurrentUser();
                if (task.isSuccessful()) {

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(userName).build();
                    user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseUser us=FirebaseAuth.getInstance().getCurrentUser();

                            if(us!=null){
                                signUpListener.newUsersignUpListener(us);
                            }
                        }
                    });

                    alertDialog.dismiss();

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_contaner, new HomeFragment())
                            .commit();
                    Toast.makeText(getContext(), "Registration Successfull", Toast.LENGTH_SHORT).show();
                }else {
                    alertDialog.dismiss();

                    Toast.makeText(getContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });




    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity=(Activity)context;

        try{
            signUpListener=(SignUpListener) activity;

        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" must ovveride OnSignUpListener ");
        }
    }
}
