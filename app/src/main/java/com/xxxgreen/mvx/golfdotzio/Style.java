package com.xxxgreen.mvx.golfdotzio;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MVX on 10/11/2017.
 */

public final class Style implements Parcelable {
    public final String STYLE_CODE;
    public final String STYLE_NAME;
    public final int SHEETS;
    public final int BACKING_CARDS;
    public final String LOCATION;
    public final int IS_POP;
    public final int IS_WHOLESALE;

    public Style(Cursor cursor) {
        this.STYLE_CODE = cursor.getString(0);
        this.STYLE_NAME = cursor.getString(1);
        this.SHEETS = cursor.getInt(2);
        this.BACKING_CARDS = cursor.getInt(3);
        this.LOCATION = cursor.getString(4);
        this.IS_POP = cursor.getInt(5);
        this.IS_WHOLESALE = cursor.getInt(6);
    }

    public Style(String code, String name, int sheets, int cards, String location,
                 int isPop, int isWholesale) {
        this.STYLE_CODE = code;
        this.STYLE_NAME = name;
        this.SHEETS = sheets;
        this.BACKING_CARDS = cards;
        this.LOCATION = location;
        this.IS_POP = isPop;
        this.IS_WHOLESALE = isWholesale;
    }

    protected Style(Parcel in) {
        this.STYLE_CODE = in.readString();
        this.STYLE_NAME = in.readString();
        this.SHEETS = in.readInt();
        this.BACKING_CARDS = in.readInt();
        this.LOCATION = in.readString();
        this.IS_POP = in.readInt();
        this.IS_WHOLESALE = in.readInt();
    }

    public static final Creator<Style> CREATOR = new Creator<Style>() {
        @Override
        public Style createFromParcel(Parcel in) {
            return new Style(in);
        }

        @Override
        public Style[] newArray(int size) {
            return new Style[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(STYLE_CODE);
        parcel.writeString(STYLE_NAME);
        parcel.writeInt(SHEETS);
        parcel.writeInt(BACKING_CARDS);
        parcel.writeString(LOCATION);
        parcel.writeInt(IS_POP);
        parcel.writeInt(IS_WHOLESALE);
    }
}
