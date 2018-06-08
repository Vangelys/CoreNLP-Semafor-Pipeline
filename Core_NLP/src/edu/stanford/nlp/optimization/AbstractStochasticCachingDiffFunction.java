package edu.stanford.nlp.optimization;

import edu.stanford.nlp.math.ArrayMath;
import edu.stanford.nlp.util.logging.Redwood;


/**
 *
 * @author Alex Kleeman
 */
public abstract class AbstractStochasticCachingDiffFunction extends AbstractCachingDiffFunction  {

  /** A logger for this class */
  private static final Redwood.RedwoodChannels log = Redwood.channels(AbstractStochasticCachingDiffFunction.class);

  public boolean hasNewVals = true;
  public boolean recalculatePrevBatch = false;
  public boolean returnPreviousValues = false;

  protected int lastBatchSize = 0;
  protected int[] lastBatch = null;
  protected int[] thisBatch = null;
  protected double[] lastXBatch = null;
  protected double[] lastVBatch = null;
  // protected double[] extFiniteDiffDerivative = null;

  protected int lastElement = 0;
  protected double[] HdotV = null;
  protected double[] gradPerturbed = null;
  protected double[] xPerturbed = null;
  protected int curElement = 0;

  protected List<Integer> allIndices = null;
  protected Random randGenerator = new Random(1);//System.currentTimeMillis());

  protected boolean scaleUp = false;

  private int[] shuffledArray = null;

  public enum SamplingMethod {
    NoneSpecified,
    RandomWithReplacement,
    RandomWithoutReplacement,
    Ordered,
    Shuffled,
  }

  public void incrementRandom(int numTimes) {
    log.info("incrementing random "+numTimes+" times.");
    for (int i = 0; i < numTimes; i++) {
      randGenerator.nextInt(this.dataDimension());
    }
  }

  public void scaleUp(boolean toScaleUp){
    scaleUp = toScaleUp;
  }

  public StochasticCalculateMethods method = StochasticCalculateMethods.ExternalFiniteDifference;
  public SamplingMethod sampleMethod = SamplingMethod.RandomWithoutReplacement;

  /**
   * finiteDifferenceStepSize - this is the fixed step size for the finite difference approximation.
   *    a few tests were run using the SMD minimizer, and step sizes of 1e-4 to 1e-3 seemed to be ideal.
   * (akleeman)
   */
  protected double finiteDifferenceStepSize = 1e-4;


  /**
   * calculateStochastic needs to calculate a stochastic approximation to the derivative and value of
   *    of a function for a given batch of the data.  The approximation to the derivative must be stored
   *    in the array <code> derivative </code>, the approximation to the value in <code> value </code>
   *    and the approximation to the Hessian vector product H.v in the array <code> HdotV </code>.  Note
   *    that the hessian vector product is used primarily with the Stochastic Meta Descent optimization
   *    routine <code> SMDMinimizer </code>.
   *
   *  Important: The stochastic approximation must be such that the sum of all stochastic calculations over
   *    each of the batches in the data must equal the full calculation.  i.e. for a data set of size 100
   *    the sum of the gradients for batches 1-10 , 11-20 , 21-30 .... 91-100 must be the same as the gradient
   *    for the full calculation (at the very least in expectation).  Be sure to take into account the priors.
   *
   *
   * @param x           -  value to evaluate at
   * @param v           -  the vector for the Hessian vector product H.v
   * @param batch       -  an array containing the indices of the data to use in the calculation, this array is being calculated
   *                        internal to the abstract, and only needs to be handled not generated by the implementation.
   */
  public abstract void calculateStochastic(double[] x, double[] v, int[] batch);


  /**
   * Data dimension must return the size of the data used by the function.
   */
  public abstract int dataDimension();

  /**
   * Clears the cache in a way that doesn't require reallocation :-).
   */
  @Override
  protected void clearCache() {
    super.clearCache();
    if (lastXBatch != null) lastXBatch[0] = Double.NaN;
    if (lastVBatch != null) lastVBatch[0] = Double.NaN;
  }

  @Override
  public double[] initial() {
    double[] initial = new double[domainDimension()];
    // Arrays.fill(initial, 0.0); // not needed; Java arrays zero initialized
    return initial;
  }

  /**
   * decrementBatch - This decrements the curElement variable by the amount batchSize.
   *  by decrementing the batch and then calling calculate you can recalculate over the previous batch.
   */
  public void decrementBatch(int batchSize){
    curElement -= batchSize;
    if(curElement < 0){curElement = 0;}
  }

