package com.geniustechnoindia.purnodaynidhi.util;

import android.content.Context;
import android.graphics.ColorFilter;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by Rupam on 05/02/18.
 */

/***
 * No changing background on error
 * using this custom TextInputLayout. Otherwise setting error
 *
 */

public class NoChangingBackgroundTextInputLayout extends TextInputLayout {
    public NoChangingBackgroundTextInputLayout(Context context) {
        super(context);
    }

    public NoChangingBackgroundTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoChangingBackgroundTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        ColorFilter defaultColorFilter = getBackgroundDefaultColorFilter();
        super.setError(error);
        //Reset EditText's background color to default.
        updateBackgroundColorFilter(defaultColorFilter);
    }

    @Override
    protected void drawableStateChanged() {
        ColorFilter defaultColorFilter = getBackgroundDefaultColorFilter();
        super.drawableStateChanged();
        //Reset EditText's background color to default.
        updateBackgroundColorFilter(defaultColorFilter);
    }

    /**
     * If {@link #getEditText()} is not null & {@link #getEditText()#getBackground()} is not null,
     * update the {@link ColorFilter} of {@link #getEditText()#getBackground()}.
     *
     * @param colorFilter {@link ColorFilter}
     */

    private void updateBackgroundColorFilter(ColorFilter colorFilter) {
        if (getEditText() != null && getEditText().getBackground() != null)
            getEditText().getBackground().setColorFilter(colorFilter);
    }

    /**
     * Get the EditText's default background color.
     *
     * @return {@link ColorFilter}
     */

    @Nullable
    private ColorFilter getBackgroundDefaultColorFilter() {
        ColorFilter defaultColorFilter = null;
        if (getEditText() != null && getEditText().getBackground() != null)
            defaultColorFilter = DrawableCompat.getColorFilter(getEditText().getBackground());
        return defaultColorFilter;
    }

}
