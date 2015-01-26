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
import org.jboss.forge.addon.resource.VirtualResource;
import org.wildfly.build.common.model.ConfigOverride;
import org.wildfly.build.pack.model.FeaturePack;
import org.wildfly.build.pack.model.FeaturePackFactory;
import org.wildfly.build.StandaloneAetherArtifactFileResolver;
import org.wildfly.build.provisioning.model.ServerProvisioningDescription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The impl of {@link org.wildfly.build.provisioning.forge.resource.FeaturePackResource}.
 * @author Eduardo Martins
 */
public class FeaturePackResourceImpl extends ServerProvisioningDescriptionResourceImpl.Child<ServerProvisioningDescription.FeaturePack> implements FeaturePackResource {

    private FeaturePack featurePack;

    public FeaturePackResourceImpl(final ServerProvisioningDescriptionResourceImpl parent, ServerProvisioningDescription.FeaturePack featurePack) {
        super("feature-pack["+featurePack.getArtifact().toString()+"]", parent, featurePack);
    }

    @Override
    public synchronized FeaturePack getFeaturePack() {
        if (featurePack == null) {
            featurePack = FeaturePackFactory.createPack(getUnderlyingResourceObject().getArtifact(), StandaloneAetherArtifactFileResolver.DEFAULT_INSTANCE, null);
        }
        return featurePack;
    }

    @Override
    public ConfigOverrideResource addConfigOverride(ConfigOverride configOverride) {
        getUnderlyingResourceObject().setConfigOverride(configOverride);
        writeXML();
        return new ConfigOverrideResourceImpl(this);
    }

    @Override
    public ConfigOverrideResource getConfigOverride() {
        return getUnderlyingResourceObject().getConfigOverride() != null ? new ConfigOverrideResourceImpl(this) : null;
    }

    @Override
    public ContentFiltersResource addContentFilters(ServerProvisioningDescription.FeaturePack.ContentFilters contentFilters) {
        getUnderlyingResourceObject().setContentFilters(contentFilters);
        writeXML();
        return new ContentFiltersResourceImpl(this);
    }

    @Override
    public ContentFiltersResource getContentFilters() {
        return getUnderlyingResourceObject().getContentFilters() != null ? new ContentFiltersResourceImpl(this) : null;
    }

    @Override
    public ModuleFiltersResource addModuleFilters(ServerProvisioningDescription.FeaturePack.ModuleFilters moduleFilters) {
        getUnderlyingResourceObject().setModuleFilters(moduleFilters);
        writeXML();
        return new ModuleFiltersResourceImpl(this);
    }

    @Override
    public ModuleFiltersResource getModuleFilters() {
        return getUnderlyingResourceObject().getModuleFilters() != null ? new ModuleFiltersResourceImpl(this) : null;
    }

    @Override
    public boolean addSubsystem(ServerProvisioningDescription.FeaturePack.Subsystem subsystem) {
        final ServerProvisioningDescription.FeaturePack provisioningFeaturePack = getUnderlyingResourceObject();
        List<ServerProvisioningDescription.FeaturePack.Subsystem> subsystems = provisioningFeaturePack.getSubsystems();
        if (subsystems == null) {
            subsystems = new ArrayList<>();
            provisioningFeaturePack.setSubsystems(subsystems);
        }
        if (subsystems.add(subsystem)) {
            writeXML();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ServerProvisioningDescription.FeaturePack.Subsystem> getSubsystems() {
        return getUnderlyingResourceObject().getSubsystems();
    }

    @Override
    public boolean removeSubsystem(ServerProvisioningDescription.FeaturePack.Subsystem subsystem) {
        final ServerProvisioningDescription.FeaturePack provisioningFeaturePack = getUnderlyingResourceObject();
        final List<ServerProvisioningDescription.FeaturePack.Subsystem> subsystems = provisioningFeaturePack.getSubsystems();
        if (subsystems != null && subsystems.remove(subsystem)) {
            if (subsystems.isEmpty()) {
                provisioningFeaturePack.setSubsystems(null);
            }
            writeXML();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete() throws UnsupportedOperationException {
        if (getParent().getServerProvisioningDescription().getFeaturePacks().remove(getUnderlyingResourceObject())) {
            writeXML();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(boolean recursive) throws UnsupportedOperationException {
        return delete();
    }

    @Override
    protected List<Resource<?>> doListResources() {
        final List<Resource<?>> resources = new ArrayList<>();
        final List <ServerProvisioningDescription.FeaturePack.Subsystem> subsystems = getSubsystems();
        if (subsystems != null) {
            for (ServerProvisioningDescription.FeaturePack.Subsystem subsystem : subsystems) {
                resources.add(new NamedVirtualResource(this, subsystem));
            }
        }
        addResourceToList(getModuleFilters(), resources);
        addResourceToList(getConfigOverride(), resources);
        addResourceToList(getContentFilters(), resources);
        return resources;
    }

    private void addResourceToList(Resource resource, List<Resource<?>> resources) {
        if (resource != null) {
            resources.add(resource);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getName());
        ServerProvisioningDescription.FeaturePack featurePack = getUnderlyingResourceObject();
        sb.append(" artifact = ").append(featurePack.getArtifact().getGACE()).append(", version = ").append(featurePack.getArtifact().getVersion());
        if (featurePack.getSubsystems() != null) {
            sb.append(", subsystems = ").append(featurePack.getSubsystems());
        }
        return sb.toString();
    }

    /**
     * The foundation for {@link org.wildfly.build.provisioning.forge.resource.FeaturePackResourceImpl} child resources.
     * @param <T>
     */
    public abstract static class Child<T> extends VirtualResource<T> {

        private final T t;

        private final String name;

        public Child(String name, FeaturePackResourceImpl parent, T t) {
            super(parent.getResourceFactory(), parent);
            this.t = t;
            this.name = name;
        }

        public void writeXML() {
            getParent().writeXML();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public FeaturePackResourceImpl getParent() {
            return (FeaturePackResourceImpl) super.getParent();
        }

        @Override
        public T getUnderlyingResourceObject() {
            return t;
        }

        @Override
        protected List<Resource<?>> doListResources() {
            return Collections.emptyList();
        }

        @Override
        public Resource<?> getChild(String name) {
            return null;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}