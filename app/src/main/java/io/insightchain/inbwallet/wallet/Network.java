package io.insightchain.inbwallet.wallet;

/**
 * Created by xyz on 2018/3/7.
 */

public class Network {
//  public static final String MAINNET = "http://192.168.1.183:6001/";
  public static final String MAINNET = "http://192.168.1.33:6001/";
//  public static final String MAINNET = "http://192.168.1.107:6001/";
  public static final String TESTNET = "http://kovan.infura.io/v3/d930ad2b5e094133a9af82f0ce3aea14";
  public static final String KOVAN = "http://kovan.infura.io/v3/d930ad2b5e094133a9af82f0ce3aea14";
//  public static final String ROPSTEN = "https://ropsten.infura.io/1UoO4I/";
  public static final String ROPSTEN = "https://ropsten.infura.io/";
//  public static final String ROPSTEN = "https://ropsten.infura.io/v3/d930ad2b5e094133a9af82f0ce3aea14";

  private String network;

  public Network(String network) {
    this.network = network;
  }

  public boolean isMainnet() {
    return MAINNET.equalsIgnoreCase(this.network);
  }
}
