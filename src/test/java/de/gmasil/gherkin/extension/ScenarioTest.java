/**
 * Gherkin Extension Jupiter
 * Copyright © 2019 Gmasil
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

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
@Story("A test method only annotated with the @Scenario annotation will be executed")
public class ScenarioTest extends GherkinAdapter {
	private static boolean scenarioWasExecuted = false;

	@Order(1)
	@Scenario("A @Scenario annotated method does not require the @Test annotation")
	public void testScenarioActsLikeTestAnnotation() {
		when("a scenario is started", () -> {
			scenarioWasExecuted = true;
		});
		then("the scenarioWasStarted flag is set to true", () -> {
			assertThat(scenarioWasExecuted, is(equalTo(true)));
		});
	}

	@Test
	@Order(2)
	public void testIfScenarioWasActuallyExecuted() {
		assertThat(scenarioWasExecuted, is(equalTo(true)));
	}
}
