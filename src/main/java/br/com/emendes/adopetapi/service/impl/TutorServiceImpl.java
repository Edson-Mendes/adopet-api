package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.mapper.TutorMapper;
import br.com.emendes.adopetapi.model.entity.Tutor;
import br.com.emendes.adopetapi.service.TutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TutorServiceImpl implements TutorService {

  private TutorMapper tutorMapper;

  @Override
  public TutorResponse create(@Valid TutorRequest tutorRequest) {
    // Verificar se password e confirmPassword são iguais.
    if (!tutorRequest.isPasswordsMatch()) {
      log.info("Passwords do not match at TutorServiceImpl#create");
      throw new PasswordsDoNotMatchException("Passwords do not match");
    }
    // Converter TutorRequest para Tutor
    Tutor tutor = tutorMapper.tutorRequestToTutor(tutorRequest);

    //Salvar no DB através de TutorRepository

    //Tratar erro de Email em uso.

    //Converter Tutor para TutorResponse e retornar.

    return null;
  }

}
