package org.example.service;

import org.example.dto.TagFormDto;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface TagService {

    Response findAll();

    Response getOne(Long id);

    Response create(TagFormDto dto, UriInfo uriInfo);

    Response update(Long id, TagFormDto dto);

    Response delete(Long id);

}
