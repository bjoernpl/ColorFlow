package com.bnpgames.android.colorflow.Colors;

import android.widget.RadioGroup;

public class ColorSetData {

    private int[] colors;
    private ColorHandler.ColorSet name;
    private int cost;
    private boolean isOwned;


    public ColorSetData(int[] colors, ColorHandler.ColorSet name, int cost, boolean isOwned) {
        this.colors = colors;
        this.name = name;
        this.cost = cost;
        this.isOwned = isOwned;
    }


    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public String getName() {
        return name.name();
    }

    public void setName(ColorHandler.ColorSet name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isOwned() {
        return isOwned;
    }

    public void setOwned(boolean owned) {
        isOwned = owned;
    }
}
