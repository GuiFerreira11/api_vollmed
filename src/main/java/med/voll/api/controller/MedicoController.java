package med.voll.api.controller;

import java.net.URI;

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
import jakarta.validation.Valid;
import med.voll.api.medico.DadosAtualizacaoMedico;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosDetalhamentoMedico;
import med.voll.api.medico.DadosListagemMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

  @Autowired private MedicoRepository repository;

  @PostMapping
  @Transactional
  public ResponseEntity<DadosDetalhamentoMedico> cadastrar(
      @RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
    Medico medico = new Medico(dados);
    repository.save(medico);
    URI uri = uriBuilder.path("/medico/{id}").buildAndExpand(medico.getId()).toUri();
    return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
  }

  @GetMapping
  public ResponseEntity<Page<DadosListagemMedico>> listar(
      @PageableDefault(
              size = 10,
              sort = {"nome"})
          Pageable pageable) {
    Page<DadosListagemMedico> page =
        repository.findAllByAtivoTrue(pageable).map(DadosListagemMedico::new);
    return ResponseEntity.ok(page);
  }

  @GetMapping("/{id}")
  public ResponseEntity<DadosDetalhamentoMedico> detalhar(@PathVariable Long id) {
    Medico medico = repository.getReferenceById(id);
    return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
  }

  @PutMapping
  @Transactional
  public ResponseEntity<DadosDetalhamentoMedico> atualizar(
      @RequestBody @Valid DadosAtualizacaoMedico dados) {
    Medico medico = repository.getReferenceById(dados.id());
    medico.atualizarInformacoes(dados);
    return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<String> deletar(@PathVariable Long id) {
    Medico medico = repository.getReferenceById(id);
    medico.excluir();
    return ResponseEntity.noContent().build();
  }
}
