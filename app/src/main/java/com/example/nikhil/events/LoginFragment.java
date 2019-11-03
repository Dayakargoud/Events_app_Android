package com.example.nikhil.events;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private Button login_button;
    private TextInputLayout email,password;
    private TextView singUp;
    private FirebaseAuth mAuth;
    private String passswordInput,emailInput;
    OnLoginListener loginListener;



    public LoginFragment() {
        // Required empty public constructor
    }
    public interface OnLoginListener{
        public void onLogin(FirebaseUser user);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v=  inflater.inflate(R.layout.fragment_login, container, false);
       mAuth=FirebaseAuth.getInstance();
       setUpUIviews(v);

       login_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(!is_email_valid() | !is_password_valid()){
                   return;

               }else {
                   attemp_login(emailInput,passswordInput);
               }
           }
       });
       singUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getActivity().getSupportFragmentManager()
                       .beginTransaction()
                       .replace(R.id.fragment_contaner, new SignUpFragment())
                       .commit();

           }
       });
       return v;
    }

    private void setUpUIviews(View v){
        login_button=(Button)v.findViewById(R.id.login_button);
        email=(TextInputLayout)v.findViewById(R.id.login_email);
        password=(TextInputLayout)v.findViewById(R.id.login_password);
        singUp=(TextView) v.findViewById(R.id.login_signUp_text);

    }
    private boolean is_email_valid(){


        emailInput=email.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            email.setError("Email can't be empty");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }


    }
    private boolean is_password_valid(){
        passswordInput=password.getEditText().getText().toString().trim();

        if(passswordInput.isEmpty()){
            password.setError("Password can't be empty");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }
    private void attemp_login(String email,String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(getContext(), "Login Success",
                                    Toast.LENGTH_SHORT).show();


                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){
                                loginListener.onLogin(user);
                            }
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_contaner, new HomeFragment())
                                    .commit();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity=(Activity)context;
        try{
            loginListener=(OnLoginListener) activity;



        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" must ovveride OnLoginListener ");
        }
    }
}
