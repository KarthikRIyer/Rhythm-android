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

public class MatchWordsFragment extends Fragment {

    ImageView bedImage, ballImage, tenImage, dogImage, cancel;
    TextView bedText, ballText, tenText, dogText;
    int matchWordScore = 0;
    Button exitButton;

    public MatchWordsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_match_words, container, false);

        matchWordScore = 0;

        bedImage = view.findViewById(R.id.bedImage);
        ballImage = view.findViewById(R.id.ballImage);
        tenImage = view.findViewById(R.id.tenImage);
        dogImage = view.findViewById(R.id.dogImage);

        bedImage.setOnTouchListener(new MyTouchListener());
        ballImage.setOnTouchListener(new MyTouchListener());
        tenImage.setOnTouchListener(new MyTouchListener());
        dogImage.setOnTouchListener(new MyTouchListener());

        bedText = view.findViewById(R.id.bedString);
        ballText = view.findViewById(R.id.ballString);
        tenText = view.findViewById(R.id.tenString);
        dogText = view.findViewById(R.id.dogString);

        exitButton = view.findViewById(R.id.exitButton);

        cancel = view.findViewById(R.id.cancel);

        bedText.setOnDragListener(new MyDragListener());
        ballText.setOnDragListener(new MyDragListener());
        tenText.setOnDragListener(new MyDragListener());
        dogText.setOnDragListener(new MyDragListener());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (matchWordScore!=4){
                    uploadScore();
                }
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_matchWordsFragment2_to_homeFragment2);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadScore();
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_matchWordsFragment2_to_homeFragment2);
            }
        });

        return view;
    }

    public void uploadScore(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabase.getReference();
        Long timeStamp = System.currentTimeMillis();
        String tsString = timeStamp.toString();
        progressDialog.show();
        databaseReference.child(user.getUid())
                .child("match_words")
                .child(tsString)
                .setValue(matchWordScore)
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
                    }
                });
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
                    TextView textView = (TextView) v;
                    ImageView imageView = (ImageView) event.getLocalState();

                    if (((imageView == bedImage) && (textView == bedText)) ||  (imageView == dogImage) && (textView == dogText) ||
                            (imageView == ballImage) && (textView == ballText)|| (imageView == tenImage) && (textView == tenText)) {
                        matchWordScore++;
                        imageView.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                        imageView.setOnTouchListener(null);
                        textView.setOnDragListener(null);
                        if (matchWordScore == 4){
                            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.tada);
                            mp.start();
                            uploadScore();
                            exitButton.setVisibility(View.VISIBLE);
                        }
                    }

                    break;
                case DragEvent.ACTION_DRAG_ENDED:

                default:
                    break;
            }
            return true;
        }

    }
}
