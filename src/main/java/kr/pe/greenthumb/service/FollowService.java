package kr.pe.greenthumb.service;

import kr.pe.greenthumb.common.exception.NotFoundException;
import kr.pe.greenthumb.dao.user.FollowRepository;
import kr.pe.greenthumb.dao.user.UserRepository;
import kr.pe.greenthumb.domain.user.Follow;
import kr.pe.greenthumb.domain.user.User;
import kr.pe.greenthumb.dto.user.FollowDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followDao;
    private final UserRepository userDao;

    // 팔로우 요청
    @Transactional
    public String add(FollowDTO.Create dto) {
        User follower = userDao.findById(dto.getFollowerId()).
                orElseThrow(NotFoundException::new);

        User following = userDao.findById(dto.getFolloweeId()).
                orElseThrow(NotFoundException::new);

        if(following.getIsBlack().equals("n") && following.getIsDeleted().equals("n")) {
            followDao.save(dto.toEntity(follower, following)).getFollowId();
            return "팔로우 요청 완료";
        } else return "해당 회원은 요청 불가";
    }

    // 유저 한명의 팔로워 목록 조회
    @Transactional
    public List<String> getFollwers(Long followeeId) {

        User followee = userDao.findById(followeeId).
                orElseThrow(NotFoundException::new);

        List<Follow> followList = followDao.findFollowerByfollowee(followee);
        List<String> nickNameList = new ArrayList<>();

        for(Follow f : followList) {
            if(f.getFollower().getIsBlack().equals("y")) followDao.delete(f);
            else nickNameList.add(f.getFollower().getUserNickname());
        }

        return nickNameList;
    }

    // 유저 한명의 팔로잉 목록 조회
    @Transactional
    public List<String> getFollowees(Long followerId) {

        User follower = userDao.findById(followerId).
                orElseThrow(NotFoundException::new);

        List<Follow> followList = followDao.findFolloweeByfollower(follower);
        List<String> nickNameList = new ArrayList<>();

        for(Follow f : followList) {
            if(f.getFollowee().getIsBlack().equals("y")) followDao.delete(f);
            else nickNameList.add(f.getFollower().getUserNickname());
        }

        return nickNameList;
    }

    // 언팔로우
    @Transactional
    public void delete(FollowDTO.Delete dto) {
        User follower = userDao.findById(dto.getFollowerId()).
                orElseThrow(NotFoundException::new);

        User followee = userDao.findById(dto.getFolloweeId()).
                orElseThrow(NotFoundException::new);

        followDao.deleteByFollowerAndFollowee(follower, followee);
    }

}