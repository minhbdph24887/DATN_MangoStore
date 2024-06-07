package com.datn.datn_mangostore.reponse;

public class PriceRange {
    private Integer priceMin;
    private Integer priceMax;

    public PriceRange(Integer priceMin,
                      Integer priceMax) {
        this.priceMin = priceMin;
        this.priceMax = priceMax;
    }

    public PriceRange() {
    }

    public Integer getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Integer priceMin) {
        this.priceMin = priceMin;
    }

    public Integer getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(Integer priceMax) {
        this.priceMax = priceMax;
    }
}
