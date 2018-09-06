package com.mmall.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by sww_6 on 2018/9/6.
 */
@Slf4j
public class PropertiesUtils {

  private static Properties props;

  static {
    String fileName = "mmall.properties";
    props = new Properties();
    try {
      props.load(
          new InputStreamReader(PropertiesUtil.class.getResourceAsStream("fileName"), "UTF-8"));
    } catch (IOException e) {
      log.error("配置文件异常！", e);
    }
  }

  public static String getProperties(String key) {
    String value = props.getProperty(key.trim());
    if (StringUtils.isBlank(value)) {
      return null;
    }
    return value.trim();
  }
  public static String getProperties(String key,String defaultValue) {
    String value = props.getProperty(key.trim());
    if (StringUtils.isBlank(value)) {
      value=defaultValue;
    }
    return value.trim();
  }
}
