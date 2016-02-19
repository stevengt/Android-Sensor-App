package edu.unc.stevengt.assignment2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/*
 * Graph for displaying data along with its mean and standard deviation.
 */

public class LineGraphView extends View {

    private static int NUM_DATA_POINTS = 5;
    private Paint paint;
    private List<Float> values;
    private List<Float> means;
    private List<Float> stddevs;
    private List<String> horlabels;
    private List<String> verlabels;
    private String title;
    private Integer numSeconds = 0;
    private String units;
    private float defaultMax,
                min,
                max,
                border,
                horstart,
                height,
                width,
                diff,
                graphHeight,
                graphWidth;


    public LineGraphView( Context context, String title, float defaultMax, String units ) {
        super( context );
        setWillNotDraw( false );

        values = new ArrayList<Float>( );
        means = new ArrayList<Float>( );
        stddevs = new ArrayList<Float>( );

        if ( title == null ) {
            title = "";
        } else {
            this.title = title;
        }

        this.horlabels = new ArrayList<String>( );
        this.verlabels = new ArrayList<String>( );

        paint = new Paint( );

        this.defaultMax = defaultMax;
        this.units = units;
    }

    @Override
    protected void onDraw( Canvas canvas ) {

        border = 40;
        horstart = border * 2;
        height = getHeight( ) - 2 * horstart;
        width = getWidth( ) - 1;
        max = getMax( ) > defaultMax ? getMax() :defaultMax;
        min = getMin( );
        diff = max - min;
        graphHeight = height - ( 4 * border );
        graphWidth = width - ( 2 * border );

        drawHorLabels( canvas );
        drawVerLabels( canvas );
        drawTitle( canvas );

        plotValues( canvas, values, units, Color.RED, 1 );
        plotValues( canvas, means, "mean", Color.CYAN, 2 );
        plotValues( canvas, stddevs, "stddev", Color.GREEN, 3 );


    }

    private float getMax( ) {
        float largest = Integer.MIN_VALUE;

        for ( int i = 0; i < values.size( ); i++ ) {
            if ( values.get( i ) > largest ) {
                largest = values.get( i );
            }
        }

        for ( int i = 0; i < means.size( ); i++ ) {
            if ( means.get( i ) > largest ) {
                largest = means.get( i );
            }
        }

        for ( int i = 0; i < stddevs.size( ); i++ ) {
            if ( stddevs.get( i ) > largest ) {
                largest = stddevs.get( i );
            }
        }
        return largest;
    }

    private float getMin( ) {
        float smallest = Integer.MAX_VALUE;

        for ( int i = 0; i < values.size( ); i++ ) {
            if ( values.get( i ) < smallest ) {
                smallest = values.get( i );
            }
        }

        for ( int i = 0; i < means.size( ); i++ ) {
            if ( means.get( i ) < smallest ) {
                smallest = means.get( i );
            }
        }

        for ( int i = 0; i < stddevs.size( ); i++ ) {
            if ( stddevs.get( i ) < smallest ) {
                smallest = stddevs.get( i );
            }
        }
        return smallest;
    }


    private float getStdDev( ) {
        float mean = getMean( );
        float temp = 0;
        for( float val : values ) {
            temp += ( mean - val ) * ( mean - val );
        }
        return ( float ) Math.sqrt( temp / values.size( ) );
    }

    private void addStdDev( ) {
        Float stddev = getStdDev( );
        stddevs.add( stddev );
        if( stddevs.size( ) > NUM_DATA_POINTS){
            stddevs.remove( 0 );
        }
    }

    private Float getMean( ){

        float total = 0;

        for( int i = 0; i < values.size( ); i++ ){
            total += values.get( i );
        }

        float mean = total / values.size( );

        return mean;

    }

    public void addMean( ) {
        Float mean = getMean( );
        means.add( mean );
        if( means.size( ) > NUM_DATA_POINTS ){
            means.remove( 0 );
        }
    }

    public void addDataPoint( float data ) {

        values.add( data );
        horlabels.add( numSeconds.toString( ) );

        if(values.size() > NUM_DATA_POINTS){
            values.remove( 0 );
            horlabels.remove( 0 );
        }

        addMean( );
        addStdDev( );

        numSeconds++;
    }

    public List<String> getVerLabels( float min, float max ) {

        int numPoints = 10;
        int i = 1;

        Float minVal = new Float( min );
        Float maxVal = new Float( max );

        float increment = ( max - min ) / numPoints;

        ArrayList<String> retVal = new ArrayList<String>( );

        retVal.add( String.format( "%.3f", maxVal ) );

        float cur = max;

        while( i < numPoints ) {
            cur -= increment;
            retVal.add( String.format( "%.3f", cur ) );
            i++;
        }

        retVal.add( String.format( "%.3f", minVal ) );

        return retVal;

    }

    private void plotValues( Canvas canvas, List<Float> data, String label, int color, int position ) {
        paint.setStrokeWidth( 6 );
        if ( max != min ) {
            paint.setColor( color );
            float datalength = data.size( );
            float colwidth = ( width - ( 2 * border ) ) / datalength;
            float halfcol = colwidth / 2;
            float lasth = 0;

            for ( int i = 0; i < data.size( ); i++ ) {
                float val = data.get( i ) - min;
                float rat = val / diff;
                float h = graphHeight * rat;
                if (i > 0) {
                    canvas.drawLine( ( ( i - 1 ) * colwidth) + ( horstart + 1 ) + halfcol,
                            ( border - lasth ) + graphHeight,
                            ( i * colwidth ) + ( horstart + 1 ) + halfcol,
                            ( border - h ) + graphHeight, paint);
                }
                lasth = h;
            }

            canvas.drawLine( position * width / 4,
                    height - border / 2,
                    position * width / 4 + 40,
                    height - border / 2,
                    paint
            );

            paint.setColor( Color.BLACK );

            canvas.drawText( label,
                    position * width / 4,
                    height - border / 2 + 20,
                    paint
            );

        }
    }

    private void drawVerLabels( Canvas canvas ) {

        verlabels = getVerLabels( min, max );

        paint.setTextAlign( Align.LEFT );
        paint.setTextSize( 20 );

        int vers = verlabels.size( ) - 1;
        for ( int i = 0; i < verlabels.size( ); i++ ) {
            paint.setColor( Color.DKGRAY );
            float y = ( ( graphHeight / vers ) * i ) + border;
            canvas.drawLine( horstart, y, width, y, paint );
            paint.setColor( Color.BLACK );
            canvas.drawText( verlabels.get( i ), 0, y, paint );
        }

    }

    private void drawHorLabels( Canvas canvas ) {

        int hors = horlabels.size( ) - 1;

        for ( int i = 0; i < horlabels.size( ); i++ ) {
            paint.setColor( Color.DKGRAY );
            float x = ( ( graphWidth / hors ) * i ) + horstart;
            canvas.drawLine( x, height - 2 * border, x, border, paint);
            paint.setTextAlign( Align.CENTER );
            if ( i==horlabels.size( )-1 )
                paint.setTextAlign( Align.RIGHT );
            if ( i==0 )
                paint.setTextAlign( Align.LEFT );
            paint.setColor( Color.BLACK );
            canvas.drawText( horlabels.get( i ), x, height - border - 4, paint );
        }
    }

    private void drawTitle( Canvas canvas ) {
        paint.setTextAlign( Align.CENTER );
        canvas.drawText( title, ( graphWidth / 2 ) + horstart, border - 4, paint );
    }

}