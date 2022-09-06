package io.study.transaction.transaction_study.post;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(of = {"id", "author", "content"})
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "POST_ID")
    private Long id;

    @Column(name = "POST_VERSION")
    private Long version;

    @Column(name = "POST_AUTHOR")
    private String author;

    @Column(name = "POST_CONTENT")
    private String content;

    public Post newVersionPost(){
        return Post.builder()
                .id(id)
                .version(version+1)
                .author(author)
                .content(content)
                .build();
    }

    public Post fromModifyRequest(PostDto postDto){
        return Post.builder()
                .id(id)
                .version(this.version)
                .author(postDto.getAuthor())
                .content(postDto.getContent())
                .build();
    }
}
