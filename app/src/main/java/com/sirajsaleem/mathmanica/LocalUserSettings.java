package com.sirajsaleem.mathmanica;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalUserSettings {

    private static LocalUserSettings instance;
    private final SharedPreferences sharedPreferences;
    private final String DONT_SHOW_SIGN_UP_DIALOG_KEY = "sign_up_dialog_checkbox_status";
    private final String DIFFICULTY_KEY = "difficulty";
    private final String VERY_EASY_MODE_SCORE_KEY = "very_easy_mode_score";
    private final String EASY_MODE_SCORE_KEY = "easy_mode_score";
    private final String MEDIUM_MODE_SCORE_KEY = "medium_mode_score";
    private final String HARD_MODE_SCORE_KEY = "hard_mode_score";
    private final String CURRENT_VERY_EASY_LEVEL_KEY = "very_easy_current_level";
    private final String CURRENT_EASY_LEVEL_KEY = "easy_current_level";
    private final String CURRENT_MEDIUM_LEVEL_KEY = "medium_current_level";
    private final String CURRENT_HARD_LEVEL_KEY = "hard_current_level";
    private final String STREAK_SCORE_KEY = "streak";
    private final String TIME_TRIAL_SCORE_KEY = "time_trial";
    private final String USERNAME_KEY = "username";
    private final String PASSWORD_KEY = "password";
    private final String FIREBASE_ANALYTICS_PERMISSION_KEY = "firebase_analytics";
    private final String DONT_SHOW_FIREBASE_DIALOG_KEY = "no_dialog";
    private final String STREAK_HIGH_SCORE_KEY = "streak_high_score";
    private final String TIME_TRIAL_HIGH_SCORE_KEY = "time_trial_high_score";
    private final String ORIENTATION_CONTROL_KEY = "orientation_control";
    private final String TIME_TRIAL_MILLISECONDS_KEY = "milliseconds";

    private LocalUserSettings(Context context) {

        sharedPreferences = context.getSharedPreferences("db", Context.MODE_PRIVATE);

        if (getDifficulty() == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("difficulty", "Easy");
            editor.apply();
            setDifficulty("easy");
        }
    }

    public String getUsername() {
        return sharedPreferences.getString(USERNAME_KEY, "unregistered");
    }


    public void setUsername(String newUsername) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USERNAME_KEY);
        editor.putString(USERNAME_KEY, newUsername);
        editor.apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD_KEY, "unregistered");
    }

    public void setPassword(String newPassword) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PASSWORD_KEY);
        editor.putString(PASSWORD_KEY, newPassword);
        editor.apply();
    }

    public int getCurrentLevel(String difficulty) {
        switch (difficulty) {
            case "Very_Easy":
                return sharedPreferences.getInt(CURRENT_VERY_EASY_LEVEL_KEY, 1);
            case "Easy":
                return sharedPreferences.getInt(CURRENT_EASY_LEVEL_KEY, 1);
            case "Medium":
                return sharedPreferences.getInt(CURRENT_MEDIUM_LEVEL_KEY, 1);
            default: // "Hard"
                return sharedPreferences.getInt(CURRENT_HARD_LEVEL_KEY, 1);
        }
    }

    public void setCurrentLevel(String difficulty, int newLevel) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (difficulty) {
            case "Very_Easy":
                editor.remove(CURRENT_VERY_EASY_LEVEL_KEY);
                editor.putInt(CURRENT_VERY_EASY_LEVEL_KEY, newLevel);
                break;
            case "Easy":
                editor.remove(CURRENT_EASY_LEVEL_KEY);
                editor.putInt(CURRENT_EASY_LEVEL_KEY, newLevel);
                break;
            case "Medium":
                editor.remove(CURRENT_MEDIUM_LEVEL_KEY);
                editor.putInt(CURRENT_MEDIUM_LEVEL_KEY, newLevel);
                break;
            default: // "Hard"
                editor.remove(CURRENT_HARD_LEVEL_KEY);
                editor.putInt(CURRENT_HARD_LEVEL_KEY, newLevel);
                break;

        }

        editor.apply();
    }

    public int getScore(String difficulty) {
        switch (difficulty) {
            case "Very_Easy":
                return sharedPreferences.getInt(VERY_EASY_MODE_SCORE_KEY, 0);
            case "Easy":
                return sharedPreferences.getInt(EASY_MODE_SCORE_KEY, 0);
            case "Medium":
                return sharedPreferences.getInt(MEDIUM_MODE_SCORE_KEY, 0);
            case "Streak":
                return sharedPreferences.getInt(STREAK_SCORE_KEY, 0);
            case "Time_Trial":
                return sharedPreferences.getInt(TIME_TRIAL_SCORE_KEY, 0);
            default: // Hard mode
                return sharedPreferences.getInt(HARD_MODE_SCORE_KEY, 0);
        }

    }

    public int getHighScore(String mode) {
        if (mode.equals("Streak")) {
            return sharedPreferences.getInt(STREAK_HIGH_SCORE_KEY, 0);
        } else { // Time_Trial
            return sharedPreferences.getInt(TIME_TRIAL_HIGH_SCORE_KEY, 0);
        }
    }

    public void setHighScore(String mode, int highScore) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (mode.equals("Streak")) {
            editor.remove(STREAK_HIGH_SCORE_KEY);
            editor.putInt(STREAK_HIGH_SCORE_KEY, highScore);
        } else { // Time_Trial
            editor.remove(TIME_TRIAL_HIGH_SCORE_KEY);
            editor.putInt(TIME_TRIAL_HIGH_SCORE_KEY, highScore);
        }
        editor.apply();
    }

    public void setScore(String difficulty, int newScore) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (difficulty) {
            case "Very_Easy":
                editor.remove(VERY_EASY_MODE_SCORE_KEY);
                editor.putInt(VERY_EASY_MODE_SCORE_KEY, newScore);
                break;
            case "Easy":
                editor.remove(EASY_MODE_SCORE_KEY);
                editor.putInt(EASY_MODE_SCORE_KEY, newScore);
                break;
            case "Medium":
                editor.remove(MEDIUM_MODE_SCORE_KEY);
                editor.putInt(MEDIUM_MODE_SCORE_KEY, newScore);
                break;
            case "Streak":
                editor.remove(STREAK_SCORE_KEY);
                editor.putInt(STREAK_SCORE_KEY, newScore);
                break;
            case "Time_Trial":
                editor.remove(TIME_TRIAL_SCORE_KEY);
                editor.putInt(TIME_TRIAL_SCORE_KEY, newScore);
                break;
            default:
                editor.remove(HARD_MODE_SCORE_KEY);
                editor.putInt(HARD_MODE_SCORE_KEY, newScore);
                break;
        }
        editor.apply();
    }

    public boolean getPermission(String identifier) {
        switch (identifier) {
            case "firebase_analytics":
                return sharedPreferences.getBoolean(FIREBASE_ANALYTICS_PERMISSION_KEY, false);
            case "noDialog":
                return sharedPreferences.getBoolean(DONT_SHOW_FIREBASE_DIALOG_KEY, false);
            default: //sign_up_checkbox
                return sharedPreferences.getBoolean(DONT_SHOW_SIGN_UP_DIALOG_KEY, false);
        }
    }

    public void grantPermissions(String identifier, boolean permission) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (identifier) {
            case "firebase_analytics":
                editor.remove(FIREBASE_ANALYTICS_PERMISSION_KEY);
                editor.putBoolean(FIREBASE_ANALYTICS_PERMISSION_KEY, permission);
                break;
            case "noDialog":
                editor.remove(DONT_SHOW_FIREBASE_DIALOG_KEY);
                editor.putBoolean(DONT_SHOW_FIREBASE_DIALOG_KEY, permission); // to make permission dialog never shows up again
                break;
            default: //sign_up_checkbox
                editor.remove(DONT_SHOW_SIGN_UP_DIALOG_KEY);
                editor.putBoolean(DONT_SHOW_SIGN_UP_DIALOG_KEY, permission);
                break;
        }
        editor.apply();
    }

    public String getDifficulty() {
        return sharedPreferences.getString(DIFFICULTY_KEY, null);
    }

    public void setDifficulty(String difficulty) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(DIFFICULTY_KEY);
        switch (difficulty) {
            case "Very_Easy":
                editor.putString(DIFFICULTY_KEY, "Very_Easy");
                break;
            case "Medium":
                editor.putString(DIFFICULTY_KEY, "Medium");
                break;
            case "Hard":
                editor.putString(DIFFICULTY_KEY, "Hard");
                break;
            default:
                editor.putString(DIFFICULTY_KEY, "Easy");
                break;
        }
        editor.apply();
    }

    public static LocalUserSettings getInstance(Context context) {
        if (instance == null) {
            return instance = new LocalUserSettings(context);
        } else {
            return instance;
        }
    }

    public boolean getOrientationControl() {
        return sharedPreferences.getBoolean(ORIENTATION_CONTROL_KEY, true);
    }

    public void setOrientationControl(boolean orientationControl) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ORIENTATION_CONTROL_KEY);
        editor.putBoolean(ORIENTATION_CONTROL_KEY, orientationControl);
        editor.apply();
    }

    public void setTimeTrialTimer(String seconds, String minutes, String hours, long currentCount) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String TIME_TRIAL_SECONDS_KEY = "seconds";
        editor.remove(TIME_TRIAL_SECONDS_KEY);
        String TIME_TRIAL_MINUTES_KEY = "minutes";
        editor.remove(TIME_TRIAL_MINUTES_KEY);
        String TIME_TRIAL_HOURS_KEY = "hours";
        editor.remove(TIME_TRIAL_HOURS_KEY);
        editor.remove(TIME_TRIAL_MILLISECONDS_KEY);
        editor.putString(TIME_TRIAL_SECONDS_KEY, seconds);
        editor.putString(TIME_TRIAL_MINUTES_KEY, minutes);
        editor.putString(TIME_TRIAL_HOURS_KEY, hours);
        editor.putLong(TIME_TRIAL_MILLISECONDS_KEY, currentCount);
        editor.apply();
    }

//    public String getSeconds(){
//        return sharedPreferences.getString(TIME_TRIAL_SECONDS_KEY, "00");
//    }
//
//    public String getMinutes(){
//        return sharedPreferences.getString(TIME_TRIAL_MINUTES_KEY, "01");
//    }
//
//    public String getHours(){
//        return sharedPreferences.getString(TIME_TRIAL_HOURS_KEY, "00");
//    }

    public long getCurrentCount() {
        return sharedPreferences.getLong(TIME_TRIAL_MILLISECONDS_KEY, 60000);
    }
}
