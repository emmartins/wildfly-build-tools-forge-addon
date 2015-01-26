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

import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.provisioning.forge.command.AbstractServerProvisioningDescriptionResourceSelectedCommand;
import org.wildfly.build.provisioning.forge.resource.CopyArtifactResource;
import org.wildfly.build.pack.model.Artifact;

import javax.inject.Inject;

/**
 * A command to add a copy artifact.
 * @author Eduardo Martins
 */
public class CopyArtifactAddCommand extends AbstractServerProvisioningDescriptionResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Artifact coords (groupId:artifactId:version)", required=true)
    private UIInput<String> artifact;

    @Inject
    @WithAttributes(label="Target location", required=true)
    private UIInput<String> location;

    @Inject
    @WithAttributes(label="Extract artifact")
    private UIInput<Boolean> extract;

    @Inject
    @WithAttributes(label="Edit after adding")
    private UIInput<Boolean> edit;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("copy-artifact-add")
                .description("Add a copy of an artifact to WildFly Server Provisioning");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        builder.add(artifact).add(location).add(extract).add(edit);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        final CopyArtifactResource copyArtifactResource = getResourceSelected(context.getUIContext()).addCopyArtifact(Artifact.parse(artifact.getValue()), location.getValue(), extract.getValue());
        if (copyArtifactResource != null) {
            if (edit.getValue()) {
                context.getUIContext().setSelection(copyArtifactResource);
            }
            return Results.success();
        } else {
            return Results.fail();
        }

    }
}
