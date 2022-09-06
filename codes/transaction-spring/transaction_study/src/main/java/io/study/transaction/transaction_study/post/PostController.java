package io.study.transaction.transaction_study.post;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.BadStringOperationException;
import java.util.stream.IntStream;

@Controller
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    public PostController(PostService postService, PostRepository postRepository){
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @ResponseBody
    @GetMapping("/test/checked")
    public void test() throws BadStringOperationException {
        postRepository.deleteAll();

        Post p1 = Post.builder().content("AAA").author("헤르만헤세").version(1L).build();
        Post p2 = Post.builder().content("BBB").author("우라카미구니오").version(1L).build();

        postRepository.save(p1);
        postRepository.save(p2);

        StringBuilder longContentBuilder = new StringBuilder();

        // 5000 글자를 넘어가는 글자 생성
        IntStream.range(1,6000).forEach(longContentBuilder::append);

        // 수정요청 객체 생성
        PostDto modifyRequest = PostDto.builder()
                .id(1L)
                .author("헤르만 헤세 Hermann Karl Hesse")
                .content(longContentBuilder.toString())
                .version(1L)
                .build();

        postService.modifyPostWithCheckedException(modifyRequest);
    }

    @ResponseBody
    @GetMapping("/test/unchecked")
    public void testUnchecked(){
        postRepository.deleteAll();

        Post p1 = Post.builder().content("AAA").author("헤르만헤세").version(1L).build();
        Post p2 = Post.builder().content("BBB").author("우라카미구니오").version(1L).build();

        postRepository.save(p1);
        postRepository.save(p2);

        StringBuilder longContentBuilder = new StringBuilder();

        // 5000 글자를 넘어가는 글자 생성
        IntStream.range(1,6000).forEach(longContentBuilder::append);

        // 수정요청 객체 생성
        PostDto modifyRequest = PostDto.builder()
                .id(1L)
                .author("헤르만 헤세 Hermann Karl Hesse")
                .content(longContentBuilder.toString())
                .version(1L)
                .build();

        postService.modifyPostWithUncheckedException(modifyRequest);
    }
}
