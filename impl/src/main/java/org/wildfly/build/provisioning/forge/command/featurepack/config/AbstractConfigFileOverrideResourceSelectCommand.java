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

import javax.inject.Inject;
import java.util.List;

/**
 * Abstract command that is only enabled if the selected name is a {@link org.wildfly.build.provisioning.forge.resource.ConfigOverrideResource} which contains {@link org.wildfly.build.provisioning.forge.resource.ConfigFileOverrideResource} children.
 * @author Eduardo Martins
 */
public abstract class AbstractConfigFileOverrideResourceSelectCommand extends AbstractConfigOverrideResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Config file", required=true)
    UISelectOne<ConfigFileOverrideResource> outputFile;

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && !getResourceSelected(context).getConfigFileOverrides().isEmpty();
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        final List<? extends ConfigFileOverrideResource> configFileOverrides = getResourceSelected(builder.getUIContext()).getConfigFileOverrides();
        outputFile.setValueChoices((Iterable) configFileOverrides);
        outputFile.setItemLabelConverter(new Converter<ConfigFileOverrideResource, String>() {
            @Override
            public String convert(ConfigFileOverrideResource source) {
                return source.getUnderlyingResourceObject().getOutputFile();
            }
        });
        outputFile.setValueConverter(new Converter<String, ConfigFileOverrideResource>() {
            @Override
            public ConfigFileOverrideResource convert(String source) {
                for (ConfigFileOverrideResource configFileOverride : configFileOverrides) {
                    if (configFileOverride.getUnderlyingResourceObject().getOutputFile().equals(source)) {
                        return configFileOverride;
                    }
                }
                return null;
            }
        });
        builder.add(outputFile);
    }
}
