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

import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;

import javax.inject.Inject;

/**
 * A command to customize the server provisioning config attributes.
 * @author Eduardo Martins
 */
public class ServerProvisioningConfigAttributesCommand extends AbstractServerProvisioningDescriptionResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Copy module artifacts", required=true)
    private UIInput<Boolean> copyModuleArtifacts;

    @Inject
    @WithAttributes(label="Extract and provision XML schemas found in module artifacts", required=true)
    private UIInput<Boolean> extractSchemas;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("server-provisioning-config-attributes")
                .description("WildFly Server Provisioning Configuration Attributes");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        builder.add(copyModuleArtifacts).add(extractSchemas);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        getResourceSelected(context.getUIContext()).setAttributes(copyModuleArtifacts.getValue(), extractSchemas.getValue());
        return Results.success();
    }
}
