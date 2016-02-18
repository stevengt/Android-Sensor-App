package edu.unc.stevengt.assignment2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;



public class LightActivity extends AppCompatActivity {
    private float xCurrentPos, yCurrentPos,currentAlpha;
    private SensorManager mSensorManager;
    private Sensor lightSensor;
    private SensorEventListener eventListener;
    private ImageView sun;
    float previousVal = 0;
    private float difference;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        mSensorManager = ( SensorManager ) getSystemService( Context.SENSOR_SERVICE );
        lightSensor = mSensorManager.getDefaultSensor( Sensor.TYPE_LIGHT );


        setContentView( R.layout.activity_light );

        sun = ( ImageView ) findViewById( R.id.sun );
        xCurrentPos = sun.getLeft( );
        yCurrentPos = sun.getTop( );
        currentAlpha = sun.getAlpha( );

        eventListener = new SensorEventListener( ) {
            @Override
            public void onSensorChanged( SensorEvent event ) {

                float val = event.values[0];
                difference = val - previousVal;

                Animation anim = new TranslateAnimation(xCurrentPos, xCurrentPos, yCurrentPos,
                        yCurrentPos + ( difference * ( float ) 0.5 ) );
                anim.setDuration( 10 );
                anim.setFillAfter( true );
                anim.setFillEnabled( true );

                Animation.AnimationListener listener = new Animation.AnimationListener( ) {

                    @Override
                    public void onAnimationStart( Animation arg0 ) {
                    }

                    @Override
                    public void onAnimationRepeat( Animation arg0 ) {
                    }

                    @Override
                    public void onAnimationEnd( Animation arg0 ) {
                        yCurrentPos += ( difference * ( float ) 0.5 );
                    }
                };

                anim.setAnimationListener( listener );

                sun.startAnimation( anim );

                previousVal = val;

            }

            @Override
            public void onAccuracyChanged( Sensor sensor, int accuracy ) {

            }
        };


        mSensorManager.registerListener( eventListener, lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL );

    }


    public void onClick( View view ){
        finish();
    }

}
