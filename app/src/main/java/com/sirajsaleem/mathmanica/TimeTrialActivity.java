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
import android.os.CountDownTimer;
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

public class TimeTrialActivity extends AppCompatActivity implements MethodsFactory {
    private TextView firstNumTxt;
    private TextView operationTxt;
    private TextView secondNumTxt;
    private TextView hoursTxt;
    private TextView minutesTxt;
    private TextView secondsTxt;
    private TextView bonusTimeTxt;
    private TextView timeTrialScoreTxt;
    private TextView firstColonTxt;
    private TextView secondColonTxt;
    private final String gameDifficulty = "Time_Trial";
    private String timeTrialNumString;
    private static String correctAnswerString;
    private String chosenRadioBtn;
    public static String hoursString;
    public static String minutesString;
    public static String secondsString;
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
    private int timeTrialScore;
    private int firstNum;
    private int secondNum;
    private static int hoursNum;
    private static int minutesNum;
    private static int secondsNum;
    private static long counter;
    private static long currentCount;
    private Button startBtn;
    private Button submitBtn;
    private Button nextBtn;
    private double multiOrDivide;
    private final ArrayList<Integer> choices = new ArrayList<>();
    private CountDownTimer timeTrialCounter;
    public static boolean isSubmitted;
    public static boolean isCounting;
    public static boolean isStarted;
    public static boolean isCounterSwitch;
    public static boolean isEndRun;
    private ConstraintLayout timeTrialLayout;
    private final ScoreColors scoreColors = new ScoreColors();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_trial);

        if (getIntent().getBooleanExtra("back", false)) {
            overridePendingTransition(R.anim.navigation_back_slide_in, R.anim.navigation_back_slide_out);
        }

        findViews();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.time_trial);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        choicesRadioGroup.setOnCheckedChangeListener((MultiLineRadioGroup.OnCheckedChangeListener) (group, button) -> {
            if (isStarted) {
                submitBtn.setEnabled(true);
            }
        });

        // to String
        hoursString = hoursTxt.getText().toString();
        minutesString = minutesTxt.getText().toString();
        secondsString = secondsTxt.getText().toString();
        // to int
        hoursNum = Integer.parseInt(hoursString);
        minutesNum = Integer.parseInt(minutesString);
        secondsNum = Integer.parseInt(secondsString);

        startBtn.setOnClickListener(v -> startBtnClicked());

        submitBtn.setOnClickListener(v -> {
            isSubmitted = true;
            submitBtn.setEnabled(false);
            isCounterSwitch = false;
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
                    if (wrongAnswerRadioBtn == R.id.timeTrialFirstChoiceRadioBtn) {
                        firstChoiceRadioButton.setTextColor(Color.RED);
                    } else if (wrongAnswerRadioBtn == R.id.timeTrialSecondChoiceRadioBtn) {
                        secondChoiceRadioButton.setTextColor(Color.RED);
                    } else if (wrongAnswerRadioBtn == R.id.timeTrialThirdChoiceRadioBtn) {
                        thirdChoiceRadioButton.setTextColor(Color.RED);
                    } else {
                        fourthChoiceRadioButton.setTextColor(Color.RED);
                    }
                }

                timeTrialScore = LocalUserSettings.getInstance(this).getScore(gameDifficulty);
                if (correctAnswerString.equals(chosenRadioBtn)) {
                    bonusTimeTxt.setAlpha(0f);
                    bonusTimeTxt.setVisibility(View.VISIBLE);
                    bonusTimeTxt.animate()
                            .alpha(0.8f)
                            .setDuration(250)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    bonusTimeTxt.animate()
                                            .alpha(0f)
                                            .setDuration(500)
                                            .setStartDelay(500)
                                            .setListener(null);
                                }
                            });
                    timeTrialScore = timeTrialScore + 10;
                    updateLocalSettings(timeTrialScore);
                    counter = currentCount + 30000; //added milliseconds
                } else {
                    if (timeTrialScore > 0) {
                        timeTrialScore = timeTrialScore - 10;
                        updateLocalSettings(timeTrialScore);
                    }
                    counter = currentCount;
                }
                timeTrialCounter.cancel();
                isCounting = false;
                currentCount = counter;
                nextBtn.setEnabled(true);
                disableRadioBtns();
                updateTimer(counter);
                updateScore(timeTrialScore);
                isStarted = false;
            }
        });

        nextBtn.setOnClickListener(v -> {
            nextEquation();
            if (!isCounting) {
                createCountDown();
            }
            refreshRadioGroup();
            nextBtn.setEnabled(false);
            submitBtn.setEnabled(false);
            isStarted = true;
            isCounterSwitch = true;
            isSubmitted = false;
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void updateLocalSettings(int timeTrialScore) {
        LocalUserSettings.getInstance(this).setScore(gameDifficulty, timeTrialScore);
        LocalUserSettings.getInstance(this).setHighScore("Time_Trial", timeTrialScore);
    }

    private void updateTimer(long timeLeft) {
        secondsNum = (int) (Math.floor((timeLeft / 1000.0) % 60));
        minutesNum = (int) (Math.floor((timeLeft / (1000.0 * 60)) % 60));
        hoursNum = (int) (Math.floor((timeLeft / (1000.0 * 60 * 60)) % 60));
        if (secondsNum < 10) {
            secondsString = "0" + secondsNum;
        } else {
            secondsString = "" + secondsNum;
        }
        if (minutesNum < 10) {
            minutesString = "0" + minutesNum;
        } else {
            minutesString = "" + minutesNum;
        }
        if (hoursNum < 10) {
            hoursString = "0" + hoursNum;
        } else {
            hoursString = "" + hoursNum;
        }

        secondsTxt.setText(secondsString);
        minutesTxt.setText(minutesString);
        hoursTxt.setText(hoursString);
    }

    private void disableRadioBtns() {
        firstChoiceRadioButton.setEnabled(false);
        secondChoiceRadioButton.setEnabled(false);
        thirdChoiceRadioButton.setEnabled(false);
        fourthChoiceRadioButton.setEnabled(false);
    }

    private void updateScore(int num) {
        timeTrialScoreTxt.setTextColor(ContextCompat.getColor(this, scoreColors.getColor(gameDifficulty, timeTrialScore)));
        if (isStarted) {
            setColor();
        }
        timeTrialNumString = Integer.toString(num);
        timeTrialScoreTxt.setText(timeTrialNumString);
        if (isOnline()) {
            updateToDatabase(num);
        }
    }

    private void updateToDatabase(int num) {
        //for database
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            DbUpdateScore dbUpdateScore = new DbUpdateScore();
            String username = LocalUserSettings.getInstance(TimeTrialActivity.this).getUsername();
            String password = LocalUserSettings.getInstance(TimeTrialActivity.this).getPassword();
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
        if (timeTrialScore <= 80 || (timeTrialScore >= 130 && timeTrialScore <= 160) || (timeTrialScore >= 210 && timeTrialScore <= 240)) {
            if (operationRandomNum <= 5) {
                operationTxt.setText(R.string.addition);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.addition_cyan_green));
            } else {
                operationTxt.setText(R.string.subtraction);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.yellow_caryola));
            }
        } else if (timeTrialScore < 410) {
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
        if (timeTrialScore <= 40) {
            firstNum = (int) (firstRandomNum * 10);
            secondNum = (int) (secondRandomNum * 10);
        } else if (timeTrialScore <= 80) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 100);
        } else if (timeTrialScore <= 120) {
            firstNum = (int) (firstRandomNum * 10);
            secondNum = (int) (secondRandomNum * 10);
            divisionFixer(operation);
        } else if (timeTrialScore <= 160) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 1000);
        } else if (timeTrialScore <= 200) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 10);
            divisionFixer(operation);
        } else if (timeTrialScore <= 240) {
            firstNum = (int) (firstRandomNum * 10000);
            secondNum = (int) (secondRandomNum * 10000);
        } else if (timeTrialScore <= 280) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 100);
            divisionFixer(operation);
        } else if (timeTrialScore <= 320) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 100);
            divisionFixer(operation);
        } else if (timeTrialScore <= 360) {
            firstNum = (int) (firstRandomNum * 10000);
            secondNum = (int) (secondRandomNum * 100);
            divisionFixer(operation);
        } else if (timeTrialScore <= 400) {
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
        if (timeTrialScore <= 40) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 10);
                if (correctAnswer > 10) {
                    choiceNum = choiceNum + 9;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (timeTrialScore <= 80) {
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
        } else if (timeTrialScore <= 120) {
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
        } else if (timeTrialScore <= 160) {
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
        } else if (timeTrialScore <= 200) {
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
        } else if (timeTrialScore <= 240) {
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
        } else if (timeTrialScore <= 280) {
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
        } else if (timeTrialScore <= 320) {
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
        } else if (timeTrialScore <= 400) {
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
                    if (timeTrialScore <= 200) {
                        secondNum = (int) (Math.random() * 10);
                    } else if (timeTrialScore <= 360) {
                        secondNum = (int) (Math.random() * 100);
                    } else if (timeTrialScore <= 400) {
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
                    if (timeTrialScore <= 200) {
                        secondNum = (int) (Math.random() * 10);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (timeTrialScore <= 360) {
                        secondNum = (int) (Math.random() * 100);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 100);
                            }
                        }
                    } else if (timeTrialScore <= 400) {
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
                    if (timeTrialScore <= 200) {
                        firstNum = (int) (Math.random() * 10);
                    } else if (timeTrialScore <= 360) {
                        firstNum = (int) (Math.random() * 100);
                    } else if (timeTrialScore <= 400) {
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
                    if (timeTrialScore <= 200) {
                        firstNum = (int) (Math.random() * 10);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (timeTrialScore <= 360) {
                        firstNum = (int) (Math.random() * 100);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 100);
                            }
                        }
                    } else if (timeTrialScore <= 400) {
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

    private void createCountDown() {
        isStarted = true;
        isCounterSwitch = true;
        isCounting = true;
        timeTrialCounter = new CountDownTimer(counter, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                updateTimer(millisUntilFinished);
                setColor();

                currentCount = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                isSubmitted = false;
                // time trial lose dialog
                timeIsUpDialog();
            }
        }.start();
    }

    private void setColor() {
        if (hoursNum == 0 && minutesNum == 0 && secondsNum < 30) {
            hoursTxt.setTextColor(Color.RED);
            minutesTxt.setTextColor(Color.RED);
            secondsTxt.setTextColor(Color.RED);
            firstColonTxt.setTextColor(Color.RED);
            secondColonTxt.setTextColor(Color.RED);
        } else if (minutesNum < 2) {
            hoursTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.yellow_shaded));
            minutesTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.yellow_shaded));
            secondsTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.yellow_shaded));
            firstColonTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.yellow_shaded));
            secondColonTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.yellow_shaded));
        } else if (minutesNum < 4) {
            hoursTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.green_shaded));
            minutesTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.green_shaded));
            secondsTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.green_shaded));
            firstColonTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.green_shaded));
            secondColonTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.green_shaded));
        } else if (minutesNum < 6) {
            hoursTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.aqua));
            minutesTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.aqua));
            secondsTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.aqua));
            firstColonTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.aqua));
            secondColonTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, R.color.aqua));
        }
    }

    private void timeIsUpDialog() {
        isEndRun = true;
        secondNum = 0;
        minutesNum = 1;
        hoursNum = 0;
        Dialog dialog = new Dialog(TimeTrialActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setContentView(R.layout.streak_lose_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.show();
        // dialog views
        TextView retryTxt = dialog.findViewById(R.id.streakDialogRetryTxt);
        TextView cancelTxt = dialog.findViewById(R.id.streakDialogCancelTxt);
        TextView exitTxt = dialog.findViewById(R.id.streakDialogExitTxt);
        TextView streakDialogNumTxt = dialog.findViewById(R.id.streakDialogNumTxt);

        streakDialogNumTxt.setText(timeTrialNumString);
        streakDialogNumTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, scoreColors.getColor(gameDifficulty, timeTrialScore)));

        retryTxt.setOnClickListener(j -> {
            dialog.dismiss();
            startBtn.setText(R.string.retry);
            disableRadioBtns();
            choicesRadioGroup.clearCheck();
            submitBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            isEndRun = false;
            startBtnClicked();
        });

        cancelTxt.setOnClickListener(j -> {
            dialog.dismiss();
            startBtn.setText(R.string.retry);
            disableRadioBtns();
            submitBtn.setEnabled(false);
            nextBtn.setEnabled(false);
        });

        exitTxt.setOnClickListener(j -> goBack());
    }

    private void startBtnClicked() {
        if (startBtn.getText().toString().equals("Start") || startBtn.getText().toString().equals("Retry")) {
            LocalUserSettings.getInstance(this).setScore(gameDifficulty, 0);
            isStarted = true;
            isEndRun = false;
            timeTrialNumString = "" + LocalUserSettings.getInstance(this).getScore(gameDifficulty);
            timeTrialScoreTxt.setText(timeTrialNumString);
            choicesRadioGroup.clearCheck();
            submitBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            nextEquation();
            refreshRadioGroup();
            counter = 60000;
            createCountDown();
            startBtn.setText(R.string.end_run);

        } else {
            if (isCounting) {
                timeTrialCounter.cancel();
                isCounting = false;
            }
            counter = currentCount;
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setContentView(R.layout.time_trial_end_run_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setCancelable(false);
            dialog.show();
            //dialog views
            TextView endRunDialogYesTxt = dialog.findViewById(R.id.timeTrialEndRunDialogYesTxt);
            TextView endRunDialogNoTxt = dialog.findViewById(R.id.timeTrialEndRunDialogNoTxt);
            TextView endRunDialogScoreTxt = dialog.findViewById(R.id.timeTrialEndRunDialogScoreTxt);

            endRunDialogScoreTxt.setText(timeTrialNumString);
            endRunDialogScoreTxt.setTextColor(ContextCompat.getColor(TimeTrialActivity.this, scoreColors.getColor(gameDifficulty, timeTrialScore)));

            endRunDialogYesTxt.setOnClickListener(j -> {
                dialog.dismiss();
                timeIsUpDialog();
            });

            endRunDialogNoTxt.setOnClickListener(j -> {
                dialog.dismiss();
                if (!isSubmitted) {
                    createCountDown();
                }
            });
        }
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

    @Override
    public void findViews() {
        hoursTxt = findViewById(R.id.hoursTxt);
        minutesTxt = findViewById(R.id.minutesTxt);
        secondsTxt = findViewById(R.id.secondsTxt);
        startBtn = findViewById(R.id.timeTrialStartBtn);
        submitBtn = findViewById(R.id.timeTrialSubmitBtn);
        timeTrialScoreTxt = findViewById(R.id.timeTrialScoreNumTxt);
        firstNumTxt = findViewById(R.id.timeTrialFirstNumTxt);
        secondNumTxt = findViewById(R.id.timeTrialSecondNumTxt);
        operationTxt = findViewById(R.id.timeTrialOperationTxt);
        choicesRadioGroup = findViewById(R.id.timeTrialChoicesRadioGroup);
        firstChoiceRadioButton = findViewById(R.id.timeTrialFirstChoiceRadioBtn);
        secondChoiceRadioButton = findViewById(R.id.timeTrialSecondChoiceRadioBtn);
        thirdChoiceRadioButton = findViewById(R.id.timeTrialThirdChoiceRadioBtn);
        fourthChoiceRadioButton = findViewById(R.id.timeTrialFourthChoiceRadioBtn);
        nextBtn = findViewById(R.id.timeTrialNextBtn);
        firstColonTxt = findViewById(R.id.firstColon);
        secondColonTxt = findViewById(R.id.secondColon);
        bonusTimeTxt = findViewById(R.id.bonusTimeTxt);
        timeTrialLayout = findViewById(R.id.timeTrialLayout);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (isCounterSwitch) {
            timeTrialCounter.cancel();
            isCounterSwitch = false;
        }
        isCounting = false;
        LocalUserSettings.getInstance(this).setTimeTrialTimer(secondsString, minutesString, hoursString, currentCount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LocalUserSettings.getInstance(this).getOrientationControl()) {
            nextEquation();
            updateScore(0);
            LocalUserSettings.getInstance(this).setOrientationControl(false);
            disableRadioBtns();
            if (!isOnline()) {
                Snackbar.make(timeTrialLayout, R.string.you_are_not_online, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.dismiss, v -> {

                        })
                        .show();
            }
        } else {
            updateScore(LocalUserSettings.getInstance(this).getScore(gameDifficulty));
            secondsTxt.setText(secondsString);
            minutesTxt.setText(minutesString);
            hoursTxt.setText(hoursString);
            firstNumTxt.setText(firstNumString);
            operationTxt.setText(operation);
            secondNumTxt.setText(secondNumString);
            firstChoiceRadioButton.setText(firstChoice);
            secondChoiceRadioButton.setText(secondChoice);
            thirdChoiceRadioButton.setText(thirdChoice);
            fourthChoiceRadioButton.setText(fourthChoice);
            if (isStarted) {
                counter = LocalUserSettings.getInstance(this).getCurrentCount();
                if (!isCounting && !isEndRun) {
                    createCountDown();
                    timeTrialCounter.start();
                    startBtn.setText(R.string.end_run);
                }
            }
            if (isSubmitted) {
                startBtn.setText(R.string.end_run);
                nextBtn.setEnabled(true);
                disableRadioBtns();
            }
            if (isEndRun) {
                setColor();
            }
        }
    }
}