package pl.marcinos.catchtheball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class result extends AppCompatActivity {
private SoundPlayer sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        sound = new SoundPlayer(this);
        sound.playOverSound();
        TextView scoreLabel =(TextView)findViewById(R.id.scoreLabel);
        TextView highScoreLabel =(TextView)findViewById(R.id.highScoreLabel);


        int score = getIntent().getIntExtra("SCORE",0);
        scoreLabel.setText(score+"");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore=settings.getInt("HIGH_SCORE",0);

        if(score>highScore)
        {
            highScoreLabel.setText("High Score: "+score);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE",score);

            editor.commit();
        }
        else
        {
            highScoreLabel.setText("High Score : "+highScore);
        }
    }

    public void TryAgain(View view)
    {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
