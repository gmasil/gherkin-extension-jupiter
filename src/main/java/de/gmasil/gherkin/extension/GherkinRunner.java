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

import java.util.LinkedList;
import java.util.List;

import de.gmasil.gherkin.extension.store.StepStore;

public class GherkinRunner {

    private String className;
    private String methodName;
    private String scenarioName;

    private boolean failed = false;
    private List<StepStore> steps = new LinkedList<>();
    private StepStore failedStep = null;

    public GherkinRunner(String className, String methodName, String scenarioName) {
        this.className = className;
        this.methodName = methodName;
        if (scenarioName == null) {
            this.scenarioName = methodName;
        } else {
            this.scenarioName = scenarioName;
        }
    }

    public void executeStep(String name, GherkinRunnable runnable, StepType type) {
        ExecutionStatus stepStatus = ExecutionStatus.SKIPPED;
        Throwable stepException = null;
        if (!failed) {
            try {
                runnable.execute();
                stepStatus = ExecutionStatus.SUCCESS;
            } catch (Throwable t) {
                failed = true;
                stepException = t;
                stepStatus = ExecutionStatus.FAILED;
            }
        }
        StepStore currentStep = new StepStore(name, type, stepStatus, stepException);
        if (stepStatus == ExecutionStatus.FAILED) {
            failedStep = currentStep;
        }
        steps.add(currentStep);
    }

    public boolean isFailed() {
        return failed;
    }

    public List<StepStore> getSteps() {
        return steps;
    }

    public boolean isFrom(String className, String methodName) {
        return getClassName().equals(className) && getMethodName().equals(methodName);
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public StepStore getFailedStep() {
        return failedStep;
    }
}
