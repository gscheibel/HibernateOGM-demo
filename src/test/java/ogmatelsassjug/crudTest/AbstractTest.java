package ogmatelsassjug.crudTest;

import ogmatelsassjug.models.Post;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
public abstract class AbstractTest {
   @PersistenceContext
   EntityManager em;

   @Inject
   UserTransaction ut;

   @Test
   public void simpleInsertionTest() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
      final Long postID = 1234L;
      final String postTitle = "OGM is cool";

      ut.begin();
      Post p = new Post();
      p.setId(postID);
      p.setTitle(postTitle);
      em.persist(p);
      ut.commit();

      Post post = em.find(Post.class, postID);
      assertThat(post, is(notNullValue()));
      assertThat(post.getTitle(), equalTo(postTitle));
    }
}
