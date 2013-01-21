package ogmatelsassjug.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@Entity
public class Post {

   @Id
   private Long id;
   public Long getId() {return id;}
   public void setId(Long id) {this.id = id;}

   private String title;
   public String getTitle() {return title;}
   public void setTitle(String title) {this.title = title;}
}
