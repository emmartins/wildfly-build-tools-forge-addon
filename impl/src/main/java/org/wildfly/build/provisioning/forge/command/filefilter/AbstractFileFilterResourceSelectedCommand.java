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

import org.wildfly.build.provisioning.forge.command.AbstractResourceSelectedCommand;
import org.wildfly.build.provisioning.forge.resource.FileFiltersResource;

/**
 * An abstract command that is enabled if the initial selection is an instance of {@link org.wildfly.build.provisioning.forge.resource.FileFiltersResource}.
 *
 * @author Eduardo Martins
 */
public abstract class AbstractFileFilterResourceSelectedCommand extends AbstractResourceSelectedCommand<FileFiltersResource> {

    /**
     *
     */
    public AbstractFileFilterResourceSelectedCommand() {
        super(FileFiltersResource.class);
    }
}
