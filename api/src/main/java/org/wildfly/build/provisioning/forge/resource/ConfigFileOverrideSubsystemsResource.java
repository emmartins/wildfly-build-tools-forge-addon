package org.wildfly.build.provisioning.forge.resource;

import org.jboss.forge.addon.resource.Resource;
import org.wildfly.build.configassembly.SubsystemConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A config file override's subsystem configs resource.
 *
 * @author Eduardo Martins
 */
public interface ConfigFileOverrideSubsystemsResource extends Resource<Map<String, SubsystemConfig>> {

    void addSubsystemConfig(SubsystemConfig subsystemConfig);

    boolean removeSubsystemConfig(SubsystemConfig subsystemConfig);

    List<SubsystemConfig> getSubsystemConfigs();

    Set<String> getSubsystemsNotAdded();

    ConfigFileOverrideResource getParent();

    String getProfileName();
}
