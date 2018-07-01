package pl.marcinos.catchtheball;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView black;
    private ImageView pink;
    private ImageView orange;


    private int frameHeight;
    private int boxSize;
    private int ScreenWidth;
    private int ScreenHeight;


    private SoundPlayer sound;

    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;

    private int score=0;


    private Handler handler = new Handler();
    private Timer timer = new Timer();


    private boolean action_flag = false;
    private boolean start_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sound = new SoundPlayer(this);


        scoreLabel=(TextView)findViewById(R.id.scoreLabel);
        startLabel=(TextView)findViewById(R.id.startLabel);
        box = (ImageView)findViewById(R.id.box);
        black = (ImageView)findViewById(R.id.black);
        orange = (ImageView)findViewById(R.id.orange);
        pink = (ImageView)findViewById(R.id.pink);


        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        ScreenWidth= size.x;
        ScreenHeight= size.y;


        orange.setX(-80);
        orange.setY(-80);
        pink.setX(-80);
        pink.setY(-80);
        black.setX(-80);
        black.setY(-80);

        scoreLabel.setText("Score: "+score);


    }

    public void changePos()
    {

        hitCheck();
        orangeX-=12;
        if(orangeX<0)
        {
            orangeX=ScreenWidth+20;
            orangeY=(int)Math.floor(Math.random() *(frameHeight-orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        blackX-=16;
        if(blackX<0)
        {
            blackX=ScreenWidth+20;
            blackY=(int)Math.floor(Math.random() *(frameHeight-black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        if(score>200)
        {
            blackX-=16;
            if(blackX<0)
            {
                blackX=ScreenWidth+20;
                blackY=(int)Math.floor(Math.random() *(frameHeight-black.getHeight()));
            }
            black.setX(blackX);
            black.setY(blackY);
        }
        pinkX-=20;
        if(pinkX<0)
        {
            pinkX=ScreenWidth+20;
            pinkY=(int)Math.floor(Math.random() *(frameHeight-pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        if(action_flag==true)
        {
            boxY-=20;
        }
        else
        {
            boxY+=20;
        }


        if(boxY<0)
            boxY=0;
        if(boxY>frameHeight-boxSize)
            boxY=frameHeight-boxSize;



        box.setY(boxY);

        scoreLabel.setText("Score: "+score);
    }

    private void hitCheck()
    {
        int orangeCenterX = orangeX+orange.getWidth()/2;
        int orangeCenterY = orangeY+orange.getHeight()/2;

        if(0<=orangeCenterX&&orangeCenterX<=boxSize&&boxY<=orangeCenterY&&orangeCenterY<=boxY+boxSize)
        {
            sound.playHitSound();
            score+=10;
            orangeX= - 10;
        }
        int pinkCenterX = pinkX+pink.getWidth()/2;
        int pinkCenterY = pinkY+pink.getHeight()/2;

        if(0<=pinkCenterX&&pinkCenterX<=boxSize&&boxY<=pinkCenterY&&pinkCenterY<=boxY+boxSize)
        {
            sound.playHitSound();
            score+=30;
            pinkX= -10;
        }


        int blackCenterX = blackX+black.getWidth()/2;
        int blackCenterY = blackY+black.getHeight()/2;

        if(0<=blackCenterX&&blackCenterX<=boxSize&&boxY<=blackCenterY&&blackCenterY<=boxY+boxSize)
        {
            sound.playOverSound();
            timer.cancel();
            timer=null;
            Intent intent = new Intent(getApplicationContext(),result.class);
            intent.putExtra("SCORE",score);
            startActivity(intent);
        }

    }

    public boolean onTouchEvent(MotionEvent me)
    {
        if(start_flag==false)
        {
            start_flag=true;


            startLabel.setVisibility(View.GONE);


            FrameLayout frame =(FrameLayout)findViewById(R.id.frame);
            frameHeight=frame.getHeight();

            boxY= (int)box.getY();

            boxSize=box.getHeight();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            },0,20);

        }
        else
        {
            if(me.getAction()==MotionEvent.ACTION_DOWN)
            {
                action_flag=true;
            }
            else if(me.getAction()==MotionEvent.ACTION_UP)
            {
                action_flag=false;
            }
        }


        return true;
    }
}
