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

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.provisioning.forge.command.featurepack.AbstractFeaturePackResourceSelectedCommand;
import org.wildfly.build.provisioning.forge.resource.FeaturePackResource;
import org.wildfly.build.provisioning.model.ServerProvisioningDescription;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * A command to remove a subsystem in the selected feature pack.
 * @author Eduardo Martins
 */
public class SubsystemRemoveCommand extends AbstractFeaturePackResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Subsystem", required=true)
    private UISelectOne<ServerProvisioningDescription.FeaturePack.Subsystem> subsystem;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("subsystem-remove")
                .description("Remove a feature pack's subsystem to provision");
    }

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && !getSubsystems(getResourceSelected(context)).isEmpty();
    }

    private List<ServerProvisioningDescription.FeaturePack.Subsystem> getSubsystems(FeaturePackResource featurePackResource) {
        List<ServerProvisioningDescription.FeaturePack.Subsystem> subsystems = featurePackResource.getUnderlyingResourceObject().getSubsystems();
        if (subsystems == null) {
            subsystems = Collections.emptyList();
        }
        return  subsystems;
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        final List<ServerProvisioningDescription.FeaturePack.Subsystem> subsystems = getSubsystems(getResourceSelected(builder.getUIContext()));
        subsystem.setValueChoices(subsystems);
        subsystem.setItemLabelConverter(new Converter<ServerProvisioningDescription.FeaturePack.Subsystem, String>() {
            @Override
            public String convert(ServerProvisioningDescription.FeaturePack.Subsystem source) {
                return source.getName();
            }
        });
        subsystem.setValueConverter(new Converter<String, ServerProvisioningDescription.FeaturePack.Subsystem>() {
            @Override
            public ServerProvisioningDescription.FeaturePack.Subsystem convert(String source) {
                for (ServerProvisioningDescription.FeaturePack.Subsystem subsystem : subsystems) {
                    if (subsystem.getName().equals(source)) {
                        return subsystem;
                    }
                }
                return null;
            }
        });
        builder.add(subsystem);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        final FeaturePackResource featurePackResource = getResourceSelected(context.getUIContext());
        return featurePackResource.removeSubsystem(subsystem.getValue()) ? Results.success("Subsystem "+subsystem.getValue()+" removed. Subsystems after update: "+String.valueOf(featurePackResource.getSubsystems())) : Results.fail();
    }
}
