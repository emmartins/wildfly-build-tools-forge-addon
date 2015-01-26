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
package org.wildfly.build.provisioning.forge.command.featurepack;

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.wildfly.build.provisioning.forge.command.AbstractServerProvisioningDescriptionResourceSelectedCommand;
import org.wildfly.build.provisioning.forge.resource.FeaturePackResource;

import javax.inject.Inject;
import java.util.List;

/**
 * An abstract command that selects a ${@link org.wildfly.build.provisioning.forge.resource.FeaturePackResource}.
 * @author Eduardo Martins
 */
public abstract class AbstractFeaturePackResourceSelectCommand extends AbstractServerProvisioningDescriptionResourceSelectedCommand {

    @Inject
    @WithAttributes(label="Feature Pack", required=true)
    UISelectOne<FeaturePackResource> artifact;

    @Override
    public boolean isEnabled(UIContext context) {
        return super.isEnabled(context) && !getFeaturePacks(context).isEmpty();
    }

    List<? extends FeaturePackResource> getFeaturePacks(UIContext context) {
        return getResourceSelected(context).getFeaturePacks();
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        final List<? extends FeaturePackResource> featurePacks = getFeaturePacks(builder.getUIContext());
        artifact.setValueChoices((Iterable)featurePacks);
        artifact.setItemLabelConverter(new Converter<FeaturePackResource, String>() {
            @Override
            public String convert(FeaturePackResource source) {
                return source.getUnderlyingResourceObject().getArtifact().toString();
            }
        });
        artifact.setValueConverter(new Converter<String, FeaturePackResource>() {
            @Override
            public FeaturePackResource convert(String source) {
                for (FeaturePackResource featurePack : featurePacks) {
                    if (featurePack.getUnderlyingResourceObject().getArtifact().toString().equals(source)) {
                        return featurePack;
                    }
                }
                return null;
            }
        });
        builder.add(artifact);
    }
}
