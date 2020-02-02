package com.mdgiitr.karthik.rythm_android;


import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.mdgiitr.karthik.rythm_android.FindMeFragment.findLevelCleared;
import static com.mdgiitr.karthik.rythm_android.MainActivity.user;


public class FindMeFragment2 extends Fragment {

    Button next;
    ImageView boy, cancel, football;

    public FindMeFragment2() {
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
        View view = inflater.inflate(R.layout.fragment_find_me_fragment2, container, false);
        boy = view.findViewById(R.id.boy);
        next = view.findViewById(R.id.next);
        cancel = view.findViewById(R.id.cancel);
        football = view.findViewById(R.id.football);
        MediaPlayer find = MediaPlayer.create(getContext(), R.raw.find);
        find.start();
        final MediaPlayer footballSound = MediaPlayer.create(getContext(), R.raw.football);
        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.tada);

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mp.start();
                findLevelCleared++;
                boy.setOnClickListener(null);
                next.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadScore();
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_findMeFragment22_to_homeFragment2);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_findMeFragment22_to_findMeFragment3);
            }
        });

        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                footballSound.start();
            }
        });

        return view;
    }

    public void uploadScore() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabase.getReference();
        Long timeStamp = System.currentTimeMillis();
        String tsString = timeStamp.toString();
        progressDialog.show();
        databaseReference.child(user.getUid())
                .child("find_me")
                .child(tsString)
                .setValue(findLevelCleared)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Unable to upload score", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
