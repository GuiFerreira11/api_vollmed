package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;
import med.voll.api.usuario.DadosAutenticacao;
import med.voll.api.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

  @Autowired private AuthenticationManager manager;

  @Autowired private TokenService tokenService;

  @PostMapping
  public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
    Authentication authentication = manager.authenticate(authenticationToken);
    String tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
    return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
  }
}
