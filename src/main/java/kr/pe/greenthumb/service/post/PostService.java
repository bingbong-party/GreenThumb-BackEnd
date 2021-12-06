package kr.pe.greenthumb.service.post;

import kr.pe.greenthumb.common.exception.NotFoundException;
import kr.pe.greenthumb.dao.post.PostRepository;
import kr.pe.greenthumb.dao.user.UserRepository;
import kr.pe.greenthumb.domain.post.Post;
import kr.pe.greenthumb.domain.user.User;
import kr.pe.greenthumb.dto.post.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postDao;
    private final UserRepository userDao;

    public Post add(PostDTO.Create dto) {
        User user = userDao.findById(dto.getUserId()).
                orElseThrow(NotFoundException::new);

        return postDao.save(postDao.save(dto.toEntity(user)));
    }

    public List<Post> getAll(String category) {
        return postDao.findPostByPostCategory(category);
    }

    public void update(Post post) {
        postDao.save(post);
    }

    public void delete(Long postId) {
        Post post = postDao.findById(postId).get();
        postDao.delete(post);
    }

}
