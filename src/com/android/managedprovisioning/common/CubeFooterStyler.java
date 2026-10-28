/*
 * Copyright (C) 2026 CubeOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.managedprovisioning.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.android.managedprovisioning.R;

import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.view.ButtonBarLayout;
import com.google.android.setupdesign.GlifLayout;

/**
 * CubeOS: gives the Glif footer the iOS setup look — a full-width rounded accent primary button
 * with a plain accent text secondary button stacked beneath it.
 *
 * <p>Purely visual: it never touches listeners, button types, text, enabled state or visibility,
 * which stay owned by {@link FooterBarMixin} and the calling screen.
 */
public final class CubeFooterStyler {

    private CubeFooterStyler() {}

    /** Restyles whatever footer buttons {@code layout} currently has. Safe to call repeatedly. */
    public static void apply(@Nullable GlifLayout layout) {
        if (layout == null) {
            return;
        }
        FooterBarMixin mixin = layout.getMixin(FooterBarMixin.class);
        if (mixin == null) {
            return;
        }
        Button primary = mixin.getPrimaryButtonView();
        Button secondary = mixin.getSecondaryButtonView();
        if (primary != null) {
            styleButton(primary, /* filled= */ true);
        }
        if (secondary != null) {
            styleButton(secondary, /* filled= */ false);
        }

        LinearLayout container = mixin.getButtonContainer();
        if (container == null) {
            return;
        }
        // Always stack: primary on top, secondary below, both full width.
        if (container instanceof ButtonBarLayout) {
            ((ButtonBarLayout) container).setStackedButtonForExpressiveStyle(true);
        }
        int gap = container.getResources().getDimensionPixelSize(R.dimen.cube_mp_button_gap);
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (!(child instanceof Button)) {
                continue;
            }
            ViewGroup.LayoutParams params = child.getLayoutParams();
            if (params instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) params;
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.weight = 0;
                lp.setMarginStart(0);
                lp.setMarginEnd(0);
                lp.leftMargin = 0;
                lp.rightMargin = 0;
                lp.topMargin = child == secondary ? gap / 2 : 0;
                lp.bottomMargin = child == primary ? gap / 2 : 0;
                child.setLayoutParams(lp);
            }
        }
        container.requestLayout();
    }

    private static void styleButton(Button button, boolean filled) {
        Context context = button.getContext();
        Resources res = context.getResources();

        ViewCompat.setBackgroundTintList(button, null);
        button.setBackgroundTintList(null);
        button.setBackground(context.getDrawable(
                filled ? R.drawable.cube_mp_button_primary : R.drawable.cube_mp_button_plain));
        button.setTextColor(context.getColorStateList(filled
                ? R.color.cube_mp_primary_button_text
                : R.color.cube_mp_secondary_button_text));
        button.setAllCaps(false);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                res.getDimension(R.dimen.cube_mp_body_size));
        button.setTypeface(Typeface.create(Typeface.SANS_SERIF, filled ? 600 : 400, false));
        button.setLetterSpacing(0f);
        button.setStateListAnimator(null);
        button.setElevation(0f);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        int height = res.getDimensionPixelSize(R.dimen.cube_mp_button_height);
        button.setMinHeight(height);
        button.setMinimumHeight(height);
        int horizontalPadding = res.getDimensionPixelSize(R.dimen.cube_mp_card_padding);
        button.setPadding(horizontalPadding, 0, horizontalPadding, 0);
    }
}
