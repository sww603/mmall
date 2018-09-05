package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import java.util.List;

/**
 * Created by sww_6 on 2018/8/31.
 */
public interface ICategoryService {

  ServerResponse addcateGory(String categoryName, Integer parentId);

  /**
   * 更新商品类别
   */
  ServerResponse updateCateGoryName(Integer categoryId, String categoryName);

  ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);

  ServerResponse selectCategoryAndChildrenById(Integer catagoryId);
}
