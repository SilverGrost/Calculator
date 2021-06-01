package ru.geekbrains.calculator;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class Calculate {

    private static final int MAXDIGITS = 16;
    private static final String operators = "+-*/^%√";
    private static final String delimiters = "() " + operators;
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    private static final char delFloat = symbols.getDecimalSeparator();
    private static final char delRank = symbols.getGroupingSeparator();
    private static double cDouble;

    // Форматируем строку и устанавливаем заданное кол-во символов после запятой
    public static String frmt(int cntAfterDot) {
        return "%." + cntAfterDot + "f";
    }

    // Устанавливаем кол-во символов после запятой (не больше 16 всего)
    public static String setCntAfterDot(double numb) {
        String numbStr = String.valueOf(numb);
        int ePosition = numbStr.indexOf('E');

        // Проверяем на экспонциональный вид и если можно - то убираем его
        if (ePosition != -1) {
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

        // Убираем незначущие нули после запятой
        if (numbStr.length() > 2) {
            char last = numbStr.charAt(numbStr.length() - 1);
            char prev = numbStr.charAt(numbStr.length() - 2);
            if (last == '0' & prev == '.') {
                return String.format(Locale.getDefault(), frmt(0), numb);
            }
        }
        return numbStr;
    }

    // Получаем кол-во символов после запятой
    public static int getCntAfterDot(String numbStr) {
        int ePosition = numbStr.indexOf('E');
        if (ePosition != -1) {
            return -2;
        } else {
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

    // Установка формата (добавляем непереносимый пробел между разрядами, экспонциоальный вид и пр.) для каждого числа в строке ввода
    public static String setFormat(String arguments) {
        ArrayList<String> arg = new ArrayList<>();
        String curr;

        // Парсим через StringTokenizer все числа (выкидываем все разделители)
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

    // Обрабатываем нажатия кнопок
    public static CalcTextData procInput(CalcTextData input, String btn) {
        CalcTextData result = new CalcTextData();

        String arguments = input.getArguments();

        result.setTvHistory(input.getTvHistory());

        String digits = "0123456789兀";
        String operations = "()^√";
        String operations2 = "%/*-+";

        // Обработка цифр и числа Pi
        if (digits.contains(btn)) {
            if (btn.equals("兀"))
                arguments = arguments + Math.PI;
            else
                arguments = arguments + btn;
            arguments = setFormat(arguments);
        }

        // Обработка набора операций №1
        if (operations.contains(btn)) {
            arguments = arguments + btn;
        }

        // Обработка кнопки Точка
        if (btn.equals(".")) {
            arguments = arguments + delFloat;
        }

        // Обработка кнопки BackSpace
        if (btn.equals("⇐")) {
            if (arguments.length() > 0) {
                if (arguments.length() == 1)
                    arguments = "";
                else
                    arguments = arguments.substring(0, arguments.length() - 1);
            }
        }

        // Обработка набора операций №2 (в отличии от набора №1 - идёт проверка на наличие символов впереди)
        if (operations2.contains(btn)) {
            if (arguments.length() > 0) {
                if (!RPN.isOperator(arguments.substring(arguments.length() - 1))) {
                    arguments = arguments + btn;
                }
            }
        }

        // Записываем текущий результат ввода
        result.setTvInput(arguments);

        // Обработка кнопки =
        if (btn.equals("=")) {

            // Записываем текущий результат ввода и добавляем знак "="
            result.setTvInput(arguments + "=");

            // Заберём текущую строку для парсинга в класс Обратной польской нотации и проверим на наличие ошибок
            List<String> expression = RPN.parse(arguments.replace(Character.toString(delRank), "").replace(Character.toString(delFloat), "."));
            if (!RPN.getErrorMsg().equals(""))
                result.setTvResult(RPN.getErrorMsg());
            else
                cDouble = RPN.calc(expression);

            result.setTvResult(String.valueOf(cDouble));
            // Проверим на Infinity в результате и заменим на знак бесконечности, иначе отформатирруем результат
            if (result.getTvResult().equals("Infinity"))
                result.setTvResult("∞");
            else {
                result.setTvResult(setCntAfterDot(cDouble));
                result.setTvResult(setFormat(result.getTvResult()));
            }

            // Заполним Исторрию
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
