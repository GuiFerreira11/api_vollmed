package med.voll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;

import med.voll.api.consulta.AgendaDeConsultas;
import med.voll.api.consulta.DadosAgendamentoConsulta;
import med.voll.api.consulta.DadosDetalhamentoConsulta;
import med.voll.api.medico.Especialidade;

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
public class ConsultaControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

  @Autowired private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

  @MockitoBean private AgendaDeConsultas agendaDeConsultas;

  @Test
  @DisplayName("Deveria devolver código HTTP 400 quando informações estão inválidas")
  @WithMockUser
  void agendarCenario1() throws Exception {
    MockHttpServletResponse response = mvc.perform(post("/consultas")).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
  

  @Test
  @DisplayName("Deveria devolver código HTTP 200 quando informações inválidas")
  @WithMockUser
  void agendarCenario2() throws Exception {

    LocalDateTime data = LocalDateTime.now().plusHours(1);
    Especialidade especialidade = Especialidade.CARDIOLOGIA;
    DadosDetalhamentoConsulta dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2l, 5l, data);

    when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);

    MockHttpServletResponse response =
        mvc.perform(
                post("/consultas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        dadosAgendamentoConsultaJson
                            .write(new DadosAgendamentoConsulta(2l, 5l, data, especialidade))
                            .getJson()))
            .andReturn()
            .getResponse();

    String jsonEsperado = dadosDetalhamentoConsultaJson.write(dadosDetalhamento).getJson();

    assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
  }
}
