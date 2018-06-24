# vwap-calc

Basic calculator for volume weighted average price calculations.

## Volume Weighted Average Price Calculator
The VWAP Calculator maintains a market time series, composed of all quotes (per market per currency pair) submitted to this calculator. From the time series data the VWAP quote can be calculated, with each new order quote changing the cumulative volume, and potentially the VWAP.

The VWAP is calculated for the ask and bid separately, and no functionality is included to account for some mid point price, and adjust the VWAP accordingly. The ask and bid VWAP values, and the ask and bid volumes are calculated independently and returned as such in the `Quote` object returned from the #calculate function.