  /**
   * incrementBatch will shift the curElement variable to mark the next batch.  It also resets the flags:
   *    hasNewElements
   *    recalculatePrevBatch
   *    returnPreviousValues
   */
  public void incrementBatch(int batchSize){
    curElement += batchSize;
    hasNewVals = false;
    recalculatePrevBatch = false;
    returnPreviousValues = false;
  }


  /**
   * getBatch is used to generate the next sequence of indices to be passed to the actual function.
   *  Depending on the current sample method this is done by:
   *    Ordered - simply generates the indices 1,2,3,4,....
   *    RandomWithReplacement - Samples uniformly from the set of possible indices
   *    RandomWithoutReplacement - Samples from the set of possible indices removing each used index, then restarting
   *          after each pass
   */

  //private int numCalls = 0;
  protected void getBatch(int batchSize){

//      if (numCalls == 0) {
//        for (int i = 0; i < 1538*\15; i++) {
//          randGenerator.nextInt(this.dataDimension());
//        }
//      }
//      numCalls++;

    if (thisBatch == null || thisBatch.length != batchSize){
      thisBatch = new int[batchSize];
    }

    //-----------------------------
    //Shuffled
    //-----------------------------
    if (sampleMethod == SamplingMethod.Shuffled) {
      if (shuffledArray == null) {
        shuffledArray = ArrayMath.range(0, this.dataDimension());
      }
      for(int i = 0; i<batchSize;i++){
        thisBatch[i] = shuffledArray[(curElement + i) % this.dataDimension()] ;          //Take the next batchSize points in order
      }
      curElement = (curElement + batchSize) % this.dataDimension();       //watch out for overflow

    //-----------------------------
    //RANDOM WITH REPLACEMENT
    //-----------------------------
    } else if (sampleMethod == SamplingMethod.RandomWithReplacement) {
      for(int i = 0; i<batchSize;i++){
        thisBatch[i] = randGenerator.nextInt(this.dataDimension());        //Just generate a random index
//        log.info("numCalls = "+(numCalls++));
      }
      //-----------------------------
      //ORDERED
      //-----------------------------
    } else if (sampleMethod == SamplingMethod.Ordered) {
      for(int i = 0; i<batchSize;i++){
        thisBatch[i] = (curElement + i) % this.dataDimension() ;          //Take the next batchSize points in order
      }
      curElement = (curElement + batchSize) % this.dataDimension();       //watch out for overflow

      //-----------------------------
      //RANDOM WITHOUT REPLACEMENT
      //-----------------------------
    } else if(sampleMethod == SamplingMethod.RandomWithoutReplacement) {
      //Declare the indices array if needed.
      if (allIndices == null || allIndices.size()!= this.dataDimension()){

        allIndices = new ArrayList<>();
        for(int i=0;i<this.dataDimension();i++){
          allIndices.add(i);
        }
        Collections.shuffle(allIndices,randGenerator);
      }

      for(int i = 0; i<batchSize;i++){
        thisBatch[i] = allIndices.get((curElement + i) % allIndices.size());  //Grab the next batchSize indices
      }

      if (curElement + batchSize > this.dataDimension()){
        Collections.shuffle(Collections.singletonList(allIndices),randGenerator);                   //Shuffle if we got to the end of the list
      }

      //watch out for overflow
      curElement = (curElement + batchSize) % allIndices.size();          //Rollover


    } else {
      throw new IllegalStateException("NO SAMPLING METHOD SELECTED");
    }

  }




