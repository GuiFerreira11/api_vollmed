package med.voll.api.medico;

import lombok.NonNull;
import med.voll.api.endereco.DadosEndereco;

public record DadosAtualizacaoMedico(
    @NonNull Long id, String nome, String telefone, DadosEndereco endereco) {}
