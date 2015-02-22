package com.engstuff.coloriphornia.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.ColorC;

import java.util.ArrayList;
import java.util.List;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.erasePrefs;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsAllToArray;

public class DialogFragmentSavedEmails extends DialogFragment {



    final List<String> emailsToDelete = new ArrayList<>();



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Activity activity = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        final String[] emails = readFromPrefsAllToArray(activity, ColorC.SAVED_EMAILS);

        builder
                .setTitle("Saved Emails")
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
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add new", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // ignore
                                    }
                                })
                                .show();
                    }
                });

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("ml", "Emails to delete: " + emailsToDelete);
    }
}
