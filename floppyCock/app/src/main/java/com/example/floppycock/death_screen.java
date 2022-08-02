package com.example.floppycock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class death_screen extends AppCompatActivity {

    private int score;
    private int highScore;
    private final String fileName = "scoreFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death_screen);

        Intent intent = getIntent();

        score = intent.getIntExtra("score", 0);

        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            highScore = ((int) reader.read());

        } catch (IOException e) {
            e.printStackTrace();
        }

        if( highScore < score) {

            highScore = score;

            try (FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE)) {

                fos.write(highScore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        TextView scoreTxt = (TextView)findViewById(R.id.score_txt);
        TextView highScoreTxt = (TextView)findViewById(R.id.highScore_txt);

        scoreTxt.setText(String.valueOf(score));
        highScoreTxt.setText(String.valueOf(highScore));
    }

    public void mainMenu(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void retry(View view) {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }
}