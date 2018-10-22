package com.bnpgames.android.colorflow.GameModes;

import android.os.Parcel;
import android.os.Parcelable;

import com.bnpgames.android.colorflow.Levels.Level;

public class FlowStatus implements Parcelable {
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private Level level;

    public FlowStatus(int x1, int x2, int y1, int y2, Level level) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.level = level;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    public Level getLevel(){
        return level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x1);
        dest.writeInt(x2);
        dest.writeInt(y1);
        dest.writeInt(y2);
        dest.writeParcelable(level,0);
    }

    private void readFromParcel(Parcel in) {
        x1 = in.readInt();
        x2 = in.readInt();
        y1 = in.readInt();
        y2 = in.readInt();
        level = in.readParcelable(Level.class.getClassLoader());
    }

    public FlowStatus(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator<FlowStatus> CREATOR = new Parcelable.Creator<FlowStatus>() {
        @Override
        public FlowStatus createFromParcel(Parcel source) {
            return new FlowStatus(source);
        }

        @Override
        public FlowStatus[] newArray(int size) {
            return new FlowStatus[size];
        }
    };

}
