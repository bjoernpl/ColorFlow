package com.bnpgames.android.colorflow.Helpers;

import com.bnpgames.android.colorflow.Colors.ColorHandler;

public class InterfaceHelper {

    public interface ColorSelectedListener{
        void onColorSetSelected(ColorHandler.ColorSet colorSet);
        void onColorUnselected(ColorHandler.ColorSet colorSet);
    }
}
