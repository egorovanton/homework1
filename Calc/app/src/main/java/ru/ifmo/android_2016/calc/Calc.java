package ru.ifmo.android_2016.calc;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Stack;

import ru.ifmo.android_2016.calc.CalcParseException;

public class Calc {

    public static double calculate(String expr) throws CalcParseException {
        Matcher numberMatcher = Pattern.compile("\\d+(\\.\\d+)?")
                .matcher(expr);
        Stack<Double> output = new Stack<Double>();
        Stack<String> operatorStack = new Stack<String>();
        int index = 0;
        while (index < expr.length()) {
            if (numberMatcher.find(index) && numberMatcher.start() == index) {
                Double number = Double.parseDouble(expr.substring(index, numberMatcher.end()));
                output.add(number);
                index = numberMatcher.end();
                continue;
            }
            char ch = expr.charAt(index);
            switch (ch) {
                case '(':
                    operatorStack.push("(");
                    break;

                case ')' :
                    while (!operatorStack.peek().equals("(")) {
                        if (output.size() > 1) {
                            double v1 = output.pop();
                            double v2 = output.pop();
                            Operator op = Operator.fromString(operatorStack.pop());
                            if (op == null)
                                throw new CalcParseException();
                            output.push(op.calculate(v2,v1));
                        } else {
                            throw new CalcParseException();
                        }

                    }
                    operatorStack.pop();
                    break;

                default:
                    String opName = String.valueOf(expr.charAt(index));
                    Operator op1 = Operator.fromString(opName);
                    if (op1 == null)
                        throw new CalcParseException();

                    while(!operatorStack.empty()) {
                        Operator op2 = Operator.fromString(operatorStack.peek());

                        if (op2 != null &&
                                (op1.precedence < op2.precedence ||
                                        (op1.isLeftAssociative() && op1.precedence == op2.precedence))) {
                            operatorStack.pop();
                            if (output.size() > 1) {
                                double v1 = output.pop();
                                double v2 = output.pop();

                                output.push(op1.calculate(v2, v1));
                            } else {
                                throw new CalcParseException();
                            }
                        } else {
                            break;
                        }
                    }
                    operatorStack.push(opName);
            }
            index++;
        }
        while(!operatorStack.empty()) {
            if (output.size() > 1) {
                double v1 = output.pop();
                double v2 = output.pop();
                Operator op = Operator.fromString(operatorStack.pop());
                if (op == null)
                    throw new CalcParseException();
                output.push(op.calculate(v2, v1));
            } else {
                throw new CalcParseException();
            }
        }

        if (output.size() == 1)
            return output.pop();
        else
            throw new CalcParseException();
    }

    public static int LEFT_ASSOCIATIVITY = 0;
    public static int RIGHT_ASSOCIATIVITY = 1;

    private enum Operator {
        ADD(1) {

            public double calculate(double x, double y) {
                return x + y;
            }

        }, SUB(1) {

            public double calculate(double x, double y) {
                return x - y;
            }

        }, MULT(2) {

            public double calculate(double x, double y) {
                return x * y;
            }

        }, DIV(2) {

            public double calculate(double x, double y) {
                return x / y;
            }
        }, POW(3, RIGHT_ASSOCIATIVITY) {
            public double calculate(double x, double y) {
                return Math.pow(x, y);
            }
        };

        private int precedence;
        private int associativity;

        Operator(int p) {
            precedence = p;
            associativity = LEFT_ASSOCIATIVITY;
        }

        Operator(int p, int f) {
            precedence = p;
            associativity = f;
        }

        boolean isLeftAssociative() {
            return associativity == LEFT_ASSOCIATIVITY;
        }

        public static Operator fromString(String str) {
            if (str.equals("+")) {
                return ADD;
            }
            if (str.equals("-")) {
                return SUB;
            }
            if (str.equals("*")) {
                return MULT;
            }
            if (str.equals("/")) {
                return DIV;
            }
            if (str.equals("^")) {
                return POW;
            }
            return null;
        }

        public abstract double calculate(double x, double y);
    }


}