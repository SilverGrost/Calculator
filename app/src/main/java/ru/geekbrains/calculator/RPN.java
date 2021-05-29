package ru.geekbrains.calculator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.StringTokenizer;

public class RPN {
    private static final String operators = "+-*/^%√";
    private static final String delimiters = "() " + operators;
    private static String errorMsg = "";

    public RPN() {
        errorMsg = "";
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public static boolean isDelimiter(String token) {
        if (token.length() != 1)
            return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i))
                return true;
        }
        return false;
    }

    public static boolean isOperator(String token) {
        if (token.equals("u-"))
            return true;
        if (token.equals("%"))
            return true;
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i))
                return true;
        }
        return false;
    }

    private static boolean isFunction(String token) {
        return token.equals("√");
    }

    private static int priority(String token) {
        if (token.equals("("))
            return 1;
        if (token.equals("+") || token.equals("-"))
            return 2;
        if (token.equals("*") || token.equals("/") || token.equals("^") || token.equals("√"))
            return 3;
        if (token.equals("%"))
            return 4;
        return 5;
    }

    public static List<String> parse(String infix) {
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);

        String prev = "";
        String curr = "";

        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(curr) && !curr.equals("%")) {
                errorMsg = "Некорректное выражение";
                return postfix;
            }
            if (curr.equals(" "))
                continue;

            if (isFunction(curr))
                stack.push(curr);
            else
            if (isDelimiter(curr)) {
                if (curr.equals("("))
                    stack.push(curr);
                else
                if (curr.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                        if (stack.isEmpty()) {
                            errorMsg = "Скобки не согласованы";
                            return postfix;
                        }
                    }
                    stack.pop();
                    if (!stack.isEmpty() && isFunction(stack.peek())) {
                        postfix.add(stack.pop());
                    }
                }
                else {
                    if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev)  && !prev.equals(")")))) {
                        // унарный минус
                        curr = "u-";
                    }
                    else {
                        while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                            postfix.add(stack.pop());
                        }
                    }
                    stack.push(curr);
                }
            }
            else {
                postfix.add(curr);
            }
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek()))
                postfix.add(stack.pop());
            else {
                errorMsg = "Скобки не согласованы";
                return postfix;
            }
        }
        return postfix;
    }

    public static Double calc(List<String> postfix) {
        Deque<Double> stack = new ArrayDeque<>();
        Double a;
        Double b;
        Double tmp;
        for (String x : postfix) {
            switch (x) {
                case "√":
                    stack.push(Math.sqrt(stack.pop()));
                    break;
                case "%":
                    tmp = stack.pop();
                    stack.push(/*stack.peek() * */tmp / 100);
                    break;
                case "^":
                    tmp = stack.pop();
                    stack.push(Math.pow(stack.pop(), tmp));
                    break;
                case "+":
                    stack.push(stack.pop() + stack.pop());
                    break;
                case "-":
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a - b);
                    break;
                case "*":
                    stack.push(stack.pop() * stack.pop());
                    break;
                case "/": {
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a / b);
                    break;
                }
                case "u-":
                    stack.push(-stack.pop());
                    break;
                default:
                    stack.push(Double.valueOf(x));
                    break;
            }
        }
        return stack.pop();
    }
}