package org.optaplanner.webexamples.nurserostering.rest.cdi;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.examples.nurserostering.domain.NurseRoster;
import org.optaplanner.examples.nurserostering.persistence.NurseRosteringImporter;

@ApplicationScoped
public class NurseRosterSolverManager implements Serializable {
	
	private static final String SOLVER_CONFIG = "org/optaplanner/examples/nurserostering/solver/nurseRosteringSolverConfig.xml";
    private static final String IMPORT_DATASET = "/org/optaplanner/webexamples/nurserostering/long01.xml";

    private SolverFactory<NurseRoster> solverFactory;
    // TODO After upgrading to JEE 7, replace ExecutorService by ManagedExecutorService:
    // @Resource(name = "DefaultManagedExecutorService")
    // private ManagedExecutorService executor;
    private ExecutorService executor;

    private Map<String, NurseRoster> sessionSolutionMap;
    private Map<String, Solver<NurseRoster>> sessionSolverMap;

    @PostConstruct
    public synchronized void init() {
        solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
        // Always terminate a solver after 2 minutes
        TerminationConfig terminationConfig = new TerminationConfig();
        terminationConfig.setMinutesSpentLimit(2L);
        solverFactory.getSolverConfig().setTerminationConfig(terminationConfig);
        executor = Executors.newFixedThreadPool(2); // Only 2 because the other examples have their own Executor
        // TODO these probably don't need to be thread-safe because all access is synchronized
        sessionSolutionMap = new ConcurrentHashMap<String, NurseRoster>();
        sessionSolverMap = new ConcurrentHashMap<String, Solver<NurseRoster>>();
    }

    @PreDestroy
    public synchronized void destroy() {
        for (Solver<NurseRoster> solver : sessionSolverMap.values()) {
            solver.terminateEarly();
        }
        executor.shutdown();
    }

    public synchronized NurseRoster retrieveOrCreateSolution(String sessionId) {
        NurseRoster solution = sessionSolutionMap.get(sessionId);
        if (solution == null) {
            URL unsolvedSolutionURL = getClass().getResource(IMPORT_DATASET);
            File unsolvedSolutionFile = new File(unsolvedSolutionURL.getFile());
            solution = (NurseRoster) new NurseRosteringImporter()
                    .readSolution(unsolvedSolutionFile);
            sessionSolutionMap.put(sessionId, solution);
        }
        return solution;
    }

    public synchronized boolean solve(final String sessionId) {
        final Solver<NurseRoster> solver = solverFactory.buildSolver();
        solver.addEventListener(new SolverEventListener<NurseRoster>() {
            @Override
            public void bestSolutionChanged(BestSolutionChangedEvent<NurseRoster> event) {
                NurseRoster bestSolution = event.getNewBestSolution();
                synchronized (NurseRosterSolverManager.this) {
                    sessionSolutionMap.put(sessionId, bestSolution);
                }
            }
        });
        if (sessionSolverMap.containsKey(sessionId)) {
            return false;
        }
        sessionSolverMap.put(sessionId, solver);
        final NurseRoster solution = retrieveOrCreateSolution(sessionId);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                NurseRoster bestSolution = solver.solve(solution);
                synchronized (NurseRosterSolverManager.this) {
                    sessionSolutionMap.put(sessionId, bestSolution);
                    sessionSolverMap.remove(sessionId);
                }
            }
        });
        return true;
    }

    public synchronized boolean terminateEarly(String sessionId) {
        Solver<NurseRoster> solver = sessionSolverMap.remove(sessionId);
        if (solver != null) {
            solver.terminateEarly();
            return true;
        } else {
            return false;
        }
    }

}
