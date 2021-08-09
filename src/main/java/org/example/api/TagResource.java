package org.example.api;

import org.example.dto.TagFormDto;
import org.example.service.TagService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path(TagResource.RESOURCE_PATH)
public class TagResource {

    public static final String RESOURCE_PATH = "/tags";

    @Context
    UriInfo uriInfo;

    @Inject
    private TagService service;

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
    public Response create(TagFormDto dto) {
        return service.create(dto, uriInfo);
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, TagFormDto dto) {
        return service.update(id, dto);
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        return service.delete(id);
    }

}
