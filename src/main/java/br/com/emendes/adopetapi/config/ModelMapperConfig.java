package br.com.emendes.adopetapi.config;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.model.entity.Tutor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  public ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();

    PropertyMap<TutorRequest, Tutor> tutorMap = new PropertyMap<>() {
      @Override
      protected void configure() {
        map().setEnabled(true);
      }
    };

    mapper.addMappings(tutorMap);

    return mapper;
  }

}
