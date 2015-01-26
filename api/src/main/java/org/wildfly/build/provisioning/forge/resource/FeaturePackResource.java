/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.build.provisioning.forge.resource;

import org.jboss.forge.addon.resource.Resource;
import org.wildfly.build.common.model.ConfigOverride;
import org.wildfly.build.pack.model.FeaturePack;
import org.wildfly.build.provisioning.model.ServerProvisioningDescription;

import java.util.List;

/**
 * The {@link org.jboss.forge.addon.resource.Resource} for a {@link org.wildfly.build.provisioning.model.ServerProvisioningDescription.FeaturePack}.
 * @author Eduardo Martins
 */
public interface FeaturePackResource extends Resource<ServerProvisioningDescription.FeaturePack> {

    /**
     * Retrieves the feature pack, creating it from its artifact if needed.
     * @return
     */
    FeaturePack getFeaturePack();

    ConfigOverrideResource addConfigOverride(ConfigOverride configOverride);

    ConfigOverrideResource getConfigOverride();

    ContentFiltersResource addContentFilters(ServerProvisioningDescription.FeaturePack.ContentFilters contentFilters);

    ContentFiltersResource getContentFilters();

    ModuleFiltersResource addModuleFilters(ServerProvisioningDescription.FeaturePack.ModuleFilters moduleFilters);

    ModuleFiltersResource getModuleFilters();

    boolean addSubsystem(ServerProvisioningDescription.FeaturePack.Subsystem subsystem);

    List<ServerProvisioningDescription.FeaturePack.Subsystem> getSubsystems();

    boolean removeSubsystem(ServerProvisioningDescription.FeaturePack.Subsystem subsystem);

}
