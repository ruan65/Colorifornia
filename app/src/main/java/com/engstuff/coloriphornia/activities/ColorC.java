package com.engstuff.coloriphornia.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;
import com.engstuff.coloriphornia.fragments.FragmentSeekBarsControl;

import java.util.Map;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.erasePrefs;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsAll;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsInt;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.writeToPrefs;

public class ColorC extends BaseActivity
        implements FragmentSeekBarsControl.ColorControlChangeListener,
        FragmentColorBox.ColorBoxEventListener {

    public final static String EXTRA_MESSAGE_COLOR = "color_parameters";
    public final static String EXTRA_MESSAGE_TEXT_COLOR = "text_color_parameters";
    public final static String SAVED_COLORS = "user_saved_colors";
    public final static String SAVED_EMAILS = "user_saved_emails";

    protected final Context ctx = this;
    protected FragmentSeekBarsControl fragmentControl;
    protected FragmentColorBox fragmentColorBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fragmentControl = new FragmentSeekBarsControl();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction
                .add(R.id.color_control_container, fragmentControl);

        fragmentColorBox = new FragmentColorBox();

        transaction
                .add(R.id.color_box_container, fragmentColorBox)
                .commit();
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.color_c;
    }


    @Override
    public void onColorClicked(FragmentColorBox color) {

        fragmentColorBox = color;
        fragmentControl.setControls(
                color.getAlpha(), color.getR(), color.getG(), color.getB());
    }

    @Override
    public void onColorLongClicked(FragmentColorBox color) {

        String[] colorParams = {
                color.getRgbColorParams(),
                color.getHexColorParams()
        };

        Intent i = new Intent(this, FullScreenColor.class);

        i.putExtra(EXTRA_MESSAGE_COLOR, colorParams);
        i.putExtra(EXTRA_MESSAGE_TEXT_COLOR, fragmentColorBox.isWhiteText());

        startActivity(i);
    }

    @Override
    public void onColorControlChange() {
        fragmentColorBox
                .setColorParams()
                .changeColor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String hexColorParams = fragmentColorBox.getHexColorParams();
        int colorHex = fragmentColorBox.getColorHex();

        switch (item.getItemId()) {

            case R.id.save_to_prefs:



                writeToPrefs(this, SAVED_COLORS, hexColorParams, colorHex);

                if (readFromPrefsInt(this, SAVED_COLORS, hexColorParams) == colorHex) {

                    Toast.makeText(this, "Color: " + hexColorParams +
                            " has been saved", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.get_saved:

                Map<String, Integer> savedColors =
                        (Map<String, Integer>) readFromPrefsAll(this, SAVED_COLORS);

                for (String colorHexadecimal : savedColors.keySet()) {

                    Log.d("ml", "hex: " + colorHexadecimal + ", int: " + savedColors.get(colorHexadecimal));
                }
                break;

            case R.id.erase:

                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Delete saved colors")
                        .setMessage("All saved colors will be deleted!?")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                erasePrefs(ctx, SAVED_COLORS);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // ignore
                            }
                        })
                        .show();
                break;

            case R.id.add_email:

                final EditText inputEmail = new EditText(this);

                inputEmail.setTextColor(Color.BLACK);

                inputEmail.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Add email for color sharing")
                        .setView(inputEmail)

                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String newEmail = inputEmail.getText().toString();

                                writeToPrefs(ctx, SAVED_EMAILS, newEmail, null);

                                Toast.makeText(ctx, "email: " + newEmail +
                                        " has been saved", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // ignore
                            }
                        }).show();
                break;

            case R.id.clear_emails:
                erasePrefs(this, SAVED_EMAILS);
                Toast.makeText(ctx, "Email list has been cleaned...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_item_share:

                Map<String, String> emailMap =
                        (Map<String, String>) readFromPrefsAll(this, SAVED_EMAILS);

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setType("message/rfc822");

                emailIntent.putExtra(Intent.EXTRA_EMAIL,
                        emailMap.keySet().toArray(new String[emailMap.size()]));

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Color parameters from Colorifornia");

                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(
                        new StringBuilder()
                                .append("<h3>Color chosen via Colorifornia: </h3><h2>")
                                .append(hexColorParams)
                                .append("</h2>")
                                .toString()
                ));

                startActivity(Intent.createChooser(emailIntent, "Send current color parameters..."));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public FragmentSeekBarsControl getFragmentControl() {
        return fragmentControl;
    }
}
