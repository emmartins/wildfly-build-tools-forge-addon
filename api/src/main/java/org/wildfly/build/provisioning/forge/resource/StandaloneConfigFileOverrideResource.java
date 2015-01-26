package org.wildfly.build.provisioning.forge.resource;

/**
 * A standalone {@link org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideResource}.
 * @author Eduardo Martins
 */
public interface StandaloneConfigFileOverrideResource extends ConfigFileOverrideResource {

    ConfigFileOverrideSubsystemsResource addSubsystems();

    ConfigFileOverrideSubsystemsResource getSubsystems();
}
