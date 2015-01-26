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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The impl of {@link ConfigFileOverrideSubsystemsResource}.
 * @author Eduardo Martins
 */
public class ConfigFileOverrideSubsystemsResourceImpl extends VirtualResource<Map<String, SubsystemConfig>> implements ConfigFileOverrideSubsystemsResource {

    private final String profileName;

    public ConfigFileOverrideSubsystemsResourceImpl(String profileName, AbstractConfigFileOverrideResource parent) {
        super(parent.getResourceFactory(), parent);
        this.profileName = profileName;
    }

    @Override
    public String getProfileName() {
        return profileName;
    }

    @Override
    public void addSubsystemConfig(SubsystemConfig subsystemConfig) {
        getUnderlyingResourceObject().put(subsystemConfig.getSubsystem(), subsystemConfig);
        getParent().writeXML();
    }

    @Override
    public boolean removeSubsystemConfig(SubsystemConfig subsystemConfig) {
        if (getUnderlyingResourceObject().remove(subsystemConfig.getSubsystem()) != null) {
            getParent().writeXML();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<SubsystemConfig> getSubsystemConfigs() {
        return new ArrayList<>(getUnderlyingResourceObject().values());
    }

    @Override
    public Set<String> getSubsystemsNotAdded() {
        try {
            final Set<String> subsystemsNotAdded = new HashSet<>(getParent().getParent().getParent().getFeaturePack().getSubsystems());
            subsystemsNotAdded.removeAll(getUnderlyingResourceObject().keySet());
            return subsystemsNotAdded;
        } catch (Exception e) {
            throw new IllegalStateException("unable to retrieve subsystems not added", e);
        }
    }

    @Override
    public AbstractConfigFileOverrideResource getParent() {
        return (AbstractConfigFileOverrideResource) super.getParent();
    }

    @Override
    public Map<String, SubsystemConfig> getUnderlyingResourceObject() {
        final ConfigFileOverride configFileOverride = getParent().getUnderlyingResourceObject();
        final Map<String, SubsystemConfig> subsystemConfigs = configFileOverride.getSubsystems().get(profileName);
        if (subsystemConfigs == null) {
            throw new IllegalStateException("subsystem configs named "+ profileName +" not found in config file override "+configFileOverride.getOutputFile());
        }
        return subsystemConfigs;
    }

    @Override
    protected List<Resource<?>> doListResources() {
        List<Resource<?>> resources = new ArrayList<>();
        for (SubsystemConfig subsystemConfig : getSubsystemConfigs()) {
            resources.add(new NamedVirtualResource(this, subsystemConfig));
        }
        return resources;
    }

    @Override
    public boolean delete() throws UnsupportedOperationException {
        return getParent().removeSubsystemsProfile(profileName);
    }

    @Override
    public boolean delete(boolean recursive) throws UnsupportedOperationException {
        return delete();
    }

    @Override
    public String getName() {
        return profileName.equals("") ? "config-file-override-subsystems" : ("config-file-override-subsystems["+ profileName +"]");
    }
}