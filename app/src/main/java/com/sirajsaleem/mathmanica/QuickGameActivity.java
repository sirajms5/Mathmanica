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
    private final ArrayList<Integer> choices = new ArrayList<>();
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
            default:
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
            hardDivisionFixer(operation);
        } else if (userScore < 250) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 10);
            hardDivisionFixer(operation);
        } else if (userScore < 500) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 100);
            hardDivisionFixer(operation);
        } else if (userScore < 875) {
            firstNum = (int) (firstRandomNum * 1000);
            secondNum = (int) (secondRandomNum * 100);
            hardDivisionFixer(operation);
        } else {
            if (operation.equals("+") || operation.equals("-")) {
                firstNum = (int) (firstRandomNum * 10000);
                secondNum = (int) (secondRandomNum * 10000);
            } else {
                multiOrDivide = Math.random() * 10;
                if (multiOrDivide <= 5) {
                    firstNum = (int) (firstRandomNum * 10000);
                    secondNum = (int) (secondRandomNum * 100);
                    hardDivisionFixer(operation);
                } else {
                    firstNum = (int) (firstRandomNum * 1000);
                    secondNum = (int) (secondRandomNum * 1000);
                    hardDivisionFixer(operation);
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
        createHardChoices(userScore);
    }

    private void createHardChoices(int userScore) {
        choices.clear();
        if (userScore < 125) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 10) {
                    choiceNum = (int) (Math.random() * 10);
                } else if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else if (correctAnswer < 400) {
                    choiceNum = (int) (Math.random() * 400);
                } else {
                    choiceNum = (int) (Math.random() * 900);
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 500) {
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
        } else if (userScore < 875) {
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
        } else {
            while (choices.size() < 3) {
                int choiceNum;
                if (operation.equals("+") || operation.equals("-")) {
                    choiceNum = (int) (Math.random() * 10000);
                    if (correctAnswer > 10000) {
                        choiceNum = choiceNum + 9999;
                    } else if (correctAnswer <= 1000) {
                        choiceNum = choiceNum / 10;
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
        createChoices();
    }

    private void hardDivisionFixer(String operation) {

        if (operation.equals("÷")) {
            if (firstNum > secondNum) {
                while (secondNum == 0) {
                    if (userScore < 250) {
                        secondNum = (int) (Math.random() * 10);
                    } else if (userScore < 875) {
                        secondNum = (int) (Math.random() * 100);
                    } else {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                        } else {
                            secondNum = (int) (Math.random() * 1000);
                        }
                    }
                }
                while (firstNum % secondNum != 0) {
                    if (userScore < 250) {
                        secondNum = (int) (Math.random() * 10);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (userScore < 875) {
                        secondNum = (int) (Math.random() * 100);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 100);
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
                    if (userScore < 250) {
                        firstNum = (int) (Math.random() * 10);
                    } else if (userScore < 875) {
                        firstNum = (int) (Math.random() * 100);
                    } else {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                        } else {
                            firstNum = (int) (Math.random() * 1000);
                        }
                    }
                }
                while (secondNum % firstNum != 0) {
                    if (userScore < 250) {
                        firstNum = (int) (Math.random() * 10);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (userScore < 875) {
                        firstNum = (int) (Math.random() * 100);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 100);
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

    private void mediumEquationGenerator() {
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
            mediumDivisionFixer(operation);
        } else if (userScore < 1000) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 10);
            mediumDivisionFixer(operation);
        } else if (userScore < 1500) {
            multiOrDivide = Math.random() * 10;
            if (multiOrDivide <= 5) {
                firstNum = (int) (firstRandomNum * 1000);
                secondNum = (int) (secondRandomNum * 10);
            } else {
                firstNum = (int) (firstRandomNum * 1000);
                secondNum = (int) (secondRandomNum * 100);
            }
            mediumDivisionFixer(operation);
        } else if (userScore < 1700) {
            firstNum = (int) (firstRandomNum * 100);
            secondNum = (int) (secondRandomNum * 100);
            mediumDivisionFixer(operation);
        } else {
            if (operation.equals("+") || operation.equals("-")) {
                firstNum = (int) (firstRandomNum * 1000);
                secondNum = (int) (secondRandomNum * 1000);
            } else {
                multiOrDivide = Math.random() * 10;
                if (multiOrDivide <= 5) {
                    firstNum = (int) (firstRandomNum * 100);
                    secondNum = (int) (secondRandomNum * 100);
                    mediumDivisionFixer(operation);
                } else {
                    firstNum = (int) (firstRandomNum * 1000);
                    secondNum = (int) (secondRandomNum * 10);
                    mediumDivisionFixer(operation);
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
        createMediumChoices(userScore);
    }

    private void createMediumChoices(int userScore) {
        choices.clear();
        if (userScore < 100) {
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
        } else if (userScore < 200) {
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
        } else if (userScore < 500) {
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
        } else if (userScore < 1000) {
            while (choices.size() < 3) {
                int choiceNum;
                if (correctAnswer <= 100) {
                    choiceNum = (int) (Math.random() * 100);
                } else {
                    choiceNum = (int) (Math.random() * 1000); //can improve difficulty by adding *500 and *1000
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else {
            while (choices.size() < 3) {
                int choiceNum;
                if (operation.equals("+") || operation.equals("-")) {
                    choiceNum = (int) (Math.random() * 1000);
                    if (correctAnswer > 1000) {
                        choiceNum = choiceNum + 999;
                    } else if (correctAnswer < 100 && correctAnswer >= 10) {
                        choiceNum = choiceNum / 10;
                    } else if (correctAnswer < 10) {
                        choiceNum = choiceNum / 100;
                    }
                } else {
                    if (correctAnswer <= 1000) {
                        choiceNum = (int) (Math.random() * 1000);
                    } else {
                        choiceNum = (int) (Math.random() * 10000); //can improve difficulty by adding *500 and *1000
                    }
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        }
        createChoices();
    }

    private void mediumDivisionFixer(String operation) {
        if (operation.equals("÷")) {
            if (firstNum > secondNum) {
                while (secondNum == 0) {
                    if (userScore < 1000) {
                        secondNum = (int) (Math.random() * 10);
                    } else if (userScore < 1500) {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                        } else {
                            secondNum = (int) (Math.random() * 10);
                        }
                    } else if (userScore < 1700) {
                        secondNum = (int) (Math.random() * 100);
                    } else {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                        } else {
                            secondNum = (int) (Math.random() * 10);
                        }
                    }
                }
                while (firstNum % secondNum != 0) {
                    if (userScore < 1000) {
                        secondNum = (int) (Math.random() * 10);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (userScore < 1500) {
                        if (multiOrDivide <= 5) {
                            secondNum = (int) (Math.random() * 100);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            secondNum = (int) (Math.random() * 10);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 10);
                                }
                            }
                        }
                    } else if (userScore < 1700) {
                        secondNum = (int) (Math.random() * 100);
                        if (secondNum == 0) {
                            while (secondNum == 0) {
                                secondNum = (int) (Math.random() * 100);
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
                            secondNum = (int) (Math.random() * 10);
                            if (secondNum == 0) {
                                while (secondNum == 0) {
                                    secondNum = (int) (Math.random() * 10);
                                }
                            }
                        }
                    }
                }
                correctAnswer = firstNum / secondNum;
            } else {
                while (firstNum == 0) {
                    if (userScore < 1000) {
                        firstNum = (int) (Math.random() * 10);
                    } else if (userScore < 1500) {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                        } else {
                            firstNum = (int) (Math.random() * 10);
                        }
                    } else if (userScore < 1700) {
                        firstNum = (int) (Math.random() * 100);
                    } else {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                        } else {
                            firstNum = (int) (Math.random() * 10);
                        }
                    }
                }
                while (secondNum % firstNum != 0) {
                    if (userScore < 1000) {
                        firstNum = (int) (Math.random() * 10);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 10);
                            }
                        }
                    } else if (userScore < 1500) {
                        if (multiOrDivide <= 5) {
                            firstNum = (int) (Math.random() * 100);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 100);
                                }
                            }
                        } else {
                            firstNum = (int) (Math.random() * 10);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 10);
                                }
                            }
                        }
                    } else if (userScore < 1700) {
                        firstNum = (int) (Math.random() * 100);
                        if (firstNum == 0) {
                            while (firstNum == 0) {
                                firstNum = (int) (Math.random() * 100);
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
                            firstNum = (int) (Math.random() * 10);
                            if (firstNum == 0) {
                                while (firstNum == 0) {
                                    firstNum = (int) (Math.random() * 10);
                                }
                            }
                        }
                    }
                }
                correctAnswer = secondNum / firstNum;
            }
        }
    }

    private void easyEquationGenerator() {
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

        getCorrectAnswer();
        createEasyChoices(userScore);
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
                if (gameDifficulty.equals("Medium")) {
                    mediumDivisionFixer(operation);
                } else {
                    hardDivisionFixer(operation);
                }
                break;
        }
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
        getCorrectAnswer();
        createVeryEasyChoices();
    }

    private void createVeryEasyChoices() {
        //creating choices
        choices.clear();
        while (choices.size() < 3) {
            int choiceNum = (int) (Math.random() * 10);
            if (correctAnswer > 10) {
                choiceNum = choiceNum + 9;
            }
            if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                choices.add(choiceNum);
            }
        }
        createChoices();
    }

    private void createEasyChoices(int userScore) {
        choices.clear();
        if (userScore < 150) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 10);
                if (correctAnswer > 10) {
                    choiceNum = choiceNum + 9;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 375) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 20);
                if (correctAnswer > 20) {
                    choiceNum = choiceNum + 19;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 2;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 750) {
            while (choices.size() < 3) {
                int choiceNum = (int) (Math.random() * 50);
                if (correctAnswer > 50) {
                    choiceNum = choiceNum + 49;
                } else if (correctAnswer < 10) {
                    choiceNum = choiceNum / 5;
                }
                if (choiceNum != correctAnswer && !choices.contains(choiceNum)) {
                    choices.add(choiceNum);
                }
            }
        } else if (userScore < 1125) {
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
        } else {
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
        }
        createChoices();
    }

    private void createChoices() {
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