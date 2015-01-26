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

import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;

/**
 * A command that selects an existent {@link org.wildfly.build.provisioning.forge.resource.ModuleFiltersResource}, to edit its content.
 * @author Eduardo Martins
 */
public class ModuleFiltersEditCommand extends AbstractModuleFiltersResourceSelectCommand {

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("module-filters-edit")
                .description("Edit a Feature Pack's module filters");
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        context.getUIContext().setSelection(getResourceSelected(context.getUIContext()).getModuleFilters());
        return Results.success();
    }
}
