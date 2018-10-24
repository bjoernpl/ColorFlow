package com.bnpgames.android.colorflow.Colors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bnpgames.android.colorflow.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColorViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.color_set_checkbox)
    CheckBox checkBox;
    @BindView(R.id.color_set_name) TextView colorSetTitle;
    @BindView(R.id.color_set_price) TextView priceView;
    @BindView(R.id.colors_lock_image) ImageView lockView;
    @BindView(R.id.color_set_background) View backgroundView;



    ColorViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }


    public void setColorSetTitle(String colorSetTitle) {
        this.colorSetTitle.setText(colorSetTitle);
    }

    public void toggleCheckBox(){
        checkBox.toggle();
    }

    @SuppressLint("DefaultLocale")
    public void setPrice(int price) {
        this.priceView.setText(String.format("%d",price));
    }

    public void setLocked( Context context,boolean locked){
        lockView.setImageDrawable(context.getDrawable(locked ? R.drawable.ic_lock_outline_black_24dp : R.drawable.ic_lock_open_black_24dp));
    }

    public void setBackGroundGradient(int[] colors){
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
        backgroundView.setBackground(drawable);
    }
}
