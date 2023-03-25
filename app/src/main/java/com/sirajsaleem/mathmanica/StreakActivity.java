package com.sirajsaleem.mathmanica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StreakActivity extends AppCompatActivity implements MethodsFactory {

    private TextView firstNumTxt;
    private TextView operationTxt;
    private TextView secondNumTxt;
    private TextView streakNumTxt;
    private TextView retryTxt;
    private TextView cancelTxt;
    private TextView exitTxt;
    private TextView streakDialogNumTxt;
    private TextView bonusStreakTxt;
    private final String gameDifficulty = "Streak";
    private String streakNumString;
    private static String correctAnswerString;
    private String chosenRadioBtn;
    private static String firstChoice;
    private static String secondChoice;
    private static String thirdChoice;
    private static String fourthChoice;
    private static String firstNumString;
    private static String secondNumString;
    private static String operation;
    private MultiLineRadioGroup choicesRadioGroup;
    private RadioButton firstChoiceRadioButton;
    private RadioButton secondChoiceRadioButton;
    private RadioButton thirdChoiceRadioButton;
    private RadioButton fourthChoiceRadioButton;
    private static int correctAnswer;
    private int streakNum;
    private int firstNum;
    private int secondNum;
    private Button streakSubmitBtn;
    private Button streakNextBtn;
    private Button streakRetryBtn;
    private double multiOrDivide;
    private final ArrayList<Integer> choices = new ArrayList<>();
    public static boolean isSubmitted;
    private ConstraintLayout streakLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streak);

        if (getIntent().getBooleanExtra("back", false)) {
            overridePendingTransition(R.anim.navigation_back_slide_in, R.anim.navigation_back_slide_out);
        }

        findViews();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.streak_with_space) + LocalUserSettings.getInstance(this).getScore("Streak")); // string + int
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (LocalUserSettings.getInstance(this).getOrientationControl()) {
            nextEquation();
            updateScore(0);
            if (!isOnline()) {
                Snackbar.make(streakLayout, R.string.you_are_not_online, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.dismiss, v -> {

                        })
                        .show();
            }
            LocalUserSettings.getInstance(this).setOrientationControl(false);

        } else {
            firstNumTxt.setText(firstNumString);
            operationTxt.setText(operation);
            secondNumTxt.setText(secondNumString);
            firstChoiceRadioButton.setText(firstChoice);
            secondChoiceRadioButton.setText(secondChoice);
            thirdChoiceRadioButton.setText(thirdChoice);
            fourthChoiceRadioButton.setText(fourthChoice);
            updateScore(LocalUserSettings.getInstance(this).getScore("Streak"));
            if (isSubmitted) {
                disableChoices();
                streakSubmitBtn.setEnabled(false);
                streakNextBtn.setEnabled(true);
            }
        }

        choicesRadioGroup.setOnCheckedChangeListener((MultiLineRadioGroup.OnCheckedChangeListener) (group, button) -> {
            if (!isSubmitted) {
                streakSubmitBtn.setEnabled(true);
            }
        });

        streakSubmitBtn.setOnClickListener(v -> {
            isSubmitted = true;
            streakSubmitBtn.setEnabled(false);
            correctAnswerString = Integer.toString(correctAnswer);
            String firstChoiceRadioButtonString = firstChoiceRadioButton.getText().toString();
            String secondChoiceRadioButtonString = secondChoiceRadioButton.getText().toString();
            String thirdChoiceRadioButtonString = thirdChoiceRadioButton.getText().toString();
            if (firstChoiceRadioButtonString.length() > 3) {
                firstChoiceRadioButtonString = firstChoiceRadioButtonString.replace(",", "");
            }
            if (secondChoiceRadioButtonString.length() > 3) {
                secondChoiceRadioButtonString = secondChoiceRadioButtonString.replace(",", "");
            }
            if (thirdChoiceRadioButtonString.length() > 3) {
                thirdChoiceRadioButtonString = thirdChoiceRadioButtonString.replace(",", "");
            }
            // highlighting correct/wrong answer and +/- points
            if (choicesRadioGroup.getCheckedRadioButtonText() != null) {
                if (correctAnswerString.equals(firstChoiceRadioButtonString)) {
                    firstChoiceRadioButton.setTextColor(Color.GREEN);
                } else if (correctAnswerString.equals(secondChoiceRadioButtonString)) {
                    secondChoiceRadioButton.setTextColor(Color.GREEN);
                } else if (correctAnswerString.equals(thirdChoiceRadioButtonString)) {
                    thirdChoiceRadioButton.setTextColor(Color.GREEN);
                } else {
                    fourthChoiceRadioButton.setTextColor(Color.GREEN);
                }

                chosenRadioBtn = choicesRadioGroup.getCheckedRadioButtonText().toString();
                if (chosenRadioBtn.length() > 3) {
                    chosenRadioBtn = chosenRadioBtn.replace(",", "");
                }
                if (!correctAnswerString.equals(chosenRadioBtn)) {
                    int wrongAnswerRadioBtn = choicesRadioGroup.getCheckedRadioButtonId();
                    if (wrongAnswerRadioBtn == R.id.streakFirstChoiceRadioBtn) {
                        firstChoiceRadioButton.setTextColor(Color.RED);
                    } else if (wrongAnswerRadioBtn == R.id.streakSecondChoiceRadioBtn) {
                        secondChoiceRadioButton.setTextColor(Color.RED);
                    } else if (wrongAnswerRadioBtn == R.id.streakThirdChoiceRadioBtn) {
                        thirdChoiceRadioButton.setTextColor(Color.RED);
                    } else {
                        fourthChoiceRadioButton.setTextColor(Color.RED);
                    }
                }

                if (correctAnswerString.equals(chosenRadioBtn)) {
                    bonusStreakTxt.setAlpha(0f);
                    bonusStreakTxt.setVisibility(View.VISIBLE);
                    bonusStreakTxt.animate()
                            .alpha(0.8f)
                            .setDuration(250)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    bonusStreakTxt.animate()
                                            .alpha(0f)
                                            .setDuration(500)
                                            .setStartDelay(500)
                                            .setListener(null);
                                }
                            });
                    streakNum = LocalUserSettings.getInstance(this).getScore(gameDifficulty) + 1;
                    updateScore(streakNum);
                    streakNextBtn.setEnabled(true);
                } else {
                    streakNum = 0;
                    LocalUserSettings.getInstance(this).setScore(gameDifficulty, streakNum);
                    // streak lose dialog
                    Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setContentView(R.layout.streak_lose_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                    dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.setCancelable(true);
                    dialog.show();
                    // dialog views
                    retryTxt = dialog.findViewById(R.id.streakDialogRetryTxt);
                    cancelTxt = dialog.findViewById(R.id.streakDialogCancelTxt);
                    exitTxt = dialog.findViewById(R.id.streakDialogExitTxt);
                    streakDialogNumTxt = dialog.findViewById(R.id.streakDialogNumTxt);

                    streakDialogNumTxt.setText(streakNumString);
                    streakDialogNumTxt.setTextColor(ContextCompat.getColor(this, ScoreColors.getInstance().getColor(gameDifficulty, streakNum)));

                    retryTxt.setOnClickListener(j -> {
                        dialog.dismiss();
                        updateScore(0);
                        streakNumString = "" + LocalUserSettings.getInstance(this).getScore(gameDifficulty);
                        streakNumTxt.setText(streakNumString);
                        nextEquation();
                        refreshRadioGroup();
                        isSubmitted = false;
                    });

                    cancelTxt.setOnClickListener(j -> {
                        dialog.dismiss();
                        streakRetryBtn.setVisibility(View.VISIBLE);
                        streakRetryBtn.setEnabled(true);
                    });

                    exitTxt.setOnClickListener(j -> goBack());
                }
                disableChoices();
            }
        });

        streakRetryBtn.setOnClickListener(v -> {
            updateScore(0);
            streakRetryBtn.setVisibility(View.GONE);
            streakRetryBtn.setEnabled(false);
            nextEquation();
            refreshRadioGroup();
            isSubmitted = false;
        });

        streakNextBtn.setOnClickListener(v -> {
            isSubmitted = false;
            nextEquation();
            refreshRadioGroup();
            streakNextBtn.setEnabled(false);
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void disableChoices() {
        firstChoiceRadioButton.setEnabled(false);
        secondChoiceRadioButton.setEnabled(false);
        thirdChoiceRadioButton.setEnabled(false);
        fourthChoiceRadioButton.setEnabled(false);
    }

    private void refreshRadioGroup() {
        firstChoiceRadioButton.setTextColor(ContextCompat.getColor(this, R.color.white_shaded));
        secondChoiceRadioButton.setTextColor((ContextCompat.getColor(this, R.color.white_shaded)));
        thirdChoiceRadioButton.setTextColor((ContextCompat.getColor(this, R.color.white_shaded)));
        fourthChoiceRadioButton.setTextColor((ContextCompat.getColor(this, R.color.white_shaded)));
        choicesRadioGroup.clearCheck();
        firstChoiceRadioButton.setEnabled(true);
        secondChoiceRadioButton.setEnabled(true);
        thirdChoiceRadioButton.setEnabled(true);
        fourthChoiceRadioButton.setEnabled(true);
    }

    private void updateScore(int num) {
        // score color control
        streakNumTxt.setTextColor(ContextCompat.getColor(this, ScoreColors.getInstance().getColor(gameDifficulty, streakNum)));
        streakNumString = Integer.toString(num);
        streakNumTxt.setText(streakNumString);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.streak_with_space) + streakNumString);
        }
        LocalUserSettings.getInstance(this).setScore(gameDifficulty, num);
        LocalUserSettings.getInstance(this).setHighScore("Streak", num);
        if (isOnline()) {
            updateToDatabase(num);
        }
    }

    private void updateToDatabase(int num) {
        //for database
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            DbUpdateScore dbUpdateScore = new DbUpdateScore();
            String username = LocalUserSettings.getInstance(StreakActivity.this).getUsername();
            String password = LocalUserSettings.getInstance(StreakActivity.this).getPassword();
            if (username.equals("")) { // incase account was not created
                DbConnect dbConnect = new DbConnect();
                dbConnect.sendData("signup", username, password);
            }
            String newScoreString = "" + num;
            dbUpdateScore.sendData("0", gameDifficulty, username, newScoreString);
        });
    }

    private void nextEquation() {
        double firstRandomNum = Math.random();
        double secondRandomNum = Math.random();
        double operationRandomNum = Math.random() * 10;
        // operation
        if (streakNum <= 4 || streakNum == 7 || streakNum == 8 || streakNum == 11 || streakNum == 12) {
            if (operationRandomNum <= 5) {
                operationTxt.setText(R.string.addition);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.addition_cyan_green));
            } else {
                operationTxt.setText(R.string.subtraction);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.yellow_caryola));
            }
        } else if (streakNum < 21) {
            if (operationRandomNum <= 5) {
                operationTxt.setText(R.string.multiplication);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.multiplication_lilac));
            } else {
                operationTxt.setText(R.string.division);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.division_capri));
            }
        } else {
            if (operationRandomNum <= 2.5) {
                operationTxt.setText(R.string.addition);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.addition_cyan_green));
            } else if (operationRandomNum <= 5) {
                operationTxt.setText(R.string.subtraction);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.yellow_caryola));
            } else if (operationRandomNum <= 7.5) {
                operationTxt.setText(R.string.multiplication);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.multiplication_lilac));
            } else {
                operationTxt.setText(R.string.division);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.division_capri));
            }
        }
        operation = operationTxt.getText().toString();

        // numbers
        if (streakNum <= 2) {
            firstNum = (int) (firstRandomNum * 10);
            secondNum = (int) (secondRandomNum * 10);
        } else if (streakNum <= 4) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 100);
        } else if (streakNum <= 6) {
            firstNum = (int) (firstRandomNum * 10);
            secondNum = (int) (secondRandomNum * 10);
            divisionFixer(operation);
        } else if (streakNum <= 8) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 1000);
        } else if (streakNum <= 10) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 10);
            divisionFixer(operation);
        } else if (streakNum <= 12) {
            firstNum = (int) (firstRandomNum * 10000);
            secondNum = (int) (secondRandomNum * 10000);
        } else if (streakNum <= 14) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 100);
            divisionFixer(operation);
        } else if (streakNum <= 16) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 100);
            divisionFixer(operation);
        } else if (streakNum <= 18) {
            firstNum = (int) (firstRandomNum * 10000);
            secondNum = (int) (secondRandomNum * 100);
            divisionFixer(operation);
        } else if (streakNum <= 20) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 1000);
            divisionFixer(operation);
        } else {
            if (operation.equals("+") || operation.equals("-")) {
                firstNum = (int) (firstRandomNum * 10000);
                secondNum = (int) (secondRandomNum * 10000);
            } else {
                multiOrDivide = Math.random() * 10;
                if (multiOrDivide <= 5) {
                    firstNum = (int) (firstRandomNum * 10000);
                    secondNum = (int) (secondRandomNum * 100);
                    divisionFixer(operation);
                } else {
                    firstNum = (int) (firstRandomNum * 1000);
                    secondNum = (int) (secondRandomNum * 1000);
                    divisionFixer(operation);
                }
            }
        }

        if (firstNum >= 1000) {
            NumberFormat nf = NumberFormat.getInstance();
            firstNumString = nf.format(firstNum);
            secondNumString = nf.format(secondNum);
        } else {
            firstNumString = Integer.toString(firstNum);
            secondNumString = Integer.toString(secondNum);
        }

        if (secondNum > firstNum) {
            firstNumTxt.setText(secondNumString);
            secondNumTxt.setText(firstNumString);
        } else {
            firstNumTxt.setText(firstNumString);
            secondNumTxt.setText(secondNumString);
        }

        getCorrectAnswer();
        createChoices();
    }

    private void createChoices() {
        choices.clear();
        if (streakNum <= 2) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 10);
                if (correctAnswer > 10) {
                    choiceNum = choiceNum + 9;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 4) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 100);
                if (correctAnswer > 100) {
                    choiceNum = choiceNum + 99;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 10;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 6) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else {
                    choiceNum = (int) (Math.random() * 100);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 8) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 1000);
                if (correctAnswer > 1000) {
                    choiceNum = choiceNum + 999;
                } else if (correctAnswer < 100 && correctAnswer >= 10) {
                    choiceNum = choiceNum / 10;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 100;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 10) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else {
                    choiceNum = (int) (Math.random() * 1000);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 12) {
            while (choices.size() < 3) {
                int choiceNum;
                choiceNum = (int) (Math.random() * 10000);
                if (correctAnswer > 10000) {
                    choiceNum = choiceNum + 9999;
                } else if (correctAnswer <= 1000 && correctAnswer > 100) {
                    choiceNum = choiceNum / 10;
                } else if (correctAnswer <= 100 && correctAnswer >= 10) {
                    choiceNum = choiceNum / 100;
                } else if (correctAnswer <= 10) {
                    choiceNum = choiceNum / 1000;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 14) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else if (correctAnswer <= 500) {
                    choiceNum = (int) (Math.random() * 500);
                } else if (correctAnswer <= 1000) {
                    choiceNum = (int) (Math.random() * 1000);
                } else if (correctAnswer <= 2500) {
                    choiceNum = (int) (Math.random() * 2500);
                } else if (correctAnswer <= 5000) {
                    choiceNum = (int) (Math.random() * 5000);
                } else {
                    choiceNum = (int) (Math.random() * 10000);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 16) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else if (correctAnswer <= 1000) {
                    choiceNum = (int) (Math.random() * 1000);
                } else if (correctAnswer <= 2500) {
                    choiceNum = (int) (Math.random() * 2500);
                } else if (correctAnswer <= 5000) {
                    choiceNum = (int) (Math.random() * 5000);
                } else if (correctAnswer <= 7500) {
                    choiceNum = (int) (Math.random() * 7500);
                } else if (correctAnswer <= 10000) {
                    choiceNum = (int) (Math.random() * 10000);
                } else if (correctAnswer <= 20000) {
                    choiceNum = (int) (Math.random() * 20000);
                } else if (correctAnswer <= 40000) {
                    choiceNum = (int) (Math.random() * 40000);
                } else if (correctAnswer <= 60000) {
                    choiceNum = (int) (Math.random() * 60000);
                } else if (correctAnswer <= 80000) {
                    choiceNum = (int) (Math.random() * 80000);
                } else {
                    choiceNum = (int) (Math.random() * 100000);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (streakNum <= 20) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else if (correctAnswer <= 1000) {
                    choiceNum = (int) (Math.random() * 1000);
                } else if (correctAnswer <= 2500) {
                    choiceNum = (int) (Math.random() * 2500);
                } else if (correctAnswer <= 5000) {
                    choiceNum = (int) (Math.random() * 5000);
                } else if (correctAnswer <= 7500) {
                    choiceNum = (int) (Math.random() * 7500);
                } else if (correctAnswer <= 10000) {
                    choiceNum = (int) (Math.random() * 10000);
                } else if (correctAnswer <= 20000) {
                    choiceNum = (int) (Math.random() * 20000);
                } else if (correctAnswer <= 40000) {
                    choiceNum = (int) (Math.random() * 40000);
                } else if (correctAnswer <= 60000) {
                    choiceNum = (int) (Math.random() * 60000);
                } else if (correctAnswer <= 80000) {
                    choiceNum = (int) (Math.random() * 80000);
                } else if (correctAnswer <= 100000) {
                    choiceNum = (int) (Math.random() * 100000);
                } else if (correctAnswer <= 250000) {
                    choiceNum = (int) (Math.random() * 250000);
                } else if (correctAnswer <= 500000) {
                    choiceNum = (int) (Math.random() * 500000);
                } else {
                    choiceNum = (int) (Math.random() * 1000000);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else {
            while (choices.size() < 3) {
                int choiceNum;
                if (operation.equals("+") || operation.equals("-")) {
                    choiceNum = (int) (Math.random() * 10000);
                    if (correctAnswer > 10000) {
                        choiceNum = choiceNum + 9999;
                    } else if (correctAnswer <= 1000 && correctAnswer > 100) {
                        choiceNum = choiceNum / 10;
                    } else if (correctAnswer <= 100 && correctAnswer >= 10) {
                        choiceNum = choiceNum / 100;
                    } else if (correctAnswer <= 10) {
                        choiceNum = choiceNum / 1000;
                    }
                } else {
                    if (correctAnswer <= 10) {
                        choiceNum = (int) (Math.random() * 10);
                    } else if (correctAnswer <= 100) {
                        choiceNum = (int) (Math.random() * 100);
                    } else if (correctAnswer <= 1000) {
                        choiceNum = (int) (Math.random() * 1000);
                    } else if (correctAnswer <= 2500) {
                        choiceNum = (int) (Math.random() * 2500);
                    } else if (correctAnswer <= 5000) {
                        choiceNum = (int) (Math.random() * 5000);
                    } else if (correctAnswer <= 7500) {
                        choiceNum = (int) (Math.random() * 7500);
                    } else if (correctAnswer <= 10000) {
                        choiceNum = (int) (Math.random() * 10000);
                    } else if (correctAnswer <= 20000) {
                        choiceNum = (int) (Math.random() * 20000);
                    } else if (correctAnswer <= 40000) {
                        choiceNum = (int) (Math.random() * 40000);
                    } else if (correctAnswer <= 60000) {
                        choiceNum = (int) (Math.random() * 60000);
                    } else if (correctAnswer <= 80000) {
                        choiceNum = (int) (Math.random() * 80000);
                    } else if (correctAnswer <= 100000) {
                        choiceNum = (int) (Math.random() * 100000);
                    } else if (correctAnswer <= 250000) {
                        choiceNum = (int) (Math.random() * 250000);
                    } else if (correctAnswer <= 500000) {
                        choiceNum = (int) (Math.random() * 500000);
                    } else {
                        choiceNum = (int) (Math.random() * 1000000);
                    }
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        }
        // creating choices UI
        choices.add(correctAnswer);
        Collections.sort(choices);
        ArrayList<String> formattedNumbers = new ArrayList<>();
        if (correctAnswer >= 1000) {
            for (int i = 0; i < 4; i++) {
                NumberFormat nf = NumberFormat.getInstance();
                formattedNumbers.add(nf.format(choices.get(i)));
            }
            firstChoice = formattedNumbers.get(0);
            secondChoice = formattedNumbers.get(1);
            thirdChoice = formattedNumbers.get(2);
            fourthChoice = formattedNumbers.get(3);
        } else {
            firstChoice = Integer.toString(choices.get(0));
            secondChoice = Integer.toString(choices.get(1));
            thirdChoice = Integer.toString(choices.get(2));
            fourthChoice = Integer.toString(choices.get(3));
        }
        firstChoiceRadioButton.setText(firstChoice);
        secondChoiceRadioButton.setText(secondChoice);
        thirdChoiceRadioButton.setText(thirdChoice);
        fourthChoiceRadioButton.setText(fourthChoice);
    }

    private void getCorrectAnswer() {
        switch (operation) {
            case "+":
                correctAnswer = firstNum + secondNum;
                break;
            case "-":
                if (firstNum >= secondNum) {
                    correctAnswer = firstNum - secondNum;
                } else {
                    correctAnswer = secondNum - firstNum;
                }
                break;
            case "×":
                correctAnswer = firstNum * secondNum;
                break;
            default: // case divide ÷
                divisionFixer(operation);
                break;
        }
    }

    private void divisionFixer(String operation) {
        if (operation.equals("÷")) {
            if (firstNum > secondNum) {
                while (secondNum == 0) {
                    if (streakNum <= 10) {
                        secondNum = (int) (Math.random() * 10);
                    } else if (streakNum <= 18) {
                        secondNum = (int) (Math.random() * 100);
                    } else if (streakNum <= 20) {
                        secondNum = (int) (Math.random() * 1000);
                    } else {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                        } else {
                            secondNum = (int) (Math.random() * 1000);
                        }
                    }
                }
                while (firstNum % secondNum != 0) {
                    if (streakNum <= 10) {
                        secondNum = (int) (Math.random() * 10);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (streakNum <= 18) {
                        secondNum = (int) (Math.random() * 100);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 100);
                            }
                        }
                    } else if (streakNum <= 20) {
                        secondNum = (int) (Math.random() * 1000);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 1000);
                            }
                        }
                    } else {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            secondNum = (int) (Math.random() * 1000);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 1000);
                                }
                            }
                        }
                    }
                }
                correctAnswer = firstNum / secondNum;
            } else {
                while (firstNum == 0) {
                    if (streakNum <= 10) {
                        firstNum = (int) (Math.random() * 10);
                    } else if (streakNum <= 18) {
                        firstNum = (int) (Math.random() * 100);
                    } else if (streakNum <= 20) {
                        firstNum = (int) (Math.random() * 1000);
                    } else {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                        } else {
                            firstNum = (int) (Math.random() * 1000);
                        }
                    }
                }
                while (secondNum % firstNum != 0) {
                    if (streakNum <= 10) {
                        firstNum = (int) (Math.random() * 10);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (streakNum <= 18) {
                        firstNum = (int) (Math.random() * 100);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 100);
                            }
                        }
                    } else if (streakNum <= 20) {
                        firstNum = (int) (Math.random() * 1000);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 1000);
                            }
                        }
                    } else {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            firstNum = (int) (Math.random() * 1000);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 1000);
                                }
                            }
                        }
                    }
                }
                correctAnswer = secondNum / firstNum;
            }
        }
    }

    @Override
    public void findViews() {
        firstNumTxt = findViewById(R.id.streakFirstNumTxt);
        operationTxt = findViewById(R.id.streakOperationTxt);
        secondNumTxt = findViewById(R.id.streakSecondNumTxt);
        choicesRadioGroup = findViewById(R.id.streakChoicesRadioGroup);
        firstChoiceRadioButton = findViewById(R.id.streakFirstChoiceRadioBtn);
        secondChoiceRadioButton = findViewById(R.id.streakSecondChoiceRadioBtn);
        thirdChoiceRadioButton = findViewById(R.id.streakThirdChoiceRadioBtn);
        fourthChoiceRadioButton = findViewById(R.id.streakFourthChoiceRadioBtn);
        streakSubmitBtn = findViewById(R.id.streakSubmitBtn);
        streakNextBtn = findViewById(R.id.streakNextBtn);
        streakNumTxt = findViewById(R.id.streakNumTxt);
        streakRetryBtn = findViewById(R.id.streakRetryBtn);
        bonusStreakTxt = findViewById(R.id.bonusStreakTxt);
        streakLayout = findViewById(R.id.streakLayout);
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