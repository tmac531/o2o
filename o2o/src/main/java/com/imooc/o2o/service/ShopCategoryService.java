package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.entity.ShopCategory;

public interface ShopCategoryService {
    public static final String SCLISTKEY = "shopcategorylist";
    /**
     * 根据查询条件获取ShopCategory列表
     * 
     * @param shopCategoryCondition
     * @return
     */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
