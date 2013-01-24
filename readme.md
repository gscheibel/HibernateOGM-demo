Hibernate OGM demo
================

This demo is used to present [Hibernate OGM](http://hibernate.org/subprojects/ogm.html) with both [EhCache](http://www.ehcache.org/) and [MongoDB](http://www.mongodb.org) modules.

It runs on [JBoss AS 7.1](http://www.jboss.org/as7) using [Arquillian](http://www.arquillian.org) with the remote container.
The part is to run tests by using JPA and Hibernate ORM (default configuration) and then you can switch to OGM.
The only test that won't pass with OGM is the JPQL test because in the current version of OGM it's not supported yet that why Hibernate Search is used instead to run queries

##Module installation:
In order to have the best integration with JBoss AS, Hibernate OGM is installed as a module (just as RDMBS connectors).

- In modules/org/hibernate/main/
   - Modify module.xml
   - Update hibernate jars (to 4.1.7.Final version used in the current version of OGM)
- In modules/org/hibernate/ogm/
   - Create module.xml
   - Add OGM core jar
- In modules/org/hibernate/ogm/mongodb/main
   - Create module.xml
   - Add OGM-MongoDB and mongodb-java-driver jars
- In modules/org/hibernate/ogm/ehcache/main
   - Create module.xml
   - Add OGM-Ehcache and ehcache core jars

##Switching from JPA (Hibernate ORM) to Hibernate OGM:

You have to switch from

 ``<jta-data-source>java:jboss/datasources/MyDataSource</jta-data-source>``  
to  
``<provider>org.hibernate.ogm.jpa.HibernateOgmPersistence</provider>``

###MongoDB configuration
Once the HibernateOgmPersistence is declared, to use MongoDB you have to set ``<property name="hibernate.ogm.datastore.provider" value="MONGODB" />``

For other configuration properties (hostname, username, password, database, etc)  please refer to the [official documentation](http://docs.jboss.org/hibernate/ogm/4.0/reference/en-US/html_single/#d0e1875)

###EhCache configuration
Once the HibernateOgmPersistence is declared, to use EhCache you have to set ``<property name="hibernate.ogm.datastore.provider" value="EHCACHE" />``