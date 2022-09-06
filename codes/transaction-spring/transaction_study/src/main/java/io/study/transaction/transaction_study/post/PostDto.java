package io.study.transaction.transaction_study.post;

import lombok.Builder;
import lombok.Data;

@Data
public class PostDto {
    private final Long id;
    private final String content;
    private final String author;
    private final Long version;

    @Builder
    public PostDto(Long id, String content, String author, Long version){
        this.id = id;
        this.content = content;
        this.author = author;
        this.version = version;
    }

    public PostDto ofNewVersion(){
        return PostDto.builder()
                .id(id)
                .content(content)
                .author(author)
                .version(version + 1)
                .build();
    }
}
