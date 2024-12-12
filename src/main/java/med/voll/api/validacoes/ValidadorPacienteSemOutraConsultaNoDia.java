package med.voll.api.validacoes;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.consulta.ConsultaRepository;
import med.voll.api.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorPacienteSemOutraConsultaNoDia implements ValidadorAgendamentoDeConsulta {

  @Autowired
  private ConsultaRepository repository;

  public void validar(DadosAgendamentoConsulta dados) {
    LocalDateTime primeiroHorario = dados.data().withHour(7);
    LocalDateTime ultimoHorario = dados.data().withHour(18);
    boolean pacienteComOutraConsultaNoDia =
        repository.existsByPacienteIdAndDataBetween(
            dados.idPaciente(), primeiroHorario, ultimoHorario);
    if (pacienteComOutraConsultaNoDia) {
      throw new ValidacaoException("Paciente já possui uma consulta agendade nesse dia");
    }
  }
}
