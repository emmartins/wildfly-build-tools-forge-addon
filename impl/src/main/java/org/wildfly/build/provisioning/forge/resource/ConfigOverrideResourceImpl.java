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
import org.wildfly.build.common.model.ConfigFile;
import org.wildfly.build.common.model.ConfigFileOverride;
import org.wildfly.build.common.model.ConfigOverride;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The impl of {@link org.wildfly.build.provisioning.forge.resource.ModuleFiltersResource}.
 * @author Eduardo Martins
 */
public class ConfigOverrideResourceImpl extends FeaturePackResourceImpl.Child<ConfigOverride> implements ConfigOverrideResource {

    public ConfigOverrideResourceImpl(FeaturePackResourceImpl parent) {
        super("config-override", parent, parent.getUnderlyingResourceObject().getConfigOverride());
    }

    @Override
    public StandaloneConfigFileOverrideResource addStandaloneConfigFileOverride(ConfigFileOverride configFileOverride) {
        getUnderlyingResourceObject().getStandaloneConfigFiles().put(configFileOverride.getOutputFile(), configFileOverride);
        writeXML();
        return new StandaloneConfigFileOverrideResourceImpl(this, configFileOverride);
    }

    @Override
    public DomainConfigFileOverrideResource addDomainConfigFileOverride(ConfigFileOverride configFileOverride) {
        getUnderlyingResourceObject().getDomainConfigFiles().put(configFileOverride.getOutputFile(), configFileOverride);
        writeXML();
        return new DomainConfigFileOverrideResourceImpl(this, configFileOverride);
    }

    @Override
    public List<? extends StandaloneConfigFileOverrideResource> getStandaloneConfigFileOverrides() {
        List<StandaloneConfigFileOverrideResource> resources = new ArrayList<>();
        for (ConfigFileOverride configFileOverride : getUnderlyingResourceObject().getStandaloneConfigFiles().values()) {
            resources.add(new StandaloneConfigFileOverrideResourceImpl(this, configFileOverride));
        }
        return resources;
    }

    @Override
    public List<? extends DomainConfigFileOverrideResource> getDomainConfigFileOverrides() {
        List<DomainConfigFileOverrideResource> resources = new ArrayList<>();
        for (ConfigFileOverride configFileOverride : getUnderlyingResourceObject().getDomainConfigFiles().values()) {
            resources.add(new DomainConfigFileOverrideResourceImpl(this, configFileOverride));
        }
        return resources;
    }

    @Override
    public List<? extends ConfigFileOverrideResource> getConfigFileOverrides() {
        List<ConfigFileOverrideResource> resources = new ArrayList<>();
        resources.addAll(getStandaloneConfigFileOverrides());
        resources.addAll(getDomainConfigFileOverrides());
        return resources;
    }

    @Override
    public List<String> getStandaloneConfigFilesNotOverridden() {
        final Set<String> alreadyOverridden = getUnderlyingResourceObject().getStandaloneConfigFiles().keySet();
        final List<String> notOverridden = new ArrayList<>();
        for (ConfigFile configFile : getParent().getFeaturePack().getDescription().getConfig().getStandaloneConfigFiles()) {
            if (!alreadyOverridden.contains(configFile.getOutputFile())) {
                notOverridden.add(configFile.getOutputFile());
            }
        }
        return notOverridden;
    }

    @Override
    public List<String> getDomainConfigFilesNotOverridden() {
        final Set<String> alreadyOverridden = getUnderlyingResourceObject().getDomainConfigFiles().keySet();
        final List<String> notOverridden = new ArrayList<>();
        for (ConfigFile configFile : getParent().getFeaturePack().getDescription().getConfig().getDomainConfigFiles()) {
            if (!alreadyOverridden.contains(configFile.getOutputFile())) {
                notOverridden.add(configFile.getOutputFile());
            }
        }
        return notOverridden;
    }

    @Override
    public boolean delete() throws UnsupportedOperationException {
        getParent().getUnderlyingResourceObject().setConfigOverride(null);
        writeXML();
        return true;
    }

    @Override
    public boolean delete(boolean recursive) throws UnsupportedOperationException {
        return delete();
    }

    @Override
    protected List doListResources() {
        return getConfigFileOverrides();
    }

    @Override
    public Resource<?> getChild(String name) {
        for (ConfigFileOverrideResource configFileOverride : getConfigFileOverrides()) {
            if (configFileOverride.getName().equals(name)) {
                return configFileOverride;
            }
        }
        return null;
    }
}