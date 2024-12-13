package med.voll.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import med.voll.api.endereco.DadosEndereco;
import med.voll.api.endereco.Endereco;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosDetalhamentoMedico;
import med.voll.api.medico.Especialidade;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class MedicoControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;

  @Autowired private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;

  @MockitoBean private MedicoRepository repository;

  @Test
  @DisplayName("Deveria devolver código HTTP 400 quando informações estão inválidas")
  @WithMockUser
  void cadastrarCenario1() throws Exception {
    MockHttpServletResponse response = mvc.perform(post("/medicos")).andReturn().getResponse();
    Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("Deveria devolver código HTTP 201 quando informações estão válidas")
  @WithMockUser
  void cadastrarCenario2() throws Exception {
    DadosCadastroMedico dadosCadastro =
        new DadosCadastroMedico(
            "Medico",
            "medico@voll.med",
            "11999999999",
            "123456",
            Especialidade.CARDIOLOGIA,
            dadosEndereco());

    DadosDetalhamentoMedico dadosDetalhamento =
        new DadosDetalhamentoMedico(
            null,
            dadosCadastro.nome(),
            dadosCadastro.email(),
            dadosCadastro.telefone(),
            dadosCadastro.crm(),
            dadosCadastro.especialidade(),
            new Endereco(dadosCadastro.endereco()));

    when(repository.save(any())).thenReturn(new Medico(dadosCadastro));
    MockHttpServletResponse response =
        mvc.perform(
                post("/medicos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dadosCadastroMedicoJson.write(dadosCadastro).getJson()))
            .andReturn()
            .getResponse();

    String jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();

    Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
  }

  private DadosEndereco dadosEndereco() {
    return new DadosEndereco("rua", "bairro", "00000000", "cidade", "sp", null, null);
  }
}
