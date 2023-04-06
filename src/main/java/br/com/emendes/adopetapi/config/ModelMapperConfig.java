package br.com.emendes.adopetapi.config;

import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.model.entity.Tutor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();

    PropertyMap<CreateTutorRequest, Tutor> tutorMap = new PropertyMap<>() {
      @Override
      protected void configure() {
        map().setEnabled(true);
      }
    };

    mapper.addMappings(tutorMap);

    return mapper;
  }

}
