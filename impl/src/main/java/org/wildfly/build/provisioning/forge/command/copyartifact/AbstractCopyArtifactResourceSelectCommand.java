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
package org.wildfly.build.provisioning.forge.command.copyartifact;

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.wildfly.build.provisioning.forge.command.AbstractServerProvisioningDescriptionResourceSelectedCommand;
import org.wildfly.build.provisioning.forge.resource.CopyArtifactResource;

import javax.inject.Inject;
import java.util.List;

/**
 * An abstract command that selects a ${@link org.wildfly.build.provisioning.forge.resource.CopyArtifactResource}.
 * @author Eduardo Martins
 */
public abstract class AbstractCopyArtifactResourceSelectCommand extends AbstractServerProvisioningDescriptionResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Artifact", required=true)
    UISelectOne<CopyArtifactResource> artifact;

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && !getCopyArtifacts(context).isEmpty();
    }

    List<? extends CopyArtifactResource> getCopyArtifacts(UIContext context) {
        return getResourceSelected(context).getCopyArtifacts();
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        final List<? extends CopyArtifactResource> copyArtifacts = getCopyArtifacts(builder.getUIContext());
        artifact.setValueChoices((Iterable)copyArtifacts);
        artifact.setItemLabelConverter(new Converter<CopyArtifactResource, String>() {
            @Override
            public String convert(CopyArtifactResource source) {
                return source.getUnderlyingResourceObject().getArtifact();
            }
        });
        artifact.setValueConverter(new Converter<String, CopyArtifactResource>() {
            @Override
            public CopyArtifactResource convert(String source) {
                for (CopyArtifactResource resource : copyArtifacts) {
                    if (resource.getUnderlyingResourceObject().getArtifact().equals(source)) {
                        return resource;
                    }
                }
                return null;
            }
        });
        builder.add(artifact);
    }
}
