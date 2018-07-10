package com.example.android.colorflow;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;

/**
 * Created by bjoer on 6/24/2018.
 */

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelHolder>{
    private Context context;
    private LevelChooser observer;

    public LevelsAdapter(Context context){
        this.context = context;
    }

    public void setListener(LevelChooser chooser){
        this.observer = chooser;
    }

    @NonNull
    @Override
    public LevelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_chooser_item, parent, false);
        LevelHolder viewHolder = new LevelHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LevelHolder holder, int position) {
        holder.title.setText(position+1+"");
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(observer!=null)observer.itemClicked(holder.getAdapterPosition());
            }
        });
        if(position > LevelHandler.getInstance().getCurrentLevel()){
            holder.title.setTextColor(Color.LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return LevelHandler.getInstance().getLevelsAmount();
    }

    public class LevelHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public LevelHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.levelIndex);
        }
    }

}
