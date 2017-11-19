package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.utils.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ShopService shopService;

	/**
	 * 返回商品列表页里的ShopCategory列表(二级或者一级)，以及区域信息列表
	 * 1、点击全部商品（即parentId=null），显示全部商品类别（一级）和区域列表
	 * 2、点击一级商品类别列表(parentId不为空)，显示该一级商品类别下的所有二级类别和区域列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 试着从前端请求中获取parentId
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		if (parentId != -1) {
			// 如果parentId存在，则取出该一级ShopCategory下的二级ShopCategory列表
			try {
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			try {
				// 如果parentId不存在，则取出所有一级ShopCategory(用户在首页选择的是全部商店列表)
				shopCategoryList = shopCategoryService.getShopCategoryList(null);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		List<Area> areaList = null;
		try {
			// 获取区域列表信息
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
			return modelMap;
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	/**
	 * 获取指定查询条件下的店铺列表（即点击全部商品或一级商品类别后，在区域列表和商品类别列表下显示的商品列表）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshops", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取一页需要显示的数据条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 非空判断
		if ((pageIndex > -1) && (pageSize > -1)) {
			// 试着获取一级类别Id
			int parentId = HttpServletRequestUtil.getInt(request, "parentId");
			System.out.println("==========="+parentId);
			// 试着获取特定二级类别Id
			int shopCategoryId = HttpServletRequestUtil.getInt(request, "shopCategoryId");
			System.out.println("shopCategoryId "+shopCategoryId);
			// 试着获取区域Id
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			// 试着获取模糊查询的名字
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			// 获取组合之后的查询条件
			Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
			// 根据查询条件和分页信息获取店铺列表，并返回总数
			//System.out.println("shopCondition的类别信息"+shopCondition.getShopCategory().getShopCategoryId());
			//System.out.println("shopCondition的一级类别："+shopCondition.getShopCategory().getParent());
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}

		return modelMap;
	}

	/**
	 * 组合查询条件，并将条件封装到ShopCondition对象里返回
	 * 
	 * @param parentId
	 * @param shopCategoryId
	 * @param areaId
	 * @param shopName
	 * @return
	 */
	private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		if (parentId != -1) {
            // 查询某个一级ShopCategory下面的所有二级ShopCategory里面的店铺列表
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            if(shopCategoryId!=-1){
                childCategory.setShopCategoryId(shopCategoryId);
            }
            shopCondition.setShopCategory(childCategory);
        }
        else if(shopCategoryId != -1) {
            // 查询某个二级ShopCategory下面的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
		if (areaId != -1) {
			// 查询位于某个区域Id下的店铺列表
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}

		if (shopName != null) {
			// 查询名字里包含shopName的店铺列表
			shopCondition.setShopName(shopName);
		}
		// 前端展示的店铺都是审核成功的店铺
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}
}
