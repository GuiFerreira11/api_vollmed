package med.voll.api.controller;

import jakarta.validation.Valid;
import java.net.URI;
import med.voll.api.paciente.DadosAtualizacaoPaciente;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.DadosDetalhamentoPaciente;
import med.voll.api.paciente.DadosListagemPaciente;
import med.voll.api.paciente.Paciente;
import med.voll.api.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/paciente")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

  @Autowired private PacienteRepository repository;

  @PostMapping
  @Transactional
  public ResponseEntity<DadosDetalhamentoPaciente> cadastrar(
      @RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
    Paciente paciente = new Paciente(dados);
    repository.save(paciente);
    URI uri = uriBuilder.path("/paciente/{id}").buildAndExpand(paciente.getId()).toUri();
    return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
  }

  @GetMapping("/{id}")
  public ResponseEntity<DadosDetalhamentoPaciente> detalhar(@PathVariable Long id) {
    Paciente paciente = repository.getReferenceById(id);
    return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
  }

  @GetMapping
  public ResponseEntity<Page<DadosListagemPaciente>> listar(
      @PageableDefault(
              size = 10,
              sort = {"nome"})
          Pageable pageable) {
    Page<DadosListagemPaciente> page = repository.findAllByAtivoTrue(pageable).map(DadosListagemPaciente::new);
    return ResponseEntity.ok(page);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<DadosDetalhamentoPaciente> atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
    Paciente paciente = repository.getReferenceById(dados.id());
    paciente.atualizar(dados);
    return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<String> deletar(@PathVariable Long id) {
    Paciente paciente = repository.getReferenceById(id);
    paciente.deletar();
    return ResponseEntity.noContent().build();
  }
}
