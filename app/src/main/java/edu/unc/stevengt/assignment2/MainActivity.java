package edu.unc.stevengt.assignment2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor lightSensor;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mSensorManager = ( SensorManager ) getSystemService( Context.SENSOR_SERVICE );

        TextView accelerometerText = ( TextView ) findViewById( R.id.accelerometerText );
        TextView lightSensorText = ( TextView ) findViewById( R.id.lightSensorText );

        if ( mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER ) != null ) {
            accelerometer = mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
            Float range = accelerometer.getMaximumRange( );
            Float resolution = accelerometer.getResolution( );
            Integer delay = accelerometer.getMinDelay( );

            String rangeString = range.toString( );
            String resolutionString = resolution.toString( );
            String delayString = delay.toString( );

            accelerometerText.setText( "Status: Accelerometer is present.\n"
                    + "Range: " + rangeString+"\n"
                    + "Resolution: " + resolutionString + "\n"
                    + "Delay: " + delayString
            );

        } else {
            accelerometerText.setText( "Status: Accelerometer is not present." );
        }

        if ( mSensorManager.getDefaultSensor( Sensor.TYPE_LIGHT ) != null ) {
            lightSensor = mSensorManager.getDefaultSensor( Sensor.TYPE_LIGHT );
            Float range = lightSensor.getMaximumRange( );
            Float resolution = lightSensor.getResolution( );
            Integer delay = lightSensor.getMinDelay( );

            String rangeString = range.toString( );
            String resolutionString = resolution.toString( );
            String delayString = delay.toString( );

            lightSensorText.setText( "Status: Light sensor is present.\n"
                            + "Range: " + rangeString+"\n"
                            + "Resolution: " + resolutionString + "\n"
                            + "Delay: " + delayString
            );

        } else {
            lightSensorText.setText( "Status: Light Sensor is not present." );
        }



    }

    public void startAccelerometerPlotActivity( View view ) {
        if ( mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER ) != null ) {
            Intent intent = new Intent( this, AccelerometerPlot.class );
            startActivity( intent );
        } else {
            Toast errorMessage = new Toast( this );
            errorMessage.setText( "Accelerometer is not present." );
            errorMessage.show( );
        }
    }

    public void startLightActivity( View view ) {
        if ( mSensorManager.getDefaultSensor( Sensor.TYPE_LIGHT) != null ) {
            Intent intent = new Intent( this, LightActivity.class );
            startActivity( intent );
        } else {
            Toast errorMessage = new Toast( this );
            errorMessage.setText( "Light sensor is not present." );
            errorMessage.show( );
        }
    }

}
