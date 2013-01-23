package ogmatelsassjug.models;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.Set;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@Entity
@Indexed
public class Author {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorSequenceGenerator")
   @SequenceGenerator(name = "authorSequenceGenerator",
                      sequenceName = "author_id_sequence",
                      initialValue = 0,
                      allocationSize = 10)
   private Long id;
   public Long getId() {return id;}
   public void setId(Long id) {this.id = id;}

   @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
   private String name;
   public String getName() {return name;}
   public void setName(String name) {this.name = name;}

   @OneToMany(cascade = CascadeType.ALL)
   @ContainedIn
   private Set<Post> posts;
   public Set<Post> getPosts() {return posts;}
   public void setPosts(Set<Post> posts) {this.posts = posts;}

   public Author() {
      super();
   }

   public Author(String name) {
      this.name = name;
   }
}
