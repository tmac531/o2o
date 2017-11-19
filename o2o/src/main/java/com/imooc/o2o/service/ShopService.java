package com.imooc.o2o.service;

import java.io.InputStream;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

public interface ShopService {
    
    /**
     * 根据shopCondition分页返回相应店铺列表
     * 
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
    
    /**
     * 通过店铺Id获取店铺信息
     * 
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     * 更新店铺信息，包括对图片的处理
     * 
     * @param shop
     * @param shopImg
     * @return
     * @throws ShopOperationException
     */
    //InputStream shopImgInputStream,String fileName换成ImageHolder，即封装成一个类
    ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

    //inputsream是获取不到文件名字的，file可以获取，所以还需要手动传入filename这个String
	ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;
}
