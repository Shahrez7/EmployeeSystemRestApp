package com.logicbig.example.resources;

import com.google.gson.Gson;
import com.logicbig.example.services.MyRestApp;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/path")
public class OrderResource {
    @GET
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllEmployees() {
        try
        {
            return new Gson().toJson(MyRestApp.getAllEmployees());
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
        return null;
    }
    @GET
    @Path("/employees/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEmployeeById(@PathParam("id") int empId) {
        return new Gson().toJson(MyRestApp.getEmployeeById(empId));
    }
    @PUT
    @Path("/employees/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEmployee(@PathParam("id") int empId, String empDetails) {
        return new Gson().toJson(MyRestApp.updateEmployee(empId, empDetails));
    }


@GET
@Produces(MediaType.APPLICATION_JSON)
public String getEmployeeList(){
    return new Gson().toJson(MyRestApp.getE());
}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createEmployee(String empDetails){
        String newEntity = MyRestApp.createEmployee(empDetails);
        return newEntity;
    }
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteEmployee(@PathParam("id") int empid) {
        boolean context = MyRestApp.deleteEmp(empid);
        if (context) {
            return Response.ok("Successfully Deleted!", MediaType.TEXT_PLAIN).build().toString();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build().toString();
        }
    }
}

