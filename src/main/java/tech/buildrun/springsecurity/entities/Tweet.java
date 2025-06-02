package tech.buildrun.springsecurity.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

/**
 * Entidade JPA que representa um Tweet no sistema.
 * 
 * Mapeada para a tabela "tb_tweets" no banco de dados.
 * 
 * Contém informações sobre o conteúdo do tweet, seu autor (usuário) e o
 * timestamp de criação.
 * 
 * @author Emanuel
 */
@Entity
@Table(name = "tb_tweets")
public class Tweet {

  /**
   * Identificador único do Tweet, gerado automaticamente via sequência.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "tweet_id")
  private Long tweetId;

  /**
   * Usuário que criou o Tweet.
   * 
   * Relacionamento Many-to-One com a entidade {@link User}, representando o autor
   * do tweet.
   */
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  /**
   * Conteúdo textual do Tweet.
   */
  private String content;

  /**
   * Timestamp da criação do Tweet, gerado automaticamente pelo Hibernate.
   */
  @CreationTimestamp
  private Instant creationTimeStamp;

  /**
   * Define o identificador do Tweet.
   * 
   * @param tweetId ID único do Tweet.
   */
  public void setTweetId(Long tweetId) {
    this.tweetId = tweetId;
  }

  /**
   * Retorna o identificador do Tweet.
   * 
   * @return ID único do Tweet.
   */
  public Long getTweetId() {
    return tweetId;
  }

  /**
   * Define o usuário que criou o Tweet.
   * 
   * @param user usuário autor do Tweet.
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Retorna o usuário autor do Tweet.
   * 
   * @return usuário que criou o Tweet.
   */
  public User getUser() {
    return user;
  }

  /**
   * Define o conteúdo textual do Tweet.
   * 
   * @param content texto do Tweet.
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Retorna o conteúdo textual do Tweet.
   * 
   * @return texto do Tweet.
   */
  public String getContent() {
    return content;
  }

  /**
   * Define o timestamp de criação do Tweet.
   * 
   * @param creationTimeStamp instante de criação.
   */
  public void setCreationTimeStamp(Instant creationTimeStamp) {
    this.creationTimeStamp = creationTimeStamp;
  }

  /**
   * Retorna o timestamp em que o Tweet foi criado.
   * 
   * @return instante da criação do Tweet.
   */
  public Instant getCreationTimeStamp() {
    return creationTimeStamp;
  }
}
