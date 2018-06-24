package scollins.blockchain.vwapcalculator;

public interface VwapCalculator {
  
  /**
   * Returns a VWAP Quote for the Currency Pair across all the Markets received.
   * Note that a new Pair/Market Quote should replace the previous Quote if it
   * exists
   *
   * @param pair the currency Pair for the quote
   * @param market the market for the quote
   * @param quote a valid quote with positive bid/ask fields
   * @return the VWAP quote for the currency pair across all the markets.
   */
   Quote calculate(CurrencyPair pair, Market market, Quote quote);

  /**
   * Outputs a visual display representation of the time series of 
   * quotes per market and currency pair, with a running VWAP value
   * and volume calculation associated with each. This running value
   * shows how the VWAP changes as new quote orders are included
   * in the cumulative weighting calculations.
   */
   void displayTimeSeries();
}
