package med.voll.api.consulta;

import jakarta.validation.Valid;
import java.util.List;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import med.voll.api.paciente.Paciente;
import med.voll.api.paciente.PacienteRepository;
import med.voll.api.validacoes.ValidacaoException;
import med.voll.api.validacoes.ValidadorAgendamentoDeConsulta;
import med.voll.api.validacoes.ValidadorCancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaDeConsultas {

  @Autowired private ConsultaRepository consultaRepository;

  @Autowired private MedicoRepository medicoRepository;

  @Autowired private PacienteRepository pacienteRepository;

  @Autowired private List<ValidadorAgendamentoDeConsulta> validadoresAgendamento;

  @Autowired private List<ValidadorCancelamentoConsulta> validadoresCancelamento;

  public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {

    if (!pacienteRepository.existsById(dados.idPaciente())) {
      throw new ValidacaoException("Id do paciente informado não existente!");
    }
    if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
      throw new ValidacaoException("Id do medico informado não existente!");
    }
    validadoresAgendamento.forEach(v -> v.validar(dados));
    Medico medico = escolherMedico(dados);
    if (medico == null) {
      throw new ValidacaoException("Não existe médico disponível nessa data");
    }
    Paciente paciente = pacienteRepository.getReferenceById(dados.idPaciente());
    Consulta consulta = new Consulta(null, medico, paciente, dados.data(), null);
    consultaRepository.save(consulta);

    return new DadosDetalhamentoConsulta(consulta);
  }

  private Medico escolherMedico(DadosAgendamentoConsulta dados) {
    if (dados.idMedico() != null) {
      return medicoRepository.getReferenceById(dados.idMedico());
    }
    if (dados.especialidade() == null) {
      throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido!");
    }
    return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
  }

  public void cancelar(@Valid DadosCancelamentoConsulta dados) {
    if (!consultaRepository.existsById(dados.idConsulta())) {
      throw new ValidacaoException("Id da consulta inválido, consulta não existente.");
    }
    validadoresCancelamento.forEach(v -> v.validar(dados));
    Consulta consulta = consultaRepository.getReferenceById(dados.idConsulta());
    consulta.cancelar(dados.motivo());
  }
}
