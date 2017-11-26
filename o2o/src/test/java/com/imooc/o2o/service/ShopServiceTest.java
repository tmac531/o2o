package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;

public class ShopServiceTest extends BaseTest{
	@Autowired
	private ShopService shopService;
	
	@Test
    public void testGetShopList() {
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(1L);
        shopCondition.setShopCategory(sc);
        ShopExecution se = shopService.getShopList(shopCondition, 3, 2);
        System.out.println("店铺列表数为：" + se.getShopList().size());
        System.out.println("店铺总数为：" + se.getCount());
    }
	
	@Test
    //@Ignore
    public void testModifyShop() throws ShopOperationException, FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(4L);
        shop.setShopName("修改后的店铺名称");
        //File shopImg = new File("/Users/baidu/work/image/dabai.jpg");
        File shopImg = new File("E:/资料/SSM-SpringBoot/821.jpg");
        InputStream is = new FileInputStream(shopImg);
        //InputStream imageHolder = new ImageHolder("dabai.jpg", is);
        ImageHolder imageHolder=new ImageHolder("821.jpg",is);
        ShopExecution shopExecution = shopService.modifyShop(shop,imageHolder);
        System.out.println("新的图片地址为：" + shopExecution.getShop().getShopImg());
    }
	@Test
	public void testAddShop() throws ShopOperationException, FileNotFoundException{
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
		shop.setShopName("测试的店铺3");
		shop.setShopDesc("test3");
		shop.setShopAddr("test3");
		shop.setPhone("test3");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		//File shopImg = new File("/Users/baidu/work/image/xiaohuangren.jpg");
		File shopImg = new File("E:/资料/SSM-SpringBoot/821.jpg");
		InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder=new ImageHolder("821.jpg",is);
		ShopExecution se = shopService.addShop(shop,imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	}

}
