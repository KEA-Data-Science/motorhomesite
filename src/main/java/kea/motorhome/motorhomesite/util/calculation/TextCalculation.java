package kea.motorhome.motorhomesite.util.calculation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DECLARATION OF INTENT:
 * <p>Class will allow two modes of use:
 * - one where an instance in initialized through the constructor,
 * - and one where it is called - unnoticed - from a Calculation-object constructor;
 * this is the most powerful way to use this class (constructor is Calculation(String calc)).
 * <p></p>
 * - this class's purpose is for instances to serve as initializers for
 * Calculation objects. Not sure if it has a technical names.
 * - class basically parses a string and delivers a Calculation</p>
 * <p></p>
 * <p><b>Syntax for constructing calculation strings:</b></p>
 * <p>First char must be a '('. Last character must be a ')'.</p>
 * <p>Second char of a calc string must be either a number (marking a constant),
 * or an n (marking a variable input, which can then be supplied when running calc(double...)
 * to do a calculation).</p>
 * <p>In general a calculation can consist of factors (constants or input variables) and operators;
 * </p>
 * <p>Implemented operators are: <b>/ , * , - , + , r (root), p (pow), m (mod)</b></p>
 * <p>All implemented operators are accept two factors and return a double.</p>
 * <p></p>
 * <p><b>Single expressions</b> are written between parenthesis; a single expression
 * is any combination of factors and operators that can be resolved going strictly left
 * to right.</p>
 * <p>Negative constants are prefixed with ' ~ ' (and NOT - ) </p>
 * <p>Unlike normal java math notation, this is a possible single expression: (n-n*n)</p>
 * In customary math notation you'd write that like: (n-n)*n
 * <p></p>
 * <p>A few examples:
 * <p>TextCalculation syntax: 4*~5/5</p>
 * <p>Orinary math syntax: ((4*-5)/5)</p>
 * <p>TextCalculation syntax: 1+3*5/6</p>
 * <p>Orinary math syntax: (((1+3)*5)/6)</p>
 * <p>TextCalculation syntax: 4*5-6*5p2+1</p>
 * <p>Orinary math syntax:((((4*5)-6)*5)pow2)+1)</p>
 * <p><i>Be aware that in all examples, factors that you need to be input/
 * method parameter variables are written as ' n '. </i></p>
 * <p></p>
 * </p>
 * <p></p>
 * <p><b>Binding operators</b> is my term of art for operators placed <i>outside parenthesis</i>:
 * a binding operator is any operator mentioned above; like above, each operator is resolved
 * strictly going from left to right. A binding operator acts on the two evaluated expressions
 * immediately adjacent to it.</p>
 * <p><b>A binding operator is written as ')' 'operator' '('</b></p>
 * <p><b>Pattern :  )[/*-+rm] ( </b></p>
 * <p>TextCalculation syntax: 5+6*5)*(5/4</p>
 * <p>Orinary math syntax: ((5+6)*5)*(5/5)</p>
 * <p></p>
 * <p>You can get the pythagorean sentence to calculate the hypotenuse of a right angled triangle like: </p>
 * <p><i>Calculation hypotenuse = new Calculation("(n^2)+(n^2)r(2)");</i></p></>
 * <p>Or %-increase from A to B:</p>
 * <p><b>Calculation increase = new Calculation("");</b></p>
 */
public class TextCalculation
{
    /// this pattern matches either a double or an integer (internally all are cast to double)
    private final String numberPattern = "([~]?\\d{1,16}[.]?[,]?\\d{0,16}){1}";
    private final String operatorPattern = "[/*\\-+mr^]{1}";
    /* The object being built from the string supplied in constructor */
    private Calculation calculation;
    private String suppliedString; // string supplied for interpretation
    //    private String reconstructedString; // string supplied for interpretation
    private Pattern operatorAndNumber;
    private Pattern operatorAndN;
    private Pattern bindingOperator;
    private Pattern numberOrN;
    private Calculation.CalcBuilder calcBuilder;
    private boolean initializationOK; // boolean true signifies that the Calculation.CalcBuilder

    // this block is run when the class is instantiated, before the constructor is called.
    {
        operatorAndNumber = Pattern.compile(operatorPattern + numberPattern);
        operatorAndN = Pattern.compile(operatorPattern + "[n]");
        bindingOperator = Pattern.compile("[)][/*\\-+mr^]([(])");
        numberOrN = Pattern.compile("(" + numberPattern + "|[n]{1}){1}");
    }
    //was initialized without errors.

    /**
     * Use this constructor to immediately initialize a Calculation internally.
     * <p></p>
     * Immediate pattern would be:
     * <p>Calculation cal = new TextCalculation("(n+n)").getCalculation();</p>
     * <b>However</b>, this is unnecessary because this happens silently, when you do:
     * <p>Calculation cal = new Calculation("(n+n)");</p>
     */
    public TextCalculation(String calculationText)
    {
        suppliedString = calculationText;

        /* Since Calculation has not yet been initialized, false. */
        initializationOK = false;

        calculation = getNewCalculation(suppliedString);
    }

    public boolean isInitializationOK(){ return initializationOK; }

    public Calculation getCalculation(){ return calculation; }

    public String getSuppliedString(){ return suppliedString; }

    Calculation.CalcBuilder getBuilder(){ return calcBuilder; }

    /**
     * Method interprets received string and returns a new initialized Calculation object
     */
    public Calculation getNewCalculation(String string)
    {
        /* This trims off the surrounding brackets; they are a hard assumption */
        string = string.substring(1, string.length() - 1);

        Matcher operatorNumberMatcher = operatorAndNumber.matcher(string);
        Matcher operatorNMatcher = operatorAndN.matcher(string);
        Matcher bindingOperatorMatcher = bindingOperator.matcher(string);
        Matcher numberOrNMatcher = numberOrN.matcher(string);

        /* state set to true, and unless error occurs, state remains true */
        initializationOK = true;

        boolean stillMatching = true;
        int nextPosition = 0;

        initializeCalculationBuilder();

        if(numberOrNMatcher.find(nextPosition) && numberOrNMatcher.start() == nextPosition)
        {
            interpretNumberOrN(numberOrNMatcher.group());
            nextPosition = numberOrNMatcher.end();

            while(stillMatching)
            {
                /* checking which pattern appears next in sequence of chars in supplied calculationText
                 * is it a binding operator?  */
                if(bindingOperatorMatcher.find(nextPosition) && bindingOperatorMatcher.start() == nextPosition)
                {
                    interpretBindingOperator(bindingOperatorMatcher.group());
                    nextPosition = bindingOperatorMatcher.end();

                    /* If there was a match on binding operator, the next match needs to be a number or
                    'n': */
                    if(numberOrNMatcher.find(nextPosition) && numberOrNMatcher.start() == nextPosition)
                    {
                        interpretNumberOrN(numberOrNMatcher.group());
                        nextPosition = numberOrNMatcher.end();
                        continue;
                    } else
                    {
                        System.out.println("There was no n / number following a binding operator\n" +
                                           "This is necessary: operators cannot act on each other.");
                    }
                }
                /* If it wasn't a binding operator, maybe an operator and a number? */
                if(operatorNumberMatcher.find(nextPosition) && operatorNumberMatcher.start() == nextPosition)
                {
                    interpretOperatorAndNumber(operatorNumberMatcher.group());
                    nextPosition = operatorNumberMatcher.end();
                    continue;
                }
                /* Perhaps it is an operator and an 'n' */
                if(operatorNMatcher.find(nextPosition) && operatorNMatcher.start() == nextPosition)
                {
                    interpretOperatorAndN(operatorNMatcher.group());
                    nextPosition = operatorNMatcher.end();
                    continue;
                }

                stillMatching = false;
            }

            // the whole input string being parsed, and values of calcBuilder set, a new
            return finalizeNewCalculation();

        } else
        {
            // if code reaches here, obviously there was a problem, so
            initializationOK = false;

            System.out.println("Your calculation string was not formatted properly from index 0.");
        }

        System.out.println("Your declaring text might have contained errors." +
                           "\n Your new Calculation object is non-functional. Sorry." +
                           "\n The Calculation-object you did get back does no" +
                           "\n computations, takes parameters (calc() is still callable and " +
                           "\n will return -1)");
        // if code reaches here, obviously there was a problem, so
        initializationOK = false;
        return new Calculation.CalcBuilder().create().build();
    }

    /**
     * Method runs once, to instantiate a Calculation (create() performs a baseline initialization)
     */
    private void initializeCalculationBuilder()
    {
        calcBuilder = new Calculation.CalcBuilder().create();
    }

    /**
     * Method runs once to return a, hopefully, fully formed Calculation.
     */
    private Calculation finalizeNewCalculation()
    {
        return calcBuilder.build();
    }

    private void interpretNumber(String subSet)
    {
        double extractedNumber = -1.11; // this value will never be used

        /* ~, indicating a negative number, means the input digit needs a -1* treatment */
        boolean negativeNumber = subSet.charAt(0) == '~';

        try
        {
            /* attempt to get number from string; go from index 1, if number marked negative */
            extractedNumber = Double.parseDouble(subSet.substring((negativeNumber ? 1 : 0)));

            /* negate number if marked negative (gotta read up on these unsafe double)*/
            extractedNumber = (negativeNumber ? extractedNumber * -1.0D : extractedNumber);

            /* this adds the factor into the being-built calculation as a constant */
            calcBuilder.constant(extractedNumber);

        } catch(NumberFormatException e)
        {
            System.out.println("Number '" + subSet + "' is malformed.\n" +
                               "This is not a possible number format:\t\"" + subSet + "\"");
            // if code reaches here, obviously there was a problem, so
            initializationOK = false;
        }
    }

    private void interpretOperator(String subSet)
    {
        switch(subSet.charAt(0))
        {
            case '*':
                calcBuilder.multiply();
                break;
            case '/':
                calcBuilder.divideBy();
                break;
            case '-':
                calcBuilder.minus();
                break;
            case '+':
                calcBuilder.plus();
                break;
            case 'r':
                calcBuilder.root();
                break;
            case '^':
                calcBuilder.pow();
                break;
            case 'm':
                calcBuilder.modulus();
                break;
            default:
                System.out.println("Something happened. '" + subSet.charAt(0) + "' is" +
                                   "not an operator (implemented).");
                // if code reaches here, obviously there was a problem, so
                initializationOK = false;
        }
    }

    private void interpretNumberOrN(String group)
    {
        /* n, indicating a variable factor, is the standard, and needs not report to calcBuilder */
        if(group.charAt(0) == 'n')
        {
            return;
        }

        interpretNumber(group);
    }

    private void interpretOperatorAndNumber(String subSet)
    {
        /* the first sign will be an operator, so we'll switch through that */
        interpretOperator(subSet);
        interpretNumber(subSet.substring(1));
    }


    private void interpretOperatorAndN(String subSet)
    {
        interpretOperator(subSet);
    }

    private void interpretBindingOperator(String subSet)
    {
        /* calcBuild method expression() signifies that a new binding expression is the next upcoming
         * thing */
        calcBuilder.expression();
        interpretOperator(subSet.substring(1));
    }

    @Override
    public String toString()
    {
        return suppliedString;
    }
}
