package com.sirajsaleem.mathmanica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingsActivity extends AppCompatActivity implements MethodsFactory {

    private Spinner difficultySpinner;
    private TextView cancelPermissions;
    private TextView deleteAccount;
    private TextView about;
    private TextView logInTxt;
    private Dialog loginDialog, cancelationDialog, aboutDialog;
    private TextView usernameErrorTxt, usernameEditTxt, passwordEditTxt, passwordErrorTxt, logInPasswordErrorTxt, rePasswordEditTxt, skipTxt, signUpTxt, logInTitleTxt, signUpTitleTxt;
    private CheckBox dontShowCheckBox;
    private ProgressBar loadingSemiCircle, settingsLoadingSemiCircle;
    private boolean dontShowCB;
    private String username, password, rePassword, result, toBeDeleted;
    private StringBuilder hashedPassword;
    private ConstraintLayout settingsLayout;
    private EditText deletionPasswordRequirement;
    private TextView deletionPasswordError;
    private TextView logOutTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.title_activity_settings));
        }

        if (getIntent().getBooleanExtra("back", false)) {
            overridePendingTransition(R.anim.navigation_back_slide_in, R.anim.navigation_back_slide_out);
        }

        findViews();

        ArrayList<String> difficulties = new ArrayList<>();
        difficulties.add(getString(R.string.very_easy));
        difficulties.add(getString(R.string.easy));
        difficulties.add(getString(R.string.medium));
        difficulties.add(getString(R.string.hard));

        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, difficulties
        ) {
            // overriding methods to inflate custom spinner layout and colors
            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.difficulty_spinner_row, null);
                }

                TextView spinnerItemLayout = v.findViewById(R.id.spinnerTarget);
                spinnerItemLayout.setText(difficulties.get(position));

                switch (position) {
                    case 0:
                        spinnerItemLayout.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.aqua));
                        break;
                    case 1:
                        spinnerItemLayout.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.green));
                        break;
                    case 2:
                        spinnerItemLayout.setTextColor(Color.YELLOW);
                        break;
                    default:
                        spinnerItemLayout.setTextColor(Color.RED);
                        break;
                }
                return v;
            }
        };

        difficultySpinner.setAdapter(difficultyAdapter);

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    switch (i) {
                        case 0:
                            ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.aqua));
                            break;
                        case 1:
                            ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.green));
                            break;
                        case 2:
                            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.YELLOW);
                            break;
                        default:
                            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.RED);
                            break;
                    }
                } catch (Exception e) {
                    Log.d("spinner_on_orientation_change", e.toString());
                }
                String selectedItem = difficultySpinner.getSelectedItem().toString();
                if (selectedItem.equals("Very Easy")) {
                    selectedItem = "Very_Easy";
                }
                LocalUserSettings.getInstance(SettingsActivity.this).setDifficulty(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        String difficulty = LocalUserSettings.getInstance(this).getDifficulty();
        if (difficulty.equals("Very_Easy")) {
            difficultySpinner.setSelection(difficulties.indexOf("Very Easy"));
        } else {
            difficultySpinner.setSelection(difficulties.indexOf(difficulty));
        }

        logOutTxt.setOnClickListener(v -> cancellationOrDeletingDialog("logout"));

        cancelPermissions.setOnClickListener(v -> cancellationOrDeletingDialog("cancel"));

        deleteAccount.setOnClickListener(v -> cancellationOrDeletingDialog("delete"));

        about.setOnClickListener(v -> {
            aboutDialog = new Dialog(this);
            aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            aboutDialog.getWindow().setContentView(R.layout.about_dialog);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70); //making width of dialog 90% of screen width
            aboutDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            aboutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            aboutDialog.setCancelable(true);
            aboutDialog.show();
            //dialog views
            TextView visitTxt = aboutDialog.findViewById(R.id.visitTxt);
            TextView dismissTxt = aboutDialog.findViewById(R.id.dismissTxt);
            visitTxt.setOnClickListener(j -> {
                aboutDialog.dismiss();
                Intent intent = new Intent(this, WebsiteActivity.class);
                startActivity(intent);
            });
            dismissTxt.setOnClickListener(j -> aboutDialog.dismiss());
        });

        logInTxt.setOnClickListener(v -> {
            loginDialog = new Dialog(this);
            loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loginDialog.setContentView(R.layout.sign_up_dialog);
            loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            loginDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            loginDialog.getWindow().setGravity(Gravity.CENTER);
            loginDialog.setCancelable(false);
            loginDialog.show();

            // dialog views
            usernameEditTxt = loginDialog.findViewById(R.id.userNameEditTxt);
            usernameErrorTxt = loginDialog.findViewById(R.id.usernameErrorTxt);
            passwordEditTxt = loginDialog.findViewById(R.id.passwordEditTxt);
            rePasswordEditTxt = loginDialog.findViewById(R.id.reEnterPasswordEditTxt);
            passwordErrorTxt = loginDialog.findViewById(R.id.passwordErrorTxt);
            dontShowCheckBox = loginDialog.findViewById(R.id.dontShowCheckBox);
            signUpTxt = loginDialog.findViewById(R.id.signUpTxt);
            skipTxt = loginDialog.findViewById(R.id.skipTxt);
            loadingSemiCircle = loginDialog.findViewById(R.id.loadingSemiCircle);
            logInTitleTxt = loginDialog.findViewById(R.id.logInTitleTxt);
            signUpTitleTxt = loginDialog.findViewById(R.id.signUpTitleTxt);
            logInPasswordErrorTxt = loginDialog.findViewById(R.id.logInPasswordErrorTxt);

            logInTitleTxt.setOnClickListener(j -> {
                passwordErrorTxt.setVisibility(View.GONE);
                usernameErrorTxt.setVisibility(View.GONE);
                logInPasswordErrorTxt.setVisibility(View.GONE);
                logInTitleTxt.setTextColor(ContextCompat.getColor(this, R.color.green));
                signUpTitleTxt.setTextColor(ContextCompat.getColor(this, R.color.green_shaded_x3));
                signUpTxt.setText(R.string.log_in);
                rePasswordEditTxt.setVisibility(View.INVISIBLE);
            });

            signUpTitleTxt.setOnClickListener(j -> {
                passwordErrorTxt.setVisibility(View.GONE);
                usernameErrorTxt.setVisibility(View.GONE);
                logInPasswordErrorTxt.setVisibility(View.GONE);
                signUpTitleTxt.setTextColor(ContextCompat.getColor(this, R.color.green));
                logInTitleTxt.setTextColor(ContextCompat.getColor(this, R.color.green_shaded_x3));
                signUpTxt.setText(R.string.sign_up);
                rePasswordEditTxt.setVisibility(View.VISIBLE);
            });

            signUpTxt.setOnClickListener(j -> {
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

            skipTxt.setOnClickListener(j -> {
                dontShowCB = dontShowCheckBox.isChecked();
                LocalUserSettings.getInstance(this).grantPermissions("sign_up_checkbox", dontShowCB);
                loginDialog.dismiss();
            });
        });

        if (LocalUserSettings.getInstance(this).getUsername().equals("unregistered")) {
            String logIn = "Log in/Register";
            logInTxt.setText(logIn);
            logOutTxt.setVisibility(View.GONE);
            deleteAccount.setVisibility(View.GONE);
        } else {
            String greet = "Signed in as " + LocalUserSettings.getInstance(this).getUsername();
            logInTxt.setText(greet);
            logInTxt.setClickable(false);
            deleteAccount.setVisibility(View.VISIBLE);
        }
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
                usernameErrorTxt.setVisibility(View.GONE);
                if (password.equals("")) {
                    logInPasswordErrorTxt.setVisibility(View.VISIBLE);
                }
            } else {
                usernameErrorTxt.setVisibility(View.VISIBLE);
                passwordErrorTxt.setVisibility(View.GONE);
            }
            return false;
        }
    }

    private void checkWithDatabase(String type) {
        hashedPassword = hashPassword(password);

        ExecutorService service = Executors.newSingleThreadExecutor();
        //pre execute
        service.execute(() -> {
            runOnUiThread(() -> {
                loadingSemiCircle.setVisibility(View.VISIBLE);
                signUpTxt.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.white_shaded_x4));
                skipTxt.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.white_shaded_x4));
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
                            loginDialog.dismiss();
                            break;
                    }
                } else {
                    Snackbar.make(settingsLayout, R.string.you_are_not_online, Snackbar.LENGTH_INDEFINITE)
                            .setAction("DISMISS", v -> {

                            })
                            .show();
                    loginDialog.dismiss();
                }
            });
        });
    }

    private void cancellationOrDeletingDialog(String action) {
        cancelationDialog = new Dialog(this);
        cancelationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancelationDialog.getWindow().setContentView(R.layout.deleting_dialog);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70); //making width of dialog 90% of screen width
        cancelationDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        cancelationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancelationDialog.setCancelable(true);
        cancelationDialog.show();
        //dialog views
        TextView title = cancelationDialog.findViewById(R.id.deletingTitle);
        TextView message = cancelationDialog.findViewById(R.id.deletingMessage);
        TextView yesTxt = cancelationDialog.findViewById(R.id.yesTxt);
        TextView noTxt = cancelationDialog.findViewById(R.id.noTxt);
        deletionPasswordError = cancelationDialog.findViewById(R.id.deletionPasswordError);
        deletionPasswordRequirement = cancelationDialog.findViewById(R.id.deletionPasswordRequirement);
        if (action.equals("cancel")) {
            yesTxt.setOnClickListener(v -> {
                cancelationDialog.dismiss();
                LocalUserSettings.getInstance(this).grantPermissions("firebase_analytics", false);
                LocalUserSettings.getInstance(this).grantPermissions("noDialog", false);
            });
            noTxt.setOnClickListener(v -> cancelationDialog.dismiss());
        } else if (action.equals("logout")) {
            title.setText(R.string.logging_out);
            message.setText(R.string.logging_out_dialog_text);
            yesTxt.setOnClickListener(v -> {
                cancelationDialog.dismiss();
                resetScores();
                logOutTxt.setVisibility(View.GONE);
                deleteAccount.setVisibility(View.GONE);
                String logIn = "Log in/Register";
                logInTxt.setText(logIn);
                logInTxt.setClickable(true);
            });
            noTxt.setOnClickListener(v -> cancelationDialog.dismiss());
        } else { //delete
            title.setText(R.string.delete_account_and_saved_data);
            message.setText(R.string.deleting_account_dialog_text);
            deletionPasswordRequirement.setVisibility(View.VISIBLE);
            yesTxt.setOnClickListener(v -> deleteUserFromDb());
            noTxt.setOnClickListener(v -> cancelationDialog.dismiss());

        }
    }

    private StringBuilder hashPassword(String toBeHashed) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert messageDigest != null;
        messageDigest.update(toBeHashed.getBytes());
        byte[] resultByteArray = messageDigest.digest();
        hashedPassword = new StringBuilder();
        for (byte b : resultByteArray) {
            hashedPassword.append(String.format("%02x", b));
        }
        return hashedPassword;
    }

    private void deleteUserFromDb() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        //pre execute
        service.execute(() -> {
            runOnUiThread(() -> settingsLoadingSemiCircle.setVisibility(View.VISIBLE));
            //heavy work

            String deletionPassword = deletionPasswordRequirement.getText().toString();
            StringBuilder deletionHashedPassword = hashPassword(deletionPassword);
            DbDeleteUser delete = new DbDeleteUser();
            String currentUserName = LocalUserSettings.getInstance(SettingsActivity.this).getUsername();
            toBeDeleted = delete.sendData(currentUserName, deletionHashedPassword.toString());

            if (toBeDeleted.equals("deleted")) {
                resetScores();
            }

            //post execute
            runOnUiThread(() -> {
                if (toBeDeleted != null) {
                    if (toBeDeleted.equals("error")) {
                        settingsLoadingSemiCircle.setVisibility(View.GONE);
                        Snackbar.make(settingsLayout, R.string.you_are_not_online, Snackbar.LENGTH_INDEFINITE)
                                .setAction("DISMISS", v -> {

                                })
                                .show();
                        cancelationDialog.dismiss();
                    } else if (toBeDeleted.equals("Wrong password")) {
                        deletionPasswordError.setVisibility(View.VISIBLE);
                        settingsLoadingSemiCircle.setVisibility(View.GONE);
                    } else {
                        settingsLoadingSemiCircle.setVisibility(View.GONE);
                        String logIn = "Log in/Register";
                        logInTxt.setText(logIn);
                        logInTxt.setClickable(true);
                        cancelationDialog.dismiss();
                        Snackbar.make(settingsLayout, currentUserName + getString(R.string.deleted), Snackbar.LENGTH_LONG)
                                .setAction(R.string.dismiss, v -> {

                                })
                                .show();
                    }
                } else {
                    settingsLoadingSemiCircle.setVisibility(View.GONE);
                    cancelationDialog.dismiss();
                }
            });
        });
    }

    private void resetScores() {
        LocalUserSettings.getInstance(SettingsActivity.this).grantPermissions("sign_up_checkbox", false);
        LocalUserSettings.getInstance(SettingsActivity.this).setUsername("unregistered");
        LocalUserSettings.getInstance(SettingsActivity.this).setScore("Very_Easy", 0);
        LocalUserSettings.getInstance(SettingsActivity.this).setScore("Easy", 0);
        LocalUserSettings.getInstance(SettingsActivity.this).setScore("Medium", 0);
        LocalUserSettings.getInstance(SettingsActivity.this).setScore("Hard", 0);
        LocalUserSettings.getInstance(SettingsActivity.this).setScore("Streak", 0);
        LocalUserSettings.getInstance(this).setHighScore("Streak", 0);
        LocalUserSettings.getInstance(SettingsActivity.this).setScore("Time Trial", 0);
        LocalUserSettings.getInstance(this).setHighScore("Time Trial", 0);
        LocalUserSettings.getInstance(SettingsActivity.this).setCurrentLevel("Very_Easy", 1);
        LocalUserSettings.getInstance(SettingsActivity.this).setCurrentLevel("Easy", 1);
        LocalUserSettings.getInstance(SettingsActivity.this).setCurrentLevel("Medium", 1);
        LocalUserSettings.getInstance(SettingsActivity.this).setCurrentLevel("Hard", 1);
    }

    private void signingUpNewUser(String type) {
        LocalUserSettings.getInstance(SettingsActivity.this).setUsername(username);
        LocalUserSettings.getInstance(SettingsActivity.this).setPassword(hashedPassword.toString());
        LocalUserSettings.getInstance(SettingsActivity.this).grantPermissions("sign_up_checkbox", true);
        if (type.equals("signup")) {
            newUserScoreUpdateDb();
        }
        LocalUserSettings.getInstance(this).grantPermissions("sign_up_checkbox", true);
        String greet = "Signed in as " + LocalUserSettings.getInstance(this).getUsername();
        logInTxt.setText(greet);
        logOutTxt.setVisibility(View.VISIBLE);
        deleteAccount.setVisibility(View.VISIBLE);
    }

    private void newUserScoreUpdateDb() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        //pre execute
        service.execute(() -> {
            //heavy work

            DbUpdateScore dbUpdateScore = new DbUpdateScore();
            dbUpdateScore.sendData(
                    LocalUserSettings.getInstance(SettingsActivity.this).getCurrentLevel("Very_Easy") + "",
                    "Very_Easy",
                    username,
                    LocalUserSettings.getInstance(SettingsActivity.this).getScore("Very_Easy") + "");
            dbUpdateScore.sendData(
                    LocalUserSettings.getInstance(SettingsActivity.this).getCurrentLevel("Easy") + "",
                    "Easy",
                    username,
                    LocalUserSettings.getInstance(SettingsActivity.this).getScore("Easy") + "");
            dbUpdateScore.sendData(
                    LocalUserSettings.getInstance(SettingsActivity.this).getCurrentLevel("Medium") + "",
                    "Medium",
                    username,
                    LocalUserSettings.getInstance(SettingsActivity.this).getScore("Medium") + "");
            dbUpdateScore.sendData(
                    LocalUserSettings.getInstance(SettingsActivity.this).getCurrentLevel("Hard") + "",
                    "Hard",
                    username,
                    LocalUserSettings.getInstance(SettingsActivity.this).getScore("Hard") + "");
            dbUpdateScore.sendData(
                    "",
                    "Streak",
                    username,
                    LocalUserSettings.getInstance(SettingsActivity.this).getHighScore("Streak") + "");
            dbUpdateScore.sendData(
                    "",
                    "Time_Trial",
                    username,
                    LocalUserSettings.getInstance(SettingsActivity.this).getHighScore("Time_Trial") + "");

            //post execute
        });
    }

    @Override
    public void findViews() {
        difficultySpinner = findViewById(R.id.difficultySpinner);
        cancelPermissions = findViewById(R.id.cancelPermissionsTxt);
        deleteAccount = findViewById(R.id.deleteAccountTxt);
        about = findViewById(R.id.aboutTxt);
        logInTxt = findViewById(R.id.loginSingUpTxt);
        settingsLoadingSemiCircle = findViewById(R.id.settingsLoadingSemiCircle);
        settingsLayout = findViewById(R.id.settingsLayout);
        logOutTxt = findViewById(R.id.logOutTxt);
    }

    @Override
    public void goBack() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
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