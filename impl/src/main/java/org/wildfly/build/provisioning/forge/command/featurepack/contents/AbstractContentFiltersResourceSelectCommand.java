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
package org.wildfly.build.provisioning.forge.command.featurepack.contents;

import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.wildfly.build.provisioning.forge.command.featurepack.AbstractFeaturePackResourceSelectedCommand;

/**
 * Abstract command that is only enabled if the selected name is a {@link org.wildfly.build.provisioning.forge.resource.FeaturePackResource} which contains content filters.
 * @author Eduardo Martins
 */
public abstract class AbstractContentFiltersResourceSelectCommand extends AbstractFeaturePackResourceSelectedCommand {

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {

    }

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && getResourceSelected(context).getContentFilters() != null;
    }
}
