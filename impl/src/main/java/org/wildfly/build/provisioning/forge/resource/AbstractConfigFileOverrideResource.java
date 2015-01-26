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
import org.wildfly.build.common.model.ConfigFileOverride;
import org.wildfly.build.configassembly.SubsystemConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The abstract impl of {@link org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideResource}.
 * @author Eduardo Martins
 */
public abstract class AbstractConfigFileOverrideResource extends VirtualResource<ConfigFileOverride> implements ConfigFileOverrideResource {

    private final ConfigFileOverride configFileOverride;

    AbstractConfigFileOverrideResource(ConfigOverrideResourceImpl parent, ConfigFileOverride configFileOverride) {
        super(parent.getResourceFactory(), parent);
        this.configFileOverride = configFileOverride;
    }

    abstract Map<String, ConfigFileOverride> getConfigFiles();

    @Override
    public boolean delete() throws UnsupportedOperationException {
        if (getConfigFiles().remove(getUnderlyingResourceObject().getOutputFile()) != null) {
            writeXML();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(boolean recursive) throws UnsupportedOperationException {
        return delete();
    }

    public ConfigFileOverrideSubsystemsResource addSubsystemsProfile(String name) {
        Map<String, Map<String, SubsystemConfig>> subsystems = configFileOverride.getSubsystems();
        if (subsystems == null) {
            subsystems = new HashMap<>();
            configFileOverride.setSubsystems(subsystems);
        }
        if (!subsystems.containsKey(name)) {
            subsystems.put(name, new HashMap<String, SubsystemConfig>());
            writeXML();
        }
        return new ConfigFileOverrideSubsystemsResourceImpl(name, this);
    }

    public ConfigFileOverrideSubsystemsResource getSubsystemsProfile(String name) {
        final Map<String, Map<String, SubsystemConfig>> subsystems = configFileOverride.getSubsystems();
        return (subsystems != null && subsystems.containsKey(name)) ? new ConfigFileOverrideSubsystemsResourceImpl(name, this) : null;
    }

    public List<ConfigFileOverrideSubsystemsResource> getSubsystemsProfiles() {
        final Map<String, Map<String, SubsystemConfig>> subsystems = configFileOverride.getSubsystems();
        if (subsystems == null) {
            return Collections.emptyList();
        }
        final List<ConfigFileOverrideSubsystemsResource> resources = new ArrayList<>();
        for (String name : subsystems.keySet()) {
            resources.add(new ConfigFileOverrideSubsystemsResourceImpl(name, this));
        }
        return resources;
    }

    public boolean removeSubsystemsProfile(String name) {
        Map<String, Map<String, SubsystemConfig>> subsystems = configFileOverride.getSubsystems();
        if (subsystems != null && subsystems.remove(name) != null) {
            if (subsystems.isEmpty()) {
                configFileOverride.setSubsystems(null);
            }
            writeXML();
            return true;
        } else {
            return false;
        }
    }

    protected void writeXML() {
        getParent().writeXML();
    }

    @Override
    public String getName() {
        return "config-file-override["+configFileOverride.getOutputFile()+"]";
    }

    @Override
    public ConfigOverrideResourceImpl getParent() {
        return (ConfigOverrideResourceImpl) super.getParent();
    }

    @Override
    public ConfigFileOverride getUnderlyingResourceObject() {
        return configFileOverride;
    }

    @Override
    protected List doListResources() {
        return getSubsystemsProfiles();
    }

    @Override
    public Resource<?> getChild(String name) {
        return getSubsystemsProfile(name);
    }

    @Override
    public String toString() {
        return getName();
    }
}