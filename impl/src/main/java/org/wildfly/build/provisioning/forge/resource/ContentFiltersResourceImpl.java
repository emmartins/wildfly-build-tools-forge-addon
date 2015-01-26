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
import org.wildfly.build.common.model.FileFilter;
import org.wildfly.build.provisioning.model.ServerProvisioningDescription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The impl of {@link org.wildfly.build.provisioning.forge.resource.ContentFiltersResource}.
 * @author Eduardo Martins
 */
public class ContentFiltersResourceImpl extends FeaturePackResourceImpl.Child<ServerProvisioningDescription.FeaturePack.ContentFilters> implements ContentFiltersResource {

    public ContentFiltersResourceImpl(FeaturePackResourceImpl parent) {
        super("content-filters", parent, parent.getUnderlyingResourceObject().getContentFilters());
    }

    @Override
    public boolean delete() throws UnsupportedOperationException {
        getParent().getUnderlyingResourceObject().setContentFilters(null);
        writeXML();
        return true;
    }

    @Override
    public boolean delete(boolean recursive) throws UnsupportedOperationException {
        return delete();
    }

    @Override
    public boolean addFilter(FileFilter filter) {
        if (getUnderlyingResourceObject().getFilters().add(filter)) {
            writeXML();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<FileFilter> getFilters() {
        return Collections.unmodifiableList(getUnderlyingResourceObject().getFilters());
    }

    @Override
    public boolean removeFilter(FileFilter filter) {
        if (getUnderlyingResourceObject().getFilters().remove(filter)) {
            writeXML();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected List<Resource<?>> doListResources() {
        List<Resource<?>> resources = new ArrayList<>();
        for (FileFilter filter : getFilters()) {
            resources.add(new NamedVirtualResource(this, filter));
        }
        return resources;
    }

    /*
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getName());
        ServerProvisioningDescription.FeaturePack featurePack = getServerProvisioningDescriptionFeaturePack();
        sb.append(" --> artifact = ").append(featurePack.getArtifact().getGACE()).append(", version = ").append(featurePack.getArtifact().getVersion());
        if (featurePack.getSubsystems() != null) {
            sb.append(", subsystems = ").append(featurePack.getSubsystems());
        }
        return sb.toString();
    }*/
}