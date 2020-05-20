package kea.motorhome.motorhomesite.util.calculation;
// package by KCN
import java.util.ArrayList;
import java.util.HashMap;


/* This solution satisfies a lot of lacks of the first one, and I believe
 * -- ++ added support for constants right now
 * -- support for unary operators upcoming
 * -- support for log, log10,  */

/**
 * The Calculation class allows the creation of objects able to perform complex calculations.
 * <p>You can check out the examples in the CalculationTest-class.</p>
 * <p> Please refer to the https://github.com/kiancn/FunctionBuilder/edit/master/README.md
 * for information on use of the class; until a stable version is reached. </p>
 * <p></p>
 * <p><b>Calculations are evaluated in the following sequence of steps:</b></p>
 * <p>a. first all (single) expressions are resolved
 * (binding operators are <i>not</i>).</p>
 * <p>b. second the 'un-bound' expressions are interpreted as belonging to one single expression consisting
 * of the results
 * of the previous calculations AND the binding operators (:a binding operators bind evaluated expressions
 * through its operation upon them (and the result)); it is resolved/calculated, and there is your
 * answer...</p>
 * <p></p>
 * <p> <b>A single expression</b> consist of factors (numbers) and operators; the term is here used so that
 * an expression is any set of factors and operators that can be evaluated/calculated going from left to
 * right (see rules).</p>
 * <p>A <b>binding operator is any operator <i>+, -, /, *, mod, pow </i>or<i> root</i></b> acting on
 * two expressions that could be written in separate parenthesis, like <i>(2) * (5+7)</i>  - here * is a
 * binding operator.</p>
 */
public class Calculation
{

    private final int numberOfExpressions;
    private final int numberOfFactors; // total number of factors in calculation

    public int getNumberOfInputFactorsExpected()
    {
        return numberOfInputFactorsExpected;
    }

    private final int numberOfInputFactorsExpected; // number of arguments expected from user

    private HashMap<Integer, Double> constants; // <K,V> Key is position in sequence of factors, and V is
    // the constant itself; the HashMap is interpolated into the given arguments when calling calc() before
    // any calculations are done.

    private ArrayList<ArrayList<Operator>> listsOfExpressionOperators; // lists of operators; operators for
    // first round of calculations.
    private ArrayList<Operator> listOfBindingOperators;


    private boolean logCalculationToConsole; // if true, Calculation object will log process to console.

    private double[] currentInputFactors; // this field/attribute is reassigned every time calc() is called
    // and modified for constants before first calculations take place.

    public Calculation(String calculation){
        this(new TextCalculation(calculation).getBuilder());
    }

    private Calculation(CalcBuilder builder)
    {
        listOfBindingOperators = builder.listOfBindingOperators;
        listsOfExpressionOperators = builder.listsOfExpressionOperators;
        numberOfExpressions = builder.numberOfExpressions;
        numberOfFactors = builder.numberOfFactors;
        numberOfInputFactorsExpected = builder.numberOfArgumentsRequired;  // this

        constants = builder.constants;
    }

    public static double equateIt(Calculation calculation, double... values)
    {
        return calculation.calc(values);
    }

    /** Single use Calculation object returns the result of your input values put though
     * the calculation you specified in the calculation-String */
    public static double equateIt(String calculation, double... values){
        return new Calculation(calculation).calc(values);
    }

    /**
     * Returns the result of the calculation
     * should fail fast if number of factors does not equal the expected number.
     */
    public double calc(double... inputFactors)
    {
        currentInputFactors = inputFactors;

        /* if logging is turned on, print info: */
        if(logCalculationToConsole)
        {
            System.out.print("\nSupplied values: ");
            for(double value : inputFactors) { System.out.print("[" + value + "] "); }
            System.out.println("\nExpression operators:    " + listsOfExpressionOperators);
            System.out.println("Binding operators: " + listOfBindingOperators);
        }

        /* do check of number of factors supplied/expected; abort if no match is found  */
        if(currentInputFactors.length != numberOfInputFactorsExpected)
        {
            System.out.println("From Calculation: Wrong number of arguments given:" + currentInputFactors.length
                               + ". This equation requires " + numberOfInputFactorsExpected + " arguments.");
            return -1;
        }

        /* inserting constants in supplied sequence of factors if necessary */
        if(numberOfFactors != numberOfInputFactorsExpected) // if these are not equal, there are constants
        {
            /* ArrayList is temporary home of factor list under construction*/
            currentInputFactors = includeConstantsWithInputFactors(currentInputFactors);
        }

        /* holds results of each expression (results from first round of calcs) */
        double[] expressionResults = new double[numberOfExpressions];
        /**/
        int positionInFactorCount = 0;

        /* Calculating 'first round' of expressions (expressions between binding operators),
         * iterating through the lists of expression operators while going through array of actual factors */

        for(int expressionCount = 0; expressionCount < numberOfExpressions; expressionCount++)
        {
            expressionResults[expressionCount] =
                    calculateExpression(listsOfExpressionOperators.get(expressionCount),
                                        currentInputFactors,
                                        positionInFactorCount);

            positionInFactorCount += listsOfExpressionOperators.get(expressionCount).size() + 1;
        }

        /* if there are no binding operators, calculation has completed; so, return that calculation */
        if(listOfBindingOperators.size() == 0)
        {
            return expressionResults[expressionResults.length - 1];
        }

        /* else, do 'second round' of calculations on the results of first round of calculation; then return
        that result */
        return calculateExpression(listOfBindingOperators,
                                   expressionResults,
                                   0);
    }

