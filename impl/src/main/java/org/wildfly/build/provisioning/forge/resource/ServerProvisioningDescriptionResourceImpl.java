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

import org.jboss.forge.addon.resource.AbstractFileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceException;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.resource.VirtualResource;
import org.wildfly.build.ArtifactResolver;
import org.wildfly.build.StandaloneAetherArtifactFileResolver;
import org.wildfly.build.common.model.CopyArtifact;
import org.wildfly.build.pack.model.Artifact;
import org.wildfly.build.pack.model.DelegatingArtifactResolver;
import org.wildfly.build.pack.model.FeaturePackArtifactResolver;
import org.wildfly.build.provisioning.ServerProvisioner;
import org.wildfly.build.provisioning.model.ServerProvisioningDescription;
import org.wildfly.build.provisioning.model.ServerProvisioningDescriptionModelParser;
import org.wildfly.build.provisioning.model.ServerProvisioningDescriptionXmlWriter;
import org.wildfly.build.util.MapPropertyResolver;
import org.wildfly.build.util.PropertiesBasedArtifactResolver;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * The impl of {@link ServerProvisioningDescriptionResource}.
 * @author Eduardo Martins
 */
public class ServerProvisioningDescriptionResourceImpl extends AbstractFileResource<ServerProvisioningDescriptionResource> implements ServerProvisioningDescriptionResource {

    /**
     * the description within the name
     */
    private final ServerProvisioningDescription description;

    /**
     *
     * @param factory
     * @param file
     */
    public ServerProvisioningDescriptionResourceImpl(final ResourceFactory factory, final File file) {
        super(factory, file);
        if (exists()) {
            description = parseXML();
        } else {
            description = new ServerProvisioningDescription();
        }
    }

    @Override
    public Resource<File> createFrom(File file) {
        return new ServerProvisioningDescriptionResourceImpl(getResourceFactory(), file);
    }

    @Override
    public Resource<?> getChild(String name) {
        for (Resource<?> child : listResources()) {
            if (child.getName().trim().equals(name)) {
                return child;
            }
        }
        return null;
    }

    @Override
    protected List<Resource<?>> doListResources() {
        final List<Resource<?>> children = new ArrayList<>();
        children.addAll(getFeaturePacks());
        children.addAll(getVersionOverrides());
        children.addAll(getCopyArtifacts());
        return children;
    }

    @Override
    public CopyArtifactResource addCopyArtifact(Artifact artifact, String location, boolean extract) {
        final CopyArtifact copyArtifact = new CopyArtifact(artifact.getGACE().toString(), location, extract);
        final CopyArtifactResourceImpl copyArtifactResource = new CopyArtifactResourceImpl(this, copyArtifact);
        if (description.getCopyArtifacts().add(copyArtifact)) {
            description.getVersionOverrides().add(artifact);
            writeXML();
            return copyArtifactResource;
        } else {
            return null;
        }
    }

    @Override
    public List<CopyArtifactResourceImpl> getCopyArtifacts() {
        final List<CopyArtifactResourceImpl> result = new ArrayList<>();
        List<CopyArtifact> copyArtifacts = description.getCopyArtifacts();
        if (copyArtifacts != null) {
           for (CopyArtifact copyArtifact : copyArtifacts) {
                result.add(new CopyArtifactResourceImpl(this, copyArtifact));
            }
        }
        return result;
    }

    @Override
    public List<FeaturePackResourceImpl> getFeaturePacks() {
        final List<FeaturePackResourceImpl> result = new ArrayList<>();
        for (ServerProvisioningDescription.FeaturePack featurePack : description.getFeaturePacks()) {
            result.add(new FeaturePackResourceImpl(this, featurePack));
        }
        return result;
    }

    @Override
    public FeaturePackResource addFeaturePack(Artifact artifact) {
        final ServerProvisioningDescription.FeaturePack featurePack = new ServerProvisioningDescription.FeaturePack(artifact, null, null, null, null);
        final FeaturePackResourceImpl featurePackResource = new FeaturePackResourceImpl(this, featurePack);
        // assert that the feature pack exists
        if (featurePackResource.getFeaturePack() == null) {
            throw new IllegalArgumentException("unable to retrieve feature pack with artifact "+artifact);
        }
        if (description.getFeaturePacks().add(featurePack)) {
            writeXML();
            return featurePackResource;
        } else {
            return null;
        }
    }

    @Override
    public List<VersionOverrideResourceImpl> getVersionOverrides() {
        final List<VersionOverrideResourceImpl> result = new ArrayList<>();
        for (Artifact artifact : description.getVersionOverrides()) {
            result.add(new VersionOverrideResourceImpl(this, artifact));
        }
        return result;
    }

    @Override
    public VersionOverrideResource addVersionOverride(Artifact artifact) {
        final VersionOverrideResource versionOverrideResource = new VersionOverrideResourceImpl(this, artifact);
        if (description.getVersionOverrides().add(artifact)) {
            writeXML();
            return versionOverrideResource;
        } else {
            return null;
        }
    }

    @Override
    public void setAttributes(boolean copyModuleArtifacts, boolean extractSchemas) {
        description.setCopyModuleArtifacts(copyModuleArtifacts);
        description.setExtractSchemas(extractSchemas);
        writeXML();
    }

    public ServerProvisioningDescriptionResource writeXML() {
        try {
            ServerProvisioningDescriptionXmlWriter.INSTANCE.writeContent(getUnderlyingResourceObject(), description);
            return this;
        } catch (Throwable e) {
            throw new ResourceException("Failed to write the server provisioning description", e);
        }
    }

    public ServerProvisioningDescription getServerProvisioningDescription() {
        return description;
    }

    private ServerProvisioningDescription parseXML() {
        try (InputStream in = getResourceInputStream()) {
            return new ServerProvisioningDescriptionModelParser(new MapPropertyResolver(System.getProperties())).parse(in);
        } catch (Throwable e) {
            throw new ResourceException("Failed to read the server provisioning description", e);
        }
    }

    @Override
    public void provision(String buildDir, String serverName) {
        // environment is the sys properties
        final Properties environment = System.getProperties();
        // create version override artifact resolver
        ArtifactResolver overrideArtifactResolver = new FeaturePackArtifactResolver(description.getVersionOverrides());
        if(Boolean.valueOf(environment.getProperty("system-property-version-overrides", "false"))) {
            overrideArtifactResolver = new DelegatingArtifactResolver(new PropertiesBasedArtifactResolver(environment), overrideArtifactResolver);
        }
        // provision the server
        final File outputDir = new File(new File(getUnderlyingResourceObject().getParent(), buildDir), serverName);
        ServerProvisioner.build(description, outputDir, StandaloneAetherArtifactFileResolver.DEFAULT_INSTANCE, overrideArtifactResolver);
        System.out.println("Server provisioning at " + outputDir + " complete.");
    }

    /**
     * The foundation for all {@link org.wildfly.build.provisioning.forge.resource.ServerProvisioningDescriptionResourceImpl} child resources.
     * @param <T>
     */
    public abstract static class Child<T> extends VirtualResource<T> {

        private final T t;

        private final String name;

        public Child(String name, ServerProvisioningDescriptionResourceImpl parent, T t) {
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
        public ServerProvisioningDescriptionResourceImpl getParent() {
            return (ServerProvisioningDescriptionResourceImpl) super.getParent();
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
