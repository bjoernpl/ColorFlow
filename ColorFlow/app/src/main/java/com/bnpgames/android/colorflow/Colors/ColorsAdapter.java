package com.bnpgames.android.colorflow.Colors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnpgames.android.colorflow.Levels.LevelHandler;
import com.bnpgames.android.colorflow.R;

import java.util.ArrayList;

public class ColorsAdapter extends RecyclerView.Adapter<ColorViewHolder> {

    private ArrayList<ColorSetData> list;
    private Context context;

    public ColorsAdapter(Context context){
        list = ColorHandler.getColorList(context);
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ColorViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.color_set_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder colorViewHolder, int i) {
        ColorSetData data = list.get(colorViewHolder.getAdapterPosition());
        colorViewHolder.setBackGroundGradient(data.getColors());
        colorViewHolder.setColorSetTitle(data.getName());
        colorViewHolder.setPrice(data.getCost());
        colorViewHolder.setLocked(context,!data.isOwned());
        colorViewHolder.itemView.setOnClickListener(view -> {
            ColorHandler.setSelectedColorSet(context, ColorHandler.ColorSet.valueOf(data.getName()));
            LevelHandler.getInstance().setColors(context);
            LevelHandler.getInstance().reset();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
