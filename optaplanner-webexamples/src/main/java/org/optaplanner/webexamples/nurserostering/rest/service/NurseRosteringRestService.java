package org.optaplanner.webexamples.nurserostering.rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.optaplanner.webexamples.nurserostering.rest.domain.JsonMessage;
import org.optaplanner.webexamples.nurserostering.rest.domain.JsonNurseRosterSolution;


/**
 * @see DefaultVehicleRoutingRestService
 */
@Path("/nurserostering")
public interface NurseRosteringRestService {
	
	@GET
    @Path("/solution")
    @Produces("application/json")
	JsonNurseRosterSolution getSolution();

	@POST
    @Path("/solution/solve")
    @Produces("application/json")
    JsonMessage solve();
	
	@POST
    @Path("/solution/terminateEarly")
    @Produces("application/json")
    JsonMessage terminateEarly();
}
