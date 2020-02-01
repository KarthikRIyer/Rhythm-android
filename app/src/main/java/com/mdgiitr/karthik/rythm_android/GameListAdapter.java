package com.mdgiitr.karthik.rythm_android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.MyViewHolder> {


    List<Game> gameList;
    Activity activity;

    public GameListAdapter(List<Game> gameList, Activity activity) {
        this.gameList = gameList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Game game = gameList.get(position);
        holder.gameNameView.setText(game.getGameName());
        holder.gameNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
                switch (position) {
                    case 0:
                        navController.navigate(R.id.action_homeFragment2_to_matchHalvesFragment);
                        break;
                    case 1:
                        navController.navigate(R.id.action_homeFragment2_to_matchWordsFragment2);
                        break;
                    case 2:
                        navController.navigate(R.id.action_homeFragment2_to_findMeFragment2);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView gameNameView;

        public MyViewHolder(View view) {
            super(view);
            gameNameView = view.findViewById(R.id.gameName);
        }

    }

}
