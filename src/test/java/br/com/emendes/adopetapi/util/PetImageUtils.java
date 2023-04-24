package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.ImageResponse;

public class PetImageUtils {

  public static ImageResponse imageResponse() {
    return ImageResponse.builder()
        .url("http://www.xptopetimages/images/cat123")
        .build();
  }

}
