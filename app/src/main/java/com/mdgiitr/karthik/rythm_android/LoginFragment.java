package com.mdgiitr.karthik.rythm_android;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.mdgiitr.karthik.rythm_android.MainActivity.mAuth;

public class LoginFragment extends Fragment {

    EditText emailEditText;
    EditText passWordEditText;
    Button loginButton;
    ProgressDialog progressDialog;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Logging in");
        progressDialog.setCanceledOnTouchOutside(false);
        emailEditText = view.findViewById(R.id.email_editText);
        passWordEditText = view.findViewById(R.id.password_editText);
        loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                final String password = passWordEditText.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()){
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                NavOptions navOptions = new NavOptions.Builder()
                                        .setPopUpTo(R.id.loginFragment2, true)
                                        .build();
                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_loginFragment2_to_homeFragment2, null,navOptions);
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                            }
                            else {
                                emailEditText.setText("");
                                passWordEditText.setText("");
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(getActivity(), "Unable to login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        return view;
    }

}
