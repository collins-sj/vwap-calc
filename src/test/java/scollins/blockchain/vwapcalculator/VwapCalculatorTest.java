package scollins.blockchain.vwapcalculator;

import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Unit tests for the VwapCalculator.
 */
public class VwapCalculatorTest {
  private VwapCalculator vwapCalculator;

  @Before
  public void init() {
    vwapCalculator = new VwapCalculatorImpl();
  }

  @Test
  public void testVwapCalculationSingleMarket() {
    Quote quote = vwapCalculator.calculate(CurrencyPair.PAIR1, Market.MARKET1, new OrderQuote(820.5, 400, 20.35, 320));
    validateVWPA(quote, 820.5, 400, 20.35, 320);

    // vwapCalculator.displayTimeSeries();
  }

  @Test
  public void testVwapCalculationMultipleMarket() {
    Quote quote = vwapCalculator.calculate(CurrencyPair.PAIR1, Market.MARKET2, new OrderQuote(10.26, 40, 70.5, 100));
    validateVWPA(quote, 10.26, 40, 70.5, 100);

    quote = vwapCalculator.calculate(CurrencyPair.PAIR1, Market.MARKET1, new OrderQuote(20d, 200d, 20d, 200d));
    validateVWPA(quote, 18.38, 240, 36.83, 300);

    // vwapCalculator.displayTimeSeries();
  }

  @Test
  public void testVwapCalculationMultipleMarketMultiplePairs() {
    // PAIR1
    Quote quote = vwapCalculator.calculate(CurrencyPair.PAIR1, Market.MARKET2, new OrderQuote(10d, 100d, 10d, 100d));
    validateVWPA(quote, 10, 100, 10, 100);

    quote = vwapCalculator.calculate(CurrencyPair.PAIR1, Market.MARKET1, new OrderQuote(30, 200, 40, 500));
    validateVWPA(quote, 23.33, 300, 35, 600);

    // PAIR2
    quote = vwapCalculator.calculate(CurrencyPair.PAIR2, Market.MARKET2, new OrderQuote(10, 100, 10, 100));
    validateVWPA(quote, 10, 100, 10, 100);

    quote = vwapCalculator.calculate(CurrencyPair.PAIR2, Market.MARKET3, new OrderQuote(20d, 200d, 50d, 900d));
    validateVWPA(quote, 16.67, 300, 46, 1000);

    quote = vwapCalculator.calculate(CurrencyPair.PAIR2, Market.MARKET4, new OrderQuote(30d, 500d, 50d, 300d));
    validateVWPA(quote, 25, 800, 46.92, 1300);

    // vwapCalculator.displayTimeSeries();
  }

  @Test
  public void testVwapCalculationAllMarketsAllPairs() {
    // 5 Markets
    Quote quote = null;
    for (CurrencyPair currencyPair : CurrencyPair.values()) {
      for (Market market : Market.values()) {
        for (int i = 1; i <= 10; i++) {
          quote = vwapCalculator.calculate(currencyPair, market, 
              new OrderQuote(10*i, 100, 10*i, 1000));
          Assert.assertNotNull(quote);
        }
      }
      //For each currency pair VWAP is calculated across 5 markets with 10 quotes
      validateVWPA(quote, 55, 5000, 55, 50000);
    }
    vwapCalculator.displayTimeSeries();
  }

  private void validateVWPA(Quote quote, double expectedBid, double expectedBidSize, double expectedAsk,
      double expectedAskSize) {
    DecimalFormat df = new DecimalFormat("#.00");
    Assert.assertEquals(df.format(expectedAsk), df.format(quote.ask()));
    Assert.assertEquals(expectedAskSize, quote.askSize());
    Assert.assertEquals(df.format(expectedBid), df.format(quote.bid()));
    Assert.assertEquals(expectedBidSize, quote.bidSize());
  }
}
