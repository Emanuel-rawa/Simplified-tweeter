package tech.buildrun.springsecurity.controller;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import tech.buildrun.springsecurity.controller.dto.CreateTweetDto;
import tech.buildrun.springsecurity.controller.dto.FeedDto;
import tech.buildrun.springsecurity.controller.dto.FeedItemDto;
import tech.buildrun.springsecurity.entities.Role;
import tech.buildrun.springsecurity.entities.Tweet;
import tech.buildrun.springsecurity.repository.TweetRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

/**
 * Controlador REST responsável por operações CRUD relacionadas aos Tweets.
 * 
 * Permite criar, deletar tweets e consultar o feed paginado de tweets.
 * 
 * Utiliza autenticação JWT para validar permissões e identificar o usuário.
 * 
 * @author Emanuel
 */
@RestController
public class TweetController {

  /**
   * Repositório para persistência e recuperação de tweets.
   */
  private final TweetRepository tweetRepository;

  /**
   * Repositório para acesso a dados de usuários.
   */
  private final UserRepository userRepository;

  /**
   * Construtor com injeção dos repositórios necessários.
   * 
   * @param tweetRepository repositório de tweets.
   * @param userRepository  repositório de usuários.
   */
  public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
    this.tweetRepository = tweetRepository;
    this.userRepository = userRepository;
  }

  /**
   * Endpoint HTTP POST para criação de um novo tweet.
   * 
   * Recebe um DTO com conteúdo do tweet e o token JWT autenticado do usuário.
   * Cria um tweet associado ao usuário autenticado.
   * 
   * @param createTweetDto DTO contendo o conteúdo do tweet.
   * @param token          token JWT autenticado contendo identificação do
   *                       usuário.
   * @return resposta HTTP 200 OK sem corpo.
   */
  @PostMapping("/tweets")
  public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto createTweetDto, JwtAuthenticationToken token) {
    var user = userRepository.findById(UUID.fromString(token.getName()));

    var tweet = new Tweet();
    tweet.setUser(user.get());
    tweet.setContent(createTweetDto.content());

    tweetRepository.save(tweet);

    return ResponseEntity.ok().build();
  }

  /**
   * Endpoint HTTP DELETE para remoção de um tweet por seu ID.
   * 
   * Permite que apenas o autor do tweet ou um usuário com role ADMIN possa
   * deletar.
   * 
   * @param tweetId ID do tweet a ser deletado.
   * @param token   token JWT autenticado do usuário que faz a requisição.
   * @return resposta HTTP 200 OK se deletado com sucesso, 403 Forbidden se
   *         usuário não autorizado,
   *         ou 404 Not Found se o tweet não existir.
   */
  @DeleteMapping("/tweets/{id}")
  public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token) {

    var user = userRepository.findById(UUID.fromString(token.getName()));

    var tweet = tweetRepository.findById(tweetId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    var isAdmin = user.get().getRoles().stream()
        .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

    if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
      tweetRepository.deleteById(tweetId);
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }

  /**
   * Endpoint HTTP GET para retornar o feed paginado de tweets ordenados por data
   * de criação decrescente.
   * 
   * Os parâmetros de paginação são opcionais e possuem valores padrão: page = 0 e
   * pageSize = 10.
   * 
   * @param page     número da página a ser consultada (zero-based).
   * @param pageSize quantidade de tweets por página.
   * @return um objeto {@link FeedDto} contendo a lista de tweets e metadados da
   *         paginação.
   */
  @GetMapping("/feed")
  public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    var tweets = tweetRepository.findAll(
        PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimeStamp"))
        .map(tweet -> new FeedItemDto(tweet.getTweetId(), tweet.getContent(), tweet.getUser().getUsername()));
    return ResponseEntity
        .ok(new FeedDto(tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements()));
  }

}
