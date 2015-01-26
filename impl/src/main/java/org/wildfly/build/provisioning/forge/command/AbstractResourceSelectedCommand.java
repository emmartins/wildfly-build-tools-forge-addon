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
package org.wildfly.build.provisioning.forge.command;

import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

/**
 * The foundation for all the server provisioning commands, an abstract command that is only enbaled for a specific name type, amd which also handles the creation of the command metadata and definition of its category.
 *
 * @param <T> the type of the name selected for the command to be enabled
 * @author Eduardo Martins
 */
public abstract class AbstractResourceSelectedCommand<T> extends org.jboss.forge.addon.ui.command.AbstractUICommand implements UICommand {

    /**
     * the type of the name selected
     */
    private final Class<T> resourceType;

    /**
     * Creates a new server provisioning command.
     * @param resourceType the type of the name selected
     */
    public AbstractResourceSelectedCommand(Class<T> resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public Metadata getMetadata(UIContext context) {
        return Metadata
                .from(super.getMetadata(context), getClass())
                .category(Categories.create("WildFly Build Tools"));
    }

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && (getResourceSelected(context) != null);
    }

    /**
     * Retrieves the name selected.
     * @param uiContext
     * @return
     */
    public T getResourceSelected(UIContext uiContext) {
        final Object object = uiContext.getInitialSelection().get();
        if (resourceType.isInstance(object)) {
            return resourceType.cast(object);
        } else {
            return null;
        }
    }
}
