/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.webexamples.nurserostering2;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpSession;

import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.Solver;

import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import org.optaplanner.examples.nurserostering.domain.NurseRoster;
import org.optaplanner.examples.nurserostering.persistence.NurseRosteringImporter;
import org.optaplanner.webexamples.nurserostering2.NurseRosteringSessionAttributeName;;

public class NurseRosteringWebAction {

    private static ExecutorService solvingExecutor = Executors.newFixedThreadPool(4);
    private static final String SOLVER_CONFIG = "org/optaplanner/examples/nurserostering/solver/nurseRosteringSolverConfig.xml";
    private static final String IMPORT_DATASET = "/org/optaplanner/webexamples/nurserostering/long01.xml";

    public void setup(HttpSession session) {
        terminateEarly(session);

        SolverFactory<NurseRoster> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
        Solver<NurseRoster> solver = solverFactory.buildSolver();
        session.setAttribute(NurseRosteringSessionAttributeName.SOLVER, solver);

        URL unsolvedSolutionURL = getClass().getResource(IMPORT_DATASET);
        File unsolvedSolutionFile = new File(unsolvedSolutionURL.getFile());
        NurseRoster unsolvedSolution =(NurseRoster) new NurseRosteringImporter()
                .readSolution(unsolvedSolutionFile);
        session.setAttribute(NurseRosteringSessionAttributeName.SHOWN_SOLUTION, unsolvedSolution);
    }

    @SuppressWarnings("unchecked")
	public void solve(final HttpSession session) {
        final Solver<NurseRoster> solver = (Solver<NurseRoster>) session.getAttribute(NurseRosteringSessionAttributeName.SOLVER);
        final NurseRoster unsolvedSolution = (NurseRoster) session.getAttribute(NurseRosteringSessionAttributeName.SHOWN_SOLUTION);

        solver.addEventListener(new SolverEventListener<NurseRoster>() {
            public void bestSolutionChanged(BestSolutionChangedEvent<NurseRoster> event) {
                NurseRoster bestSolution = event.getNewBestSolution();
                session.setAttribute(NurseRosteringSessionAttributeName.SHOWN_SOLUTION, bestSolution);
            }
        });
        solvingExecutor.submit(new Runnable() {
            public void run() {
                solver.solve(unsolvedSolution);
            }
        });
    }

    @SuppressWarnings("unchecked")
	public void terminateEarly(HttpSession session) {
        final Solver<NurseRoster> solver = (Solver<NurseRoster>) session.getAttribute(NurseRosteringSessionAttributeName.SOLVER);
        if (solver != null) {
            solver.terminateEarly();
            session.setAttribute(NurseRosteringSessionAttributeName.SOLVER, null);
        }
    }

}
