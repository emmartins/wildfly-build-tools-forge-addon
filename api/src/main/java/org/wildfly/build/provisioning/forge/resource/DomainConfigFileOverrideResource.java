package org.wildfly.build.provisioning.forge.resource;

import java.util.List;

/**
 * A domain {@link ConfigFileOverrideResource}.
 * @author Eduardo Martins
 */
public interface DomainConfigFileOverrideResource extends ConfigFileOverrideResource {

    ConfigFileOverrideSubsystemsResource addSubsystemsProfile(String name);

    ConfigFileOverrideSubsystemsResource getSubsystemsProfile(String name);

    List<ConfigFileOverrideSubsystemsResource> getSubsystemsProfiles();
}
