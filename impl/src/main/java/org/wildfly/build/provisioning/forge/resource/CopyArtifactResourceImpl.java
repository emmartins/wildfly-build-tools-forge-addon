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
import org.wildfly.build.common.model.CopyArtifact;
import org.wildfly.build.common.model.FileFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The impl of {@link org.wildfly.build.provisioning.forge.resource.CopyArtifactResource}.
 * @author Eduardo Martins
 */
public class CopyArtifactResourceImpl extends ServerProvisioningDescriptionResourceImpl.Child<CopyArtifact> implements CopyArtifactResource {

    /**
     *
     * @param parent
     * @param copyArtifact
     */
    public CopyArtifactResourceImpl(final ServerProvisioningDescriptionResourceImpl parent, CopyArtifact copyArtifact) {
        super("copy-artifact["+copyArtifact.getArtifact()+"]", parent, copyArtifact);
    }

    @Override
    public boolean delete() throws UnsupportedOperationException {
        if (getParent().getServerProvisioningDescription().getCopyArtifacts().remove(getUnderlyingResourceObject())) {
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
    public String toString() {
        StringBuilder sb = new StringBuilder(getName());
        CopyArtifact artifact = getUnderlyingResourceObject();
        sb.append(" artifact = ").append(artifact.getArtifact()).append(", toLocation = ").append(artifact.getToLocation()).append(", extract = ").append(artifact.isExtract());
        return sb.toString();
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
}