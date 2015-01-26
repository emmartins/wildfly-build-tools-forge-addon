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
package org.wildfly.build.provisioning.forge.resource;

import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.VirtualResource;

import java.util.List;

/**
 * A {@link org.jboss.forge.addon.resource.VirtualResource} with a name.
 * @author Eduardo Martins
 */
public class NamedVirtualResource extends VirtualResource<String> {

    private final String name;

    public NamedVirtualResource(Resource<?> parent, String name) {
        super(parent.getResourceFactory(), parent);
        this.name = name;
    }

    public NamedVirtualResource(Resource<?> parent, Object object) {
        this(parent, String.valueOf(object));
    }

    @Override
    protected List<Resource<?>> doListResources() {
        return null;
    }

    @Override
    public boolean delete() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(boolean recursive) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUnderlyingResourceObject() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
