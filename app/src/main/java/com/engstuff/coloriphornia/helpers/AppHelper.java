package com.engstuff.coloriphornia.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.FullScreenColorC;
import com.engstuff.coloriphornia.data.Cv;
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
        box.getInfo().setImageResource(
                box.isWhiteText()
                        ? R.drawable.ic_info_white
                        : R.drawable.ic_info_black
        );
    }

    public static void setLikesAndInfo(Context ctx, FragmentColorBox... boxes) {
        for (FragmentColorBox box : boxes) {
            setInfoIcon(box);
            setLike(ctx, box);
        }
    }
}
