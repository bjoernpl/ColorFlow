package com.bnpgames.android.colorflow.Colors;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bnpgames.android.colorflow.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColorHeader extends RecyclerView.ViewHolder {

    @BindView(R.id.headerTitle) TextView title;

    public ColorHeader(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
}
