package scollins.blockchain.vwapcalculator;

public interface Quote {
  
  /** Buy price. */
  double bid();

  /** Buy amount. */
  double bidSize();

  /** Sell price. */
  double ask();

  /** Sell amount. */
  double askSize();
}
