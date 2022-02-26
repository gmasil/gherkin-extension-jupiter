/**
 * Gherkin Extension Jupiter
 * Copyright © 2019 - 2022 Gmasil
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import de.gmasil.gherkin.extension.store.StepStore;
import de.gmasil.gherkin.extension.store.StoryStore;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GherkinExtensionTest {

    @InjectMocks
    private GherkinExtension extension = new GherkinExtension();

    @Mock
    private ExtensionContext context;

    @Mock
    private GherkinRunner runner;

    @Mock
    private GherkinRunnerHolder holder;

    @Mock
    private Store store;

    @Mock
    private StoryStore storyStore;

    @BeforeEach
    public void initMocks() throws Exception {
        // prepare runner
        when(runner.isFrom(any(), any())).thenReturn(true);
        when(runner.isFailed()).thenReturn(true);
        when(holder.getRunner()).thenReturn(runner);
        // prepare store
        when(store.get(any(), eq(StoryStore.class))).thenReturn(storyStore);
        // prepare context
        when(context.getRequiredTestInstance()).thenReturn(holder);
        when(context.getRequiredTestClass()).then(invocation -> GherkinExtensionTest.class);
        when(context.getRequiredTestMethod())
                .thenReturn(GherkinExtensionTest.class.getDeclaredMethod("testMockMethod"));
        when(context.getRoot()).thenReturn(context);
        when(context.getStore(any())).thenReturn(store);
    }

    @Test
    void testAfterEachWithAssertionError() throws Exception {
        // prepare step
        StepStore step = new StepStore("someStapName", StepType.GIVEN, ExecutionStatus.FAILED,
                new AssertionError("A != B"));
        when(runner.getFailedStep()).thenReturn(step);
        // execute method
        assertThrows(RuntimeException.class, () -> {
            extension.afterEach(context);
        });
    }

    @Test
    void testAfterEachWithArrayIndexOutOfBoundsException() throws Exception {
        // prepare step
        StepStore step = new StepStore("someStapName", StepType.GIVEN, ExecutionStatus.FAILED,
                new ArrayIndexOutOfBoundsException("Index: 3"));
        when(runner.getFailedStep()).thenReturn(step);
        // execute method
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            extension.afterEach(context);
        });
    }

    @Test
    void testAfterEachWithoutExceptionAvailable() throws Exception {
        // prepare step
        StepStore step = new StepStore("someStapName", StepType.GIVEN, ExecutionStatus.FAILED, null);
        when(runner.getFailedStep()).thenReturn(step);
        // execute method
        try {
            extension.afterEach(context);
            fail("extension.afterEach(context) should have thrown an exception");
        } catch (Exception e) {
            assertThat(e, is(instanceOf(IllegalStateException.class)));
            assertThat(e.getMessage(), is(equalTo(
                    String.format("The step '%s' failed, but there was no exception available", "someStapName"))));
        }
    }

    @Test
    void testIfCheckRunnerReturnsFalseOnNull() {
        when(holder.getRunner()).thenReturn(null);
        try {
            extension.afterEach(context);
            fail("extension.afterEach(context) should have thrown an exception");
        } catch (Exception e) {
            assertThat(e, is(instanceOf(IllegalStateException.class)));
            assertThat(e.getMessage(), startsWith("The Gherkin Runner has changed during execution of class"));
        }
    }

    public void testMockMethod() {

    }
}
