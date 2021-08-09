package org.example.service;

import org.example.dto.PostFormDto;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface PostService {

    Response findAll();

    Response getOne(Long id);

    Response create(PostFormDto dto, UriInfo uriInfo);

    Response update(Long id, PostFormDto dto);

    Response delete(Long id);

}
