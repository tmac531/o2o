package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest {
	@Autowired
	private ShopDao shopDao;

	@Test
    public void testQueryShopListAndCount() {
        Shop shopCondition = new Shop();
        
       //1、普通的店铺查询
//        PersonInfo info=new PersonInfo();
//        info.setUserId(1l);
//        shopCondition.setOwner(info);
//        
//        ShopCategory category=new ShopCategory();
//        category.setShopCategoryId(1l);
//        shopCondition.setShopCategory(category);
        
        //2、首页点击店铺详情，进入该类型店铺下所有子店铺的界面的店铺查询
        ShopCategory childCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();
        parentCategory.setShopCategoryId(1L);
        childCategory.setParent(parentCategory);
        shopCondition.setShopCategory(childCategory);
        
        
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 6);
        int count = shopDao.queryShopCount(shopCondition);
        System.out.println("符合条件店铺个数"+count);
        System.out.println("店铺列表的大小：" + shopList.size());
        for (Shop shop : shopList) {
            System.out.println("名字："+shop.getShopName());
        }
       // System.out.println("店铺总数：" + count);        
    }

	
	@Test
   // @Ignore
    public void testQueryByShopId() {
        long shopId = 2;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println("areaId:" + shop.getArea().getAreaId());
        System.out.println("areaName" + shop.getArea().getAreaName());
        System.out.println("shopCategory"+shop.getShopCategory().getShopCategoryName());
    }
	
	@Test
	@Ignore
	public void testInsertShop() {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(2);
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺");
		shop.setShopDesc("test");
		shop.setShopAddr("test");
		shop.setPhone("test");
		shop.setShopImg("test");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(0);
		shop.setAdvice("审核中");
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testUpdateShop() {
		Shop shop = new Shop();
		shop.setShopId(2L);		
		shop.setShopDesc("测试描述");
		shop.setShopAddr("测试地址");
		shop.setLastEditTime(new Date());
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}
}
