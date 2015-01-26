package org.wildfly.build.provisioning.forge.resource;

import org.jboss.forge.addon.resource.Resource;
import org.wildfly.build.provisioning.model.ModuleFilter;
import org.wildfly.build.provisioning.model.ServerProvisioningDescription;

import java.util.List;

/**
 * A feature pack's module filters resource.
 * @author Eduardo Martins
 */
public interface ModuleFiltersResource extends Resource<ServerProvisioningDescription.FeaturePack.ModuleFilters> {

    /**
     *
     * @param filter
     * @return
     */
    boolean addFilter(ModuleFilter filter);

    /**
     *
     * @return
     */
    List<ModuleFilter> getFilters();

    /**
     *
     * @param filter
     * @return
     */
    boolean removeFilter(ModuleFilter filter);
}
