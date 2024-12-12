package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.consulta.AgendaDeConsultas;
import med.voll.api.consulta.DadosAgendamentoConsulta;
import med.voll.api.consulta.DadosCancelamentoConsulta;
import med.voll.api.consulta.DadosDetalhamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

  @Autowired private AgendaDeConsultas agenda;

  @PostMapping
  @Transactional
  public ResponseEntity<DadosDetalhamentoConsulta> agendar(
      @RequestBody @Valid DadosAgendamentoConsulta dados) {
    DadosDetalhamentoConsulta dto = agenda.agendar(dados);
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping
  @Transactional
  public ResponseEntity<String> cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados) {
    agenda.cancelar(dados);
    return ResponseEntity.noContent().build();
  }
}