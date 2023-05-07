package com.sirajsaleem.mathmanica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

public class QuickGameActivity extends AppCompatActivity implements MethodsFactory {

    private TextView firstNumTxt;
    private TextView operationTxt;
    private TextView secondNumTxt;
    private TextView levelNumTxt;
    private TextView scoreTxt;
    private TextView difficultyModeTxt;
    private TextView levelNumTitleTxt;
    private TextView bonusPointsTxt;
    private String currentScore;
    private String maxLevelReached;
    private String username;
    private String gameDifficulty;
    private String scoreString;
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
    private int levelNum;
    private int firstNum;
    private int secondNum;
    private int userScore;
    private Button submitBtn;
    private Button nextBtn;
    private double multiOrDivide;
    private ArrayList<Integer> choicesArray = new ArrayList<>();
    public static boolean isSubmitted;
    private ConstraintLayout quickGameLayout;
    private final ScoreColors scoreColors = new ScoreColors();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game);

        if (getIntent().getBooleanExtra("back", false)) {
            overridePendingTransition(R.anim.navigation_back_slide_in, R.anim.navigation_back_slide_out);
        }

        findViews();
        username = LocalUserSettings.getInstance(this).getUsername();

        gameDifficulty = LocalUserSettings.getInstance(this).getDifficulty();
        if (gameDifficulty.equals("Very_Easy")) {
            difficultyModeTxt.setText(R.string.very_easy);
        } else {
            difficultyModeTxt.setText(gameDifficulty);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.level_with_space) + LocalUserSettings.getInstance(this).getCurrentLevel(gameDifficulty)); // string + int
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String bonusPoints;
        switch (gameDifficulty) {
            case "Very_Easy":
                difficultyModeTxt.setTextColor(ContextCompat.getColor(this, R.color.aqua));
                levelNumTxt.setTextColor(ContextCompat.getColor(this, R.color.aqua));
                levelNumTitleTxt.setTextColor(ContextCompat.getColor(this, R.color.aqua));
                bonusPoints = getString(R.string.add_10);
                bonusPointsTxt.setTextColor(ContextCompat.getColor(this, R.color.aqua));
                break;
            case "Easy":
                difficultyModeTxt.setTextColor(Color.GREEN);
                levelNumTxt.setTextColor(ContextCompat.getColor(this, R.color.green_shaded));
                levelNumTitleTxt.setTextColor(ContextCompat.getColor(this, R.color.green_shaded));
                bonusPoints = getString(R.string.add_15);
                bonusPointsTxt.setTextColor(ContextCompat.getColor(this, R.color.green_shaded));
                break;
            case "Medium":
                difficultyModeTxt.setTextColor(Color.YELLOW);
                levelNumTxt.setTextColor(Color.YELLOW);
                levelNumTitleTxt.setTextColor(Color.YELLOW);
                bonusPoints = getString(R.string.add_20);
                bonusPointsTxt.setTextColor(Color.YELLOW);
                break;
            default: // Hard difficulty
                difficultyModeTxt.setTextColor(Color.RED);
                levelNumTxt.setTextColor(Color.RED);
                levelNumTitleTxt.setTextColor(Color.RED);
                bonusPoints = getString(R.string.add_25);
                bonusPointsTxt.setTextColor(Color.RED);
                break;
        }
        bonusPointsTxt.setText(bonusPoints);

        if (username.equals("unregistered")) {
            levelNum = LocalUserSettings.getInstance(this).getCurrentLevel(gameDifficulty);
            maxLevelReached = Integer.toString(levelNum);
            updateCurrentLevel();
        } else {
            if (isOnline()) {
                getLevelOrScoreFromDb("get-level");
                getLevelOrScoreFromDb("get-score");
            }
        }

        if (LocalUserSettings.getInstance(this).getOrientationControl()) {
            if (!isOnline()) {
                Snackbar.make(quickGameLayout, R.string.you_are_not_online, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.dismiss), v -> {

                        })
                        .show();
            }
            LocalUserSettings.getInstance(this).setOrientationControl(false);
            nextEquation();
        } else {
            firstNumTxt.setText(firstNumString);
            operationTxt.setText(operation);
            secondNumTxt.setText(secondNumString);
            firstChoiceRadioButton.setText(firstChoice);
            secondChoiceRadioButton.setText(secondChoice);
            thirdChoiceRadioButton.setText(thirdChoice);
            fourthChoiceRadioButton.setText(fourthChoice);
            if (isSubmitted) {
                submitBtn.setEnabled(false);
                nextBtn.setEnabled(true);
                disableChoices();
            }
        }
        getScore();

        choicesRadioGroup.setOnCheckedChangeListener((MultiLineRadioGroup.OnCheckedChangeListener) (group, button) -> {
            if (!isSubmitted) {
                submitBtn.setEnabled(true);
            }
        });


        submitBtn.setOnClickListener(v -> {
            isSubmitted = true;
            correctAnswerString = Integer.toString(correctAnswer);
            chosenRadioBtn = choicesRadioGroup.getCheckedRadioButtonText().toString();
            if (chosenRadioBtn.length() > 3) {
                chosenRadioBtn = chosenRadioBtn.replace(",", "");
            }
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

                if (!correctAnswerString.equals(chosenRadioBtn)) {
                    int wrongAnswerRadioBtn = choicesRadioGroup.getCheckedRadioButtonId();
                    if (wrongAnswerRadioBtn == R.id.firstChoiceRadioBtn) {
                        firstChoiceRadioButton.setTextColor(Color.RED);
                    } else if (wrongAnswerRadioBtn == R.id.secondChoiceRadioBtn) {
                        secondChoiceRadioButton.setTextColor(Color.RED);
                    } else if (wrongAnswerRadioBtn == R.id.thirdChoiceRadioBtn) {
                        thirdChoiceRadioButton.setTextColor(Color.RED);
                    } else {
                        fourthChoiceRadioButton.setTextColor(Color.RED);
                    }
                }

                if (correctAnswerString.equals(chosenRadioBtn)) {
                    levelNum = LocalUserSettings.getInstance(this).getCurrentLevel(gameDifficulty) + 1;
                    LocalUserSettings.getInstance(this).setCurrentLevel(gameDifficulty, levelNum);
                    bonusPointsTxt.setAlpha(0f);
                    bonusPointsTxt.setVisibility(View.VISIBLE);
                    bonusPointsTxt.animate()
                            .alpha(0.8f)
                            .setDuration(250)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    bonusPointsTxt.animate()
                                            .alpha(0f)
                                            .setDuration(500)
                                            .setStartDelay(500)
                                            .setListener(null);
                                }
                            });
                    updateScore(1);
                } else if (LocalUserSettings.getInstance(this).getScore(gameDifficulty) > 0) {
                    updateScore(-1);
                } else {
                    updateScore(0);
                }
                nextBtn.setEnabled(true);
                submitBtn.setEnabled(false);
                disableChoices();
            }
        });

        nextBtn.setOnClickListener(v -> {
            isSubmitted = false;
            // level number
            if (correctAnswerString.equals(chosenRadioBtn)) {
                updateCurrentLevel();
            }
            nextEquation();
            // removing choices colors
            firstChoiceRadioButton.setTextColor(ContextCompat.getColor(this, R.color.white_shaded));
            secondChoiceRadioButton.setTextColor((ContextCompat.getColor(this, R.color.white_shaded)));
            thirdChoiceRadioButton.setTextColor((ContextCompat.getColor(this, R.color.white_shaded)));
            fourthChoiceRadioButton.setTextColor((ContextCompat.getColor(this, R.color.white_shaded)));
            choicesRadioGroup.clearCheck();

            nextBtn.setEnabled(false);
            firstChoiceRadioButton.setEnabled(true);
            secondChoiceRadioButton.setEnabled(true);
            thirdChoiceRadioButton.setEnabled(true);
            fourthChoiceRadioButton.setEnabled(true);
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

    private void updateCurrentLevel() {
        String currentLevelString = Integer.toString(levelNum);
        levelNumTxt.setText(currentLevelString);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.level_with_space) + currentLevelString);
        }
    }

    private void nextEquation() {
        switch (gameDifficulty) {
            case "Very_Easy":
                veryEasyEquationGenerator();
                break;
            case "Easy":
                easyEquationGenerator();
                break;
            case "Medium":
                mediumEquationGenerator();
                break;
            default:
                HardEquationGenerator();
                break;
        }
    }

    private void HardEquationGenerator() {
        Equation equation = new Equation();
        Choices choices = new Choices();
        double firstRandomNum = Math.random();
        double secondRandomNum = Math.random();
        double operationRandomNum = Math.random() * 10;
        userScore = LocalUserSettings.getInstance(this).getScore(gameDifficulty);

        if (userScore < 875) {
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

        if (userScore < 125) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 10);
            if(equation.hardDivisionFixer(firstNum, operation, secondNum, userScore) != -1) {
                correctAnswer = equation.hardDivisionFixer(firstNum, operation, secondNum, userScore);
                firstNum = equation.getFirstNum();
                secondNum = equation.getSecondNum();
            }
        } else if (userScore < 250) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 10);
            if(equation.hardDivisionFixer(firstNum, operation, secondNum, userScore) != -1) {
                correctAnswer = equation.hardDivisionFixer(firstNum, operation, secondNum, userScore);
                firstNum = equation.getFirstNum();
                secondNum = equation.getSecondNum();
            }
        } else if (userScore < 500) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 100);
            if(equation.hardDivisionFixer(firstNum, operation, secondNum, userScore) != -1) {
                correctAnswer = equation.hardDivisionFixer(firstNum, operation, secondNum, userScore);
            }
            firstNum = equation.getFirstNum();
            secondNum = equation.getSecondNum();
        } else if (userScore < 875) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 100);
            if(equation.hardDivisionFixer(firstNum, operation, secondNum, userScore) != -1) {
                correctAnswer = equation.hardDivisionFixer(firstNum, operation, secondNum, userScore);
                firstNum = equation.getFirstNum();
                secondNum = equation.getSecondNum();
            }
        } else {
            if (operation.equals("+") || operation.equals("-")) {
                firstNum = (int) (firstRandomNum * 10000);
                secondNum = (int) (secondRandomNum * 10000);
            } else {
                multiOrDivide = Math.random() * 10;
                if (multiOrDivide <= 5) {
                    firstNum = (int) (firstRandomNum * 10000);
                    secondNum = (int) (secondRandomNum * 100);
                } else {
                    firstNum = (int) (firstRandomNum * 1000);
                    secondNum = (int) (secondRandomNum * 1000);
                }
                if(equation.hardDivisionFixer(firstNum, operation, secondNum, userScore) != -1) {
                    correctAnswer = equation.hardDivisionFixer(firstNum, operation, secondNum, userScore);
                    firstNum = equation.getFirstNum();
                    secondNum = equation.getSecondNum();
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

        correctAnswer = equation.getAnswer(firstNum, operation, secondNum, gameDifficulty, userScore);
        choicesArray = choices.createHardChoices(correctAnswer, userScore, operation);
        createChoices();
    }

    private void mediumEquationGenerator() {
        Equation equation = new Equation();
        Choices choices = new Choices();
        double firstRandomNum = Math.random();
        double secondRandomNum = Math.random();
        double operationRandomNum = Math.random() * 10;
        userScore = LocalUserSettings.getInstance(this).getScore(gameDifficulty);
        // equation
        if (userScore < 200) {
            if (operationRandomNum <= 5) {
                operationTxt.setText(R.string.addition);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.addition_cyan_green));
            } else {
                operationTxt.setText(R.string.subtraction);
                operationTxt.setTextColor(ContextCompat.getColor(this, R.color.yellow_caryola));
            }
        } else if (userScore < 1700) {
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

        if (userScore < 100) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 100);
        } else if (userScore < 200) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 1000);
        } else if (userScore < 500) {
            firstNum = (int) (firstRandomNum * 10);
            secondNum = (int) (secondRandomNum * 10);
            if (equation.mediumDivisionFixer(firstNum, operation, secondNum, userScore) != -1) {
                correctAnswer = equation.mediumDivisionFixer(firstNum, operation, secondNum, userScore);
                firstNum = equation.getFirstNum();
                secondNum = equation.getSecondNum();
            }
        } else if (userScore < 1000) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 10);
            if (equation.mediumDivisionFixer(firstNum, operation, secondNum, userScore) != -1) {
                correctAnswer = equation.mediumDivisionFixer(firstNum, operation, secondNum, userScore);
                firstNum = equation.getFirstNum();
                secondNum = equation.getSecondNum();
            }
        } else if (userScore < 1500) {
            multiOrDivide = Math.random() * 10;
            if (multiOrDivide <= 5) {
                firstNum = (int) (firstRandomNum * 1000);
                secondNum = (int) (secondRandomNum * 10);
            } else {
                firstNum = (int) (firstRandomNum * 1000);
                secondNum = (int) (secondRandomNum * 100);
            }
            if (equation.mediumDivisionFixer(firstNum, operation, secondNum, userScore) != -1) {
                correctAnswer = equation.mediumDivisionFixer(firstNum, operation, secondNum, userScore);
                firstNum = equation.getFirstNum();
                secondNum = equation.getSecondNum();
            }
        } else if (userScore < 1700) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 100);
            if (equation.mediumDivisionFixer(firstNum, operation, secondNum, userScore) != -1) {
                correctAnswer = equation.mediumDivisionFixer(firstNum, operation, secondNum, userScore);
                firstNum = equation.getFirstNum();
                secondNum = equation.getSecondNum();
            }
        } else {
            if (operation.equals("+") || operation.equals("-")) {
                firstNum = (int) (firstRandomNum * 1000);
                secondNum = (int) (secondRandomNum * 1000);
            } else {
                multiOrDivide = Math.random() * 10;
                if (multiOrDivide <= 5) {
                    firstNum = (int) (firstRandomNum * 100);
                    secondNum = (int) (secondRandomNum * 100);
                } else {
                    firstNum = (int) (firstRandomNum * 1000);
                    secondNum = (int) (secondRandomNum * 10);
                }
                if (equation.mediumDivisionFixer(firstNum, operation, secondNum, userScore) != -1) {
                    correctAnswer = equation.mediumDivisionFixer(firstNum, operation, secondNum, userScore);
                    firstNum = equation.getFirstNum();
                    secondNum = equation.getSecondNum();
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

        correctAnswer = equation.getAnswer(firstNum, operation, secondNum, gameDifficulty, userScore);
        choicesArray = choices.createMediumChoices(correctAnswer, userScore, operation);
        createChoices();
    }

    private void easyEquationGenerator() {
        Equation equation = new Equation();
        Choices choices = new Choices();
        double firstRandomNum = Math.random();
        double secondRandomNum = Math.random();
        double operationRandomNum = Math.random() * 10;
        // equation
        if (operationRandomNum <= 5) {
            operationTxt.setText(R.string.addition);
            operationTxt.setTextColor(ContextCompat.getColor(this, R.color.addition_cyan_green));
        } else {
            operationTxt.setText(R.string.subtraction);
            operationTxt.setTextColor(ContextCompat.getColor(this, R.color.yellow_caryola));
        }
        operation = operationTxt.getText().toString();

        userScore = LocalUserSettings.getInstance(this).getScore(gameDifficulty);
        if (userScore < 150) {
            firstNum = (int) (firstRandomNum * 10);
            secondNum = (int) (secondRandomNum * 10);
        } else if (userScore < 375) {
            firstNum = (int) (firstRandomNum * 20);
            secondNum = (int) (secondRandomNum * 20);
        } else if (userScore < 750) {
            firstNum = (int) (firstRandomNum * 50);
            secondNum = (int) (secondRandomNum * 50);
        } else if (userScore < 1125) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 100);
        } else if (userScore < 1350) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 100);
        } else {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 1000);
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

        correctAnswer = equation.getAnswer(firstNum, operation, secondNum, gameDifficulty, userScore);
        choicesArray = choices.createEasyChoices(correctAnswer, userScore);
        createChoices();
    }


    private void getScore() {
        userScore = LocalUserSettings.getInstance(this).getScore(gameDifficulty);
        scoreTxt.setTextColor(ContextCompat.getColor(this, scoreColors.getColor(gameDifficulty, userScore)));
        scoreString = Integer.toString(userScore);
        scoreTxt.setText(scoreString);
    }

    private void updateScore(int addSubtract) {
        int newScore = LocalUserSettings.getInstance(this).getScore(gameDifficulty);
        if (addSubtract == 1) {
            switch (gameDifficulty) {
                case "Very_Easy":
                    newScore = newScore + 10;
                    break;
                case "Easy":
                    newScore = newScore + 15;
                    break;
                case "Medium":
                    newScore = newScore + 20;
                    break;
                default: // "Hard"
                    newScore = newScore + 25;
                    break;
            }
        } else if (addSubtract == -1) {
            switch (gameDifficulty) {
                case "Very_Easy":
                    newScore = newScore - 10;
                    break;
                case "Easy":
                    newScore = newScore - 15;
                    break;
                case "Medium":
                    newScore = newScore - 20;
                    break;
                default: // "Hard"
                    newScore = newScore - 25;
                    break;
            }
        }
        // score color control
        scoreTxt.setTextColor(ContextCompat.getColor(this, scoreColors.getColor(gameDifficulty, userScore)));
        scoreString = Integer.toString(newScore);
        scoreTxt.setText(scoreString);
        LocalUserSettings.getInstance(this).setScore(gameDifficulty, newScore);
        if (isOnline()) {
            uploadToDatabase(newScore);
        }
    }

    private void getLevelOrScoreFromDb(String type) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            DbGetLevelAndScore dbGetLevelAndScore = new DbGetLevelAndScore();
            if (type.equals("get-level")) {
                maxLevelReached = dbGetLevelAndScore.sendData(gameDifficulty, username, type);
                levelNum = Integer.parseInt(maxLevelReached);
                LocalUserSettings.getInstance(QuickGameActivity.this).setCurrentLevel(gameDifficulty, levelNum);
            } else {
                currentScore = dbGetLevelAndScore.sendData(gameDifficulty, username, type);
                LocalUserSettings.getInstance(QuickGameActivity.this).setScore(gameDifficulty, Integer.parseInt(currentScore));
            }

            runOnUiThread(() -> {
                levelNumTxt.setText(maxLevelReached);
                scoreTxt.setText(currentScore);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.level_with_space) + maxLevelReached);
                }
            });
        });
    }

    private void uploadToDatabase(int newScore) {
        //for database
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            DbUpdateScore dbUpdateScore = new DbUpdateScore();
            String password = LocalUserSettings.getInstance(QuickGameActivity.this).getPassword();
            if (username.equals("")) { // incase account was not created
                DbConnect dbConnect = new DbConnect();
                dbConnect.sendData("signup", username, password);
            }
            String newScoreString = "" + newScore;
            String levelNumber = "" + levelNum;
            dbUpdateScore.sendData(levelNumber, gameDifficulty, username, newScoreString);
        });
    }

    private void veryEasyEquationGenerator() {
        Equation equation = new Equation();
        Choices choices = new Choices();
        double firstRandomNum = Math.random();
        double secondRandomNum = Math.random();
        double operationRandomNum = Math.random() * 10;
        userScore = LocalUserSettings.getInstance(this).getScore(gameDifficulty); // to control score color
        // equation
        if (operationRandomNum <= 5) {
            operationTxt.setText(R.string.addition);
            operationTxt.setTextColor(ContextCompat.getColor(this, R.color.addition_cyan_green));
        } else {
            operationTxt.setText(R.string.subtraction);
            operationTxt.setTextColor(ContextCompat.getColor(this, R.color.yellow_caryola));
        }

        firstNum = (int) (firstRandomNum * 10);
        secondNum = (int) (secondRandomNum * 10);
        firstNumString = Integer.toString(firstNum);
        secondNumString = Integer.toString(secondNum);
        operation = operationTxt.getText().toString();
        if (secondNum > firstNum) {
            firstNumTxt.setText(secondNumString);
            secondNumTxt.setText(firstNumString);
        } else {
            firstNumTxt.setText(firstNumString);
            secondNumTxt.setText(secondNumString);
        }
        correctAnswer = equation.getAnswer(firstNum, operation, secondNum, gameDifficulty, userScore);
        choicesArray = choices.createVeryEasyChoices(correctAnswer);
        createChoices();
    }

    private void createChoices() {
        choicesArray.add(correctAnswer);
        Collections.sort(choicesArray);
        ArrayList<String> formattedNumbers = new ArrayList<>();
        if (correctAnswer >= 1000) {
            for (int i = 0; i < 4; i++) {
                NumberFormat nf = NumberFormat.getInstance();
                formattedNumbers.add(nf.format(choicesArray.get(i)));
            }
            firstChoice = formattedNumbers.get(0);
            secondChoice = formattedNumbers.get(1);
            thirdChoice = formattedNumbers.get(2);
            fourthChoice = formattedNumbers.get(3);
        } else {
            firstChoice = Integer.toString(choicesArray.get(0));
            secondChoice = Integer.toString(choicesArray.get(1));
            thirdChoice = Integer.toString(choicesArray.get(2));
            fourthChoice = Integer.toString(choicesArray.get(3));
        }
        firstChoiceRadioButton.setText(firstChoice);
        secondChoiceRadioButton.setText(secondChoice);
        thirdChoiceRadioButton.setText(thirdChoice);
        fourthChoiceRadioButton.setText(fourthChoice);
    }

    @Override
    public void findViews() {
        firstNumTxt = findViewById(R.id.firstNumTxt);
        operationTxt = findViewById(R.id.operationTxt);
        secondNumTxt = findViewById(R.id.secondNumTxt);
        levelNumTxt = findViewById(R.id.levelNumTxt);
        choicesRadioGroup = findViewById(R.id.choicesRadioGroup);
        firstChoiceRadioButton = findViewById(R.id.firstChoiceRadioBtn);
        secondChoiceRadioButton = findViewById(R.id.secondChoiceRadioBtn);
        thirdChoiceRadioButton = findViewById(R.id.thirdChoiceRadioBtn);
        fourthChoiceRadioButton = findViewById(R.id.fourthChoiceRadioBtn);
        submitBtn = findViewById(R.id.submitBtn);
        scoreTxt = findViewById(R.id.scoreTxt);
        difficultyModeTxt = findViewById(R.id.difficultyModeTxt);
        nextBtn = findViewById(R.id.nextBtn);
        levelNumTitleTxt = findViewById(R.id.levelNumTitle);
        bonusPointsTxt = findViewById(R.id.quickGameBonusTxt);
        quickGameLayout = findViewById(R.id.quickGameLayout);
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