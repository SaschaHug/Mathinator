package de.dhbw.app.mathinator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class PictureActivity extends Activity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeapicture);
        videoView = (VideoView) findViewById(R.id.videoView);
        Button button = (Button) findViewById(R.id.Photo);
        button.setOnClickListener(new StartButtonListener());
    }

    public class StartButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v){
            videoView.start();
        }
    }

}
