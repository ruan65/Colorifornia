package com.engstuff.coloriphornia.helpers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.ColorFromImage;
import com.engstuff.coloriphornia.activities.FullScreenColorC;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.ColorControlAbstractFragment;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;

public class AppHelper {

    public static void startFullColorC(Activity activity, String rgb, String hex) {

        String[] colorParams = {rgb, hex};

        Intent i = new Intent(activity, FullScreenColorC.class);

        i.putExtra(Cv.EXTRA_MESSAGE_COLOR_1, colorParams);
        i.putExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, ColorParams.blackOrWhiteText(hex));

        activity.startActivity(i);
    }

    public static void setLike(Context ctx, FragmentColorBox colorBox) {

        String currentHexParams = colorBox.getHexColorParams();

        boolean isColorSaved = false;

        for (String c : PrefsHelper.readFromPrefsAllToArray(ctx, Cv.SAVED_COLORS)) {
            if (c.equals(currentHexParams)) isColorSaved = true;
        }

        if (isColorSaved) {
            ImageView like = colorBox.getLike();
            like.setImageResource(
                    colorBox.isWhiteText()
                            ? R.drawable.ic_loyalty_white_24dp
                            : R.drawable.ic_loyalty_black_24dp
            );

            if (like.getParent() == null) colorBox.getLayout().addView(like);
        }
    }

    public static void setInfoIcon(FragmentColorBox box) {
        ImageView info = box.getInfo();

        if (null != info) {
            info.setImageResource(
                    box.isWhiteText()
                            ? R.drawable.ic_info_white
                            : R.drawable.ic_info_black
            );
        }
    }

    public static void setLikesAndInfo(Context ctx, FragmentColorBox... boxes) {
        for (FragmentColorBox box : boxes) {
            setInfoIcon(box);
            setLike(ctx, box);
        }
    }

    public static void unsetLike(FragmentColorBox box) {

        ImageView like = box.getLike();

        if (like.getParent() != null) {
            box.getLayout().removeView(like);
        }
    }

    public static void setColorToColorBox(Context ctx,
                                          String prefsKey,
                                          ColorControlAbstractFragment control,
                                          FragmentColorBox color) {
        String hexColor = PrefsHelper.readFromPrefsString(
                ctx, Cv.PREFS_RETAIN, prefsKey);

        if (hexColor.equals("")) {

            control.setControls(255, 255, 0, 0);
            color.setColorParams().changeColor();

        } else {

            int[] argb = ColorParams.hexStringToARGB(hexColor);

            control.setControls(argb[0], argb[1], argb[2], argb[3]);
            color.setColorParams().changeColor();
        }
    }

    public static void startLastSavedActivity(Context ctx) {

        Class<?> activityClass;
        try {
            activityClass = Class.forName(
                    PrefsHelper.readFromPrefsString(ctx, Cv.LAST_ACTIVITY));

        } catch (ClassNotFoundException e) {

            activityClass = ColorFromImage.class;
        }
        if (activityClass.equals(ctx.getClass())) {

            Toast.makeText(ctx, "History stack is empty. Use \"Navigation drawer\".",
                    Toast.LENGTH_LONG).show();
        } else {
            ctx.startActivity(new Intent(ctx, activityClass));
        }
    }

    public static String getDeviceGoogleEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return "";
        } else {
            return account.name;
        }
    }
    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        } return account;
    }
}
