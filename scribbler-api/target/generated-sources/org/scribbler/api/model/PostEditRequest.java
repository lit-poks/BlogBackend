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
 * PostEditRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-11-15T16:23:31.846+06:00")

public class PostEditRequest   {
  @JsonProperty("title")
  private String title = null;

  @JsonProperty("body")
  private String body = null;

  @JsonProperty("likes")
  private String likes = null;

  public PostEditRequest title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Post title
   * @return title
  **/
  @ApiModelProperty(value = "Post title")


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public PostEditRequest body(String body) {
    this.body = body;
    return this;
  }

  /**
   * Post content
   * @return body
  **/
  @ApiModelProperty(value = "Post content")


  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public PostEditRequest likes(String likes) {
    this.likes = likes;
    return this;
  }

  /**
   * likes
   * @return likes
  **/
  @ApiModelProperty(value = "likes")


  public String getLikes() {
    return likes;
  }

  public void setLikes(String likes) {
    this.likes = likes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostEditRequest postEditRequest = (PostEditRequest) o;
    return Objects.equals(this.title, postEditRequest.title) &&
        Objects.equals(this.body, postEditRequest.body) &&
        Objects.equals(this.likes, postEditRequest.likes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, body, likes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostEditRequest {\n");
    
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    likes: ").append(toIndentedString(likes)).append("\n");
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

