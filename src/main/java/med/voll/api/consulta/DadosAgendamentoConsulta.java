package med.voll.api.consulta;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import lombok.NonNull;
import med.voll.api.medico.Especialidade;

public record DadosAgendamentoConsulta(
    Long idMedico,
    @NonNull Long idPaciente,
    @NonNull @Future @JsonFormat(pattern = "dd/MM/yyy HH:mm") LocalDateTime data,
    Especialidade especialidade) {}
