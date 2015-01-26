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
package org.wildfly.build.provisioning.forge.command.versionoverride;

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.provisioning.forge.command.AbstractServerProvisioningDescriptionResourceSelectedCommand;
import org.wildfly.build.provisioning.forge.resource.VersionOverrideResource;

import javax.inject.Inject;
import java.util.List;

/**
 * A command to remove a version override from the server provisioning config.
 * @author Eduardo Martins
 */
public class VersionOverrideRemoveCommand extends AbstractServerProvisioningDescriptionResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Artifact", required=true)
    private UISelectOne<VersionOverrideResource> artifact;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("version-override-remove")
                .description("Remove an artifact version override from WildFly Server Provisioning");
    }

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && !getVersionOverrides(context).isEmpty();
    }

    private List<? extends VersionOverrideResource> getVersionOverrides(UIContext context) {
        return getResourceSelected(context).getVersionOverrides();
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        final List<? extends VersionOverrideResource> valueChoices = getVersionOverrides(builder.getUIContext());
        artifact.setValueChoices((Iterable)valueChoices);
        artifact.setItemLabelConverter(new Converter<VersionOverrideResource, String>() {
            @Override
            public String convert(VersionOverrideResource source) {
                return source.getUnderlyingResourceObject().toString();
            }
        });
        artifact.setValueConverter(new Converter<String, VersionOverrideResource>() {
            @Override
            public VersionOverrideResource convert(String source) {
                for (VersionOverrideResource resource : valueChoices) {
                    if (resource.getUnderlyingResourceObject().toString().equals(source)) {
                        return resource;
                    }
                }
                return null;
            }
        });
        builder.add(artifact);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        return artifact.getValue().delete() ? Results.success() : Results.fail();
    }
}
