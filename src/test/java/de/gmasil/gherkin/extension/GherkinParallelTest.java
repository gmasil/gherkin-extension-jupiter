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

import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
@Story("The Gherkin Extension does not get confused by parallel test execution")
public class GherkinParallelTest extends GherkinAdapter {
	// @formatter:off
	@Scenario("Scenario A")public void testA(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	@Scenario("Scenario B")public void testB(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	@Scenario("Scenario C")public void testC(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	@Scenario("Scenario D")public void testD(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	@Scenario("Scenario E")public void testE(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	@Scenario("Scenario F")public void testF(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	@Scenario("Scenario G")public void testG(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	@Scenario("Scenario H")public void testH(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	@Scenario("Scenario I")public void testI(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	@Scenario("Scenario J")public void testJ(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	@Scenario("Scenario K")public void testK(){given("something is there",()->{Thread.sleep(100);});then("everything is alright",()->{Thread.sleep(100);});}
	// @formatter:on
}
