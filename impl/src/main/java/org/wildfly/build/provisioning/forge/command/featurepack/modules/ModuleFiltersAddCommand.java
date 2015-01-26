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
package org.wildfly.build.provisioning.forge.command.featurepack.modules;

import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.provisioning.forge.command.featurepack.AbstractFeaturePackResourceSelectedCommand;
import org.wildfly.build.provisioning.forge.resource.ModuleFiltersResource;
import org.wildfly.build.provisioning.model.ServerProvisioningDescription;

import javax.inject.Inject;

/**
 * A command to add module filters to the selected feature pack.
 * @author Eduardo Martins
 */
public class ModuleFiltersAddCommand extends AbstractFeaturePackResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Excludes files not matching any filter")
    private UIInput<Boolean> excludeFilesNotFiltered;

    @Inject
    @WithAttributes(label="Edit after adding")
    private UIInput<Boolean> edit;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("module-filters-add")
                .description("Add feature pack's module filters");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        builder.add(excludeFilesNotFiltered).add(edit);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        final UIContext uiContext = context.getUIContext();
        final ModuleFiltersResource resource = getResourceSelected(uiContext).addModuleFilters(new ServerProvisioningDescription.FeaturePack.ModuleFilters(!excludeFilesNotFiltered.getValue()));
        if (resource != null) {
            if (edit.getValue()) {
                uiContext.setSelection(resource);
            }
            return Results.success();
        } else {
            return Results.fail();
        }
    }

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && getResourceSelected(context).getUnderlyingResourceObject().getModuleFilters() == null;
    }
}
