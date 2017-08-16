package pe.edu.upc.parknina.models.login_loading;

import android.content.Context;

/**
 * Created by hugo_ on 23/07/2017.
 */

public class DensityUtil {
    private DensityUtil() {

    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
