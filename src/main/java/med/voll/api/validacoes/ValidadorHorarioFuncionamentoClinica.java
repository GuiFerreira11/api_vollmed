package med.voll.api.validacoes;

import java.time.DayOfWeek;

import org.springframework.stereotype.Component;

import med.voll.api.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoDeConsulta{

  public void validar(DadosAgendamentoConsulta dados) {

    boolean domingo = dados.data().getDayOfWeek().equals(DayOfWeek.SUNDAY);
    boolean antesDaAberturaDaClinica = dados.data().getHour() < 7;
    boolean depoisDoFechamentoDaClinica = dados.data().getHour() > 18;
    if (domingo || antesDaAberturaDaClinica || depoisDoFechamentoDaClinica) {
      throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica");
    }
  }
}
