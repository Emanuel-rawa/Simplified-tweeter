package tech.buildrun.springsecurity.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

/**
 * Classe de configuração responsável por definir as regras de segurança
 * da aplicação, utilizando autenticação via JWT (JSON Web Token) com chaves
 * RSA.
 * 
 * Esta configuração inclui:
 * <ul>
 * <li>Política de segurança de endpoints</li>
 * <li>Configuração de codificação e decodificação de JWT</li>
 * <li>Gerenciamento de sessões como stateless</li>
 * <li>Configuração de criptografia de senhas com BCrypt</li>
 * </ul>
 * 
 * As anotações {@link Configuration}, {@link EnableWebSecurity} e
 * {@link EnableMethodSecurity}
 * habilitam a configuração de segurança, segurança web e segurança baseada em
 * métodos, respectivamente.
 * 
 * @author Emanuel
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  /**
   * Chave pública RSA usada para a verificação da assinatura dos tokens JWT.
   * 
   * Injetada via variável de ambiente ou arquivo de configuração
   * {@code application.properties}.
   */
  @Value("${jwt.public.key}")
  private RSAPublicKey publicKey;

  /**
   * Chave privada RSA usada para assinar os tokens JWT.
   * 
   * Injetada via variável de ambiente ou arquivo de configuração
   * {@code application.properties}.
   */
  @Value("${jwt.private.key}")
  private RSAPrivateKey privateKey;

  /**
   * Configura a cadeia de filtros de segurança da aplicação.
   * 
   * As configurações incluem:
   * <ul>
   * <li>Permissão irrestrita aos endpoints de login e criação de usuários</li>
   * <li>Exigência de autenticação para qualquer outro endpoint</li>
   * <li>Desabilitação de CSRF (adequado para APIs REST)</li>
   * <li>Configuração do recurso OAuth2 com JWT</li>
   * <li>Política de sessão stateless</li>
   * </ul>
   * 
   * @param http o objeto {@link HttpSecurity} para configurar a segurança HTTP.
   * @return a cadeia de filtros de segurança configurada.
   * @throws Exception caso ocorra algum erro na configuração.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, "/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/users").permitAll()
            .anyRequest().authenticated())
        .csrf(csrf -> csrf.disable())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  /**
   * Configura o conversor de autenticação JWT, que extrai as autoridades do
   * token.
   * 
   * Define o prefixo das autoridades como {@code "SCOPE_"} e utiliza a claim
   * {@code "scope"}
   * para a extração de permissões.
   * 
   * @return um {@link JwtAuthenticationConverter} configurado.
   */
  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    var converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("SCOPE_");
    converter.setAuthoritiesClaimName("scope");

    var jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
    return jwtConverter;
  }

  /**
   * Configura o codificador de tokens JWT utilizando a chave RSA.
   * 
   * Cria um {@link JWK} contendo a chave pública e privada, que será utilizado
   * pelo {@link NimbusJwtEncoder}.
   * 
   * @return uma instância de {@link JwtEncoder}.
   */
  @Bean
  public JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
    var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

    return new NimbusJwtEncoder(jwks);
  }

  /**
   * Configura o decodificador de tokens JWT, utilizando a chave pública RSA.
   * 
   * Este decodificador verifica a assinatura dos tokens recebidos.
   * 
   * @return uma instância de {@link JwtDecoder}.
   */
  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  /**
   * Configura o codificador de senhas utilizando o algoritmo BCrypt.
   * 
   * O uso de BCrypt é uma prática recomendada para o armazenamento seguro de
   * senhas.
   * 
   * @return uma instância de {@link BCryptPasswordEncoder}.
   */
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
