package ulti72.pnplayer;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.HashSet;


public class FullscreenActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {


    public static final int SWIPE_THRESHOLD = 100;
    public static final int VELOCITY_THRESHOLD = 100;
    VideoView vid;
    String[] path= {"android.resource://ulti72.pnplayer/"+R.raw.a,
            "android.resource://ulti72.pnplayer/"+R.raw.b,
            "android.resource://ulti72.pnplayer/"+R.raw.c,
            "android.resource://ulti72.pnplayer/"+R.raw.d,
            "android.resource://ulti72.pnplayer/"+R.raw.e,};

    static int count=0;
    private GestureDetector gestureDetector;
   Toast end , start ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        //playing Video
        vid = findViewById(R.id.videoView);

         end = Toast.makeText(getApplicationContext(),
                "No new videos..",
                Toast.LENGTH_SHORT);
        start = Toast.makeText(getApplicationContext(),
                "Swipe down for more",
                Toast.LENGTH_SHORT);

        playVideo(vid,path[0]);

        gestureDetector= new GestureDetector(this)  ;





    }

//playing video
    public void playVideo(View v,String path) {


        Uri u = Uri.parse(path);
        vid.setVideoURI(u);
        vid.start();
        vid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                         MediaController m ;   m = new MediaController(FullscreenActivity.this);
                        vid.setMediaController(m);
                        m.setAnchorView(vid);
                    }
                });
            }
        });

    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        boolean result = false ;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();
        if(Math.abs(diffX)>Math.abs(diffY)){
            //right or left swipe
            if(Math.abs(diffX)> SWIPE_THRESHOLD && Math.abs(velocityX)>VELOCITY_THRESHOLD){
                    if(diffX>0){
                        onSwipeRight();
                    }
                    else
                        onSwipeLeft();
            } result= true ;

        }else{
            //up or down swipe
            if(Math.abs(diffY)>SWIPE_THRESHOLD&& Math.abs(velocityY)>VELOCITY_THRESHOLD){
                if(diffY>0){
                    onSwipeBottom();
                }
                else{
                    onSwipeTop();
                }result=true ;
            }

        }

        return result;
    }
    HashSet<Integer> hs = new HashSet<>();
    private void subscribe(String s){
        View v = getWindow().getCurrentFocus() ;
        Snackbar mySnackbar = Snackbar.make(v, s, Snackbar.LENGTH_SHORT);
        mySnackbar.show();

    }

    private void onSwipeTop() {
        count++;
        if(count==5){end.show();count=4 ;}
        playVideo(vid,path[count]);

    }

    private void onSwipeBottom() {
        count--;
        if(count==-1){start.show(); count=0;}
        playVideo(vid,path[count]);

    }

    private void onSwipeLeft() {
        int temp=count+1;
        String s ;
        if(!hs.contains(count))
        { s="User "+temp+" successfully subscribed";
          hs.add(count);}
        else
             s = "User "+temp+" Already Subscribed";
         subscribe(s);

    }

    private void onSwipeRight() {
            profile();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void profile(){
        Intent intent = new Intent(FullscreenActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);

    }



@Override
    public  void onResume(){
    super.onResume();
    playVideo(vid,path[count]);

}

}
