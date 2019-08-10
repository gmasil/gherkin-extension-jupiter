# Gherkin Extension Jupiter
This project is Junit 5 (Jupiter) extension to allow writing tests in a gherkin like style.

## Maven
Maven dependency:

    <dependency>
      <groupId>de.gmasil</groupId>
      <artifactId>gherkin-extension-jupiter</artifactId>
      <version>1.1</version>
      <scope>test</scope>
    </dependency>

Add the repository to your pom or settings:

    <repositories>
      <repository>
        <id>gmasil-nexus</id>
        <url>https://nexus.gmasil.de/repository/maven-releases/</url>
      </repository>
    </repositories>

## Example

```java
@Story("A normal user may not change any system settings")
public class ExampleTest extends GherkinTest {
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
```
A full example can be seen here: [ExampleTest.java](src/test/java/de/gmasil/gherkin/example/ExampleTest.java)

The above test code would create a XML report like this:

```xml
<story name="A normal user may not change any system settings">
  <className>de.gmasil.gherkin.example.ExampleTest</className>
  <scenarios>
    <scenario name="A normal user does not see the admin menu">
      <methodName>testNormalUserHasNoAdminMenu</methodName>
      <failed>false</failed>
      <steps>
        <step readable="Given the user 'Peter' with password 'secret' exists">
          <type>GIVEN</type>
          <name>the user 'Peter' with password 'secret' exists</name>
          <status>SUCCESS</status>
        </step>
        <step readable="When the user logs in">
          <type>WHEN</type>
          <name>the user logs in</name>
          <status>SUCCESS</status>
        </step>
        <step readable="Then no admin menu is shown">
          <type>THEN</type>
          <name>no admin menu is shown</name>
          <status>SUCCESS</status>
        </step>
      </steps>
    </scenario>
  </scenarios>
</story>
```

## License
[GNU GPL v3 License](LICENSE.md)  

TestAPI is free software: you can redistribute it and/or modify  
it under the terms of the GNU General Public License as published by  
the Free Software Foundation, either version 3 of the License, or  
(at your option) any later version.  

TestAPI is distributed in the hope that it will be useful,  
but WITHOUT ANY WARRANTY; without even the implied warranty of  
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  
GNU General Public License for more details.  