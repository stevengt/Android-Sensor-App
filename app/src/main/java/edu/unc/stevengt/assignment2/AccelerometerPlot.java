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

public class AccelerometerPlot extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private List<Float> values = new ArrayList<Float>( );
    private SensorEventListener eventListener;
    private final float alpha = ( float ) 0.8;
    private float[] gravity;
    private final Handler mHandler = new Handler( );

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        mSensorManager = ( SensorManager ) getSystemService( Context.SENSOR_SERVICE );
        accelerometer = mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
        final LineGraphView accelerometerGraph = new LineGraphView( getApplicationContext( ),
                "Accelerometer Values", 5 );


        setContentView( R.layout.activity_accelerometer_plot );

        LinearLayout layout = ( LinearLayout ) findViewById( R.id.linearLayout );
        layout.addView( accelerometerGraph );

        gravity = new float[3];

        eventListener = new SensorEventListener( ) {
            @Override
            public void onSensorChanged( SensorEvent event ) {


                // Isolate the force of gravity with the low-pass filter.
                gravity[0] = alpha * gravity[0] + ( 1 - alpha ) * event.values[0];
                gravity[1] = alpha * gravity[1] + ( 1 - alpha ) * event.values[1];
                gravity[2] = alpha * gravity[2] + ( 1 - alpha ) * event.values[2];

                // Remove the gravity contribution with the high-pass filter.

                float x = event.values[0] - gravity[0];
                float y = event.values[1] - gravity[1];
                float z = event.values[2] - gravity[2];

                values.add( ( float ) Math.sqrt( ( x * x ) + ( y * y ) + ( z * z ) ) );
            }

            @Override
            public void onAccuracyChanged( Sensor sensor, int accuracy ) {

            }
        };


        mSensorManager.registerListener( eventListener, accelerometer,
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
                                    accelerometerGraph.addDataPoint( dataPoint );
                                    values.clear( );
                                    accelerometerGraph.invalidate( );
                                }
                            }
                        });
                    } catch ( Exception e ) {
                        // TODO: handle exception
                    }
                }
            }
        } ).start( );
    }


    public void onClick( View view ){
        finish();
    }

}
