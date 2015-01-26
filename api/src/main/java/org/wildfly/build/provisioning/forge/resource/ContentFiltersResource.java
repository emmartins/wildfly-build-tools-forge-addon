package org.wildfly.build.provisioning.forge.resource;

import org.jboss.forge.addon.resource.Resource;
import org.wildfly.build.provisioning.model.ServerProvisioningDescription;

/**
 * A feature pack's content filters resource.
 * @author Eduardo Martins
 */
public interface ContentFiltersResource extends Resource<ServerProvisioningDescription.FeaturePack.ContentFilters>, FileFiltersResource {
}
