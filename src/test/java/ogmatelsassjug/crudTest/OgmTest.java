package ogmatelsassjug.crudTest;

import ogmatelsassjug.models.Post;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@RunWith(Arquillian.class)
public class OgmTest {

   @PersistenceContext( type = PersistenceContextType.EXTENDED)
   EntityManager em;

   @Inject
   UserTransaction ut;

   @Deployment
   public static Archive<?> createDeployment(){
      return ShrinkWrap.create(JavaArchive.class)
            .addPackage(Post.class.getPackage())
            .addAsResource("ogm-persistence.xml", "META-INF/persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource("manifest.mf", "META-INF/MANIFEST.MF");
   }

   @Test
   public void HelloWordTest() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
      final Long postID = new Long(1234L);
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
