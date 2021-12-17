package com.ssh.greenthumb.api.service;

import com.ssh.greenthumb.api.common.exception.NotFoundException;
import com.ssh.greenthumb.api.dao.like.LikePostRepository;
import com.ssh.greenthumb.api.dao.post.FileRepository;
import com.ssh.greenthumb.api.dao.post.PostRepository;
import com.ssh.greenthumb.api.dao.user.UserRepository;
import com.ssh.greenthumb.api.domain.post.File;
import com.ssh.greenthumb.api.domain.post.Post;
import com.ssh.greenthumb.api.domain.user.User;
import com.ssh.greenthumb.api.dto.post.FileDTO;
import com.ssh.greenthumb.api.dto.post.PostDTO;
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
    private final FileRepository fileDao;
    private final LikePostRepository likePostDao;

    @Transactional
    public Long add(PostDTO.Create dto) {
        User user = userDao.findById(dto.getUserId()).
                orElseThrow(NotFoundException::new);

//        for(File f : dto.getFileList()) {
//            fileDao.save(File.builder()
//                    .post(f.getPost())
//                    .fileUrl(f.getFileUrl())
//                    .build());
//        }
        Post post = postDao.save(dto.toEntity(user, dto.getTitle(), dto.getCategory(), dto.getContent()));

        File file = fileDao.save(File.builder().fileUrl(dto.getFileUrl()).post(post).build());

        return post.getId();
    }

    @Transactional
    public List<PostDTO.Get> getAll() {
        return postDao.findAllByIsDeleted("n").stream().map(PostDTO.Get::new).collect(Collectors.toList());
    }

    @Transactional
    public List<PostDTO.Get> getAllByCategory(String postCategory) {
        return postDao.findAllPostByCategoryAndIsDeleted(postCategory, "n").stream().map(PostDTO.Get::new).collect(Collectors.toList());
    }

    @Transactional
    public PostDTO.Get getOne(Long postId) {
        return postDao.findById(postId).map(PostDTO.Get::new).get();
    }

    @Transactional
    public Long update(Long postId, PostDTO.Update dto) {
        Post post = postDao.findById(postId).
                orElseThrow(NotFoundException::new);

        post.update(dto.getTitle(), dto.getCategory(), dto.getContent());

        return post.getId();
    }

    @Transactional
    public Long updateCheck(Long postId) {
        Post post = postDao.findById(postId).
                orElseThrow(NotFoundException::new);

        post.updateCheck(post.getIsComplete());

        return post.getId();
    }

    @Transactional
    public void delete(Long postId) {
        Post post = postDao.findById(postId)
                .orElseThrow(NotFoundException::new);

        post.delete();
    }

    // 파일 생성
    @Transactional
    public Long addFile(Long postId, FileDTO.Create dto) {
        Post post = postDao.findById(postId).
                orElseThrow(NotFoundException::new);

        return fileDao.save(dto.toEntity(post, dto.getFileUrl())).getId();
    }

    // 파일 삭제
    @Transactional
    public void deleteFile(Long fileId) {
        File file = fileDao.findById(fileId).
                orElseThrow(NotFoundException::new);

        fileDao.delete(file);
    }

}