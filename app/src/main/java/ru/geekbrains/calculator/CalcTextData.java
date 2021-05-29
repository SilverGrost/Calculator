package ru.geekbrains.calculator;

public class CalcTextData {
    private String tvHistory;
    private String tvInput;
    private String tvResult;
    private String arguments;

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
}
