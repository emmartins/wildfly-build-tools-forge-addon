wildly-build-tools-forge-addon
==============================

A JBoss Forge addon which provides interactive provisioning of WildFly 9+ Application Servers, through WildFly Build Tools.

The addon supports auto-complete of param values provided by analysis of referenced artifacts, for instance all the config files and subsystems available in a feature pack.

Commands are enabled depending on the selected resource (feature pack, copy artifact, config override, etc).

Installation
============

After installing and starting JBoss Forge

     `$ addon-install --coordinate org.wildfly.build:wildfly-build-tools-forge-addon,1.0.0.Alpha1-SNAPSHOT `


Basic Usage
=====

1. Create/Load WildFly server provisioning configuration XML descriptor

    `$ server-provisioning-config`

    The default XML descriptor file name is server-provisioning.xml.

2. Add a Feature Pack

    `$ feature-pack-add --groupId org.wildfly --artifactId wildfly-web-feature-pack --version 9.0.0.Alpha1`

3. Copy an artifact

    `$ copy-artifact-add --artifact groupId:artifactId:version --location standalone/deployments/`

4. Override an artifact version

    `$ version-override-add --groupId org.wildfly.core --artifactId  wildfly-platform-mbean --version 1.0.0.Alpha13`

5. Build WildFly server

    `$ server-provisioning-config-build`

    The default build dir is target, and the default server name is wildfly, i.e. the provisioned server will be created at target/wildfly

Advanced Usage
=====

1. Change default configuration attributes

    `$ server-provisioning-config-attributes [--copyModuleArtifacts] [--extractSchemas]`

2. Edit a Feature Pack

    `$ feature-pack-edit --groupId org.wildfly --artifactId wildfly-web-feature-pack --version 9.0.0.Alpha1`

    The feature pack resource will be selected, which enables commands to filter the feature pack content. To go back and select the server provisioning configuration

    `[feature-pack[org.wildfly:wildfly-web-feature-pack:9.0.0.Alpha1]]$ cd ..`

    2.1. High Level subsystem filtering

    `[feature-pack[org.wildfly:wildfly-web-feature-pack:9.0.0.Alpha1]]$ subsystem-add --subsystem ee.xml`

    The command above filters the feature pack to provision only the subsystem EE (and its dependencies). Any configuration files provided by other feature packs, which output file are also present in the filtered feature pack, will be modified to include the configurations for added subsystem.

    2.2. Content Filtering

    TODO

    2.3. Module Filtering

    TODO

    2.4. Config Override

    TODO

3. Edit the copy of an artifact

    `$ copy-artifact-edit --artifact groupId:artifactId:version --location standalone/deployments/`

4. Add+Edit and Remove Commands

    Every *-add command that creates a resource, which may be edit afterwards, include an optional param named --edit, that may be used to add and edit the resource in a single command, for instance

    `$ feature-pack-add --groupId org.wildfly --artifactId wildfly-web-feature-pack --version 9.0.0.Alpha1 --edit`

    Every *-add command have a related *-remove command, which reverts the *-add operation, e.g.

    `$ copy-artifact-remove --artifact groupId:artifactId:version`

5. Support of JBoss Forge specific commands

    Besides support for `cd ..` to select parent resources

    `$ ls`

    Lists child resources, for instance, if the server provisioning root resource is selected, each feature pack, copy artifact and version override will be listed.

    `$ cat .`

    If used with the root server provisioning resource selected, the content of the XML descriptor will be printed.


6. Example usage of provided commands

    `server-provisioning-config

    copy-artifact-add --artifact org.wildly:emmartins:1.0 --location tmp --edit
    file-filter-add --pattern *.bat
    file-filter-add --pattern *.tab --include
    file-filter-remove --pattern *.tab
    cd ..
    copy-artifact-edit --artifact org.wildly:emmartins
    cd ..
    copy-artifact-remove --artifact org.wildly:emmartins

    version-override-add --groupId org.blabla --artifactId  bleble --classifier bloblo --extension zip --version 1.0
    version-override-add --groupId org.blabla --artifactId  bleble --version 1.0
    version-override-remove --artifact org.blabla:bleble::bloblo:1.0
    version-override-remove --artifact org.blabla:bleble:1.0

    feature-pack-add --groupId org.wildfly --artifactId wildfly-web-feature-pack --version 9.0.0.Alpha2-SNAPSHOT --edit

    content-filters-add --edit
    file-filter-add --pattern *.bat
    file-filter-add --pattern *.tab --include
    file-filter-remove --pattern *.tab
    cd ..
    content-filters-edit
    cd ..
    content-filters-remove
    content-filters-add

    module-filters-add --edit
    module-filter-add --pattern com/sun --transitive
    module-filter-add --pattern org/jboss --include --transitive
    module-filter-add --pattern com/jboss --include
    module-filter-remove --pattern  com/sun
    cd ..
    module-filters-edit
    cd ..
    module-filters-remove
    module-filters-add

    config-override-add --edit

    standalone-config-file-override-add --outputFile standalone/configuration/standalone.xml --edit
    config-file-override-subsystems-add --edit
    config-file-override-subsystem-add --subsystem ee.xml
    config-file-override-subsystem-add --subsystem naming.xml --supplement web
    config-file-override-subsystem-remove --subsystem naming.xml
    cd ..
    config-file-override-subsystems-edit
    cd ..
    config-file-override-subsystems-remove
    cd ..
    config-file-override-edit --outputFile standalone/configuration/standalone.xml
    cd ..
    config-file-override-remove --outputFile standalone/configuration/standalone.xml

    domain-config-file-override-add --outputFile domain/configuration/domain.xml --edit
    config-file-override-subsystems-add --name default --edit
    config-file-override-subsystem-add --subsystem ee.xml
    config-file-override-subsystem-add --subsystem naming.xml --supplement web
    config-file-override-subsystem-remove --subsystem naming.xml
    cd ..
    config-file-override-subsystems-edit --name default
    cd ..
    config-file-override-subsystems-remove --name default
    cd ..
    config-file-override-edit --outputFile domain/configuration/domain.xml
    cd ..
    config-file-override-remove --outputFile domain/configuration/domain.xml

    cd ..
    config-override-edit
    cd ..
    config-override-remove

    cd ..
    feature-pack-edit --artifact org.wildfly:wildfly-web-feature-pack:9.0.0.Alpha2-SNAPSHOT
    cd ..
    feature-pack-remove --artifact org.wildfly:wildfly-web-feature-pack:9.0.0.Alpha2-SNAPSHOT`

