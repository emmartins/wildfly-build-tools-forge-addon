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

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.configassembly.SubsystemConfig;

import javax.inject.Inject;

/**
 * A command to remove an existent config file override's subsystem
 * @author Eduardo Martins
 */
public class ConfigFileOverrideSubsystemRemoveCommand extends AbstractConfigFileOverrideSubsystemsResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Subsystem", required=true)
    private UISelectOne<SubsystemConfig> subsystem;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("config-file-override-subsystem-remove")
                .description("Remove a subsystem from the selected config file override's subsystems");
    }

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && !getResourceSelected(context).getSubsystemConfigs().isEmpty();
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        subsystem.setValueChoices(getResourceSelected(builder.getUIContext()).getSubsystemConfigs())
                .setItemLabelConverter(new Converter<SubsystemConfig, String>() {
                    @Override
                    public String convert(SubsystemConfig source) {
                        return source.getSubsystem();
                    }
                });
        builder.add(subsystem);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        return getResourceSelected(context.getUIContext()).removeSubsystemConfig(subsystem.getValue()) ? Results.success() : Results.fail();
    }
}
