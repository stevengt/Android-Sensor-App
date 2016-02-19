package edu.unc.stevengt.assignment2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class LightPlot extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor lightSensor;
    private List<Float> values = new ArrayList<Float>( );
    private SensorEventListener eventListener;
    private final Handler mHandler = new Handler( );

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        mSensorManager = ( SensorManager ) getSystemService( Context.SENSOR_SERVICE );
        lightSensor = mSensorManager.getDefaultSensor( Sensor.TYPE_LIGHT );
        final LineGraphView lightGraph = new LineGraphView( getApplicationContext( ),
                "Light Values", 50 , "Sensor Readings");


        setContentView( R.layout.activity_light_plot );

        LinearLayout layout = ( LinearLayout ) findViewById( R.id.linearLayout );
        layout.addView( lightGraph );

        eventListener = new SensorEventListener( ) {
            @Override
            public void onSensorChanged( SensorEvent event ) {

                values.add( event.values[0] );
            }

            @Override
            public void onAccuracyChanged( Sensor sensor, int accuracy ) {

            }
        };


        mSensorManager.registerListener( eventListener, lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL );




        new Thread( new Runnable( ) {
            @Override
            public void run( ) {

                while ( true ) {
                    try {
                        Thread.sleep( 1000 );
                        mHandler.post( new Runnable( ) {

                            @Override
                            public void run( ) {
                                if( values.size( ) > 0 ) {
                                    float dataPoint = values.get( values.size( ) - 1 );
                                    lightGraph.addDataPoint( dataPoint );
                                    values.clear( );
                                    lightGraph.invalidate( );
                                }
                            }
                        });
                    } catch ( Exception e ) {
                        // TODO: handle exception
                    }
                }
            }
        } ).start();
    }


    public void onClick( View view ){
        finish();
    }
}
