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

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GherkinExtension.class)
public interface Gherkin extends GherkinRunnerHolder {

    default void given(String name, GherkinRunnable runnable) {
        step(name, runnable, StepType.GIVEN);
    }

    default void when(String name, GherkinRunnable runnable) {
        step(name, runnable, StepType.WHEN);
    }

    default void then(String name, GherkinRunnable runnable) {
        step(name, runnable, StepType.THEN);
    }

    default void and(String name, GherkinRunnable runnable) {
        step(name, runnable, StepType.AND);
    }

    default void step(String name, GherkinRunnable runnable, StepType type) {
        getRunner().executeStep(name, runnable, type);
    }
}
