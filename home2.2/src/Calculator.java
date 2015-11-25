import java.math.BigInteger;
import java.util.Vector;


/**
 * This Program evaluates mathematical expression based on
 * given precedence
 *
 * @author Abhishek Indukumar
 * 
 * @version 2.0 : Calculator.java, 2015/09/07
 */
class Calculator {
    private Vector<String> expressionTokens = new Vector<String>();
    private String precedence;
    public static final String DUMMY_OPERATOR = "D";
    public static final String PRECEDENCE = "(+-*/%^";

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setOperationPrecedence(PRECEDENCE);
        System.out.println(calculator.evaluate(args));
    }

    /**
     * @param precedence
     */
    public void setOperationPrecedence(String precedence) {
        this.precedence = precedence;
    }

    /**
     * @param expression
     * @return
     */
    public BigInteger evaluate(String[] expression) {
        pushToList(expression);

        //Invokes handlers based on precedence
        for (char c : precedence.toCharArray()) {
            switch (c) {
                case ('+'):
                    expressionTokens = handleAddition(expressionTokens);
                    break;
                case ('-'):
                    expressionTokens = handleSubtraction(expressionTokens);
                    break;
                case ('%'):
                    expressionTokens = handleModuloOperation(expressionTokens);
                    break;
                case ('*'):
                    expressionTokens = handleMultiplication(expressionTokens);
                    break;
                case ('/'):
                    expressionTokens = handleDivision(expressionTokens);
                    break;
                case ('^'):
                    expressionTokens = handleExponentOperation(expressionTokens);
                    break;
                case ('('):
                    expressionTokens = handleParenthesis(expressionTokens);
                    break;
            }
        }

        return new BigInteger(expressionTokens.get(0));
    }

    /**
     * Takes expression tokens and adds it to expression token vector.
     *
     * @param tokens expression tokens
     */
    private void pushToList(String[] tokens) {
        for (String token : tokens) {
            expressionTokens.add(token);
        }
    }


    /**
     * Handles Addition Operation
     *
     * @param tokens Expression tokens
     * @return Tokens after evaluation
     */
    private Vector<String> handleAddition(Vector<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if ("+".equals(tokens.get(i))) {
                tokens.set(i, Calculator.DUMMY_OPERATOR);

                tokens.set(i + 1,
                        new BigInteger(tokens.get(i - 1)).add(new
                                BigInteger(tokens.get(i + 1))).toString());

                tokens.set(i - 1, Calculator.DUMMY_OPERATOR);
            }
        }
        return compactList(tokens);
    }


    /**
     * Handles Subtraction
     *
     * @param tokens Expression tokens
     * @return Tokens after evaluation
     */
    private Vector<String> handleSubtraction(Vector<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if ("-".equals(tokens.get(i))) {
                tokens.set(i, Calculator.DUMMY_OPERATOR);

                tokens.set(i + 1,
                        new BigInteger(tokens.get(i - 1)).subtract(new
                                BigInteger(tokens.get(i + 1))).toString());

                tokens.set(i - 1, Calculator.DUMMY_OPERATOR);
            }
        }
        return compactList(tokens);
    }


    /**
     * Handles division
     *
     * @param tokens Expression tokens
     * @return Tokens after evaluation
     */
    private Vector<String> handleDivision(Vector<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if ("/".equals(tokens.get(i))) {
                tokens.set(i, Calculator.DUMMY_OPERATOR);

                tokens.set(i + 1,
                        new BigInteger(tokens.get(i - 1)).divide(new
                                BigInteger(tokens.get(i + 1))).toString());

                tokens.set(i - 1, Calculator.DUMMY_OPERATOR);
            }
        }
        return compactList(tokens);
    }


    /**
     * Handles Multiplication
     *
     * @param tokens Expression tokens
     * @return Tokens after evaluation
     */
    private Vector<String> handleMultiplication(Vector<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if ("*".equals(tokens.get(i))) {
                tokens.set(i, Calculator.DUMMY_OPERATOR);

                tokens.set(i + 1,
                        new BigInteger(tokens.get(i - 1)).multiply(new
                                BigInteger(tokens.get(i + 1))).toString());

                tokens.set(i - 1, Calculator.DUMMY_OPERATOR);
            }
        }
        return compactList(tokens);
    }


    /**
     * Handles Modulo operation
     *
     * @param tokens Expression tokens
     * @return Tokens after evaluation
     */
    private Vector<String> handleModuloOperation(Vector<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if ("%".equals(tokens.get(i))) {
                tokens.set(i, Calculator.DUMMY_OPERATOR);

                tokens.set(i + 1,
                        new BigInteger(tokens.get(i - 1)).mod(new
                                BigInteger(tokens.get(i + 1))).toString());

                tokens.set(i - 1, Calculator.DUMMY_OPERATOR);
            }
        }
        return compactList(tokens);
    }


    /**
     * Handles Exponent operation
     *
     * @param tokens Expression tokens
     * @return Tokens after evaluation
     */
    private Vector<String> handleExponentOperation(Vector<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if ("^".equals(tokens.get(i))) {
                tokens.set(i, Calculator.DUMMY_OPERATOR);

                tokens.set(i + 1,
                        (new BigInteger((tokens.get(i - 1)))).pow(new Integer
                                (tokens.get(i + 1))).toString());

                tokens.set(i - 1, Calculator.DUMMY_OPERATOR);
            }
        }
        return compactList(tokens);
    }


    /**
     * Handles Parenthesis
     *
     * @param tokens Expression tokens
     * @return Tokens after evaluation
     */
    private Vector<String> handleParenthesis(Vector<String> tokens) {
        int openingIndex = 0;
        int closingIndex = 0;
        boolean foundOpeningIndex = false;
        int depth = 0;
        for (int i = 0; i < tokens.size(); i++) {
            if ("(".equals(tokens.get(i))) {

                //First open parenthesis
                if (depth == 0)
                    openingIndex = i;

                foundOpeningIndex = true;
                depth++;
            }

            if (foundOpeningIndex && ")".equals(tokens.get(i))) {

                //Continue until last parenthesis
                if (--depth != 0) {
                    continue;
                }

                //If ")" found without "(" throw exception
                if (!foundOpeningIndex && depth == 0) {
                    throw new RuntimeException("Malformed expression");
                } else {
                    closingIndex = i;
                    foundOpeningIndex = false;
                }

                //create a calculator object and evaluate
                if (closingIndex != 0) {
                    Calculator calculator = new Calculator();
                    calculator.setOperationPrecedence(Calculator.PRECEDENCE);
                    String result = calculator.evaluate(tokens.subList
                            (openingIndex + 1, closingIndex).
                            toArray(new String[closingIndex - openingIndex
                                    - 2])).toString();
                    tokens.set(openingIndex, result);
                    fillInDummy(tokens, openingIndex + 1, closingIndex);
                }
            }
        }
        return compactList(tokens);
    }

    /**
     * Fills in Dummy values in token vector fromIndex to toIndex
     *
     * @param tokens    Expression tokens
     * @param fromIndex From Index
     * @param toIndex   To Index
     */
    private static void fillInDummy(Vector<String> tokens, int fromIndex,
                                    int toIndex) {
        for (int i = fromIndex; i <= toIndex; i++) {
            tokens.set(i, Calculator.DUMMY_OPERATOR);
        }
    }

    /**
     * @param list Input list with Dummy characters
     * @return return new list without Dummy character.
     */
    protected Vector<String> compactList(Vector<String> list) {
        Vector<String> newList = new Vector<String>(list.size());

        for (String string : list) {
            if (!string.equals(Calculator.DUMMY_OPERATOR)) {
                newList.add(string);
            }
        }
        return newList;
    }
}
