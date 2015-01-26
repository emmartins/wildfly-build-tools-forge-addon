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
package org.wildfly.build.provisioning.forge.command.featurepack;

import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.provisioning.forge.command.AbstractServerProvisioningDescriptionResourceSelectedCommand;
import org.wildfly.build.provisioning.forge.resource.FeaturePackResource;
import org.wildfly.build.pack.model.Artifact;

import javax.inject.Inject;

/**
 * A command to add a feature pack to the server provisioning config.
 * @author Eduardo Martins
 */
public class FeaturePackAddCommand extends AbstractServerProvisioningDescriptionResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Feature Pack artifact's groupId", required=true)
    private UIInput<String> groupId;

    @Inject
    @WithAttributes(label="Feature Pack artifact's artifactId", required=true)
    private UIInput<String> artifactId;

    @Inject
    @WithAttributes(label="Feature Pack artifact's version", required=true)
    private UIInput<String> version;

    @Inject
    @WithAttributes(label="Feature Pack artifact's classifier", required=false)
    private UIInput<String> classifier;

    @Inject
    @WithAttributes(label="Edit after adding")
    private UIInput<Boolean> edit;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("feature-pack-add")
                .description("Add a Feature Pack to WildFly Server Provisioning");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        builder.add(groupId).add(artifactId).add(classifier).add(version).add(edit);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        FeaturePackResource featurePackResource = getResourceSelected(context.getUIContext()).addFeaturePack(new Artifact(groupId.getValue(), artifactId.getValue(), classifier.getValue(), "zip", version.getValue()));
        if (featurePackResource != null) {
            if (edit.getValue()) {
                context.getUIContext().setSelection(featurePackResource);
            }
            return Results.success();
        } else {
            return Results.fail();
        }
    }
}
