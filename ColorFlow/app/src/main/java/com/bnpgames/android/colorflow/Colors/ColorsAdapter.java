package com.bnpgames.android.colorflow.Colors;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.bnpgames.android.colorflow.Fragments.ColorSetFragment;
import com.bnpgames.android.colorflow.Helpers.ColorSetListSerializer;
import com.bnpgames.android.colorflow.Helpers.InterfaceHelper;
import com.bnpgames.android.colorflow.Levels.LevelHandler;
import com.bnpgames.android.colorflow.R;

import java.util.ArrayList;

public class ColorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEWTYPE_COLORSET = 777;
    private static final int VIEWTYPE_HEADER = 1;

    private ArrayList<ColorSetData> list;
    private Context context;
    private InterfaceHelper.ColorSelectedListener listener;
    private ArrayList<ColorHandler.ColorSet> selectedSets;

    private final String[] headers = {
            "Default",
            null,
            "Seasons",
            null,
            null,
            null,
            null,
            "Countries",
            null,
            "Misc.",
            null
    };


    public ColorsAdapter(Context context, InterfaceHelper.ColorSelectedListener listener){
        list = ColorHandler.getColorList(context);
        this.context = context;
        this.listener = listener;
        selectedSets = ColorSetListSerializer.getSelectedSets(context);
    }


    @Override
    public int getItemViewType(int position) {
        if(headers[position]!=null)return VIEWTYPE_HEADER;
        else return VIEWTYPE_COLORSET;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==VIEWTYPE_HEADER)return new ColorHeader(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.colors_set_header,viewGroup,false));
        else return new ColorViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.color_set_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if(holder instanceof ColorViewHolder){
            ColorViewHolder colorViewHolder = (ColorViewHolder) holder;
            ColorSetData data = list.get(getRealPosition(holder.getAdapterPosition()));
            colorViewHolder.setBackGroundGradient(data.getColors());
            colorViewHolder.setColorSetTitle(data.getName());
            colorViewHolder.setPrice(data.getCost());
            colorViewHolder.setLocked(context, !data.isOwned());
            if(selectedSets.contains(data.getColorSet()))colorViewHolder.checkBox.setChecked(true);
            if(ColorHandler.ColorSet.valueOf(data.getName()).equals(ColorHandler.ColorSet.Default)){
                colorViewHolder.checkBox.setChecked(true);
                colorViewHolder.checkBox.setEnabled(false);
            }else {
                colorViewHolder.itemView.setOnClickListener(view -> {
                    colorViewHolder.toggleCheckBox();
                    if (colorViewHolder.checkBox.isChecked()) {
                        listener.onColorSetSelected(data.getColorSet());
                    } else {
                        listener.onColorUnselected(data.getColorSet());
                    }
                });
            }
        }else{
            ColorHeader header = (ColorHeader) holder;
            header.setTitle(headers[i]);
        }
    }

    @Override
    public int getItemCount() {
        return headers.length;
    }

    private int getRealPosition(int position){
        int counter = 0;
        for(int i = 0; i < position; i++){
            if(headers[i]==null)counter++;
        }
        return counter;
    }


}
