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
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.common.model.ConfigFileOverride;
import org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideResource;

import javax.inject.Inject;

/**
 * A command to add a config file override of standalone type.
 * @author Eduardo Martins
 */
public class StandaloneConfigFileOverrideAddCommand extends AbstractConfigOverrideResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Config Output File", required=true)
    UISelectOne<String> outputFile;

    @Inject
    @WithAttributes(label="Use config file template?")
    private UIInput<Boolean> useTemplate;

    @Inject
    @WithAttributes(label="Edit after adding")
    private UIInput<Boolean> edit;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("standalone-config-file-override-add")
                .description("Add feature pack's standalone config file override");
    }

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && !getResourceSelected(context).getStandaloneConfigFilesNotOverridden().isEmpty();
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        builder.add(outputFile.setValueChoices(getResourceSelected(builder.getUIContext()).getStandaloneConfigFilesNotOverridden())).add(useTemplate).add(edit);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        final ConfigFileOverrideResource resource  = getResourceSelected(context.getUIContext()).addStandaloneConfigFileOverride(new ConfigFileOverride(useTemplate.getValue(), outputFile.getValue()));
        if (edit.getValue()) {
            context.getUIContext().setSelection(resource);
        }
        return Results.success();
    }
}
