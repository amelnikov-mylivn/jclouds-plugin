/*
 * Copyright 2016 Fritz Elfert
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jenkins.plugins.jclouds.config;

import hudson.Extension;
import org.jenkinsci.lib.configprovider.model.Config;
import org.jenkinsci.lib.configprovider.model.ContentType;
import org.kohsuke.stapler.DataBoundConstructor;

import jenkins.plugins.jclouds.compute.UserData;

public class UserDataIncludeOnce extends Config {

    @DataBoundConstructor
    public UserDataIncludeOnce(final String id, final String name, final String comment, final String content) {
        super(id, name, comment, content);
    }

    @Extension(ordinal = 70)
    @ConfigSuitableFor(target=UserData.class)
    public static class UserDataIncludeOnceProvider extends AbstractJCloudsConfigProviderImpl {

        private static final String SIGNATURE = "^#include-once[\\r\\n]+";
        private static final String DEFAULT_CONTENT = "#include-once\n";
        private static final String DEFAULT_NAME = "jclouds.include-once";

        public UserDataIncludeOnceProvider() {
            load();
        }

        public String getSignature() {
            return SIGNATURE;
        }

        @Override
        public ContentType getContentType() {
            return null;
        }

        @Override
        public ContentType getRealContentType() {
            return CloudInitContentType.INCLUDEONCE;
        }

        @Override
        public String getDisplayName() {
            return "JClouds user data (include once)";
        }

        @Override
        public UserDataIncludeOnce getConfigById(final String configId) {
            final Config c = super.getConfigById(configId);
            return new UserDataIncludeOnce(c.id, c.name, c.comment, c.content);
        }

        @Override
        public Config newConfig() {
            String id = getProviderId() + "." + System.currentTimeMillis();
            return new Config(id, DEFAULT_NAME, "", DEFAULT_CONTENT);
        }

        @Override
        public Config newConfig(final String id) {
            return new Config(id, DEFAULT_NAME, "", DEFAULT_CONTENT, getProviderId());
        }
    }
}
