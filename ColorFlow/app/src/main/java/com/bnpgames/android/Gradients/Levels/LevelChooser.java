package com.bnpgames.android.Gradients.Levels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bnpgames.android.Gradients.Activities.Ingame.GameActivity;
import com.bnpgames.android.Gradients.R;

public class LevelChooser extends Activity {

    private RecyclerView rv;
    private LevelsAdapter adapter;
    private int currentLevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_chooser);
        rv = findViewById(R.id.recyclerView);
        adapter = new LevelsAdapter(this);
        rv.setLayoutManager(new GridLayoutManager(this,4));
        rv.setAdapter(adapter);
        adapter.setListener(this);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void itemClicked(int position){
        if(position<=LevelHandler.getInstance().getCurrentLevel()) {
            if(position==LevelHandler.getInstance().getCurrentLevel()){
                LevelHandler.getInstance().incrementLevel();
                adapter.notifyDataSetChanged();
            }
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("level", position);
            startActivity(intent);
        }
    }

}
