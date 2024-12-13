package med.voll.api.medico;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import med.voll.api.consulta.Consulta;
import med.voll.api.endereco.DadosEndereco;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MedicoRepositoryTest {

  @Autowired private MedicoRepository repository;

  @Autowired private EntityManager em;

  @Test
  @DisplayName("Deveria devolver null quando o único médico cadastrado não está disponível na data")
  void escolherMedicoAleatorioLivreNaDataCenario1() {
    // given ou arrange
    LocalDateTime proximaSegundaAs10 =
        LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
    Medico medico =
        cadastrarMedico("Medico", "medico@voll.med", "123456", Especialidade.CARDIOLOGIA);
    Paciente paciente = cadastrarPaciente("Paciente", "paciente@voll.med", "12345678900");
    cadastrarConsulta(medico, paciente, proximaSegundaAs10);

    // when ou act
    Medico medicoLivre =
        repository.escolherMedicoAleatorioLivreNaData(
            Especialidade.CARDIOLOGIA, proximaSegundaAs10);

    // then ou assert
    assertThat(medicoLivre).isNull();
  }

  @Test
  @DisplayName("Deveria devolver médico quando ele estiver disponível na data")
  void escolherMedicoAleatorioLivreNaDataCenario2() {
    //given
    LocalDateTime proximaSegundaAs10 =
        LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
    Medico medico =
        cadastrarMedico("Medico", "medico@voll.med", "123456", Especialidade.CARDIOLOGIA);
    //when
    Medico medicoLivre =
        repository.escolherMedicoAleatorioLivreNaData(
            Especialidade.CARDIOLOGIA, proximaSegundaAs10);
    //then
    assertThat(medicoLivre).isEqualTo(medico);
  }

  private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
    em.persist(new Consulta(null, medico, paciente, data, null));
  }

  private Medico cadastrarMedico(
      String nome, String email, String crm, Especialidade especialidade) {
    var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
    em.persist(medico);
    return medico;
  }

  private Paciente cadastrarPaciente(String nome, String email, String cpf) {
    var paciente = new Paciente(dadosPaciente(nome, email, cpf));
    em.persist(paciente);
    return paciente;
  }

  private DadosCadastroMedico dadosMedico(
      String nome, String email, String crm, Especialidade especialidade) {
    return new DadosCadastroMedico(nome, email, "61999999999", crm, especialidade, dadosEndereco());
  }

  private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
    return new DadosCadastroPaciente(nome, email, "61999999999", cpf, dadosEndereco());
  }

  private DadosEndereco dadosEndereco() {
    return new DadosEndereco("rua xpto", "bairro", "00000000", "Brasilia", "DF", null, null);
  }
}
