package com.bnpgames.android.colorflow.Helpers;

import com.bnpgames.android.colorflow.Levels.GameFinished;

public class GameFinishedHelper {

    private GameFinished gameFinished;
    private static GameFinishedHelper instance;

    public GameFinishedHelper(GameFinished gameFinished) {
        this.gameFinished = gameFinished;
        instance = this;
    }

    public static GameFinishedHelper getInstance(){
        return instance;
    }

    public GameFinished getGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(GameFinished gameFinished) {
        this.gameFinished = gameFinished;
    }
}
