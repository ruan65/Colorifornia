package com.engstuff.coloriphornia.data;

/**
 *  Cv stands for "Constant value"
 */
public interface Cv {

    String SAVED_COLORS = "user_saved_colors";
    String PREFS_RETAIN = "prefs_retain_colors";

    String EXTRA_MESSAGE_COLOR_1 = "color_parameters_1";
    String EXTRA_MESSAGE_TEXT_COLOR_1 = "text_color_parameters_1";
    String EXTRA_MESSAGE_COLOR_2 = "color_parameters_2";
    String EXTRA_MESSAGE_TEXT_COLOR_2 = "text_color_parameters_2";
    String EXTRA_MESSAGE_FONT_COLOR = "font_color";
    String CALLED_FROM_FAVORITES = "called_from_favorites";

    String CURRENT_COLOR_IMG = "current_color_img";

    String AIM_X = "aim_x";
    String AIM_Y = "aim_y";

    String CURRENT_IMAGE = "current_image";
    String SAVED_EMAILS = "user_saved_emails";
    String LAST_ACTIVITY = "last_used_activity";

    String G_YES = "yes";
    String G_NEXT = "next";
    String G_PREV = "prev";

    String LAST_COLOR_BOX_1 = "last_color_box1";
    String LAST_COLOR_BOX_2 = "last_color_box2";
    String LAST_COLOR_FONT = "last_color_font";
    String LAST_BACKGROUND = "font_back";

    String IMAGE_FRAGMENT_RETAINED = "retained_img_fragment";
    String LAST_COLOR = "last_color";
    String EMAIL_SUBJ = "Color parameters from \"Colorifornia\"";

    String CHOOSER_TITLE = "Send color(s) parameters...";

    int RED = 0xf00;
    int GREEN = 0x0f0;
    int BLUE = 0x00f;
    int ALPHA = 0x000;

    int INIT_RED = 0x68;
    int INIT_GREEN = 0x9f;
    int INIT_BLUE = 0x38;
    int INIT_ALPHA = 0xff;
}
