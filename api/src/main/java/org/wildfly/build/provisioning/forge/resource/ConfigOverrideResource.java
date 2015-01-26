package org.wildfly.build.provisioning.forge.resource;

import org.jboss.forge.addon.resource.Resource;
import org.wildfly.build.common.model.ConfigFileOverride;
import org.wildfly.build.common.model.ConfigOverride;

import java.util.List;

/**
 * A feature pack's config override resource.
 * @author Eduardo Martins
 */
public interface ConfigOverrideResource extends Resource<ConfigOverride> {

    StandaloneConfigFileOverrideResource addStandaloneConfigFileOverride(ConfigFileOverride configFileOverride);

    DomainConfigFileOverrideResource addDomainConfigFileOverride(ConfigFileOverride configFileOverride);

    List<? extends StandaloneConfigFileOverrideResource> getStandaloneConfigFileOverrides();

    List<? extends DomainConfigFileOverrideResource> getDomainConfigFileOverrides();

    List<? extends ConfigFileOverrideResource> getConfigFileOverrides();

    List<String> getStandaloneConfigFilesNotOverridden();

    List<String> getDomainConfigFilesNotOverridden();
}
