package org.example.service.impl;

import org.example.dto.TagFormDto;
import org.example.model.Tag;
import org.example.repository.PostRepository;
import org.example.repository.TagRepository;
import org.example.service.TagService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.function.Function;

@ApplicationScoped
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    @Inject
    public TagServiceImpl(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Response findAll() {
        return Response.ok(
                tagRepository.findAll().list()
        ).build();
    }

    @Override
    public Response getOne(Long id) {
        var optional = tagRepository.findByIdOptional(id);
        if (optional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(Function.identity().apply(optional.get())).build();
    }

    @Override
    @Transactional
    public Response create(TagFormDto dto, UriInfo uriInfo) {
        var optionalTag = tagRepository.find("label", dto.getLabel()).singleResultOptional();
        if (!optionalTag.isPresent()) {
            Tag entity = new Tag(dto.getLabel());
            tagRepository.persist(entity);

            URI location = uriInfo.getAbsolutePathBuilder()
                    .path("{id}")
                    .resolveTemplate("id", entity.getId())
                    .build();

            return Response.created(location).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Override
    @Transactional
    public Response update(Long id, TagFormDto dto) {
        var optionalTag = tagRepository.find("label", dto.getLabel()).singleResultOptional();
        if (optionalTag.isPresent()) {
            var entity = optionalTag.get();
            entity.setLabel(dto.getLabel());
            tagRepository.persist(entity);
            return Response.ok(entity).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    @Transactional
    public Response delete(Long id) {
        var optionalTag = tagRepository.findByIdOptional(id);
        if (optionalTag.isPresent()) {
            var tag = optionalTag.get();
            if (tag.getPosts() != null && tag.getPosts().size() > 0) {
                tag.getPosts().stream().forEach(post -> {
                    post.getTags().remove(tag);
                    postRepository.persist(post);
                });
            }
            tag.getPosts().clear();
            tagRepository.delete(tag);
        }
        return Response.noContent().build();
    }

}