    private double[] includeConstantsWithInputFactors(double[] inputFactors)
    {
        if(logCalculationToConsole){ System.out.println("Constants:" + constants); }

        ArrayList<Double> tempListOfFactors = new ArrayList<>();

        /* adding each number to list (do not want to use Arrays.toList, it links the list and the array.) */
        for(double number : inputFactors) { tempListOfFactors.add(number); }

            /* going through HashMap constants, and adding/interpolating constants to/with temp list of
            factors (see source of this solution at bottom ) */
        for(HashMap.Entry<Integer, Double> constantsEntry : constants.entrySet())
        {
            /* adding each value at proper place in temp list */
            tempListOfFactors.add(constantsEntry.getKey(), constantsEntry.getValue());
            // since add(int,T) attempts to add T-element at supplied index, it should fail when
            // trying to add an element beyond it current size , but doesn't; i believe the documentation is
            // faulty, and that an element added at 'last index + 1' are simply add()'ed to the list, at
            // the now existent index position (so it'd fail when sought index overshot by more than one).
        }


        /* Placing the newly unified complete set of factors into an array and returning it */
        double[] unifiedFactorsArray = new double[tempListOfFactors.size()];
        for(int i = 0; i < unifiedFactorsArray.length; i++)
        {
            unifiedFactorsArray[i] = tempListOfFactors.get(i);
        }

        if(logCalculationToConsole)
        {
            System.out.print("Full factor list:");
            for(double number : unifiedFactorsArray) { System.out.print(" [" + number + "]"); }
            System.out.println();
        }

        return unifiedFactorsArray;
    }

    private double calculateExpression(ArrayList<Operator> operators,
                                       double[] inputFactors,
                                       int positionInFactorCount)
    {
        double tempResult = inputFactors[positionInFactorCount];

        if(logCalculationToConsole)
        {
            System.out.println("Calculation starting at position " + positionInFactorCount);
            System.out.print("Calculation: " + tempResult);
        }

        /* later improvements: it seems the unary operators will be their own switch statement; so actually a
        method for unary operation expressions, and a method for binary operation statements .. */

        /* ' i ' is set up like it is because the traversal of actual input-Factors
         happens across the calculation of multiple expressions (the first round of calculations
         go though user input arguments/numbers (and soon 'constants'); the second rounds goes through the
         results of the first round of calculated expressions. */
        for(int i = positionInFactorCount + 1, j = 0; j <= operators.size() - 1; i++, j++)
        {
            switch(operators.get(j))
            {
                case Plus:
                    tempResult += inputFactors[i];
                    break;
                case Minus:
                    tempResult -= inputFactors[i];
                    break;
                case Multiply:
                    tempResult *= inputFactors[i];
                    break;
                case Divide:
                    tempResult /= inputFactors[i];
                    break;
                case Mod:
                    tempResult %= inputFactors[i];
                    break;
                case Pow:
                    tempResult = Math.pow(tempResult, inputFactors[i]);
                    break;
                case Root:
                    tempResult = Math.pow(Math.exp(1 / inputFactors[i]), Math.log(tempResult));
                    break;
            }

            if(logCalculationToConsole){System.out.print(" " + operators.get(j) + " " + inputFactors[i]);}
        }

        if(logCalculationToConsole){System.out.println(" = " + tempResult);}

        return tempResult;
    }

    /**
     * Call method with to 'true' turn on console-logging of calculation process and calculation stats on
     */
    public void logToConsole(boolean logCalculationToConsole)
    {
        this.logCalculationToConsole = logCalculationToConsole;
    }

    /* Internal class - builder pattern ... electric magic */

    public static class CalcBuilder
    {
        private int numberOfExpressions;
        private int numberOfFactors; // total number of factors in calculation

