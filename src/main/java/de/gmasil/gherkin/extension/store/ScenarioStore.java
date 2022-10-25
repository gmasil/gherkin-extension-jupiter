/*
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
package de.gmasil.gherkin.extension.store;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonRootName("Scenario")
public class ScenarioStore {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    private String methodName;

    private boolean failed = false;

    @JacksonXmlElementWrapper(localName = "steps")
    @JacksonXmlProperty(localName = "step")
    private List<StepStore> steps = new LinkedList<>();

    public ScenarioStore() {
    }

    public ScenarioStore(String name, String methodName, boolean failed) {
        this.name = name;
        this.methodName = methodName;
        this.failed = failed;
    }

    public String getName() {
        return name;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean isFailed() {
        return failed;
    }

    public List<StepStore> getSteps() {
        return steps;
    }
}
