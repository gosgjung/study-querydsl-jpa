package io.study.transaction.transaction_study.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import javax.management.BadStringOperationException;

import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
//    @Transactional
    public boolean modifyPostWithCheckedException(PostDto postDto) throws BadStringOperationException {
        Optional<Post> optPost = postRepository.findById(postDto.getId());
        if(optPost.isEmpty()) return false;

        // 사실 checkValid 메서드 수행 후에 Version 을 업그레이드 하는게 맞는데, 예제 상황을 가정하기 위해 조금은 억지 상황을 가정했다.
        postRepository.save(optPost.get().newVersionPost());
//        postRepository.updateNewVersion(optPost.get().getId());

        if(checkValidContent_CheckedException(postDto)){
            Post forSave = optPost.get().fromModifyRequest(postDto);
            postRepository.save(forSave);
            return true;
        }
        TransactionTemplate

        return false;
    }

    public boolean checkValidContent_CheckedException(PostDto postDto) throws BadStringOperationException {
        if(Optional.ofNullable(postDto).isEmpty()) return false;
        if(Optional.ofNullable(postDto.getContent()).isEmpty()) return false;
        if(!StringUtils.hasText(postDto.getContent())) return false;

        if(postDto.getContent().length() > 5000){
            throw new BadStringOperationException("허용 가능한 글자수를 초과했습니다.");
        }

        return true;
    }

//    @Transactional(rollbackFor = Exception.class, readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
//    @Transactional
    public boolean modifyPostWithUncheckedException(PostDto postDto){
        return postRepository.findById(postDto.getId())
                .map(post -> {
                    // 사실 checkValid 메서드 수행 후에 Version 을 업그레이드 하는게 맞는데, 예제 상황을 가정하기 위해 조금은 억지 상황을 가정했다.
                    Post newVersion = post.newVersionPost();
//                    postRepository.updateNewVersion(post.getId());
                    postRepository.saveAndFlush(newVersion);

                    if(checkValidContent_UncheckedException(postDto)) {
                        Post forSave = newVersion.fromModifyRequest(postDto);
                        postRepository.save(forSave);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    public boolean checkValidContent_UncheckedException(PostDto postDto) {
        if(Optional.ofNullable(postDto).isEmpty()) return false;
        if(Optional.ofNullable(postDto.getContent()).isEmpty()) return false;
        if(!StringUtils.hasText(postDto.getContent())) return false;

        if(postDto.getContent().length() > 5000){
            throw new IllegalArgumentException("허용 가능한 글자수를 초과했습니다.");
        }

        return true;
    }

    public Post ofNewPost(PostDto postDto){
        return Post.builder()
                .author(postDto.getAuthor())
                .content(postDto.getContent())
                .build();
    }
}
