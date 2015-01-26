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
import org.wildfly.build.provisioning.model.ModuleFilter;

import javax.inject.Inject;

/**
 * A command that adds a module filter.
 * @author Eduardo Martins
 */
public class ModuleFilterAddCommand extends AbstractModuleFiltersResourceSelectedCommand {

    @Inject
    @WithAttributes(label="The regex's pattern that will be used to match modules", required=true)
    private UIInput<String> pattern;

    @Inject
    @WithAttributes(label="Provision modules that match the regex?")
    private UIInput<Boolean> include;

    @Inject
    @WithAttributes(label="Match modules which are transitive dependencies?")
    private UIInput<Boolean> transitive;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("module-filter-add")
                .description("Adds a feature pack's module filter");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        builder.add(pattern).add(include).add(transitive);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        // add the file filter
        return getResourceSelected(context.getUIContext()).addFilter(new ModuleFilter(pattern.getValue(), include.getValue(), transitive.getValue())) ? Results.success() : Results.fail();
    }
}
