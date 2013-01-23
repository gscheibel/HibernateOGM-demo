package ogmatelsassjug.test;

import ogmatelsassjug.models.Author;
import ogmatelsassjug.models.Post;
import ogmatelsassjug.utils.FTEMProvider;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@RunWith(Arquillian.class)
public class DefaultTest {
   @PersistenceContext
   EntityManager em;

   @Inject
   UserTransaction ut;

   @Inject
   FullTextEntityManager ftm;

   @Deployment(name="default")
   public static WebArchive createDeployment(){
      return ShrinkWrap.create(WebArchive.class)
            .addPackage(Post.class.getPackage())
            .addClass(FTEMProvider.class)
            .addClass(DefaultTest.class)
            .addAsLibraries(Maven.resolver().resolve("org.hibernate:hibernate-search:4.2.0.Final").withTransitivity().asFile())
            .addAsResource("manifest.mf", "META-INF/MANIFEST.MF")
            .addAsResource("persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void simpleInsertionTest() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
      final Long postID = 1L;
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

   @Test
   public void associationTest() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
      final String postOGMTitle = "OGM at ElsassJUG";
      final Long postOGMID = 2L;

      final String postInfinispanTitle = "Infinispan cachestore";
      final Long postInfinispanID = 3L;

      final String authorName = "Guillaume";
      Author guillaume = new Author(authorName);

      Post postOGM = new Post(postOGMID, postOGMTitle);
      postOGM.setAuthor(guillaume);

      Post postInfinispan = new Post(postInfinispanID, postInfinispanTitle);
      postInfinispan.setAuthor(guillaume);

      guillaume.setPosts(new HashSet<Post>());
      guillaume.getPosts().add(postInfinispan);
      guillaume.getPosts().add(postOGM);
      ut.begin();
      em.persist(guillaume);
      ut.commit();

      final Post post = em.find(Post.class, postOGMID);
      assertThat(post, is(notNullValue()));
      assertThat(post.getTitle(), is(equalTo(postOGMTitle)));

      final Author postAuthor = post.getAuthor();
      assertThat(postAuthor, is(notNullValue()));
      assertThat(postAuthor.getName(), is(equalTo(authorName)));
      assertThat(postAuthor.getPosts().size(), is(2));
   }

   @Test
   public void elementCollectionTest() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
      Post postElementCollection = new Post(4L, "Element Collection with OGM");
      final HashSet<String> comments = new HashSet<>();
      comments.add("Good to know");
      comments.add("It's not working !!");
      postElementCollection.setComments(comments);

      ut.begin();
      em.persist(postElementCollection);
      ut.commit();

      final Post post = em.find(Post.class, 4L);
      assertThat(post, is(notNullValue()));
      final Set<String> postComments = post.getComments();
      assertThat(postComments, is(notNullValue()));
      assertThat(postComments.size(), is(2));
   }

   @Test
   public void removeTest() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
      final Long id = 5L;
      final String title = "remove post";
      ut.begin();
      em.persist(new Post(id, title));
      ut.commit();

      ut.begin();
      Post p = em.find(Post.class, id);
      assertThat(p, is(notNullValue()));
      assertThat(p.getTitle(), is(equalTo(title)));
      em.remove(p);
      ut.commit();

      Post removedPost = em.find(Post.class, id);
      assertThat(removedPost, is(nullValue()));
   }

   @Test
   public void jpqlTest() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
      final Long id = 6L;
      final String title = "testing query";
      ut.begin();
      em.persist(new Post(id, title));
      ut.commit();

      final Query query = em.createQuery("SELECT p FROM Post p WHERE p.id = :id");
      query.setParameter("id", id);
      final Object result = query.getSingleResult();
      assertThat(result, is(notNullValue()));
      assertThat(result, is(instanceOf(Post.class)));
      Post resultPost = (Post) result;
      assertThat(resultPost.getTitle(), is(equalTo(title)));
   }

   @Test
   public void hsearchQueryTest() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
      final Long id = 7L;
      final String title = "testing query";
      ut.begin();
      em.persist(new Post(id, title));
      ut.commit();

      final QueryBuilder queryBuilder = ftm.getSearchFactory().buildQueryBuilder().forEntity(Post.class).get();
      final org.apache.lucene.search.Query query = queryBuilder.keyword().onField("title").matching(title).createQuery();

      final FullTextQuery fullTextQuery = ftm.createFullTextQuery(query, Post.class);
      fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SKIP,DatabaseRetrievalMethod.FIND_BY_ID);
      final Object result = fullTextQuery.getSingleResult();
      assertThat(result, is(notNullValue()));
      assertThat(result, is(instanceOf(Post.class)));
      Post resultPost = (Post) result;
      assertThat(resultPost.getTitle(), is(equalTo(title)));


   }

   @Test
   public void thousandInsertTest() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
      ut.begin();
      for(long i = 8L; i<= 1008L; i++){
         em.persist(new Post(i, "Post #" + i));
      }
      ut.commit();

      final Post post = em.find(Post.class, 1000L);
      assertThat(post.getTitle(), is(equalTo("Post #1000")));
   }
}
