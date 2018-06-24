package scollins.blockchain.vwapcalculator;

public class OrderQuote implements Quote {

  private double bid;
  private double bidSize;
  private double ask;
  private double askSize;
  
  public OrderQuote() {
  }

  public OrderQuote(double bid, double bidSize, double ask, double askSize) {
    this.bid = bid;
    this.bidSize = bidSize;
    this.ask = ask;
    this.askSize = askSize;
  }

  @Override
  public double bid() {
    return bid;
  }

  @Override
  public double bidSize() {
    return bidSize;
  }

  @Override
  public double ask() {
    return ask;
  }

  @Override
  public double askSize() {
    return askSize;
  }

  @Override
  public String toString() {
    return "TradeQuote [bid=" + bid + ", bidSize=" + bidSize + ", ask=" + ask + ", askSize=" + askSize + "]";
  }
  
}
