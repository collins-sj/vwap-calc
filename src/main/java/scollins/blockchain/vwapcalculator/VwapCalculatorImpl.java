package scollins.blockchain.vwapcalculator;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

/**
 * VWAP Calculator that maintains a market time series, composed of all
 * quotes (per market per currency pair) submitted to this calculator.
 * From the time series data the VWAP quote can be calculated, with each 
 * new order quote changing the cumulative volume, and potentially the VWAP.
 * 
 * The VWAP is calculated for the ask and bid separately, and no functionality
 * is included to account for some mid point price, and adjust the VWAP accordingly.
 * The ask and bid VWAP values, and the ask and bid volumes are calculated independently
 * and returned as such in the {@link Quote} object returned from the #calculate function.
 *
 */
public class VwapCalculatorImpl implements VwapCalculator {

  private Map<CurrencyPair, List<MarketPairQuote>> marketTimeSeries = new HashMap<>();
  
  @Override
  public Quote calculate(CurrencyPair pair, Market market, Quote quote) {
    MarketPairQuote marketPairQuote = new MarketPairQuote(pair, market, quote);
    marketTimeSeries.computeIfAbsent(pair, k -> new ArrayList<>()).add(marketPairQuote);
    
    //find volume & bid across all markets
    double cumulativeBid = computeCumulative(pair, market, 
        e -> e.quote.bid() * e.quote.bidSize());
    
    double cumulativeBidVolume = computeCumulative(pair, market, 
        e -> e.quote.bidSize());
    
    //find volume & ask across all markets
    double cumulativeAsk = computeCumulative(pair, market, 
        e -> e.quote.ask() * e.quote.askSize());
    
    double cumulativeAskVolume = computeCumulative(pair, market, 
        e -> e.quote.askSize());
    
    Quote vWAPQuote = new VwapQuote(
        vwap(cumulativeBid, cumulativeBidVolume), 
        cumulativeBidVolume,
        vwap(cumulativeAsk, cumulativeAskVolume), 
        cumulativeAskVolume);
    
    marketPairQuote.addVWAP(vWAPQuote);
    
    return vWAPQuote;
  }

  public double vwap(double cumulativeWeightedPrice, double cumulativeVolume) {
    return cumulativeWeightedPrice / cumulativeVolume;
  }
  
  public double computeCumulative(CurrencyPair pair, Market market, ToDoubleFunction<MarketPairQuote> f) {
    return marketTimeSeries.get(pair).stream()
    .filter(e -> e.pair == pair)
    .mapToDouble(f)
    .sum();
  }
    
  public void displayTimeSeries() {
    DecimalFormat df = new DecimalFormat("#.00"); 
    DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_TIME;
    
    marketTimeSeries.entrySet().stream().forEach(e -> {
      System.out.println(e.getKey());
      List<MarketPairQuote> quotes = e.getValue();
      StringBuffer asks = new StringBuffer(" Asks:\n");
      StringBuffer bids = new StringBuffer(" Bids:\n");
      for(MarketPairQuote q : quotes) {
        asks.append("  -" + q.timestamp.format(dtf) + " " 
            + q.market + "["
            + "Ask:" + q.quote.ask() 
            + ",Size:"+ q.quote.askSize()
            + "][VWPA:" + df.format(q.weightedAverage.ask())
            + ",Volume:" + q.weightedAverage.askSize() + "]\n");

        bids.append("  -" + q.timestamp.format(dtf) + " "
            + q.market + "["
            + "Bid:" + q.quote.bid()
            + ",Size:"+ q.quote.bidSize()
            + "][VWPA:" + df.format(q.weightedAverage.bid())
            + ",Volume:" + q.weightedAverage.bidSize() + "]\n");
      }
      System.out.print(asks.toString());
      System.out.print(bids.toString());
      
    });
  }
  
  private class MarketPairQuote {
    Market market;
    CurrencyPair pair;
    Quote quote;
    Quote weightedAverage;
    LocalDateTime timestamp;
    
    public MarketPairQuote(CurrencyPair pair, Market market, Quote quote) {
      this.market = market;
      this.pair = pair;
      this.quote = quote;
      timestamp = LocalDateTime.now();
    }
    
    public void addVWAP(Quote weightedAverage) {
      this.weightedAverage = weightedAverage;
    }

    @Override
    public String toString() {
      return "MarketPairQuote [market=" + market + ", quote=" + quote + ", weightedAverage=" + weightedAverage
          + ", timestamp=" + timestamp + "]";
    }
  }

}
