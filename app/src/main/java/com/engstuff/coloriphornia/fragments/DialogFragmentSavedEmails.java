package com.engstuff.coloriphornia.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;

import java.util.ArrayList;
import java.util.List;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.erasePrefs;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsAllToArray;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.writeToPrefs;

public class DialogFragmentSavedEmails extends DialogFragment {

    private Activity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        activity = getActivity();

        final List<String> emailsToDelete = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT);

        final String[] emails = readFromPrefsAllToArray(activity, Cv.SAVED_EMAILS);

        builder
                .setTitle(R.string.prefs_emails)
                .setMultiChoiceItems(
                        emails,
                        null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                                if (isChecked) {
                                    emailsToDelete.add(emails[which]);
                                } else {
                                    emailsToDelete.remove(emails[which]);
                                }
                            }
                        })
                .setNegativeButton(R.string.btn_ok, null)
                .setPositiveButton(R.string.btn_add_new, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final EditText inputEmail = new EditText(activity);

                        inputEmail.setTextColor(Color.BLACK);

                        inputEmail.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                        new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT)
                                .setTitle(R.string.dial_title_add_email)
                                .setView(inputEmail)

                                .setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String newEmail = inputEmail.getText().toString();

                                        writeToPrefs(activity, Cv.SAVED_EMAILS, newEmail, null);

                                        Toast.makeText(activity, text(R.string.toast_email) + newEmail +
                                                text(R.string.toast_hb_saved), Toast.LENGTH_SHORT).show();

                                        activity.recreate();
                                    }
                                })
                                .setNegativeButton(R.string.btn_cancel, null).show();
                    }
                })
                .setNeutralButton(R.string.btn_del_selected, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuilder sb = new StringBuilder();

                        sb.append(text(R.string.sb_emails));

                        if (emailsToDelete.size() == 0) {
                            sb.append(text(R.string.sb_wb_deleted));
                        } else {
                            for (String e : emailsToDelete) {
                                sb.append(text(R.string.new_line) + e);
                            }
                        }
                        sb.append(text(R.string.sb_wb_deleted));

                        new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT)
                                .setTitle(R.string.dial_title_del_emails)
                                .setMessage(sb.toString())
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        for (String key : emailsToDelete) {
                                            erasePrefs(activity, Cv.SAVED_EMAILS, key);
                                        }
                                        activity.recreate();
                                    }
                                })
                                .setNegativeButton(R.string.no, null)
                                .show();
                    }
                });

        return builder.create();
    }

    private String text(int id) {
        return activity.getResources().getString(id);
    }
}