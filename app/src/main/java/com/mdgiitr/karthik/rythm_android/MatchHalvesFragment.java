package com.mdgiitr.karthik.rythm_android;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import static com.mdgiitr.karthik.rythm_android.MainActivity.user;

public class MatchHalvesFragment extends Fragment {


    ImageView startRight, starLeft, starOption2, starOption3, cancel;
    TextView correct;
    ProgressDialog progressDialog;
    Button starNext;
    
    static int matchHalvesLevelCleared = 0;
    
    public MatchHalvesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_match_halves, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        matchHalvesLevelCleared = 0;
        startRight = view.findViewById(R.id.starRightImage);
        starLeft = view.findViewById(R.id.starLeftImage);
        starOption2 = view.findViewById(R.id.starOption2);
        starOption3 = view.findViewById(R.id.starOption3);
        correct = view.findViewById(R.id.starCorrect);
        starNext = view.findViewById(R.id.starNext);

        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = mDatabase.getReference();
                Long timeStamp = System.currentTimeMillis();
                String tsString = timeStamp.toString();
                progressDialog.show();
                databaseReference.child(user.getUid())
                        .child("match_halves")
                        .child(tsString)
                        .setValue(matchHalvesLevelCleared)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    if (progressDialog.isShowing())
                                    {
                                        progressDialog.dismiss();
                                    }
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Unable to upload score", Toast.LENGTH_SHORT).show();
                                }
                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_matchHalvesFragment_to_homeFragment2);
                            }
                        });
            }
        });

        starNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_matchHalvesFragment_to_matchHalvesFragment2);
            }
        });

        startRight.setOnTouchListener(new MyTouchListener());
        starLeft.setOnDragListener(new MyDragListener());

        return view;
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    ImageView imageView = (ImageView) v;
                    imageView.setImageResource(R.mipmap.star_complete);
                    correct.setVisibility(View.VISIBLE);
                    starNext.setVisibility(View.VISIBLE);
                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.tada);
                    matchHalvesLevelCleared++;
                    starLeft.setOnDragListener(null);
                    mp.start();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:

                default:
                    break;
            }
            return true;
        }
    }

}
