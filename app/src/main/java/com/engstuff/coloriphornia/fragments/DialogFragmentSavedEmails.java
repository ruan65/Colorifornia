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
import com.engstuff.coloriphornia.activities.ColorC;

import java.util.ArrayList;
import java.util.List;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.erasePrefs;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsAllToArray;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.writeToPrefs;

public class DialogFragmentSavedEmails extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Activity activity = getActivity();

        final List<String> emailsToDelete = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        final String[] emails = readFromPrefsAllToArray(activity, ColorC.SAVED_EMAILS);

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
                .setPositiveButton("Add new", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final EditText inputEmail = new EditText(activity);

                        inputEmail.setTextColor(Color.BLACK);

                        inputEmail.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                        new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT)
                                .setTitle("Add email for color sharing")
                                .setView(inputEmail)

                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String newEmail = inputEmail.getText().toString();

                                        writeToPrefs(activity, ColorC.SAVED_EMAILS, newEmail, null);

                                        Toast.makeText(activity, "email: " + newEmail +
                                                " has been saved", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // ignore
                                    }
                                }).show();
                    }
                })
                .setNeutralButton("Delete selected", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuilder sb = new StringBuilder("Email(s):\n");

                        if (emailsToDelete.size() == 0) {
                            sb.append("\nNo emails to delete...");
                        } else {
                            for (String e : emailsToDelete) {
                                sb.append("\n" + e);
                            }
                        }

                        sb.append("\n\nwill be deleted");

                        new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT)
                                .setTitle("Deleting emails")
                                .setMessage(sb.toString())
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        for (String key : emailsToDelete) {
                                            erasePrefs(activity, ColorC.SAVED_EMAILS, key);
                                        }
                                    }
                                })
                                .setNegativeButton(R.string.no, null)
                                .show();
                    }
                });

        return builder.create();
    }
}
