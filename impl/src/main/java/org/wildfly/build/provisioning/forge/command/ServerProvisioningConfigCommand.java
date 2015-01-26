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

import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.provisioning.forge.resource.ServerProvisioningDescriptionResource;

import javax.inject.Inject;
import java.io.File;

/**
 * The command to config a WildFly server provisioning.
 * @author Eduardo Martins
 */
public class ServerProvisioningConfigCommand extends AbstractResourceSelectedCommand<DirectoryResource> {

    public ServerProvisioningConfigCommand() {
        super(DirectoryResource.class);
    }

    @Inject
    private ResourceFactory resourceFactory;

    @Inject
    @WithAttributes(label="Server Provisioning Description XML File", required=true, defaultValue = "server-provisioning.xml")
    private UIInput<String> fileName;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("server-provisioning-config")
                .description("WildFly Server Provisioning Configuration");
    }
    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        builder.add(fileName);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        final File currentDir = getResourceSelected(context.getUIContext()).getUnderlyingResourceObject();
        final ServerProvisioningDescriptionResource resource = resourceFactory.create(ServerProvisioningDescriptionResource.class, new File(currentDir, fileName.getValue()));
        context.getUIContext().setSelection(resource);
        return Results.success("Server provisioning description " +resource.getUnderlyingResourceObject()+(resource.exists() ? " loaded." : " created."));
    }
}
