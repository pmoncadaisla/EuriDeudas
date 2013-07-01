package es.moncadaisla.eurideudas.util;

import android.graphics.Color;

public class ColorList {

	// private static final int[] COLORS = {Color.parseColor("#0099CC"),
	// Color.parseColor("#9933CC"),
	// Color.parseColor("#669900"),
	// Color.parseColor("#FF8800"),
	// Color.parseColor("#CC0000")};

	private static final int[] COLORS = { Color.parseColor("#0099CC"), Color.parseColor("#9933CC"),
			Color.parseColor("#669900"), Color.parseColor("#FF8800"), Color.parseColor("#CC0000"),

			Color.parseColor("#00AEDB"), Color.parseColor("#00B159"), Color.parseColor("#F37735"),
			Color.parseColor("#EC098C"), Color.parseColor("#D11141"), Color.parseColor("#FFC425"), };

	private static int id = 0;

	public static int getColor() {
		int color = COLORS[id];
		if (id >= COLORS.length - 1) {
			id = 0;
		} else {
			id++;
		}
		return color;
	}

	public static int getColor(int i) {
		if (i > COLORS.length) {
			i = (i % COLORS.length) - 1;
		}
		return COLORS[i];
	}
}
