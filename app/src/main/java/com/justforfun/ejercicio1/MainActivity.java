package com.justforfun.ejercicio1;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private ImageButton adelantar,pausa,play,retroceder;
    private MediaPlayer mediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;//Tiempo de adelanto
    private int backwardTime = 5000;//Tiempo de retraso
    private SeekBar seekbar;
    private TextView tiempoPlay,duracion;

    public static int oneTimeOnly = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        adelantar = (ImageButton) findViewById(R.id.adelantar);
        pausa = (ImageButton) findViewById(R.id.pausa);
        play = (ImageButton)findViewById(R.id.play);
        retroceder = (ImageButton)findViewById(R.id.retroceder);
        tiempoPlay = (TextView)findViewById(R.id.tiempoPlay);
        duracion = (TextView)findViewById(R.id.duracion);
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        mediaPlayer = MediaPlayer.create(this, R.raw.vivalavida);
        seekbar.setClickable(false);
        pausa.setEnabled(false);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Reproduciendo",Toast.LENGTH_SHORT).show();
                mediaPlayer.start();//Iniciamos la reproducción

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                duracion.setText(String.format("%d min, %d s",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );

                tiempoPlay.setText(String.format("%d min, %d s",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );

                seekbar.setProgress((int)startTime);
                myHandler.postDelayed(UpdateSongTime,100);
                pausa.setEnabled(true);
                play.setEnabled(false);
            }
        });

        pausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausa",Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                pausa.setEnabled(false);
                play.setEnabled(true);
            }
        });

        adelantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"+5 s",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Imposible hacerlo",Toast.LENGTH_SHORT).show();
                }
            }
        });

        retroceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"-5 s",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Imposible hacerlo",Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int duration = mediaPlayer.getDuration();
            boolean humanInteraction = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (humanInteraction) {
                    if (i > 0) {
                        Log.d("MainActivity", "SeekBar = " + i);
                        Log.d("MainActivity", "getDuration = " + mediaPlayer.getDuration());
                        int seekTo = i;
                        Toast.makeText(MainActivity.this, "Seek To = " + seekTo, Toast.LENGTH_SHORT).show();
                        mediaPlayer.seekTo(seekTo);
                    } else {
                        mediaPlayer.seekTo(0);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                humanInteraction = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                humanInteraction = false;
            }
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tiempoPlay.setText(String.format("%d min, %d s",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };
}
