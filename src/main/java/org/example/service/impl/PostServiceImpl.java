package org.example.service.impl;

import io.quarkus.panache.common.Parameters;
import org.example.dto.PostFormDto;
import org.example.mapper.PostMapper;
import org.example.model.Post;
import org.example.repository.PostRepository;
import org.example.repository.TagRepository;
import org.example.service.PostService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Inject
    public PostServiceImpl(PostMapper postMapper, PostRepository postRepository, TagRepository tagRepository) {
        this.postMapper = postMapper;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Response findAll() {
        return Response.ok(
                        postRepository.findAll().list()
                )
                .build();
    }

    @Override
    public Response getOne(Long id) {
        var optional = postRepository.findByIdOptional(id);
        if (optional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(Function.identity().apply(optional.get())).build();
    }

    @Override
    @Transactional
    public Response create(PostFormDto dto, UriInfo uriInfo) {
        var entity = postMapper.toEntity(dto);
        populateTags(entity, dto.getTags());

        postRepository.persist(entity);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path("{id}")
                .resolveTemplate("id", entity.getId())
                .build();

        return Response.created(location).build();
    }

    @Override
    @Transactional
    public Response update(Long id, PostFormDto dto) {
        var optionalPost = postRepository.findByIdOptional(id);
        if (optionalPost.isPresent()) {
            var entity = optionalPost.get();
            entity.setContent(dto.getContent());
            entity.setTitle(dto.getTitle());
            entity.getTags().removeIf(tag -> dto.getTags().stream().noneMatch(s -> s.equalsIgnoreCase(tag.getLabel())));
            var temp = postMapper.toEntity(dto);
            if (temp.getTags() != null && temp.getTags().size() > 0) {
                entity.getTags().addAll(
                        temp.getTags()
                                .stream()
                                .filter(tag -> entity.getTags().stream().noneMatch(tag1 -> tag1.getLabel().equalsIgnoreCase(tag.getLabel())))
                                .collect(Collectors.toList())
                );
                populateTags(entity, dto.getTags());
                postRepository.persist(entity);
                return Response.ok(entity).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    @Transactional
    public Response delete(Long id) {
        var optionalPost = postRepository.findByIdOptional(id);
        optionalPost.ifPresent(post -> postRepository.delete(post));
        return Response.noContent().build();
    }

    private void populateTags(Post entity, Set<String> tags) {
        var list = tagRepository.list("label in :tags", Parameters.with("tags", tags));
        if (list != null && list.size() > 0) {
            entity.getTags().removeAll(list);
            entity.getTags().addAll(list);
        }
    }

}
