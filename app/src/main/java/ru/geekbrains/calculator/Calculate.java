package ru.geekbrains.calculator;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class Calculate {

    public static final int MAXDIGITS = 16;

    public static final String operators = "+-*/^%√";
    public static final String delimiters = "() " + operators;

    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    public static final char delFloat = symbols.getDecimalSeparator();
    public static final char delRank = symbols.getGroupingSeparator();



    public static double cDouble;


    public static String frmt(int cntAfterDot){
        return "%." + cntAfterDot + "f";
    }


    public static String setCntAfterDot(double numb){
        String numbStr = String.valueOf(numb);
        int ePosition = numbStr.indexOf('E');

        if (ePosition != -1){
            String cnt = "0";
            if (ePosition - 1 > 0) {
                cnt = numbStr.substring(ePosition + 1);
            }
            int cntAfterDot = 0;
            try {
                cntAfterDot = Integer.parseInt(cnt);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (cntAfterDot < MAXDIGITS && cntAfterDot > 0) {
                numbStr = String.format(Locale.getDefault(), frmt(cntAfterDot), numb);
            }
            if (cntAfterDot < 0) {
                numbStr = String.format(Locale.getDefault(), frmt(14), numb);
            }
            numbStr = numbStr.replace(Character.toString(delRank), "");
            numbStr = numbStr.replace(Character.toString(delFloat), ".");
            numbStr = numbStr.replaceAll("\\.(.*?)0+$", ".$1").replaceAll("\\.$", "");
        }
        if (numbStr.length() > 2) {
            char last = numbStr.charAt(numbStr.length() - 1);
            char prev = numbStr.charAt(numbStr.length() - 2);
            if (last == '0' & prev == '.') {
                return String.format(Locale.getDefault(), frmt(0), numb);
            }
        }
        return numbStr;
    }

    public static int getCntAfterDot(String numbStr){
        int ePosition = numbStr.indexOf('E');
        if (ePosition != -1){
            return -2;
        }
        else {
            if (numbStr.length() > 2) {
                char last = numbStr.charAt(numbStr.length() - 1);
                char prev = numbStr.charAt(numbStr.length() - 2);
                if (last == '0' & prev == '.') {
                    return 0;
                }
            }
            int res = numbStr.indexOf('.');
            if (res == -1)
                return 0;
            else
                return numbStr.length() - res - 1;
        }
    }


    public static String setFormat(String arguments) {
        ArrayList<String> arg = new ArrayList<>();
        String curr;
        StringTokenizer tokenizer = new StringTokenizer(arguments, delimiters, true);
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            curr = curr.replace(Character.toString(delRank), "");
            curr = curr.replace(Character.toString(delFloat), ".");
            if (!RPN.isOperator(curr) & !RPN.isDelimiter(curr)) {
                int cnt = getCntAfterDot(curr);
                double currDouble = Double.parseDouble(curr);
                if (cnt != -2)
                    curr = String.format(Locale.getDefault(), "%,." + cnt + "f", currDouble);
            }
            arg.add(curr);
        }
        StringBuilder argumentsBuilder = new StringBuilder();
        for (int i = 0; i < arg.size(); i++) {
            argumentsBuilder.append(arg.get(i));
        }
        arguments = argumentsBuilder.toString();
        return arguments;
    }

    public static CalcTextData procInput(CalcTextData input, String btn) {
        CalcTextData result = new CalcTextData();

        String arguments = input.getArguments();

        result.setTvHistory(input.getTvHistory());


        String digits = "0123456789兀";
        String operations = "()^√";
        String operations2 = "%/*-+";

        if (digits.contains(btn)) {
            if (btn.equals("兀"))
                arguments = arguments + Math.PI;
            else
                arguments = arguments + btn;
            arguments = setFormat(arguments);

        }

        if (operations.contains(btn)) {
            arguments = arguments + btn;

        }

        if (btn.equals(".")) {
            arguments = arguments + delFloat;

        }


        if (btn.equals("⇐")) {
            if (arguments.length() > 0) {
                if (arguments.length() == 1)
                    arguments = "";
                else
                    arguments = arguments.substring(0, arguments.length() - 1);
            }
        }

        if (operations2.contains(btn)) {
            if (arguments.length() > 0){
                if (!RPN.isOperator(arguments.substring(arguments.length() - 1))){
                    arguments = arguments + btn;
                }
            }

        }

        result.setTvInput(arguments);

        if (btn.equals("=")) {
            result.setTvInput(arguments + "=");


            List<String> expression = RPN.parse(arguments.replace(Character.toString(delRank), "").replace(Character.toString(delFloat), "."));
            if (!RPN.getErrorMsg().equals(""))
                result.setTvResult(RPN.getErrorMsg());
            else
                cDouble = RPN.calc(expression);

            result.setTvResult(String.valueOf(cDouble));
            if (result.getTvResult().equals("Infinity"))
                result.setTvResult("∞");
            else{
                result.setTvResult(setCntAfterDot(cDouble));
                result.setTvResult(setFormat(result.getTvResult()));
            }

            if (result.getTvHistory().equals(""))
                result.setTvHistory(arguments + "=" + result.getTvResult());
            else
                result.setTvHistory(result.getTvHistory() + "\n" + arguments + "=" + result.getTvResult());
            arguments = result.getTvResult();
        }

        result.setArguments(arguments);

        return result;
    }
}
