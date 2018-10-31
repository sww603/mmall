package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

/**
 * Created by sww_6 on 2018/8/23.
 */
@Slf4j
public class JsonUtil {

  private static ObjectMapper objectMapper = new ObjectMapper();

  static {
    //序列化的时候序列化对象的所有属性
    objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
    //反序列化的时候，如果多了其他属性，不抛出异常
    objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //如果是空对象的时候,不抛异常
    objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
    objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true);
    //objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static <T> String object2String(T obj) {
    if (obj == null) {
      return null;
    }
    try {
      return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static <T> String object2StringPretty(T obj) {
    if (obj == null) {
      return null;
    }
    try {
      return obj instanceof String ? (String) obj
          : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static <T> T string2Obg(String str, Class<T> clazz) {
    if (StringUtils.isEmpty(str) || clazz == null) {
      return null;
    }
    try {
      return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
    } catch (IOException e) {
      log.warn("Parse String to object error", e);
      e.printStackTrace();
    }
    return null;
  }

  public static <T> T string2obj(String str, TypeReference<T> typeReference) {
    if (StringUtils.isEmpty(str) || typeReference == null) {
      return null;
    }
    try {
      return (T) (typeReference.equals(String.class) ? (T) str
          : objectMapper.readValue(str, typeReference));
    } catch (IOException e) {
      log.warn("Parse String to object error", e);
      e.printStackTrace();
    }
    return null;
  }

  public static <T> T string2obj(String str, Class<?> collectionClass,
      Class<?>... eleclementClass) {
    JavaType javaType = objectMapper.getTypeFactory()
        .constructParametricType(collectionClass, eleclementClass);

    try {
      return objectMapper.readValue(str, javaType);
    } catch (IOException e) {
      log.warn("Parse String to object error", e);
      e.printStackTrace();
    }
    return null;
  }


  public static void main(String[] args) {
    /*User user = new User();
    user.setId(111);
    user.setEmail("sww603@163.com");
    String json = JsonUtil.object2String(user);
    String jsonpretty = JsonUtil.object2StringPretty(user);

    User users = JsonUtil.string2Obg(json, User.class);
    log.info("json{}", json);
    log.info("jsonpretty{}", jsonpretty);*/

    List<User> userList = Lists.newArrayList();
    User user1 = new User();
    user1.setUsername("沈文文");
    user1.setEmail("sww@163.com");

    User user2 = new User();
    user2.setUsername("刘庆鹤");
    user2.setEmail("lqh@163.com");
    userList.add(user1);
    userList.add(user2);

    String userliststr = JsonUtil.object2StringPretty(userList);

    log.info("userliststr", userliststr);

    List list = JsonUtil.string2obj(userliststr, new TypeReference<List<User>>() {

    });
    List<User> list1 = JsonUtil.string2obj(userliststr, List.class, User.class);
  }
}
