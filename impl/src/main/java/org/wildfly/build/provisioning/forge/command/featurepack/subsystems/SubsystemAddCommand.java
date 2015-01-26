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
package org.wildfly.build.provisioning.forge.command.featurepack.subsystems;

import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.provisioning.forge.command.featurepack.AbstractFeaturePackResourceSelectedCommand;
import org.wildfly.build.provisioning.forge.resource.FeaturePackResource;
import org.wildfly.build.provisioning.model.ServerProvisioningDescription;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A command to add a subsystem in the selected feature pack.
 * @author Eduardo Martins
 */
public class SubsystemAddCommand extends AbstractFeaturePackResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Subsystems available to add", required=true)
    private UISelectOne<String> subsystem;

    @Inject
    @WithAttributes(label="Transitively provision subsystem dependencies?")
    private UIInput<Boolean> transitive;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("subsystem-add")
                .description("Add a feature pack's subsystem to provision");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        // retrieve selected feature pack name
        final FeaturePackResource featurePackResource = getResourceSelected(builder.getUIContext());
        // retrieve the subsystems already added to provisioning, these should not be available in the UI
        final List<ServerProvisioningDescription.FeaturePack.Subsystem> subsystemsAdded = featurePackResource.getUnderlyingResourceObject().getSubsystems();
        final Iterable<String> valueChoices;
        if (subsystemsAdded == null || subsystemsAdded.isEmpty()) {
            // all may be selected
            valueChoices = featurePackResource.getFeaturePack().getSubsystems();
        } else {
            // exclude the subsystems already added
            final Set<String> subsystems = new HashSet<>(featurePackResource.getFeaturePack().getSubsystems());
            final Set<String> exclude = new HashSet<>();
            for (ServerProvisioningDescription.FeaturePack.Subsystem subsystemAdded : subsystemsAdded) {
                exclude.add(subsystemAdded.getName());
            }
            subsystems.removeAll(exclude);
            valueChoices = subsystems;
        }
        // define the UI values available to select
        builder.add(subsystem.setValueChoices(valueChoices)).add(transitive);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        final FeaturePackResource featurePackResource = getResourceSelected(context.getUIContext());
        featurePackResource.addSubsystem(new ServerProvisioningDescription.FeaturePack.Subsystem(subsystem.getValue(), transitive.getValue()));
        return Results.success("Subsystem "+subsystem.getValue()+" added. Subsystems after update: "+featurePackResource.getSubsystems());
    }

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && getResourceSelected(context).getUnderlyingResourceObject().getConfigOverride() == null;
    }
}
