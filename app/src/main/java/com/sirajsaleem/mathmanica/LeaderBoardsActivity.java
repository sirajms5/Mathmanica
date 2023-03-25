package com.sirajsaleem.mathmanica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class LeaderBoardsActivity extends AppCompatActivity implements MethodsFactory {
    private TextView leaderBoardsQuickGameTxt;
    private TextView leaderBoardsStreak;
    private TextView leaderBoardsTimeTrial;
    private TextView leaderBoardsVeryEasy;
    private TextView leaderBoardsEasy;
    private TextView leaderBoardsMedium;
    private TextView leaderBoardsHard;
    private TextView splitter1;
    private TextView splitter2;
    private TextView splitter3;
    private TextView userRank1;
    private TextView userRank2;
    private TextView userRank3;
    private TextView userRank4;
    private TextView userRank5;
    private TextView userRank6;
    private TextView userRank7;
    private TextView userRank8;
    private TextView userRank9;
    private TextView userRank10;
    private TextView scoreRank1;
    private TextView scoreRank2;
    private TextView scoreRank3;
    private TextView scoreRank4;
    private TextView scoreRank5;
    private TextView scoreRank6;
    private TextView scoreRank7;
    private TextView scoreRank8;
    private TextView scoreRank9;
    private TextView scoreRank10;
    private TextView currentUser;
    private TextView currentUserScore;
    private TextView currentUserRank;
    private ProgressBar loadingSemiCircleProgressBar;
    private String type = "Quick Game";
    private String difficulty = "Easy";
    private static String user1 = "";
    private static String user2 = "";
    private static String user3 = "";
    private static String user4 = "";
    private static String user5 = "";
    private static String user6 = "";
    private static String user7 = "";
    private static String user8 = "";
    private static String user9 = "";
    private static String user10 = "";
    private static String currentUserRankString = "";
    private static String score1 = "";
    private static String score2 = "";
    private static String score3 = "";
    private static String score4 = "";
    private static String score5 = "";
    private static String score6 = "";
    private static String score7 = "";
    private static String score8 = "";
    private static String score9 = "";
    private static String score10 = "";
    private static String currentUserScoreString = "";
    private static String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_boards);

        if (getIntent().getBooleanExtra("back", false)) {
            overridePendingTransition(R.anim.navigation_back_slide_in, R.anim.navigation_back_slide_out);
        }

        findViews();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.leaderboards).toUpperCase(Locale.ROOT));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        username = LocalUserSettings.getInstance(this).getUsername();


        if (LocalUserSettings.getInstance(this).getOrientationControl()) {
            if (isOnline()) {
                getDataFromDb();
            }
            LocalUserSettings.getInstance(this).setOrientationControl(false);
        } else {
            if (isOnline()) {
                setScoresAndRanks();
            }
        }

        leaderBoardsQuickGameTxt.setEnabled(false);

        leaderBoardsQuickGameTxt.setOnClickListener(v -> {
            leaderBoardsQuickGameTxt.setTextColor(ContextCompat.getColor(this, R.color.green_shaded_x3));
            leaderBoardsStreak.setTextColor(Color.GREEN);
            leaderBoardsTimeTrial.setTextColor(Color.GREEN);
            isDifficultiesVisibility(true);
            type = "Quick Game";
            difficulty = "Easy";
            leaderBoardsVeryEasy.setTextColor(ContextCompat.getColor(this, R.color.aqua));
            leaderBoardsEasy.setTextColor(ContextCompat.getColor(this, R.color.green_shaded_x3));
            leaderBoardsMedium.setTextColor(ContextCompat.getColor(this, R.color.yellow));
            leaderBoardsHard.setTextColor(ContextCompat.getColor(this, R.color.red));
            leaderBoardsQuickGameTxt.setEnabled(false);
            leaderBoardsStreak.setEnabled(true);
            leaderBoardsTimeTrial.setEnabled(true);
            getDataFromDb();
        });

        leaderBoardsStreak.setOnClickListener(v -> {
            leaderBoardsQuickGameTxt.setTextColor(Color.GREEN);
            leaderBoardsStreak.setTextColor(ContextCompat.getColor(this, R.color.green_shaded_x3));
            leaderBoardsTimeTrial.setTextColor(Color.GREEN);
            isDifficultiesVisibility(false);
            type = "Streak";
            difficulty = "Streak";
            leaderBoardsQuickGameTxt.setEnabled(true);
            leaderBoardsStreak.setEnabled(false);
            leaderBoardsTimeTrial.setEnabled(true);
            getDataFromDb();
        });

        leaderBoardsTimeTrial.setOnClickListener(v -> {
            leaderBoardsQuickGameTxt.setTextColor(Color.GREEN);
            leaderBoardsStreak.setTextColor(Color.GREEN);
            leaderBoardsTimeTrial.setTextColor(ContextCompat.getColor(this, R.color.green_shaded_x3));
            isDifficultiesVisibility(false);
            type = "Time Trial";
            difficulty = "Time_Trial";
            leaderBoardsQuickGameTxt.setEnabled(true);
            leaderBoardsStreak.setEnabled(true);
            leaderBoardsTimeTrial.setEnabled(false);
            getDataFromDb();
        });

        leaderBoardsVeryEasy.setOnClickListener(v -> {
            leaderBoardsVeryEasy.setTextColor(ContextCompat.getColor(this, R.color.aqua_shaded));
            leaderBoardsEasy.setTextColor(Color.GREEN);
            leaderBoardsMedium.setTextColor(ContextCompat.getColor(this, R.color.yellow));
            leaderBoardsHard.setTextColor(ContextCompat.getColor(this, R.color.red));
            difficulty = "Very_Easy";
            getDataFromDb();
        });

        leaderBoardsEasy.setOnClickListener(v -> {
            leaderBoardsVeryEasy.setTextColor(ContextCompat.getColor(this, R.color.aqua));
            leaderBoardsEasy.setTextColor(ContextCompat.getColor(this, R.color.green_shaded_x3));
            leaderBoardsMedium.setTextColor(ContextCompat.getColor(this, R.color.yellow));
            leaderBoardsHard.setTextColor(ContextCompat.getColor(this, R.color.red));
            difficulty = "Easy";
            getDataFromDb();
        });

        leaderBoardsMedium.setOnClickListener(v -> {
            leaderBoardsVeryEasy.setTextColor(ContextCompat.getColor(this, R.color.aqua));
            leaderBoardsEasy.setTextColor(ContextCompat.getColor(this, R.color.green));
            leaderBoardsMedium.setTextColor(ContextCompat.getColor(this, R.color.yellow_shaded_x2));
            leaderBoardsHard.setTextColor(ContextCompat.getColor(this, R.color.red));
            difficulty = "Medium";
            getDataFromDb();
        });

        leaderBoardsHard.setOnClickListener(v -> {
            leaderBoardsVeryEasy.setTextColor(ContextCompat.getColor(this, R.color.aqua));
            leaderBoardsEasy.setTextColor(ContextCompat.getColor(this, R.color.green));
            leaderBoardsMedium.setTextColor(ContextCompat.getColor(this, R.color.yellow));
            leaderBoardsHard.setTextColor(ContextCompat.getColor(this, R.color.red_shaded));
            difficulty = "Hard";
            getDataFromDb();
        });
    }

    // check internet connection
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void setScoresAndRanks() {
        userRank1.setText(user1);
        userRank2.setText(user2);
        userRank3.setText(user3);
        userRank4.setText(user4);
        userRank5.setText(user5);
        userRank6.setText(user6);
        userRank7.setText(user7);
        userRank8.setText(user8);
        userRank9.setText(user9);
        userRank10.setText(user10);
        scoreRank1.setText(score1);
        scoreRank2.setText(score2);
        scoreRank3.setText(score3);
        scoreRank4.setText(score4);
        scoreRank5.setText(score5);
        scoreRank6.setText(score6);
        scoreRank7.setText(score7);
        scoreRank8.setText(score8);
        scoreRank9.setText(score9);
        scoreRank10.setText(score10);
        currentUserRank.setText(currentUserRankString);
        currentUserScore.setText(currentUserScoreString);
        currentUser.setText(username);
    }

    private void getDataFromDb() {
        ExecutorService service = new ForkJoinPool();
        service.execute(() -> {
            runOnUiThread(() -> loadingSemiCircleProgressBar.setVisibility(View.VISIBLE));

            DbGetLeaderBoards dbGetLeaderBoards = new DbGetLeaderBoards();
            for (int i = 0; i < 11; i++) {
                switch (i) {
                    case 0:
                        user1 = dbGetLeaderBoards.sendData("", type, i + "", "username", difficulty, "");
                        score1 = dbGetLeaderBoards.sendData("", type, i + "", "score", difficulty, "");
                        break;
                    case 1:
                        user2 = dbGetLeaderBoards.sendData("", type, i + "", "username", difficulty, "");
                        score2 = dbGetLeaderBoards.sendData("", type, i + "", "score", difficulty, "");
                        break;
                    case 2:
                        user3 = dbGetLeaderBoards.sendData("", type, i + "", "username", difficulty, "");
                        score3 = dbGetLeaderBoards.sendData("", type, i + "", "score", difficulty, "");
                        break;
                    case 3:
                        user4 = dbGetLeaderBoards.sendData("", type, i + "", "username", difficulty, "");
                        score4 = dbGetLeaderBoards.sendData("", type, i + "", "score", difficulty, "");
                        break;
                    case 4:
                        user5 = dbGetLeaderBoards.sendData("", type, i + "", "username", difficulty, "");
                        score5 = dbGetLeaderBoards.sendData("", type, i + "", "score", difficulty, "");
                        break;
                    case 5:
                        user6 = dbGetLeaderBoards.sendData("", type, i + "", "username", difficulty, "");
                        score6 = dbGetLeaderBoards.sendData("", type, i + "", "score", difficulty, "");
                        break;
                    case 6:
                        user7 = dbGetLeaderBoards.sendData("", type, i + "", "username", difficulty, "");
                        score7 = dbGetLeaderBoards.sendData("", type, i + "", "score", difficulty, "");
                        break;
                    case 7:
                        user8 = dbGetLeaderBoards.sendData("", type, i + "", "username", difficulty, "");
                        score8 = dbGetLeaderBoards.sendData("", type, i + "", "score", difficulty, "");
                        break;
                    case 8:
                        user9 = dbGetLeaderBoards.sendData("", type, i + "", "username", difficulty, "");
                        score9 = dbGetLeaderBoards.sendData("", type, i + "", "score", difficulty, "");
                        break;
                    case 9:
                        user10 = dbGetLeaderBoards.sendData("", type, i + "", "username", difficulty, "");
                        score10 = dbGetLeaderBoards.sendData("", type, i + "", "score", difficulty, "");
                        break;
                    default:
                        currentUserRankString = dbGetLeaderBoards.sendData(username, type, "", "", difficulty, "");
                        currentUserScoreString = dbGetLeaderBoards.sendData(username, type, "", "", difficulty, "score");
                        break;
                }
            }

            runOnUiThread(() -> {
                loadingSemiCircleProgressBar.setVisibility(View.GONE);
                int currentRank = Integer.parseInt(currentUserRankString);
                if (!username.equals("unregistered") && currentRank <= 2) {
                    currentUser.setTextColor(Color.RED);
                    currentUserScore.setTextColor(Color.RED);
                    currentUserRank.setTextColor(Color.RED);
                } else if (!username.equals("unregistered") && currentRank <= 5) {
                    currentUser.setTextColor(Color.YELLOW);
                    currentUserScore.setTextColor(Color.YELLOW);
                    currentUserRank.setTextColor(Color.YELLOW);
                } else if (!username.equals("unregistered") && currentRank <= 8) {
                    currentUser.setTextColor(Color.GREEN);
                    currentUserScore.setTextColor(Color.GREEN);
                    currentUserRank.setTextColor(Color.GREEN);
                } else {
                    currentUser.setTextColor(ContextCompat.getColor(LeaderBoardsActivity.this, R.color.aqua));
                    currentUserScore.setTextColor(ContextCompat.getColor(LeaderBoardsActivity.this, R.color.aqua));
                    currentUserRank.setTextColor(ContextCompat.getColor(LeaderBoardsActivity.this, R.color.aqua));
                }
                if (!username.equals("unregistered")) {
                    currentUserRankString = (Integer.parseInt(currentUserRankString) + 1) + ". ";
                } else {
                    currentUserRankString = "";
                }
                setScoresAndRanks();
            });
        });
    }

    private void isDifficultiesVisibility(boolean b) {
        if (b) {
            leaderBoardsVeryEasy.setVisibility(View.VISIBLE);
            leaderBoardsEasy.setVisibility(View.VISIBLE);
            leaderBoardsMedium.setVisibility(View.VISIBLE);
            leaderBoardsHard.setVisibility(View.VISIBLE);
            splitter1.setVisibility(View.VISIBLE);
            splitter2.setVisibility(View.VISIBLE);
            splitter3.setVisibility(View.VISIBLE);
        } else {
            leaderBoardsVeryEasy.setVisibility(View.GONE);
            leaderBoardsEasy.setVisibility(View.GONE);
            leaderBoardsMedium.setVisibility(View.GONE);
            leaderBoardsHard.setVisibility(View.GONE);
            splitter1.setVisibility(View.GONE);
            splitter2.setVisibility(View.GONE);
            splitter3.setVisibility(View.GONE);
        }
    }

    @Override
    public void findViews() {
        leaderBoardsQuickGameTxt = findViewById(R.id.leaderBoardsQuickGameTxt);
        leaderBoardsStreak = findViewById(R.id.leaderBoardsStreakTxt);
        leaderBoardsTimeTrial = findViewById(R.id.leaderBoardsTimeTrialTxt);
        leaderBoardsVeryEasy = findViewById(R.id.leaderBoardsVeryEasyTxt);
        leaderBoardsEasy = findViewById(R.id.leaderBoardsEasyTxt);
        leaderBoardsMedium = findViewById(R.id.leaderBoardsMediumTxt);
        leaderBoardsHard = findViewById(R.id.leaderBoardsHardTxt);
        splitter1 = findViewById(R.id.quickGameSplitter1);
        splitter2 = findViewById(R.id.quickGameSplitter2);
        splitter3 = findViewById(R.id.quickGameSplitter3);
        userRank1 = findViewById(R.id.rank_1_user);
        userRank2 = findViewById(R.id.rank_2_user);
        userRank3 = findViewById(R.id.rank_3_user);
        userRank4 = findViewById(R.id.rank_4_user);
        userRank5 = findViewById(R.id.rank_5_user);
        userRank6 = findViewById(R.id.rank_6_user);
        userRank7 = findViewById(R.id.rank_7_user);
        userRank8 = findViewById(R.id.rank_8_user);
        userRank9 = findViewById(R.id.rank_9_user);
        userRank10 = findViewById(R.id.rank_10_user);
        scoreRank1 = findViewById(R.id.rank_1_score);
        scoreRank2 = findViewById(R.id.rank_2_score);
        scoreRank3 = findViewById(R.id.rank_3_score);
        scoreRank4 = findViewById(R.id.rank_4_score);
        scoreRank5 = findViewById(R.id.rank_5_score);
        scoreRank6 = findViewById(R.id.rank_6_score);
        scoreRank7 = findViewById(R.id.rank_7_score);
        scoreRank8 = findViewById(R.id.rank_8_score);
        scoreRank9 = findViewById(R.id.rank_9_score);
        scoreRank10 = findViewById(R.id.rank_10_score);
        currentUser = findViewById(R.id.userUsernameTxt);
        currentUserScore = findViewById(R.id.userScoreTxt);
        currentUserRank = findViewById(R.id.userRankNumTxt);
        loadingSemiCircleProgressBar = findViewById(R.id.leaderBoardsProgressBar);
    }

    @Override
    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("back", true);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            goBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}