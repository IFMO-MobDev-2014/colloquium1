package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity implements OnClickListener {
    Button butt;
    TextView tv;
    public static int GAME_MODE=0;
    public static int GAME_SCORE=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStart(){
        super.onStart();
        if (GAME_MODE==0){
        }
        else
        {
        }
    }
    public void onClick(View v) {
        Intent i = new Intent(this, GameActivity.class);
        GAME_MODE=0;
        GAME_SCORE=0;
        this.startActivity(i);
    }

}