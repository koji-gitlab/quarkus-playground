package org.example.mapper;

import org.example.dto.PostFormDto;
import org.example.model.Post;
import org.example.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface PostMapper {

    @Mapping(source = "tags", target = "tags")
    Post toEntity(PostFormDto dto);

    default Tag fromStringToTag(String label) {
        return new Tag(label);
    }

}
