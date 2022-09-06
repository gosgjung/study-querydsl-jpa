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

import java.util.stream.IntStream;

/**
 * 실제 테스트 케이스 시에는 트랜잭션 테스트가 아니라면,
 * 서비스 계층의 테스트시에는 Repository 계층의 의존성은 가급적 Mock 객체로 주입해주는게 맞다.
 *
 * 이번 테스트는 트랜잭션이 실제로 롤백하는지를 검증하기 위한 것이 목적이기에
 * 테스트 용도의 도커 postgresql 에서 구동하기로 했고,
 * TestContainer 없이 직접 로컬에서 docker-compose up -d 명령으로 구동시켜놓은 데몬에 접속해서
 * 결과를 확인하고, 결과 스크린샷과 함께 결과문서를 남기기로 했다.
 *
 * 결과를 스크린샷과 함께 문서로 남기는 것이 목적이기에, 예외적으로 @Transactional(rollback=false)를 적용해두었다.
 * 실제 다른 테스트케이스 작성시에는 테스트가 끝난 후에는 다시 원래 상태로 되돌려지도록 작성하는 것이 일반적인 케이스이다.
 */
@ActiveProfiles("test-docker")
@SpringBootTest
@Transactional
@Rollback(value = false)
public class ModifyPostWithUncheckedExceptionTest {

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
    public void TEST_UNCHECKED(){
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

        boolean result = postService.modifyPostWithUncheckedException(modifyRequest);

    }
}
