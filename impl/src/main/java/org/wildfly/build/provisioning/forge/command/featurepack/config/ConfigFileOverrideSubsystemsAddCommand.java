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
package org.wildfly.build.provisioning.forge.command.featurepack.config;

import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideResource;
import org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideSubsystemsResource;
import org.wildfly.build.provisioning.forge.resource.DomainConfigFileOverrideResource;
import org.wildfly.build.provisioning.forge.resource.StandaloneConfigFileOverrideResource;

import javax.inject.Inject;

/**
 * A command to add subsystems to a config file override, overriding the original config file related subsystems.
 *
 * {@link org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideResource} of domain type support multiple subsystems profiles, if such resource is selected then the profile --name param is activated.
 * @author Eduardo Martins
 */
public class ConfigFileOverrideSubsystemsAddCommand extends AbstractConfigFileOverrideResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Subsystems Profile's name", required=true)
    private UIInput<String> name;

    @Inject
    @WithAttributes(label="Edit after adding")
    private UIInput<Boolean> edit;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("config-file-override-subsystems-add")
                .description("Add subsystems filter to a config file override");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        ConfigFileOverrideResource resourceSelected = getResourceSelected(builder.getUIContext());
        if (resourceSelected.getType() == ConfigFileOverrideResource.Type.domain) {
            builder.add(name);
        }
        builder.add(edit);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        final ConfigFileOverrideResource resourceSelected = getResourceSelected(context.getUIContext());
        final ConfigFileOverrideSubsystemsResource resourceAdded;
        switch (resourceSelected.getType()) {
            case standalone:
                resourceAdded = ((StandaloneConfigFileOverrideResource)resourceSelected).addSubsystems();
                break;
            case domain:
                resourceAdded = ((DomainConfigFileOverrideResource)resourceSelected).addSubsystemsProfile(name.getValue());
                break;
            default:
                throw new IllegalStateException("unexpected config file override resource type selected");
        }
        if (resourceAdded != null) {
            if (edit.getValue()) {
                context.getUIContext().setSelection(resourceAdded);
            }
            return Results.success();
        } else {
            return Results.fail();
        }
    }

    @Override
    public boolean isEnabled(UIContext context) {
        if (super.isEnabled(context)) {
            final ConfigFileOverrideResource resourceSelected = getResourceSelected(context);
            if (resourceSelected.getType() == ConfigFileOverrideResource.Type.standalone) {
                return ((StandaloneConfigFileOverrideResource) resourceSelected).getSubsystems() == null;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
