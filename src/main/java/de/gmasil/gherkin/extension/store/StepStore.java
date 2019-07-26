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
package de.gmasil.gherkin.extension.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import de.gmasil.gherkin.extension.ExecutionStatus;
import de.gmasil.gherkin.extension.StepType;

public class StepStore {
	private StepType type;
	private String name;
	private ExecutionStatus status;
	@JsonIgnore
	private Throwable exception;

	public StepStore(String name, StepType type, ExecutionStatus status, Throwable exception) {
		this.name = name;
		this.type = type;
		this.status = status;
		this.exception = exception;
	}

	public String getName() {
		return name;
	}

	public StepType getType() {
		return type;
	}

	public ExecutionStatus getStatus() {
		return status;
	}

	public Throwable getException() {
		return exception;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getReadable() {
		return String.format("%s %s", type.getName(), name);
	}
}
