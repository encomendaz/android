package com.alienlabz.packagez.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontUtil {

	public static void setFutura(AssetManager assetManager, TextView... textViews) {
		Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/Futura-Condensed-Normal.ttf");

		for (TextView textView : textViews) {
			textView.setTypeface(typeface);
		}
	}

}
