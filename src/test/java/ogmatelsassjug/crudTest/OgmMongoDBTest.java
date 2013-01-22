package ogmatelsassjug.crudTest;

import ogmatelsassjug.models.Post;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@RunWith(Arquillian.class)
public class OgmMongoDBTest extends AbstractTest {

   @Deployment
   public static JavaArchive createDeployment(){
      return ShrinkWrap.create(JavaArchive.class)
            .addPackage(Post.class.getPackage())
            .addClass(OgmMongoDBTest.class)
            .addClass(AbstractTest.class)
            .addAsResource("persistencefiles/ogm-mongodb-persistence.xml", "META-INF/persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource("manifestfiles/manifest-mongodb.mf", "META-INF/MANIFEST.MF");
   }
}
