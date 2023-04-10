package br.com.emendes.adopetapi.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class ConstantUtils {

  public static final Pageable PAGEABLE = PageRequest.of(0, 10);
  public static final String CONTENT_TYPE = "application/json;charset=UTF-8";

}
