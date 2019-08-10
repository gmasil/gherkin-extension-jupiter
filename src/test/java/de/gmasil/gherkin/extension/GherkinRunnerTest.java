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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class GherkinRunnerTest {

    @Test
    public void testRunnerWithFailingStep() {
        GherkinRunner runner = new GherkinRunner("My.class", "My.method", "My scenario");
        runner.executeStep("a step fails", () -> {
            assertThat("Peter", is(equalTo("Panda")));
        }, StepType.GIVEN);
    }

    @Test
    public void testRunnerDoesNotExecuteStepAfterFailing() {
        GherkinRunner runner = new GherkinRunner("My.class", "My.method", "My scenario");
        // execute GIVEN which fails
        Reference<Boolean> givenExecuted = new Reference<>();
        givenExecuted.set(false);
        runner.executeStep("a step fails", () -> {
            givenExecuted.set(true);
            assertThat("Peter", is(equalTo("Panda")));
        }, StepType.GIVEN);
        // check if really executed
        assertThat(givenExecuted.get(), is(equalTo(true)));
        assertThat(runner.isFailed(), is(equalTo(true)));
        // execute another step
        Reference<Boolean> thenExecuted = new Reference<>();
        thenExecuted.set(false);
        runner.executeStep("this step is not executed", () -> {
            thenExecuted.set(true);
        }, StepType.THEN);
        // check that step was not executed
        assertThat(thenExecuted.get(), is(equalTo(false)));
        assertThat(runner.getFailedStep().getName(), is(equalTo("a step fails")));
    }

    @Test
    public void testIsFromRecognition() {
        GherkinRunner runner = new GherkinRunner("class", "method", "");
        assertThat(runner.isFrom("class", "---"), is(equalTo(false)));
        assertThat(runner.isFrom("---", "method"), is(equalTo(false)));
        assertThat(runner.isFrom("class", "method"), is(equalTo(true)));
        assertThat(runner.isFrom("---", "---"), is(equalTo(false)));
    }
}
