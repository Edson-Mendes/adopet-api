package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.ImageResponse;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.mapper.PetMapper;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.model.entity.PetImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PetMapperImpl implements PetMapper {

  @Override
  public Pet createPetRequestToPet(CreatePetRequest createPetRequest) {
    PetImage petImage = PetImage.builder()
        .url(createPetRequest.image())
        .build();

    List<PetImage> petImageList = List.of(petImage);

    Pet pet = Pet.builder()
        .name(createPetRequest.name())
        .description(createPetRequest.description())
        .age(createPetRequest.age())
        .images(petImageList)
        .build();

    petImage.setPet(pet);

    return pet;
  }

  @Override
  public PetResponse petToPetResponse(Pet pet) {
    List<ImageResponse> urlImages = pet.getImages().stream()
        .map(petImage -> new ImageResponse(petImage.getUrl())).toList();

    return PetResponse.builder()
        .id(pet.getId())
        .name(pet.getName())
        .description(pet.getDescription())
        .age(pet.getAge())
        .adopted(pet.isAdopted())
        .shelterId(pet.getShelter().getId())
        .images(urlImages)
        .build();
  }

  @Override
  public void merge(UpdatePetRequest updatePetRequest, Pet pet) {
    pet.setName(updatePetRequest.name());
    pet.setDescription(updatePetRequest.description());
    pet.setAge(updatePetRequest.age());
  }

}
