package med.voll.api.paciente;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.endereco.Endereco;

@Entity
@Table(name = "pacientes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nome;
  private String email;
  private String telefone;
  private String cpf;
  @Embedded private Endereco endereco;
  private Boolean ativo = true;

  public Paciente(DadosCadastroPaciente dados) {
    this.nome = dados.nome();
    this.email = dados.email();
    this.telefone = dados.telefone();
    this.cpf = dados.cpf();
    this.endereco = new Endereco(dados.endereco());
  }

  public void atualizar(@Valid DadosAtualizacaoPaciente dados) {
    if (dados.nome() != null) {
      this.nome = dados.nome();
    }
    if (dados.telefone() != null) {
      this.telefone = dados.telefone();
    }
    if (dados.endereco() != null) {
      this.endereco.atualizarInformacoes(dados.endereco());
    }
  }

  public void deletar(){
    this.ativo = false;
  }
}
