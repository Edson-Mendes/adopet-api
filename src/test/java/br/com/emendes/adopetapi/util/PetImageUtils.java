package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.ImageResponse;
import br.com.emendes.adopetapi.model.entity.PetImage;

import java.util.List;

public class PetImageUtils {

  public static ImageResponse imageResponse() {
    return ImageResponse.builder()
        .url("http://www.xptopetimages/images/cat123")
        .build();
  }

  public static List<PetImage> petImageList() {
    PetImage petImage = PetImage.builder()
        .id(10_000_000L)
        .url("http://www.xptopetimages/images/cat123")
        .build();

    return List.of(petImage);
  }

  public static List<PetImage> petImageWithoutIdList() {
    PetImage petImage = PetImage.builder()
        .url("http://www.xptopetimages/images/cat123")
        .build();

    return List.of(petImage);
  }

}
