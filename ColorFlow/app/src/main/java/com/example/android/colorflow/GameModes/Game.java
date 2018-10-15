package com.example.android.colorflow.GameModes;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.colorflow.Levels.Level;

public class Game implements Parcelable {

    public enum FlowMode {
        Radial,
        Linear,
        Random
    }

    public enum GameMode {
        Casual,
        Speed,
        Continuous,
        Endurance
    }

    public enum Difficulty {
        Easy,
        Medium,
        Hard,
        Legendary
    }

    private FlowMode flowMode;
    private GameMode gameMode;
    private Difficulty difficulty;
    private int time = 0;
    //private Level level;
    private int index = 0;

    public Game(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /*public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }*/

    public FlowMode getFlowMode() {
        return flowMode;
    }

    public void setFlowMode(FlowMode flowMode) {
        this.flowMode = flowMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(difficulty!=null) dest.writeString(difficulty.name());
        else dest.writeString(null);
        if(gameMode!=null) dest.writeString(gameMode.name());
        else dest.writeString(null);
        if(flowMode!=null) dest.writeString(flowMode.name());
        else dest.writeString(null);
        dest.writeInt(time);
        //dest.writeParcelable(level,0);
        dest.writeInt(index);
    }

    private void readFromParcel(Parcel in) {

        String difficultyString = in.readString();
        difficulty = difficultyString != null? Difficulty.valueOf(difficultyString): null;

        String gameModeString = in.readString();
        gameMode = gameModeString != null ? GameMode.valueOf(gameModeString) : null;

        String flowModeString = in.readString();
        flowMode = flowModeString != null ? FlowMode.valueOf(flowModeString) : null;
        time = in.readInt();
        //level = in.readParcelable(Level.class.getClassLoader());
        index = in.readInt();
    }

    public Game(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