  void stochasticEnsure(double[] x, double[] v, int batchSize){

    if (lastXBatch == null) {
      lastXBatch = new double[domainDimension()];
      log.info("Setting previous position (x).");
    }

    if (lastVBatch == null) {
      lastVBatch = new double[domainDimension()];
      log.info("Setting previous gain (v)");
    }

    if (derivative == null) {
      derivative = new double[domainDimension()];
      log.info("Setting Derivative.");
    }

    if (HdotV == null) {
      HdotV = new double[domainDimension()];
      log.info("Setting HdotV.");
    }

    if (lastBatch == null){
      lastBatch = new int[batchSize];
      log.info("Setting last batch");
    }
    //If we want to recalculate using the previous batch
    if(recalculatePrevBatch && batchSize == lastBatch.length){

      thisBatch = lastBatch;

    }else{

      /*
     If we dont want to calculate anything we just want the last values.  This is especially usefull if you know
         the values have already been calculated, and you don't want to waste time comparing the entire
         array of x's and v's.
      */
      if(returnPreviousValues){
        returnPreviousValues = false;
        return;
      }

      //If we dont know there are new values, and we havnt asked to recalculate then compare
      //to avoid needing to recalculate
      if( !hasNewVals && lastElement!=curElement ){
        if ((lastBatchSize==batchSize) && Arrays.equals(x, lastXBatch) && Arrays.equals(v,lastVBatch) && Arrays.equals(thisBatch,lastBatch)) {
          return;
        }
      }

      getBatch(batchSize);
    }


    copy(lastXBatch,x);
    if(lastBatch.length != batchSize){
      lastBatch = new int[batchSize];
    }
    System.arraycopy(thisBatch,0,lastBatch,0,thisBatch.length);

    if(v!=null){copy(lastVBatch,v);}
    lastBatchSize = batchSize;

    calculateStochastic(x,v,thisBatch);

    //This is used to make the function evaluation equal the true function in expectation.
    if(scaleUp){
      double ratio = ( (double) this.dataDimension()) / ( (double)batchSize) ;
      for(int i=0;i<x.length;i++){
        derivative[i] = derivative[i] * ratio;
      }
      value = ratio*value;
    }

    incrementBatch(batchSize);
    lastElement = curElement;

  }

  /*
  void stochasticEnsure(double[] x, double[] v, int batchSize) {


    if (lastXBatch == null) {
      lastXBatch = new double[domainDimension()];
      System.out.println("Setting previous position (x).");
    }

    if (lastVBatch == null) {
      lastVBatch = new double[domainDimension()];
      System.out.println("Setting previous gain (v)");
    }

    if (derivative == null) {
      derivative = new double[domainDimension()];
      System.out.println("Setting Derivative.");
    }

    if (HdotV == null) {
      HdotV = new double[domainDimension()];
      System.out.println("Setting HdotV.");
    }



    //If we want to recalculate using the previous batch
    if(recalculatePrevBatch){

      decrementBatch(batchSize);

    }else{

      //
      //If we dont want to calculate anything we just want the last values.  This is especially usefull if you know
      //   the values have already been calculated, and you don't want to waste time comparing the entire
      //   array of x's and v's.
      //
      if(returnPreviousValues){
        returnPreviousValues = false;
        return;
      }

      //If we dont know there are new values, and we havnt asked to recalculate then compare
      //to avoid needing to recalculate
      if( !hasNewVals && lastElement!=curElement ){
        if ((lastBatchSize==batchSize) && Arrays.equals(x, lastXBatch) && Arrays.equals(v,lastVBatch)) {
          return;
        }
      }

    }


    copy(lastXBatch,x);
    if(v!=null){copy(lastVBatch,v);}
    lastBatchSize = batchSize;
    calculateStochastic(x,v,batchSize);

    lastElement = curElement;

    incrementBatch(batchSize);

  }
  */




  /**
   *    valueAt(x,batchSize)   derivativeAt(x,batchSize)
   *
   * invokes the calculateStochastic function to get the current value at x for the next batchSize data points.  Will
   *    not return a HdotV since it passes v = null;
   *
   */

  public double valueAt(double[] x, int batchSize){
    stochasticEnsure(x,null,batchSize);
    return value;
  }

  public double[] derivativeAt(double[] x, int batchSize) {
    stochasticEnsure(x,null,batchSize);
    return derivative;
  }

  /**
   *
   * This function will return the stochastic approximation at the point x.  the vector v is the vector
   *    to be used in the vector product H.v.
   *
   * passing v = null will simply revert to a calculation without the hessian vector product.
   *
   *
   */
  public double valueAt(double[] x, double[] v, int batchSize) {
    stochasticEnsure(x,v,batchSize);
    return value;
  }

  public double[] derivativeAt(double[] x, double[] v, int batchSize) {
    stochasticEnsure(x,v,batchSize);
    return derivative;
  }



