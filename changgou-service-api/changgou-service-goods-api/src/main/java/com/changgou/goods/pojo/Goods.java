package com.changgou.goods.pojo;

import java.io.Serializable;
import java.util.List;

public class Goods implements Serializable {
    //SPU
    private Spu spu;
    //SKU集合
    private List<Sku> skulist;

    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSkulist() {
        return skulist;
    }

    public void setSkulist(List<Sku> skulist) {
        this.skulist = skulist;
    }
}
