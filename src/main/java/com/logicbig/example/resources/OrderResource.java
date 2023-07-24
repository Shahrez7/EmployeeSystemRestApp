package com.logicbig.example.resources;
import com.google.gson.Gson;
import com.logicbig.example.services.MyRestApp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/path")
public class OrderResource {
    private static final Logger LOGGER = LogManager.getLogger(OrderResource.class);

    @GET
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllEmployees() {
        try {
            LOGGER.info("Request received: getAllEmployees");
            return new Gson().toJson(MyRestApp.getAllEmployees());
        } catch (Exception exc) {
            LOGGER.error("Error occurred during getAllEmployees", exc);
            exc.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("/employees/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEmployeeById(@PathParam("id") int empId) {
        try {
            LOGGER.info("Request received: getEmployeeById, Employee ID: " + empId);
            return new Gson().toJson(MyRestApp.getEmployeeById(empId));
        } catch (Exception exc) {
            LOGGER.error("Error occurred during getEmployeeById", exc);
            exc.printStackTrace();
        }
        return null;
    }

    @PUT
    @Path("/employees/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEmployee(@PathParam("id") int empId, String empDetails) {
        try {
            LOGGER.info("Request received: updateEmployee, Employee ID: " + empId);
            return new Gson().toJson(MyRestApp.updateEmployee(empId, empDetails));
        } catch (Exception exc) {
            LOGGER.error("Error occurred during updateEmployee", exc);
            exc.printStackTrace();
        }
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getEmployeeList() {
        try {
            LOGGER.info("Request received: getEmployeeList");
            return new Gson().toJson(MyRestApp.getE());
        } catch (Exception exc) {
            LOGGER.error("Error occurred during getEmployeeList", exc);
            exc.printStackTrace();
        }
        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createEmployee(String empDetails) {
        try {
            LOGGER.info("Request received: createEmployee");
            String newEntity = MyRestApp.createEmployee(empDetails);
            return newEntity;
        } catch (Exception exc) {
            LOGGER.error("Error occurred during createEmployee", exc);
            exc.printStackTrace();
        }
        return null;
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteEmployee(@PathParam("id") int empid) {
        try {
            LOGGER.info("Request received: deleteEmployee, Employee ID: " + empid);
            boolean context = MyRestApp.deleteEmp(empid);
            if (context) {
                return Response.ok("Successfully Deleted!", MediaType.TEXT_PLAIN).build().toString();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build().toString();
            }
        } catch (Exception exc) {
            LOGGER.error("Error occurred during deleteEmployee", exc);
            exc.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build().toString();
        }
    }
}
