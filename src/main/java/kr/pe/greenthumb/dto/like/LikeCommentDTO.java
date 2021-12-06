package kr.pe.greenthumb.dto.like;

import kr.pe.greenthumb.domain.like.LikeComment;
import kr.pe.greenthumb.domain.post.Comment;
import kr.pe.greenthumb.domain.user.User;
import lombok.Builder;
import lombok.Getter;

public class LikeCommentDTO {

    @Getter
    public static class Create {
        private Long commentId;
        private Long userId;

        @Builder
        public Create(Long commentId, Long userId) {
            this.commentId = commentId;
            this.userId = userId;
        }

        public LikeComment toEntity(Comment comment, User user) {
            return LikeComment.builder()
                    .comment(comment)
                    .user(user)
                    .build();
        }
    }

    @Getter
    public static class Delete {
        private Long commentId;
        private Long userId;
    }

    @Getter
    public static class Get {
        private Long commentId;
        private Long userId;

        public Get(LikeComment entity) {
            this.commentId = entity.getComment().getCommentId();
            this.userId = entity.getUser().getUserId();
        }
    }

}