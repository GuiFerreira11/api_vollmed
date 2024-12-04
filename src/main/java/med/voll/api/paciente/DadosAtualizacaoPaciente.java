package med.voll.api.paciente;

import lombok.NonNull;
import med.voll.api.endereco.DadosEndereco;

public record DadosAtualizacaoPaciente(@NonNull Long id, String nome, String telefone, DadosEndereco endereco) {}
