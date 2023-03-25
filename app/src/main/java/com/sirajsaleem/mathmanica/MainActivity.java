package com.sirajsaleem.mathmanica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MethodsFactory {

    private TextView quickGameTxt;
    private TextView gameModesTxt;
    private TextView leaderboardsTxt;
    private TextView settingsTxt;
    private TextView passwordErrorTxt;
    private TextView signUpTxt;
    private TextView skipTxt;
    private TextView usernameErrorTxt;
    private TextView logInTitleTxt;
    private TextView signUpTitleTxt;
    private TextView logInPasswordErrorTxt;
    private EditText usernameEditTxt;
    private EditText passwordEditTxt;
    private EditText rePasswordEditTxt;
    private CheckBox dontShowCheckBox;
    private String username;
    private String password;
    private String rePassword;
    private Boolean dontShowCB;
    private Button streakBtn, timeTrialBtn;
    private ProgressBar loadingSemiCircle;
    private Dialog dialog;
    private String result = "";
    private StringBuilder hashedPassword;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ConstraintLayout mainActivityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getBooleanExtra("back", false)) {
            overridePendingTransition(R.anim.navigation_back_slide_in, R.anim.navigation_back_slide_out);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        findViews();
        LocalUserSettings.getInstance(this);
        refreshControls();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (!LocalUserSettings.getInstance(this).getPermission("sign_up_checkbox")) {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.sign_up_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setCancelable(false);
            dialog.show();

            // dialog views
            usernameEditTxt = dialog.findViewById(R.id.userNameEditTxt);
            usernameErrorTxt = dialog.findViewById(R.id.usernameErrorTxt);
            passwordEditTxt = dialog.findViewById(R.id.passwordEditTxt);
            rePasswordEditTxt = dialog.findViewById(R.id.reEnterPasswordEditTxt);
            passwordErrorTxt = dialog.findViewById(R.id.passwordErrorTxt);
            dontShowCheckBox = dialog.findViewById(R.id.dontShowCheckBox);
            signUpTxt = dialog.findViewById(R.id.signUpTxt);
            skipTxt = dialog.findViewById(R.id.skipTxt);
            loadingSemiCircle = dialog.findViewById(R.id.loadingSemiCircle);
            logInTitleTxt = dialog.findViewById(R.id.logInTitleTxt);
            signUpTitleTxt = dialog.findViewById(R.id.signUpTitleTxt);
            logInPasswordErrorTxt = dialog.findViewById(R.id.logInPasswordErrorTxt);

            logInTitleTxt.setOnClickListener(v -> {
                passwordErrorTxt.setVisibility(View.GONE);
                usernameErrorTxt.setVisibility(View.GONE);
                logInPasswordErrorTxt.setVisibility(View.GONE);
                logInTitleTxt.setTextColor(ContextCompat.getColor(this, R.color.green));
                signUpTitleTxt.setTextColor(ContextCompat.getColor(this, R.color.green_shaded_x3));
                signUpTxt.setText(R.string.log_in);
                rePasswordEditTxt.setVisibility(View.INVISIBLE);
            });

            signUpTitleTxt.setOnClickListener(v -> {
                passwordErrorTxt.setVisibility(View.GONE);
                usernameErrorTxt.setVisibility(View.GONE);
                logInPasswordErrorTxt.setVisibility(View.GONE);
                signUpTitleTxt.setTextColor(ContextCompat.getColor(this, R.color.green));
                logInTitleTxt.setTextColor(ContextCompat.getColor(this, R.color.green_shaded_x3));
                signUpTxt.setText(R.string.sign_up);
                rePasswordEditTxt.setVisibility(View.VISIBLE);
            });

            signUpTxt.setOnClickListener(v -> {
                usernameErrorTxt.setVisibility(View.GONE);
                logInPasswordErrorTxt.setVisibility(View.GONE);
                username = usernameEditTxt.getText().toString();
                password = passwordEditTxt.getText().toString();
                dontShowCB = dontShowCheckBox.isChecked();
                if (signUpTxt.getText().toString().equals("Sign Up")) {
                    rePassword = rePasswordEditTxt.getText().toString();
                    if (checkValidity()) {
                        checkWithDatabase("signup");
                    }
                } else {
                    if (!username.equals("") && !password.equals("")) {
                        checkWithDatabase("login");
                    } else if (!username.equals("")) {
                        logInPasswordErrorTxt.setVisibility(View.VISIBLE);
                        usernameErrorTxt.setVisibility(View.GONE);
                    } else if (!password.equals("")) {
                        usernameErrorTxt.setVisibility(View.VISIBLE);
                        logInPasswordErrorTxt.setVisibility(View.GONE);
                    } else {
                        logInPasswordErrorTxt.setVisibility(View.VISIBLE);
                        usernameErrorTxt.setVisibility(View.VISIBLE);
                    }

                }
            });

            skipTxt.setOnClickListener(v -> {
                dontShowCB = dontShowCheckBox.isChecked();
                LocalUserSettings.getInstance(this).grantPermissions("sign_up_checkbox", dontShowCB);
                dialog.dismiss();
            });
        }

        if (!LocalUserSettings.getInstance(this).getPermission("firebase_analytics")) {
            firebaseDialog();
        }

        quickGameTxt.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuickGameActivity.class);
            startActivity(intent);
        });

        gameModesTxt.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.game_modes_choices_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setCancelable(true);
            dialog.show();

            //dialog views
            streakBtn = dialog.findViewById(R.id.streakBtn);
            timeTrialBtn = dialog.findViewById(R.id.timeTrialBtn);
            streakBtn.setOnClickListener(j -> {
                Intent intent = new Intent(this, StreakActivity.class);
                startActivity(intent);
            });
            timeTrialBtn.setOnClickListener(j -> {
                Intent intent = new Intent(this, TimeTrialActivity.class);
                startActivity(intent);
            });
        });

        leaderboardsTxt.setOnClickListener(v -> {
            Intent intent = new Intent(this, LeaderBoardsActivity.class);
            startActivity(intent);
        });

        settingsTxt.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void refreshControls() {
        LocalUserSettings.getInstance(this).setOrientationControl(true); // resetting orientation control settings
        LocalUserSettings.getInstance(this).setScore("Time_Trial", 0);
        TimeTrialActivity.isCounterSwitch = false;
        TimeTrialActivity.isSubmitted = false; // resetting orientation control settings
        TimeTrialActivity.isCounting = false; // resetting orientation control settings
        TimeTrialActivity.isStarted = false; // resetting orientation control settings
        TimeTrialActivity.isEndRun = false; // resetting orientation control settings
        TimeTrialActivity.secondsString = "00"; // resetting orientation control settings
        TimeTrialActivity.minutesString = "01"; // resetting orientation control settings
        TimeTrialActivity.hoursString = "00"; // resetting orientation control settings
        StreakActivity.isSubmitted = false; // resetting orientation control settings
        QuickGameActivity.isSubmitted = false; // resetting orientation control settings
    }

    private void firebaseDialog() {
        if (LocalUserSettings.getInstance(this).getPermission("firebase_analytics")) {
            mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        } else {
            if (!LocalUserSettings.getInstance(this).getPermission("noDialog")) {
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setContentView(R.layout.firebase_permission_dialog);
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90); //making width of dialog 90% of screen width
                dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();

                TextView allow = dialog.findViewById(R.id.allowTxt);
                TextView deny = dialog.findViewById(R.id.dismissTxt);
                CheckBox neverShowCheckBox = dialog.findViewById(R.id.permissionCheckBox);

                allow.setOnClickListener(v -> {
                    dialog.dismiss();
                    mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
                    LocalUserSettings.getInstance(MainActivity.this).grantPermissions("firebase_analytics", true);
                    if (neverShowCheckBox.isChecked()) {
                        LocalUserSettings.getInstance(this).grantPermissions("noDialog", true);
                    }
                });

                deny.setOnClickListener(v -> {
                    dialog.dismiss();
                    mFirebaseAnalytics.setAnalyticsCollectionEnabled(false);
                    LocalUserSettings.getInstance(MainActivity.this).grantPermissions("firebase_analytics", false);
                    if (neverShowCheckBox.isChecked()) {
                        LocalUserSettings.getInstance(this).grantPermissions("noDialog", true);
                    }
                });
            }
        }
    }

    private void checkWithDatabase(String type) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            hashedPassword = new StringBuilder();
            for (byte b : resultByteArray) {
                hashedPassword.append(String.format("%02x", b));
            }

            ExecutorService service = Executors.newSingleThreadExecutor();
            //pre execute
            service.execute(() -> {
                runOnUiThread(() -> {
                    loadingSemiCircle.setVisibility(View.VISIBLE);
                    signUpTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_shaded_x4));
                    skipTxt.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_shaded_x4));
                });
                //heavy work

                DbConnect dbConnect = new DbConnect();
                result = dbConnect.sendData(type, username, hashedPassword.toString());

                //post execute
                runOnUiThread(() -> {
                    if (result != null) {
                        result = result.trim();
                        switch (result) {
                            case "Username taken":
                                loadingSemiCircle.setVisibility(View.GONE);
                                signUpTxt.setTextColor(Color.GREEN);
                                skipTxt.setTextColor(Color.GREEN);
                                usernameErrorTxt.setVisibility(View.VISIBLE);
                                usernameErrorTxt.setText(R.string.username_taken);
                                break;
                            case "Wrong password":
                                loadingSemiCircle.setVisibility(View.GONE);
                                signUpTxt.setTextColor(Color.GREEN);
                                skipTxt.setTextColor(Color.GREEN);
                                logInPasswordErrorTxt.setVisibility(View.VISIBLE);
                                usernameErrorTxt.setVisibility(View.GONE);
                                logInPasswordErrorTxt.setText(R.string.wrong_password);
                                break;
                            case "No such username":
                                loadingSemiCircle.setVisibility(View.GONE);
                                signUpTxt.setTextColor(Color.GREEN);
                                skipTxt.setTextColor(Color.GREEN);
                                usernameErrorTxt.setVisibility(View.VISIBLE);
                                logInPasswordErrorTxt.setVisibility(View.GONE);
                                usernameErrorTxt.setText(R.string.username_does_not_exist);
                                break;
                            default:
                                signingUpNewUser(type);
                                dialog.dismiss();
                                break;
                        }
                    } else {
                        Snackbar.make(mainActivityLayout, R.string.error_signing_in, Snackbar.LENGTH_INDEFINITE)
                                .setAction("DISMISS", v -> {

                                })
                                .show();
                        dialog.dismiss();
                    }
                });
            });
        } catch (NoSuchAlgorithmException e) {
            Log.d("Exception-error", "NoSuchAlgorithmException error");
        }
    }

    private void signingUpNewUser(String type) {
        LocalUserSettings.getInstance(MainActivity.this).setUsername(username);
        LocalUserSettings.getInstance(MainActivity.this).setPassword(hashedPassword.toString());
        LocalUserSettings.getInstance(MainActivity.this).grantPermissions("sign_up_checkbox", true);
        if (type.equals("signup")) {
            newUserScoreUpdateDb();
        }
        LocalUserSettings.getInstance(this).grantPermissions("sign_up_checkbox", true);
    }

    private void newUserScoreUpdateDb() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        //pre execute
        service.execute(() -> {
            //heavy work

            DbUpdateScore dbUpdateScore = new DbUpdateScore();
            dbUpdateScore.sendData(
                    LocalUserSettings.getInstance(MainActivity.this).getCurrentLevel("Very_Easy") + "",
                    "Very_Easy",
                    username,
                    LocalUserSettings.getInstance(MainActivity.this).getScore("Very_Easy") + "");
            dbUpdateScore.sendData(
                    LocalUserSettings.getInstance(MainActivity.this).getCurrentLevel("Easy") + "",
                    "Easy",
                    username,
                    LocalUserSettings.getInstance(MainActivity.this).getScore("Easy") + "");
            dbUpdateScore.sendData(
                    LocalUserSettings.getInstance(MainActivity.this).getCurrentLevel("Medium") + "",
                    "Medium",
                    username,
                    LocalUserSettings.getInstance(MainActivity.this).getScore("Medium") + "");
            dbUpdateScore.sendData(
                    LocalUserSettings.getInstance(MainActivity.this).getCurrentLevel("Hard") + "",
                    "Hard",
                    username,
                    LocalUserSettings.getInstance(MainActivity.this).getScore("Hard") + "");
            dbUpdateScore.sendData(
                    "",
                    "Streak",
                    username,
                    LocalUserSettings.getInstance(MainActivity.this).getHighScore("Streak") + "");
            dbUpdateScore.sendData(
                    "",
                    "Time_Trial",
                    username,
                    LocalUserSettings.getInstance(MainActivity.this).getHighScore("Time_Trial") + "");


            //post execute
        });
    }

    private boolean checkValidity() {
        if (
                !username.equals("")
                        && !password.equals("")
                        && !rePassword.equals("")
                        && password.equals(rePassword)
        ) {
            return true;
        } else {
            if (username.equals("") && (password.equals("") || rePassword.equals("") || !password.equals(rePassword))) {
                usernameErrorTxt.setVisibility(View.VISIBLE);
                passwordErrorTxt.setVisibility(View.VISIBLE);
                if (password.equals("")) {
                    logInPasswordErrorTxt.setVisibility(View.VISIBLE);
                }
            } else if (!username.equals("")) {
                passwordErrorTxt.setVisibility(View.VISIBLE);
                if (password.equals("")) {
                    logInPasswordErrorTxt.setVisibility(View.VISIBLE);
                }
                usernameErrorTxt.setVisibility(View.GONE);
            } else {
                usernameErrorTxt.setVisibility(View.VISIBLE);
                passwordErrorTxt.setVisibility(View.GONE);
            }
            return false;
        }
    }

    @Override
    public void findViews() {
        quickGameTxt = findViewById(R.id.quickGameTxt);
        gameModesTxt = findViewById(R.id.gameModesTxt);
        leaderboardsTxt = findViewById(R.id.leaderboardTxt);
        settingsTxt = findViewById(R.id.settingsTxt);
        mainActivityLayout = findViewById(R.id.mainActivityLayout);
    }

    @Override
    public void goBack() {
        //keep empty
    }

    @Override
    public void onBackPressed() {
        //to prevent unwanted going back from main page
    }
}