package med.voll.api.validacoes;

import java.time.Duration;
import java.time.LocalDateTime;
import med.voll.api.consulta.Consulta;
import med.voll.api.consulta.ConsultaRepository;
import med.voll.api.consulta.DadosCancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorAntecedenciaCancelamento implements ValidadorCancelamentoConsulta {

  @Autowired private ConsultaRepository repository;

  public void validar(DadosCancelamentoConsulta dados) {
    Consulta consulta = repository.getReferenceById(dados.idConsulta());
    Long antecedenciaEmHoras = Duration.between(LocalDateTime.now(), consulta.getData()).toHours();
    if (antecedenciaEmHoras < 24) {
      throw new ValidacaoException(
          "Consluta somente pode ser cancelado com antecedência mínima de 24h");
    }
  }
}
