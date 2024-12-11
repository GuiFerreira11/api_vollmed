package med.voll.api.infra;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> tratarErro404() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<DadosErroValidacao>> tratarErro400(
      MethodArgumentNotValidException ex) {
    List<FieldError> erros = ex.getFieldErrors();
    return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
  }

  private record DadosErroValidacao(String campo, String mensagem) {
    public DadosErroValidacao(FieldError erro) {
      this(erro.getField(), erro.getDefaultMessage());
    }
  }
}
