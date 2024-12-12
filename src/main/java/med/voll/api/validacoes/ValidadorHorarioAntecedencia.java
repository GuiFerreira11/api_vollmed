package med.voll.api.validacoes;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import med.voll.api.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoDeConsulta {

  public void validar(DadosAgendamentoConsulta dados) {
    Long antecedencia = Duration.between(LocalDateTime.now(), dados.data()).toMinutes();
    if (antecedencia < 30) {
      throw new ValidacaoException(
          "Consulta deve ser agendada com antecedência mínima de 30 minutos.");
    }
  }
}
