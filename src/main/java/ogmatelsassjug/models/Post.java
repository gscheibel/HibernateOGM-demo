package ogmatelsassjug.models;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Set;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@Entity
@Indexed
public class Post {

   @Id
   private Long id;
   public Long getId() {return id;}
   public void setId(Long id) {this.id = id;}

   @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
   private String title;
   public String getTitle() {return title;}
   public void setTitle(String title) {this.title = title;}

   @ManyToOne
   private Author author;
   public Author getAuthor() {return author;}
   public void setAuthor(Author author) {this.author = author;}

   @ElementCollection
   @CollectionTable(name = "post_comments")
   @Column(length = 500)
   private Set<String> comments;
   public Set<String> getComments() {return comments;}
   public void setComments(Set<String> comments) {this.comments = comments;}


   public Post() {
      super();
   }

   public Post(Long id, String title) {
      this.id = id;
      this.title = title;
   }
}
