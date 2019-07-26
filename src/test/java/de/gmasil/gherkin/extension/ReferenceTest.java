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

public class ReferenceTest extends GherkinAdapter {
	@Scenario("A reference can be requested")
	public void testLoc(Reference<String> name) {
		given("the name 'Peter' is saved in the reference", () -> {
			name.set("Peter");
		});
		when("the name is changed to 'Ben'", () -> {
			name.set("Ben");
		});
		then("the name is reached over to the next step", () -> {

		});
		and("the name evaluates to 'Ben' correctly", () -> {
			assertThat(name.get(), is(equalTo("Ben")));
		});
	}
}
