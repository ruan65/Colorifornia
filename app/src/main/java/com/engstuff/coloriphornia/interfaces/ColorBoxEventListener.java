package com.engstuff.coloriphornia.interfaces;

import com.engstuff.coloriphornia.fragments.FragmentColorBox;

public interface ColorBoxEventListener {

    void onColorClicked(FragmentColorBox colorBox);

    void onInfoClicked(FragmentColorBox colorBox);

    void onTextColorChanged();
}
