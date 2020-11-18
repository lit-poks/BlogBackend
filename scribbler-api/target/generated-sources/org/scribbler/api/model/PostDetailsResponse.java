package org.scribbler.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PostDetailsResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-11-15T16:23:31.846+06:00")

public class PostDetailsResponse   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("body")
  private String body = null;

  @JsonProperty("likes")
  private String likes = null;

  @JsonProperty("author")
  private String author = null;

  public PostDetailsResponse id(String id) {
    this.id = id;
    return this;
  }

  /**
   * post uuid
   * @return id
  **/
  @ApiModelProperty(required = true, value = "post uuid")
  @NotNull


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PostDetailsResponse title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Post title
   * @return title
  **/
  @ApiModelProperty(required = true, value = "Post title")
  @NotNull


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public PostDetailsResponse body(String body) {
    this.body = body;
    return this;
  }

  /**
   * Post content
   * @return body
  **/
  @ApiModelProperty(required = true, value = "Post content")
  @NotNull


  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public PostDetailsResponse likes(String likes) {
    this.likes = likes;
    return this;
  }

  /**
   * Post likes
   * @return likes
  **/
  @ApiModelProperty(required = true, value = "Post likes")
  @NotNull


  public String getLikes() {
    return likes;
  }

  public void setLikes(String likes) {
    this.likes = likes;
  }

  public PostDetailsResponse author(String author) {
    this.author = author;
    return this;
  }

  /**
   * author
   * @return author
  **/
  @ApiModelProperty(required = true, value = "author")
  @NotNull


  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostDetailsResponse postDetailsResponse = (PostDetailsResponse) o;
    return Objects.equals(this.id, postDetailsResponse.id) &&
        Objects.equals(this.title, postDetailsResponse.title) &&
        Objects.equals(this.body, postDetailsResponse.body) &&
        Objects.equals(this.likes, postDetailsResponse.likes) &&
        Objects.equals(this.author, postDetailsResponse.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, body, likes, author);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostDetailsResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    likes: ").append(toIndentedString(likes)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

