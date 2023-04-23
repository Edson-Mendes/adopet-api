package br.com.emendes.adopetapi.config;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.model.entity.Shelter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

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

    PropertyMap<CreateGuardianRequest, Guardian> guardianMap = new PropertyMap<>() {
      @Override
      protected void configure() {
        map().getUser().setEmail(this.source("email"));
        map().getUser().setPassword(this.source("password"));
        map().getUser().setId(null);
      }
    };

    PropertyMap<CreateShelterRequest, Shelter> shelterMap = new PropertyMap<>() {
      @Override
      protected void configure() {
        map().getUser().setEmail(this.source("email"));
        map().getUser().setPassword(this.source("password"));
        map().getUser().setId(null);
      }
    };

    mapper.addMappings(petMap);
    mapper.addMappings(guardianMap);
    mapper.addMappings(shelterMap);
    mapper.addMappings(adoptionMap());

    return mapper;
  }

  private PropertyMap<AdoptionRequest, Adoption> adoptionMap() {
    return new PropertyMap<>() {
      @Override
      protected void configure() {
        map().setId(null);
        map().getPet().setId(this.source("petId"));
      }
    };
  }
}
