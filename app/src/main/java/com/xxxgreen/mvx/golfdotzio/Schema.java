package com.xxxgreen.mvx.golfdotzio;

import android.provider.BaseColumns;

/**
 * Created by MVX on 10/11/2017.
 */

public final class Schema {
    // constructor is private to prevent instantiation
    private Schema() {}

    // table contents
    public static class SheetColumns implements BaseColumns {
        public static final String TABLE_NAME = "STYLE";
        public static final String COL_1 = "STYLE_CODE";
        public static final String COL_2 = "STYLE_NAME";
        public static final String COL_3 = "SHEETS";
        public static final String COL_4 = "BACKING_CARDS";
        public static final String COL_5 = "LOCATION";
        public static final String COL_6 = "IS_POP";
        public static final String COL_7 = "IS_WHOLESALE";
    }
}
