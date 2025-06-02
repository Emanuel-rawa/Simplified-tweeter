package tech.buildrun.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.buildrun.springsecurity.entities.Tweet;

/**
 * Repositório Spring Data JPA para a entidade {@link Tweet}.
 * 
 * Fornece operações padrão de CRUD (Create, Read, Update, Delete) para tweets,
 * utilizando o identificador do tipo {@link Long}.
 * 
 * Essa interface permite a manipulação persistente dos dados da entidade Tweet,
 * abstraindo as operações de banco de dados e facilitando consultas e
 * modificações.
 * 
 * @author Emanuel
 */
@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
