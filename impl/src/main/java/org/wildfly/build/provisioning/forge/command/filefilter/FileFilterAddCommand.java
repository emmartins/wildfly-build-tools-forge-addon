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
package org.wildfly.build.provisioning.forge.command.filefilter;

import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.common.model.FileFilter;

import javax.inject.Inject;

/**
 * A command to add a file filter to a selected {@link org.wildfly.build.provisioning.forge.resource.FileFiltersResource}.
 * @author Eduardo Martins
 */
public class FileFilterAddCommand extends AbstractFileFilterResourceSelectedCommand {

    @Inject
    @WithAttributes(label="The regex's pattern that will be used to match files paths", required=true)
    private UIInput<String> pattern;

    @Inject
    @WithAttributes(label="Include files that match pattern")
    private UIInput<Boolean> include;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("file-filter-add")
                .description("Adds a file filter");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        builder.add(pattern).add(include);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        return getResourceSelected(context.getUIContext()).addFilter(new FileFilter(pattern.getValue(), include.getValue())) ? Results.success() : Results.fail();
    }
}
