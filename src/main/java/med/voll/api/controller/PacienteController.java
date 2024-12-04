package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.paciente.DadosAtualizacaoPaciente;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.DadosListagemPaciente;
import med.voll.api.paciente.Paciente;
import med.voll.api.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paciente")
public class PacienteController {

  @Autowired private PacienteRepository repository;

  @PostMapping
  @Transactional
  public void cadastrar(@RequestBody @Valid DadosCadastroPaciente dados) {
    repository.save(new Paciente(dados));
  }

  @GetMapping
  public Page<DadosListagemPaciente> listar(
      @PageableDefault(
              size = 10,
              sort = {"nome"})
          Pageable pageable) {
    return repository.findAllByAtivoTrue(pageable).map(DadosListagemPaciente::new);
  }

  @PutMapping
  @Transactional
  public void atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
    Paciente paciente = repository.getReferenceById(dados.id());
    paciente.atualizar(dados);
  }

  @DeleteMapping("/{id}")
  @Transactional
  public void deletar(@PathVariable Long id){
    Paciente paciente = repository.getReferenceById(id);
    paciente.deletar();
  }
}
