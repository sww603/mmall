package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sww_6 on 2018/8/31.
 */
@Service("ICategoryService ")
@Slf4j
public class ICategoryServiceImpl implements ICategoryService {

  @Autowired
  private CategoryMapper categoryMapper;

  @Override
  public ServerResponse addcateGory(String categoryName, Integer parentId) {
    if (parentId == null && StringUtils.isBlank(categoryName)) {
      return ServerResponse.createByErrorMessage("参加品类参数错误！");
    }
    Category category = new Category();
    category.setParentId(parentId);
    category.setName(categoryName);
    category.setStatus(true);
    int rowCount = categoryMapper.insert(category);
    if (rowCount > 0) {
      return ServerResponse.createBySuccessMessage("添加品类成功！");
    }
    return ServerResponse.createByErrorMessage("添加品类失败！");
  }

  @Override
  public ServerResponse updateCateGoryName(Integer categoryId, String categoryName) {
    if (categoryId == null || StringUtils.isBlank(categoryName)) {
      return ServerResponse.createByErrorMessage("更新品类参数错误！");
    }
    Category category = new Category();
    category.setId(categoryId);
    category.setName(categoryName);
    int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
    if (rowCount > 0) {
      return ServerResponse.createBySuccessMessage("更新品类名字成功！");
    }
    return ServerResponse.createByErrorMessage("更新品类名字失败!");
  }

  @Override
  public ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId) {
    List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(parentId);
    if (CollectionUtils.isEmpty(categoryList)) {
      log.info("为找到当前分类的子分类！");
    }
    return ServerResponse.createBySuccess(categoryList);
  }

  public ServerResponse selectCategoryAndChildrenById(Integer catagoryId) {
    HashSet<Category> categorySet = Sets.newHashSet();
    findChildCategoy(categorySet, catagoryId);

    ArrayList<Integer> categoryList = Lists.newArrayList();
    if (catagoryId != null) {
      for (Category categoryItem : categorySet) {
        categoryList.add(categoryItem.getId());
      }
    }
    return ServerResponse.createBySuccess(categoryList);
  }

  private Set<Category> findChildCategoy(Set<Category> categorySet, Integer categoryId) {
    Category category = categoryMapper.selectByPrimaryKey(categoryId);
    if (category != null) {
      categorySet.add(category);
    }
    List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
    for (Category categoryItem : categoryList) {
      findChildCategoy(categorySet, categoryItem.getId());
    }
    return categorySet;
  }
}
