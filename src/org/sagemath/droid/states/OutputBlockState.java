package org.sagemath.droid.states;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * The {@linkplain android.view.View.BaseSavedState} for {@linkplain org.sagemath.droid.views.OutputView}
 *
 * @author Nikhil Peter Raj
 */
public class OutputBlockState extends View.BaseSavedState {

    private String savedHtml;

    public OutputBlockState(Parcelable superState, String savedHtml) {
        super(superState);
        this.savedHtml = savedHtml;
    }

    private OutputBlockState(Parcel source) {
        super(source);
        this.savedHtml = source.readString();
    }

    public String getSavedHtml() {
        return savedHtml;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(savedHtml);
    }

    public static final Creator<OutputBlockState> CREATOR = new Creator<OutputBlockState>() {
        @Override
        public OutputBlockState createFromParcel(Parcel source) {
            return new OutputBlockState(source);
        }

        @Override
        public OutputBlockState[] newArray(int size) {
            return new OutputBlockState[size];
        }
    };
}
