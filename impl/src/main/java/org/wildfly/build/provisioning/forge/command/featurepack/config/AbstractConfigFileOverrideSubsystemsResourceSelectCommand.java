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
package org.wildfly.build.provisioning.forge.command.featurepack.config;

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideResource;
import org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideSubsystemsResource;
import org.wildfly.build.provisioning.forge.resource.DomainConfigFileOverrideResource;
import org.wildfly.build.provisioning.forge.resource.StandaloneConfigFileOverrideResource;

import javax.inject.Inject;
import java.util.List;

/**
 * Abstract command that is only enabled if the selected name is a {@link org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideResource} which contains the {@link org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideSubsystemsResource} child.
 * @author Eduardo Martins
 */
public abstract class AbstractConfigFileOverrideSubsystemsResourceSelectCommand extends AbstractConfigFileOverrideResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Subsystems Profile", required=true)
    protected UISelectOne<ConfigFileOverrideSubsystemsResource> name;

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        ConfigFileOverrideResource resourceSelected = getResourceSelected(builder.getUIContext());
        if (resourceSelected.getType() == ConfigFileOverrideResource.Type.domain) {
            final List<ConfigFileOverrideSubsystemsResource> subsystemsProfiles = ((DomainConfigFileOverrideResource)resourceSelected).getSubsystemsProfiles();
            name.setValueChoices(subsystemsProfiles);
            name.setItemLabelConverter(new Converter<ConfigFileOverrideSubsystemsResource, String>() {
                @Override
                public String convert(ConfigFileOverrideSubsystemsResource source) {
                    return source.getProfileName();
                }
            });
            name.setValueConverter(new Converter<String, ConfigFileOverrideSubsystemsResource>() {
                @Override
                public ConfigFileOverrideSubsystemsResource convert(String source) {
                    for (ConfigFileOverrideSubsystemsResource subsystemsResource : subsystemsProfiles) {
                        if (subsystemsResource.getProfileName().equals(source)) {
                            return subsystemsResource;
                        }
                    }
                    return null;
                }
            });
            builder.add(name);
        }
    }

    @Override
    public boolean isEnabled(UIContext context) {
        if (super.isEnabled(context)) {
            ConfigFileOverrideResource resourceSelected = getResourceSelected(context);
            switch (resourceSelected.getType()) {
                case standalone:
                    return ((StandaloneConfigFileOverrideResource)resourceSelected).getSubsystems() != null;
                case domain:
                    return !((DomainConfigFileOverrideResource)resourceSelected).getSubsystemsProfiles().isEmpty();
            }
        }
        return false;
    }
}
