/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.gradle.dependencymanagement.internal.dsl;

import java.util.Map;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.artifacts.Configuration;

import io.spring.gradle.dependencymanagement.dsl.DependenciesHandler;
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementConfigurer;
import io.spring.gradle.dependencymanagement.dsl.ImportsHandler;
import io.spring.gradle.dependencymanagement.internal.DependencyManagementContainer;

/**
 * Standard implementation of {@link DependencyManagementConfigurer}.
 *
 * @author Andy Wilkinson
 */
class StandardDependencyManagementConfigurer implements DependencyManagementConfigurer {

    private final DependencyManagementContainer container;

    private final Configuration configuration;

    StandardDependencyManagementConfigurer(DependencyManagementContainer container) {
        this(container, null);
    }

    StandardDependencyManagementConfigurer(DependencyManagementContainer container, Configuration configuration) {
        this.container = container;
        this.configuration = configuration;
    }

    @Override
    public void imports(Closure closure) {
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(new StandardImportsHandler(this.container, this.configuration));
        closure.call();
    }

    @Override
    public void imports(Action<ImportsHandler> action) {
        action.execute(new StandardImportsHandler(this.container, this.configuration));
    }

    @Override
    public void dependencies(Closure closure) {
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(new StandardDependenciesHandler(this.container, this.configuration));
        closure.call();
    }

    @Override
    public void dependencies(Action<DependenciesHandler> action) {
        action.execute(new StandardDependenciesHandler(this.container, this.configuration));
    }

    @Override
    public Map<String, String> getImportedProperties() {
        return this.container.importedPropertiesForConfiguration(this.configuration);
    }

    @Override
    public Map<String, String> getManagedVersions() {
        return managedVersions(true);
    }

    @Override
    public Map<String, String> getOwnManagedVersions() {
        return managedVersions(false);
    }

    private Map<String, String> managedVersions(boolean inherited) {
        return this.container.getManagedVersionsForConfiguration(this.configuration, inherited);
    }

}
