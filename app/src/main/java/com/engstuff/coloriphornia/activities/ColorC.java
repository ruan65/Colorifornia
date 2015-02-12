package com.engstuff.coloriphornia.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.helpers.HexColorFrom4parts;

import java.util.Map;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.erasePrefs;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsAll;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsInt;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.writeToPrefs;

public class ColorC extends BaseActivity
        implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {


    private final Context ctx = this;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private ImageView iv;

    private SeekBar sbAlfa, sbRed, sbGreen, sbBlue;

    private int alpha, r, g, b; // alpha, red, green, blue
    private int colorHex;
    private Paint tp;

    public final static String EXTRA_MESSAGE_COLOR = "color_parameters";
    public final static String EXTRA_MESSAGE_TEXT_COLOR = "text_color_parameters";
    public final static String SAVED_COLORS = "user_saved_colors";
    public final static String SAVED_EMAILS = "user_saved_emails";


    private String rgbColorParams;
    private String hexColorParams;
    private boolean white = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iv = (ImageView) findViewById(R.id.colorView);
        iv.setOnClickListener(this);

        sbAlfa = (SeekBar) findViewById(R.id.sbAlfa);
        sbRed = (SeekBar) findViewById(R.id.sbRed);
        sbGreen = (SeekBar) findViewById(R.id.sbGreen);
        sbBlue = (SeekBar) findViewById(R.id.sbBlue);

        sbAlfa.setOnSeekBarChangeListener(this);
        sbRed.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);

        alpha = getResources().getInteger(R.integer.sbMax);
        r = g = b = getResources().getInteger(R.integer.sbProgress);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.color_c;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        int w = iv.getWidth();
        int h = iv.getHeight();

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        tp = new Paint();

        changeColor();
    }

    @SuppressWarnings("deprecation")
    private void changeColor() {

        colorHex = HexColorFrom4parts.composeHex(alpha, r, g, b);

        mCanvas.drawRGB(r, g, b);

        if (r + g + b > 480 || g > 200) {

            tp.setColor(Color.BLACK);
            white = false;

        } else {

            tp.setColor(Color.WHITE);
            white = true;
        }

        tp.setTextSize(30);

        rgbColorParams = "\u03b1: " + alpha + " r:" + r + " g:" + g + " b:" + b;
        hexColorParams = "#" + Integer.toHexString(colorHex);

        mCanvas.drawText(rgbColorParams, 10, 40, tp);
        mCanvas.drawText(hexColorParams, 10, 80, tp);
        mCanvas.drawText("Full screen \u21aa", 10, 180, tp);

        iv.setImageBitmap(mBitmap);
        iv.setAlpha(alpha);

    }

    @Override
    public void onClick(View v) {

        String[] colorParams = {
                rgbColorParams,
                hexColorParams
        };

        Intent i = new Intent(this, FullScreenColor.class);

        i.putExtra(EXTRA_MESSAGE_COLOR, colorParams);
        i.putExtra(EXTRA_MESSAGE_TEXT_COLOR, white);

        startActivity(i);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()) {
            case R.id.sbAlfa:
                alpha = progress;
                break;
            case R.id.sbRed:
                r = progress;
                break;
            case R.id.sbGreen:
                g = progress;
                break;
            case R.id.sbBlue:
                b = progress;
                break;
            default:
                break;
        }
        changeColor();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // ignore
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // ignore
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


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

                for (String colorHex : savedColors.keySet()) {

                    Log.d("ml", "hex: " + colorHex + ", int: " + savedColors.get(colorHex));
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
                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
                                .append("<h3>Color chosen via Colorifornia: </h3><h2>")
                                .append(hexColorParams + "</h2>")
                                .toString()
                ));

                startActivity(Intent.createChooser(emailIntent, "Send current color parameters..."));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
