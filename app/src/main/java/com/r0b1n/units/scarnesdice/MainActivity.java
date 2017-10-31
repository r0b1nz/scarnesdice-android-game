package com.r0b1n.units.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView userScoreText;
    TextView compScoreText;
    TextView userSubScoreText;
    TextView compSubScoreText;
    ImageView imageView;
    Button roll;
    Button hold;
    Button reset;

    int userOverallScore = 0;
    int userTurnScore = 0;
    int compOverallScore = 0;
    int compTurnScore = 0;
    Random random = new Random();
    int computerTurns = 0;

    int MAX_TURNS = 4;
    int SAFE_DICE_NUM = 4;
    int WINNING_SCORE = 15;

    int [] diceImage = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    Handler turnHandler = new Handler();
    Runnable playTurn = new Runnable() {
        @Override
        public void run() {
            int diceNum = 1 + random.nextInt(6);
            imageView.setImageDrawable(getDrawable(diceImage[diceNum - 1]));
            if (diceNum == 1) {
                compTurnScore = 0;
                refreshScores();
                updateUI();
                roll.setEnabled(true);
                hold.setEnabled(true);
                return;
            } else {
                compTurnScore += diceNum;
                updateUI();
                if (diceNum >= SAFE_DICE_NUM && ++computerTurns <=  MAX_TURNS) {
                    //call handler
                    turnHandler.postDelayed(playTurn, 500);
                } else {
                    write("Your turn now!");
                    refreshScores();
                    roll.setEnabled(true);
                    hold.setEnabled(true);
                    updateUI();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userScoreText = (TextView) findViewById(R.id.textView3);
        compScoreText = (TextView) findViewById(R.id.textView4);
        userSubScoreText = (TextView) findViewById(R.id.textView7);
        compSubScoreText = (TextView) findViewById(R.id.textView8);
        imageView = (ImageView) findViewById(R.id.imageView);
        roll = (Button) findViewById(R.id.button);
        hold = (Button) findViewById(R.id.button2);
        reset = (Button) findViewById(R.id.button3);
    }

    public void roll(View view) {
        int diceNum = 1 + random.nextInt(6);
        imageView.setImageDrawable(getDrawable(diceImage[diceNum-1]));
        if (diceNum == 1) {
            userTurnScore = 0;
            hold(null);
            return;
        } else {
            userTurnScore += diceNum;
        }
        updateUI();
    }

    public void hold(View view) {
        userOverallScore += userTurnScore;
        if (updateUI() == 0) { // updateUI() will return 0 if no one has cleared the winning threshold
            userTurnScore = 0;
            compTurnScore = 0;
            write("Computer's turn now");
            compTurn();
        }
    }

    public void reset(View view) {
        userOverallScore = userTurnScore = compOverallScore = compTurnScore = 0;
        updateUI();
        roll.setEnabled(true);
        hold.setEnabled(true);
    }

    private int updateUI() {
        userScoreText.setText("" + userOverallScore);
        userSubScoreText.setText("" + userTurnScore);
        compScoreText.setText("" + compOverallScore);
        compSubScoreText.setText("" + compTurnScore);
        if (userOverallScore >= WINNING_SCORE) {
            roll.setEnabled(false);
            hold.setEnabled(false);
            write("YOU WIN!");
            return -1;

        } else if (compOverallScore >= WINNING_SCORE) {
            roll.setEnabled(false);
            hold.setEnabled(false);
            write("Computer WINS!");
            return -1;
        }
        return 0;
    }

    private void refreshScores() {
        compOverallScore += compTurnScore;
        userOverallScore += userTurnScore;
        compTurnScore = 0;
        userTurnScore = 0;
    }

    private void write(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void compTurn() {
        roll.setEnabled(false);
        hold.setEnabled(false);
        computerTurns = 0;
        turnHandler.postDelayed(playTurn, 500);
        updateUI();
    }
}
