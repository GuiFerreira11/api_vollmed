package med.voll.api.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.consulta.DadosAgendamentoConsulta;
import med.voll.api.paciente.PacienteRepository;

@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta {

  @Autowired
  private PacienteRepository repository;

  public void validar(DadosAgendamentoConsulta dados) {
    boolean pacienteAtivo = repository.findAtivoById(dados.idPaciente());
    if (!pacienteAtivo) {
      throw new ValidacaoException("Consulta não pode ser agendada para paciente inativo");
    }
  }
}
