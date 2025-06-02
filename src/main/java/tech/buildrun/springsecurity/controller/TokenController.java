package tech.buildrun.springsecurity.controller;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.buildrun.springsecurity.controller.dto.LoginRequest;
import tech.buildrun.springsecurity.controller.dto.LoginResponse;
import tech.buildrun.springsecurity.repository.UserRepository;

/**
 * Controlador REST responsável pelo endpoint de autenticação via login,
 * fornecendo um token JWT para usuários autenticados com sucesso.
 * 
 * Utiliza o repositório {@link UserRepository} para buscar usuários e
 * {@link JwtEncoder} para geração dos tokens JWT.
 * 
 * A autenticação verifica se o usuário existe e se a senha fornecida está
 * correta,
 * comparando o hash com {@link BCryptPasswordEncoder}.
 * 
 * @author Emanuel
 */
@RestController
public class TokenController {

  /**
   * Encoder responsável pela geração de tokens JWT assinados.
   */
  private final JwtEncoder jwtEncoder;

  /**
   * Repositório para busca de dados de usuários.
   */
  private final UserRepository userRepository;

  /**
   * Encoder para validação de senha usando BCrypt.
   */
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * Construtor para injeção das dependências.
   * 
   * @param jwtEncoder            componente para geração de tokens JWT.
   * @param userRepository        repositório para acesso a usuários.
   * @param bCryptPasswordEncoder encoder para validação de senhas.
   */
  public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.jwtEncoder = jwtEncoder;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  /**
   * Endpoint HTTP POST para autenticação de usuário.
   * 
   * Recebe um objeto {@link LoginRequest} contendo username e senha,
   * valida as credenciais, e caso sejam válidas, retorna um token JWT e o tempo
   * de expiração.
   * 
   * Se as credenciais forem inválidas, lança {@link BadCredentialsException}.
   * 
   * @param loginRequest objeto contendo as credenciais de login.
   * @return um {@link ResponseEntity} contendo {@link LoginResponse} com o token
   *         JWT e expiração.
   * @throws BadCredentialsException caso username ou senha sejam inválidos.
   */
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    var user = userRepository.findByUsername(loginRequest.username());

    if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
      throw new BadCredentialsException("user or password is invalid!");
    }

    var now = Instant.now();

    var expiresIn = 300L; // tempo de expiração do token em segundos (5 minutos)

    // Concatena os nomes das roles do usuário, convertendo para uppercase,
    // separadas por espaço
    var scopes = user.get().getRoles()
        .stream()
        .map(role -> role.getName().toUpperCase())
        .collect(Collectors.joining(" "));

    // Construção do conjunto de claims para o JWT
    var claims = JwtClaimsSet
        .builder()
        .issuer("mybackend") // Emissor do token
        .subject(user.get().getUserId().toString()) // Identificador do usuário
        .issuedAt(now) // Data/hora de emissão
        .expiresAt(now.plusSeconds(expiresIn)) // Data/hora de expiração
        .claim("scope", scopes) // Claim customizada com permissões do usuário
        .build();

    // Geração do token JWT
    var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    // Retorna o token e o tempo de expiração na resposta HTTP 200
    return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
  }

}
