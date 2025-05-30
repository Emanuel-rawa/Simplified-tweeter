package tech.buildrun.springsecurity.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_tweets")
public class Tweet {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "tweet_id")
  private Long tweetId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String content;

  @CreationTimestamp
  private Instant creationTimeStamp;

  public void setTweetId(Long tweetId) {
    this.tweetId = tweetId;
  }

  public Long getTweetId() {
    return tweetId;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public void setCreationTimeStamp(Instant creationTimeStamp) {
    this.creationTimeStamp = creationTimeStamp;
  }

  public Instant getCreationTimeStamp() {
    return creationTimeStamp;
  }
}
