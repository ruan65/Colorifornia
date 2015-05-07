package com.engstuff.coloriphornia.helpers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.widget.ImageView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.ColorFromImage;
import com.engstuff.coloriphornia.activities.FullScreenColorC;
import com.engstuff.coloriphornia.activities.MockUpActivity;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.ColorControlAbstractFragment;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsAllToArray;

public class AppHelper {

    public static void startFullColorC(Activity activity, String hex, boolean... favorites) {

        startFullColorC(activity, hex, -1, favorites);
    }

    public static void startFullColorC(Activity activity,
                                       String background, int font, boolean... favorites) {

        background = ColorParams.replaceNotValidHexForZeroColor(background);

        Intent i = new Intent(activity, FullScreenColorC.class);

        i.putExtra(Cv.EXTRA_MESSAGE_COLOR_1, background);
        i.putExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, ColorParams.blackOrWhiteText(background));
        i.putExtra(Cv.CALLED_FROM_FAVORITES, favorites.length > 0 && favorites[0]);
        i.putExtra(Cv.EXTRA_MESSAGE_FONT_COLOR, font);

        ((MockUpActivity) activity).setFullColorStarted(true);
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

        if (like != null && like.getParent() != null) {
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

    public static boolean startLastSavedActivity(Context ctx) {

        Class<?> activityClass;
        try {
            activityClass = Class.forName(
                    PrefsHelper.readFromPrefsString(ctx, Cv.LAST_ACTIVITY));

        } catch (ClassNotFoundException e) {

            activityClass = ColorFromImage.class;
        }
        if (activityClass.equals(ctx.getClass())) {

            return false;

        } else {

            ctx.startActivity(new Intent(ctx, activityClass));
            return true;
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

    public static void fireShareIntent(Context ctx, String html) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("message/rfc822");

        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                readFromPrefsAllToArray(ctx, Cv.SAVED_EMAILS));

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, Cv.EMAIL_SUBJ);

        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(html));

        ctx.startActivity(Intent.createChooser(emailIntent, Cv.CHOOSER_TITLE));
    }

    public static Uri resourceToUri (Context context,int resID) {

        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID) );
    }
}