  /**
   * Calculate the Hessian vector product with a vector v of the current function based on a finite difference approximation.
   *    This is done using the approximation:
   *
   * H.v ~ ( Grad( x + h * v ) - Grad( x ) ) / h
   *
   * Note that this method will not be exact, and the value of h should be choosen to be small enough to avoid truncation error
   *    due to neglecting second order taylor series terms, and big enough to avoid numerical error which is almost gaurenteed
   *    since the operation involves subtracting similar values and dividing by a small number.  In general a step size of
   *    h = 1e-4 has proved to provide accurate enough calculations.
   *
   * @param x                 the point at which the hessian should be calculated
   * @param v                 the vector for the vector product ... thus the function will return  H(x) . v
   * @param curDerivative     the derivative at x.  Note this must have been calculated using the same batchSize
   */
  private void getHdotVFiniteDifference(double[] x, double[] v, double[] curDerivative){


    double h = finiteDifferenceStepSize;
    double hInv = 1/h;    // this avoids dividing too much since it's a bit more expensive than multiplying

    if (gradPerturbed == null) {
      gradPerturbed = new double[x.length];
      System.out.println("Setting approximate gradient.");
    }

    if (xPerturbed == null){
      xPerturbed = new double[x.length];
      System.out.println("Setting perturbed.");
    }

    if (HdotV == null) {
      HdotV = new double[x.length];
      System.out.println("Setting H dot V.");
    }

    //  This adds h*v to x  --->  x = x + h*v
    for( int i = 0;i<x.length;i++){
      xPerturbed[i] = x[i] + h*v[i];
    }


    double prevValue = value;

    recalculatePrevBatch = true;

    calculateStochastic(xPerturbed,null,thisBatch);  // Call the calculate function without updating the batch

    // System.arraycopy(derivative, 0, gradPerturbed, 0, gradPerturbed.length);

    //  This comes up with the approximate difference, and renormalizes it on h.
    for( int i = 0;i<x.length;i++){
      double tmp = (derivative[i]-curDerivative[i]);
      HdotV[i] = hInv*(tmp);
    }

    //Make sure the original derivative is in place
    System.arraycopy(curDerivative,0,derivative,0,derivative.length);
    value = prevValue;
    hasNewVals = false;
    recalculatePrevBatch = false;
    returnPreviousValues = false;

  }

  /**
   *
   * HdotVAt  will return the hessian vector product H.v at the point x for a batchSize subset of the data
   *
   * There are several ways to perform this calculation, as of now Finite Difference, and Algorithmic Differentiation
   *  are the methods that have been used.  To use this function calculateStochastic must also fill the array
   *  Hv with the hessian vector product.
   *
   * Alternative:  use the function getHdotVFiniteDifference which will simply make two calls to the function and
   *    come up with an approximation to this value.
   *
   */

  public double[] HdotVAt(double[] x, double[] v, int batchSize){

    if (method == StochasticCalculateMethods.ExternalFiniteDifference){
      log.info("Attempt to use ExternalFiniteDifference without passing currentDerivative");
      throw new RuntimeException();
      /*
      if( extFiniteDiffDerivative == null )
        extFiniteDiffDerivative = new double[x.length];

      System.arraycopy(derivativeAt(x,x,batchSize),0,extFiniteDiffDerivative,0,extFiniteDiffDerivative.length);
      getHdotVFiniteDifference(x,v,extFiniteDiffDerivative,batchSize);
      */

    } else {
      //Call the objective Function
      stochasticEnsure(x,v,batchSize);
    }
    return HdotV;
  }


  public double[] HdotVAt(double[] x, double[] v, double[] curDerivative, int batchSize){
    if (method == StochasticCalculateMethods.ExternalFiniteDifference){
      //If H.v is going to be calculated by using Finite Difference using two calls to the objective Function
      //
      //note:  This assumes that the derivative was calculated at x already.
      getHdotVFiniteDifference(x,v,curDerivative);
    } else {
      //Call the objective Function
      stochasticEnsure(x,v,batchSize);
    }
    return HdotV;
  }


  public double[] HdotVAt(double[] x,double[] v){

    if (method == StochasticCalculateMethods.ExternalFiniteDifference){

      log.info("Attempt to use ExternalFiniteDifference without passing currentDerivative");
      throw new RuntimeException();

    }
    //Call the objective Function
    stochasticEnsure(x,v,this.dataDimension());

    //Roll back the batch to the previous val.
    decrementBatch(this.dataDimension());
    return HdotV;
  }

  public double[] lastDerivative() {
    return derivative;
  }

  @Override
  public double lastValue() {
    return value;
  }

  // It doesn't seem like this should exist in the class and it wasn't used.
  // public void setValue(double v) {
  //   value = v;
  // }

}
