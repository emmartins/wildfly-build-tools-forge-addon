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
import org.wildfly.build.common.model.ConfigOverride;
import org.wildfly.build.provisioning.forge.command.featurepack.AbstractFeaturePackResourceSelectedCommand;
import org.wildfly.build.provisioning.forge.resource.ConfigOverrideResource;

import javax.inject.Inject;

/**
 * A command to add config override to the selected feature pack.
 * @author Eduardo Martins
 */
public class ConfigOverrideAddCommand extends AbstractFeaturePackResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Edit after adding")
    private UIInput<Boolean> edit;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("config-override-add")
                .description("Add feature pack's config override");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        builder.add(edit);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        final ConfigOverrideResource resource = getResourceSelected(context.getUIContext()).addConfigOverride(new ConfigOverride());
        if (resource != null) {
            if (edit.getValue()) {
                context.getUIContext().setSelection(resource);
            }
            return Results.success();
        } else {
            return Results.fail();
        }
    }

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && getResourceSelected(context).getConfigOverride() == null;
    }
}
