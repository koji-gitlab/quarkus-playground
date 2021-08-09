package org.example.api;

import org.example.dto.PostFormDto;
import org.example.service.PostService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path(PostResource.RESOURCE_PATH)
public class PostResource {

    public static final String RESOURCE_PATH = "/posts";

    @Context
    UriInfo uriInfo;

    @Inject
    private PostService service;

    @GET
    public Response getAll() {
        return service.findAll();
    }

    @GET
    @Path("{id}")
    public Response getOne(@PathParam("id") Long id) {
        return service.getOne(id);
    }

    @POST
    public Response create(PostFormDto dto) {
        return service.create(dto, uriInfo);
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, PostFormDto dto) {
        return service.update(id, dto);
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        return service.delete(id);
    }

}
