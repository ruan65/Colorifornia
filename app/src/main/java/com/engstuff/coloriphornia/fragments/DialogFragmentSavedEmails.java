package com.engstuff.coloriphornia.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.engstuff.coloriphornia.activities.ColorC;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsAllToArray;

public class DialogFragmentSavedEmails extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder
                .setTitle("Saved Emails")
                .setMultiChoiceItems(
                        readFromPrefsAllToArray(activity, ColorC.SAVED_EMAILS),
                        null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                            }
                        }
                );


        return builder.create();
    }
}
