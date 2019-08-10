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
package de.gmasil.gherkin.example;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.gmasil.gherkin.example.dummy.Database;
import de.gmasil.gherkin.example.dummy.User;
import de.gmasil.gherkin.example.dummy.WebDriver;
import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Reference;
import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;

@Story("A normal user may not change any system settings")
@ExtendWith(MockitoExtension.class)
public class ExampleTest extends GherkinTest {

    @Mock
    private Database database;

    @Mock
    private WebDriver driver;

    @Scenario("A normal user does not see the admin menu")
    public void testNormalUserHasNoAdminMenu(Reference<User> user) {
        given("the user 'Peter' with password 'secret' exists", () -> {
            user.set(new User("Peter", "secret"));
        });
        when("the user logs in", () -> {
            driver.fillTextfield("loginform-username", user.get().getUsername());
            driver.fillTextfield("loginform-password", user.get().getPassword());
            driver.clickElement("loginform-submit");
        });
        then("no admin menu is shown", () -> {
            assertThat(driver.hasElement("menu-admin"), is(equalTo(false)));
        });
    }
}
