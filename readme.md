OgmAtElsassJUG
==============

OGM demo at ElsassJUG

Module installation:
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