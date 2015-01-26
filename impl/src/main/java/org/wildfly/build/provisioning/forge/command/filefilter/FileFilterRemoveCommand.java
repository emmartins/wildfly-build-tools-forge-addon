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

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.common.model.FileFilter;

import javax.inject.Inject;
import java.util.List;

/**
 * A command to remove a file filter from a selected {@link org.wildfly.build.provisioning.forge.resource.FileFiltersResource}.
 * @author Eduardo Martins
 */
public class FileFilterRemoveCommand extends AbstractFileFilterResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Filter to remove", required=true)
    private UISelectOne<FileFilter> pattern;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("file-filter-remove")
                .description("Removes a file filter");
    }

    @Override
    public boolean isEnabled(UIContext context) {
        if (super.isEnabled(context)) {
            final List<FileFilter> filters = getResourceSelected(context).getFilters();
            return filters != null && !filters.isEmpty();
        } else {
            return false;
        }
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        final List<FileFilter> filters = getResourceSelected(builder.getUIContext()).getFilters();
        pattern.setValueChoices(filters);
        pattern.setItemLabelConverter(new Converter<FileFilter, String>() {
            @Override
            public String convert(FileFilter source) {
                return source.getPattern();
            }
        });
        pattern.setValueConverter(new Converter<String, FileFilter>() {
            @Override
            public FileFilter convert(String source) {
                for (FileFilter filter : filters) {
                    if (filter.getPattern().equals(source)) {
                        return filter;
                    }
                }
                return null;
            }
        });
        builder.add(pattern);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        return getResourceSelected(context.getUIContext()).removeFilter(pattern.getValue()) ? Results.success() : Results.fail();
    }
}
