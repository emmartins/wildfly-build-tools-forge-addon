package org.wildfly.build.provisioning.forge.resource;

import org.jboss.forge.addon.resource.Resource;
import org.wildfly.build.common.model.ConfigFileOverride;

/**
 * A {@link org.wildfly.build.common.model.ConfigFileOverride} resource.
 * @author Eduardo Martins
 */
public interface ConfigFileOverrideResource extends Resource<ConfigFileOverride> {

    public enum Type {
        standalone, domain;
    }

    /**
     * Retrieves the resource's parent.
     * @return
     */
    ConfigOverrideResource getParent();

    /**
     *
     * @return
     */
    Type getType();
}
