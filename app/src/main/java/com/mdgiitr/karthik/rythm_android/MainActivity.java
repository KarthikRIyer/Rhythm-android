package com.mdgiitr.karthik.rythm_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    static FirebaseAuth mAuth;
    static FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null){
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment2, true)
                    .build();
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_loginFragment2_to_homeFragment2, null,navOptions);
        }
    }
}
