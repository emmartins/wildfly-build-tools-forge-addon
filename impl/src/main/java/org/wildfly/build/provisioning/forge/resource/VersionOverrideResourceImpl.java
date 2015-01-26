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

import org.wildfly.build.pack.model.Artifact;

/**
 * The impl of {@link org.wildfly.build.provisioning.forge.resource.VersionOverrideResource}.
 * @author Eduardo Martins
 */
public class VersionOverrideResourceImpl extends ServerProvisioningDescriptionResourceImpl.Child<Artifact> implements VersionOverrideResource {

    /**
     *
     * @param parent
     * @param artifact
     */
    public VersionOverrideResourceImpl(final ServerProvisioningDescriptionResourceImpl parent, Artifact artifact) {
        super("version-override["+artifact.toString()+"]", parent, artifact);
    }

    @Override
    public boolean delete() throws UnsupportedOperationException {
        final ServerProvisioningDescriptionResourceImpl parent = getParent();
        if (parent.getServerProvisioningDescription().getVersionOverrides().remove(getUnderlyingResourceObject())) {
            parent.writeXML();
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
        Artifact artifact = getUnderlyingResourceObject();
        sb.append(" artifact = ").append(artifact.getGACE()).append(", version = ").append(artifact.getVersion());
        return sb.toString();
    }
}