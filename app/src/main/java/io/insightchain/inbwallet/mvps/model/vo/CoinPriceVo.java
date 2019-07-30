package io.insightchain.inbwallet.mvps.model.vo;

import java.io.Serializable;

public class CoinPriceVo implements Serializable {
    private double price;       //数币价格，单位是美元
    private double cnyPrice;    //1人民币兑换美元比例

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(double cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

    public double getCoinCnyPrice(){
        if(cnyPrice != 0d){
            return price/cnyPrice;
        }
        return 0d;
    }

    @Override
    public String toString() {
        return "CoinPriceVo{" +
                "price=" + price +
                ", cnyPrice=" + cnyPrice +
                '}';
    }
}
