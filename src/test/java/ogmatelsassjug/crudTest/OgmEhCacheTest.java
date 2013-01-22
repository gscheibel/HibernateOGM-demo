package ogmatelsassjug.crudTest;

import ogmatelsassjug.models.Post;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@RunWith(Arquillian.class)
public class OgmEhCacheTest extends AbstractTest {

   @Deployment
   public static JavaArchive createDeployment(){
      return ShrinkWrap.create(JavaArchive.class)
            .addPackage(Post.class.getPackage())
            .addClass(OgmEhCacheTest.class)
            .addClass(AbstractTest.class)
            .addAsResource("persistencefiles/ogm-ehcache-persistence.xml", "META-INF/persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource("manifestfiles/manifest-ehcache.mf", "META-INF/MANIFEST.MF");
   }
}
