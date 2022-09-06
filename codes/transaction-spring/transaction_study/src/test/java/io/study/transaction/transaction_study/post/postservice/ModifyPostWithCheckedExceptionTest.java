package io.study.transaction.transaction_study.post.postservice;

import io.study.transaction.transaction_study.post.Post;
import io.study.transaction.transaction_study.post.PostDto;
import io.study.transaction.transaction_study.post.PostRepository;
import io.study.transaction.transaction_study.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.management.BadStringOperationException;
import java.util.stream.IntStream;

@ActiveProfiles("test-docker")
@SpringBootTest
public class ModifyPostWithCheckedExceptionTest {

    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void setup(){
        postService = new PostService(postRepository);
        postRepository.deleteAll();

        Post p1 = Post.builder().content("AAA").author("헤르만헤세").version(1L).build();
        Post p2 = Post.builder().content("BBB").author("우라카미구니오").version(1L).build();

        postRepository.save(p1);
        postRepository.save(p2);
    }

    @Test
    public void TEST_CHECKED() throws BadStringOperationException {
        StringBuilder longContentBuilder = new StringBuilder();
        // 5000 글자를 넘어가는 글자 생성
        IntStream.range(1,6000).forEach(longContentBuilder::append);
        TransactionTemplate

        // 수정요청 객체 생성
        PostDto modifyRequest = PostDto.builder()
                .id(1L)
                .author("헤르만 헤세 Hermann Karl Hesse")
                .content(longContentBuilder.toString())
                .version(1L)
                .build();

//        try {
            boolean result = postService.modifyPostWithCheckedException(modifyRequest);
            System.out.println(result);
//        } catch (BadStringOperationException e) {
//            e.printStackTrace();
//        }
    }
}