        private int numberOfConstants; // registered constants (each constant mean -=1 to
        //                                           numberOfInputFactorsForCalculation)
        private int numberOfArgumentsRequired; // number of arguments required by final calculation

        private boolean newExpression = false; // boolean flips to true, when expression() is fired,
        //                                        which implies that the next operator is going to be
        //                                        interpreted as a binding operator.

        private ArrayList<Operator> listOfBindingOperators;
        private ArrayList<ArrayList<Operator>> listsOfExpressionOperators;

        private HashMap<Integer, Double> constants; // K = pos in sequence of factors, V = constant value


        public CalcBuilder create()
        {
            listOfBindingOperators = new ArrayList<>();
            listsOfExpressionOperators = new ArrayList<>();
            listsOfExpressionOperators.add(new ArrayList<>()); // newing up the first list

            numberOfExpressions = 0;
            numberOfFactors = 0;
            newExpression = false;

            constants = new HashMap<>();

            return this;
        }

        /** Calling the method returns a Calculation object: Method to be used in conjunction with builder
         * pattern (where the method is the last one called in the defining series of calls).*/
        public Calculation build()
        {
            numberOfFactors += 1; // because, after the last operator add, there is always room for the
            // factor it works on
            numberOfExpressions += 1; // because there is always at least one expression.
            /* expected number of arguments from user will be the total number of factors minus the number
            of constants in the calculation */
            numberOfArgumentsRequired = numberOfFactors - numberOfConstants;


            return new Calculation(this);
        }

        // tiny utility, because repetition makes me hate typing
        private CalcBuilder addOperator(Operator operator)
        {
            /* if its a new expression, the next sign must be the desired connecting operator */
            if(newExpression)
            {
                listOfBindingOperators.add(operator);

                listsOfExpressionOperators.add(new ArrayList<Operator>());

                numberOfExpressions++;
                newExpression = false;
            } else
            {
                /* add operator to the appropriate current list of operators */
                listsOfExpressionOperators.get(numberOfExpressions).add(operator);
            }

            numberOfFactors++; // maybe this will allow constants the right position in factor list
            return this;
        }

        /**
         * Conceptually expression() indicates the separation of two expression
         * AND that next mentioned operator is a binding operator: this means
         * <p>a) that <i>that next operator</i> will act on all previously
         * calculated expressions,</p>
         * <p>and b) the operator(-method) after the binding operator starts a new expression</p>
         * <p>Important: if expression() is placed before any other operator, like this;
         * <p><p><i>create().expression().<\operator>()>. ... </i></p></p>
         * the first factor in the calculation (when calling calc()) will be acted upon
         * by the operator defined by the call following the expression()-call), so;</p>
         * <p><p><i>create().expression().<\OPERATOR>()>. ... </i></p></p>
         * <p> <>See examples is test class for clarification for now. I'm out of juice.<> </p>
         */
        public CalcBuilder expression()
        {
            newExpression = true;
            return this;
        }

        /**
         * Registers supplied constant
         * Calling constant registers the current position (in the calculation being built)
         * where a constant is desired, and 'saves' that constant for 'recall' at the
         * time when calc() is called; at which point all constants will be reinserted into
         * the list of user input factors to form the list of actual factors, that a calculation is
         * performed on.
         */
        public CalcBuilder constant(double constant)
        {
            /* the numberOfFactors number argumented/supplied will be the POSITION in sequence of factors for
            calculation AT WHICH the constant is added when running calc() on Calculation object */

            constants.put(numberOfFactors, constant);
            numberOfConstants++;
            return this;
        }

        public CalcBuilder plus()
        {
            return addOperator(Operator.Plus);
        }

        public CalcBuilder minus()
        {
            return addOperator(Operator.Minus);
        }

        public CalcBuilder divideBy()
        {
            return addOperator(Operator.Divide);
        }

        public CalcBuilder modulus()
        {
            return addOperator(Operator.Mod);
        }

        // I decided off the bat to double the potency method, because I like the second name better. It's
        // bad style, I know. Functionality is identical; pow == raisedBy = true ....
        public CalcBuilder pow()
        {
            return addOperator(Operator.Pow);
        }

        public CalcBuilder raisedBy()
        {
            return addOperator(Operator.Pow);
        }

        public CalcBuilder root()
        {
            return addOperator(Operator.Root);
        }

        public CalcBuilder multiply()
        {
            return addOperator(Operator.Multiply);
        }
    }
}

/* Sources helpful during construction
 * https://stackoverflow.com/questions/46898/how-do-i-efficiently-iterate-over-each-entry-in-a-java-map
 * */