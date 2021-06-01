package ru.geekbrains.calculator;

import android.os.Parcel;
import android.os.Parcelable;

public class CalcTextData implements Parcelable {
    private String tvHistory;
    private String tvInput;
    private String tvResult;
    private String arguments;

    protected CalcTextData(Parcel in) {
        tvHistory = in.readString();
        tvInput = in.readString();
        tvResult = in.readString();
        arguments = in.readString();
    }

    public static final Creator<CalcTextData> CREATOR = new Creator<CalcTextData>() {
        @Override
        public CalcTextData createFromParcel(Parcel in) {
            return new CalcTextData(in);
        }

        @Override
        public CalcTextData[] newArray(int size) {
            return new CalcTextData[size];
        }
    };

    public String getArguments() {
        return arguments;
    }

    public String getTvHistory() {
        return tvHistory;
    }

    public String getTvInput() {
        return tvInput;
    }

    public String getTvResult() {
        return tvResult;
    }

    public void setTvHistory(String tvHistory) {
        this.tvHistory = tvHistory;
    }

    public void setTvInput(String tvInput) {
        this.tvInput = tvInput;
    }

    public void setTvResult(String tvResult) {
        this.tvResult = tvResult;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public CalcTextData() {
        this.tvHistory = "";
        this.tvInput = "";
        this.tvResult = "";
        arguments = "";
    }

    public CalcTextData(String tvHistory, String tvInput, String tvResult, String arguments) {
        this.tvHistory = tvHistory;
        this.tvInput = tvInput;
        this.tvResult = tvResult;
        this.arguments = arguments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tvHistory);
        dest.writeString(tvInput);
        dest.writeString(tvResult);
        dest.writeString(arguments);
    }
}
