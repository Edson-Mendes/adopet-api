package br.com.emendes.adopetapi.config;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.model.entity.Pet;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();

    PropertyMap<CreatePetRequest, Pet> petMap = new PropertyMap<>() {
      protected void configure() {
        map().setId(null);
      }
    };
    mapper.addMappings(petMap);

    return mapper;
  }

}
