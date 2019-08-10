/**
 * Gherkin Extension Jupiter
 * Copyright © 2022 Gmasil
 *
 * This file is part of Gherkin Extension Jupiter.
 *
 * Gherkin Extension Jupiter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gherkin Extension Jupiter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Gherkin Extension Jupiter.  If not, see <https://www.gnu.org/licenses/>.
 */
package de.gmasil.gherkin.extension;

import java.io.File;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import de.gmasil.gherkin.extension.store.ScenarioStore;
import de.gmasil.gherkin.extension.store.StoryStore;

public class GherkinExtension
        implements BeforeEachCallback, AfterEachCallback, BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private File outputFolder = new File("target/gherkin/");

    // Callbacks

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        createStory(context);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        StoryStore storyStore = getRequiredStoryStore(context);
        outputFolder.mkdirs();
        ObjectMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(outputFolder, storyStore.getClassName() + ".xml"), storyStore);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        createRunner(context);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        GherkinRunner runner = getRunner(context);
        if (!checkRunner(context, runner)) {
            throw new IllegalStateException(String.format(
                    "The Gherkin Runner has changed during execution of class %s:%s",
                    context.getRequiredTestInstance().getClass().getName(), context.getRequiredTestMethod().getName()));
        }
        addScenario(context, runner);
        if (runner.isFailed()) {
            Throwable exception = runner.getFailedStep().getException();
            if (exception != null) {
                if (exception instanceof Exception) {
                    throw (Exception) exception;
                }
                throw new ThrowableWrapperException(exception);
            } else {
                throw new IllegalStateException(
                        String.format("The step '%s' failed, but there was no exception available",
                                runner.getFailedStep().getName()));
            }
        }
    }

    // ParameterResolver

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> expectedType = parameterContext.getParameter().getType();
        return expectedType.equals(Reference.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return new Reference<>();
    }

    // other

    private boolean checkRunner(ExtensionContext context, GherkinRunner runner) {
        if (runner == null) {
            return false;
        }
        return runner.isFrom(context.getRequiredTestInstance().getClass().getName(),
                context.getRequiredTestMethod().getName());
    }

    private void createRunner(ExtensionContext context) {
        Object instance = context.getRequiredTestInstance();
        if (instance instanceof GherkinRunnerHolder) {
            Scenario scenario = context.getRequiredTestMethod().getAnnotation(Scenario.class);
            String scenarioName = null;
            if (scenario != null) {
                scenarioName = scenario.value();
            }
            GherkinRunnerHolder.class.cast(instance)
                    .setRunner(new GherkinRunner(context.getRequiredTestInstance().getClass().getName(),
                            context.getRequiredTestMethod().getName(), scenarioName));
        }
    }

    private GherkinRunner getRunner(ExtensionContext context) {
        Object instance = context.getRequiredTestInstance();
        if (instance instanceof GherkinRunnerHolder) {
            return GherkinRunnerHolder.class.cast(instance).getRunner();
        }
        return null;
    }

    private Store getRequiredStore(ExtensionContext context) {
        Store store = getStore(context);
        if (store == null) {
            throw new IllegalStateException("Extension Context Store is null");
        }
        return store;
    }

    private Store getStore(ExtensionContext context) {
        return context.getRoot().getStore(Namespace.create(GherkinExtension.class));
    }

    private StoryStore getRequiredStoryStore(ExtensionContext context) {
        StoryStore storyStore = getStoryStore(context);
        if (storyStore == null) {
            throw new IllegalStateException("Extension Context Story Store is null");
        }
        return storyStore;
    }

    private StoryStore getStoryStore(ExtensionContext context) {
        Store store = getStore(context);
        if (store != null) {
            Class<?> requiredTestClass = context.getRequiredTestClass();
            return store.get(requiredTestClass.getName(), StoryStore.class);
        }
        return null;
    }

    public void createStory(ExtensionContext context) {
        Store store = getRequiredStore(context);
        Class<?> requiredTestClass = context.getRequiredTestClass();
        Story story = requiredTestClass.getAnnotation(Story.class);
        String storyName;
        if (story != null) {
            storyName = story.value();
        } else {
            storyName = requiredTestClass.getName();
        }
        store.put(requiredTestClass.getName(), new StoryStore(storyName, requiredTestClass.getName()));
    }

    public synchronized void addScenario(ExtensionContext context, GherkinRunner runner) {
        StoryStore storyStore = getRequiredStoryStore(context);
        ScenarioStore scenarioStore = new ScenarioStore(runner.getScenarioName(), runner.getMethodName(),
                runner.isFailed());
        scenarioStore.getSteps().addAll(runner.getSteps());
        storyStore.getScenarios().add(scenarioStore);
    }
}
