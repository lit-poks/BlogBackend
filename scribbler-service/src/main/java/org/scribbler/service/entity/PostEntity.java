package org.scribbler.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "post")
@NamedQueries(
        {
                @NamedQuery(name = "getAllPosts", query = "select p from PostEntity p"),
                @NamedQuery(name = "postByUuid", query = "select p from PostEntity p where p.uuid =:uuid"),
                @NamedQuery(name = "postByid", query = "select p from PostEntity p where p.id =:id"),
                @NamedQuery(name = "getAllPostsByUser", query = "select p from PostEntity p where p.user= :user")
        }
)

public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 64)
    @NotNull
    private String uuid;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "USER_ID")
    @NotNull
    private UserEntity user;

    @Column
    @NotNull
    private String title;

    @Column
    @NotNull
    private String body;

    @Column
    @NotNull
    private String likes;

    @Column
    @NotNull
    private ZonedDateTime date;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
