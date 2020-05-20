package kea.motorhome.motorhomesite.util.calculation;

/**
 * <p>Instances of Formula class is able to do  between 0 and n series calculations on input factors
 * supplied as Double arrays.</p>
 *
 * <p>A description should be made, but I can't be bothered right now:</p>
 * <p>Ex.</p>
 * <p>Formula formulaEx = new Formula("(n*n)^(n+1)");</p>
 * <p>double[] results = formulaEx.computeSeries(</p>
 * <p>                        new Double[]{2d,3d,4d}, // factor n (1st n)</p>
 * <p>                        new Double[]{2d,3d,4d}, // factor n (2nd n)</p>
 * <p>                        new Double[]{2d,3d,4d}); // factor n (3rd n);</p>
 * <p></p>
 * <p>If you need to do a single calculation at a time, use a Calculation class object instead.</p>
 *
 * <b>There are certain special rules on how calculations can be put together: see Calculation class doc.
 * For rules on how to form a calculation properly, see TextCalculation class doc
 * (aka the top of those classes).</b>
 */

public class Formula
{
    Calculation calculation;            /* Calculation object to perform calculations.. */
    TextCalculation textCalculation;    /* Text-calculation object brought along to allow massive mutability */

    /**
     * Initializes a formula object through interpreting supplied string as a math formula.
     * <p>Please see github readme for details on how to form calculation strings/formula:</p>
     * <p><a href>https://github.com/kiancn/FunctionBuilder/blob/master/README.md</a></p>
     */
    public Formula(String calculationString)
    {
        setCalculation(calculationString);
    }

    public Calculation getCalculation(){ return calculation; }

    public void setCalculation(String calculationString)
    {
        textCalculation = new TextCalculation(calculationString);
        calculation = textCalculation.getCalculation();
    }

    /**
     * Method takes in a series of (comma-separated) Double[] or a single Double[][];
     * series of factors. Method returns a single double[], containing the result of each
     * calculation done on the supplied series of factors.
     * <p></p>
     *
     * @param factorArrays varArg Double[]..., each [] is a single value axis, ex. all x's in
     *                     calculation like (x*3+y) . The y's would need own series/[ ]/axis.
     *                     Varargs also allow 0 parameter input.
     * @return A single double[] is returned containing one result for the length of
     * supplied series of number.
     */
    public double[] computeSeries(Double[]... factorArrays)
    {
        /* Check for bad input */
        /* 1. check that number of arrays supplied matches number of input factors expected */
        if(!checkNumberOfFactors(factorArrays)) return new double[]{-1};

        /* 2. Compare lengths of supplied array; they must be equal */
        if(!areArraysEqualLength(factorArrays)) return new double[]{-1};

        /* Actual rounds of calculations */
        return generateResults(factorArrays);
    }

    /**
     * The actual calculation, begun when supplied data has been checked for aptness,
     * returning results of each round as a single double[].
     */
    private double[] generateResults(Double[][] factorArrays)
    {
        /* one result for length of supplied series */
        double[] results = new double[factorArrays[0].length];
        /* one/ a single factor for each array of factors supplied */
        double[] singleCalcFactors = new double[factorArrays.length];

        /*go through length of first series */
        for(int i = 0, j = 0; i < factorArrays[0].length; i++, j = 0)
        {
            /* for each iteration get numbers in parallel series (a set of factors)*/
            for(; j < factorArrays.length; j++)
            {
                /* get factors specific to round i (j's are the specifics, i's are rounds)*/
                singleCalcFactors[j] = factorArrays[j][i];
            }
            /* do calc with parallel numbers (a set of factors), saving each result in a spot in results[] */
            results[i] = calculation.calc(singleCalcFactors);
        }
        return results;
    }

    /**
     * Method returns true if number of input factors excepted for Calculalation matches supplied
     * Double[][] length - if they don't match, false.
     */
    private boolean checkNumberOfFactors(Double[][] factorArrays)
    {
        if(calculation.getNumberOfInputFactorsExpected() != factorArrays.length)
        {
            System.out.println("Wrong number of series (arrays) supplied, " +
                               calculation.getNumberOfInputFactorsExpected() + " expected.");
            return false;
        }
        return true;
    }

    /**
     * Method evaluates the length of each supplied factorArray; the must of course all the equally long;
     * If they are not, return false.
     */
    private boolean areArraysEqualLength(Double[][] factorArrays)
    {
        boolean equalNumbersDetected = true;
        int factorCount = factorArrays[0].length;

        for(int i = 0; i < factorArrays.length; i++)
        {
            /* if there is just one factor, this is compared to itself, hardly any harm is done, and it
            works to detect badly formed factorArrays in general. */
            if(factorArrays[i].length != factorCount){equalNumbersDetected = false;}
        }
        /* Refuse to work if bad input is given. */
        if(!equalNumbersDetected)
        {
            System.out.println("Debug: Bad length of supplied factor arrays.\n" +
                               "Supplied arrays were not equally long.");
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Formula:\t" + textCalculation + "\t Status: " +
               (textCalculation.isInitializationOK() ? "OK" : "NOT GOOD");
    }
}
