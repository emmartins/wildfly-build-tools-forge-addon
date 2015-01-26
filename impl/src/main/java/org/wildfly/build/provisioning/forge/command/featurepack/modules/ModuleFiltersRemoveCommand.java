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
 * A command that removes an existent {@link org.wildfly.build.provisioning.forge.resource.ModuleFiltersResource}.
 * @author Eduardo Martins
 */
public class ModuleFiltersRemoveCommand extends AbstractModuleFiltersResourceSelectCommand {

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("module-filters-remove")
                .description("Remove the feature pack's module filters");
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        return getResourceSelected(context.getUIContext()).getModuleFilters().delete() ? Results.success() : Results.fail();
    }
}
