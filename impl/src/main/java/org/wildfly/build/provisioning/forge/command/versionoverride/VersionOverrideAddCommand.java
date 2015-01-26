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

import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.wildfly.build.provisioning.forge.command.AbstractServerProvisioningDescriptionResourceSelectedCommand;
import org.wildfly.build.pack.model.Artifact;

import javax.inject.Inject;

/**
 * A command to add a version override to the server provisioning config.
 * @author Eduardo Martins
 */
public class VersionOverrideAddCommand extends AbstractServerProvisioningDescriptionResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Artifact's groupId", required=true)
    private UIInput<String> groupId;

    @Inject
    @WithAttributes(label="Artifact's artifactId", required=true)
    private UIInput<String> artifactId;

    @Inject
    @WithAttributes(label="Artifact's version", required=true)
    private UIInput<String> version;

    @Inject
    @WithAttributes(label="Artifact's classifier", required=false)
    private UIInput<String> classifier;

    @Inject
    @WithAttributes(label="Artifact's extension", required=false)
    private UIInput<String> extension;

    @Override
    public Metadata getMetadata(UIContext context) {
        return super.getMetadata(context)
                .name("version-override-add")
                .description("Add an artifact version override to WildFly Server Provisioning");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        builder.add(groupId).add(artifactId).add(classifier).add(extension).add(version);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        return getResourceSelected(context.getUIContext()).addVersionOverride(new Artifact(groupId.getValue(), artifactId.getValue(), classifier.getValue(), extension.getValue(), version.getValue())) != null ? Results.success() : Results.fail();
    }
}
