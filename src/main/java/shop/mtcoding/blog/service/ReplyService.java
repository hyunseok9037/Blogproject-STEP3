package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.ReplyRepository;

@Transactional(readOnly = true)
@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Transactional
    public void 댓글쓰기(ReplySaveReqDto replySaveReqDto, int principalId) {
        int result = replyRepository.insert(
                replySaveReqDto.getComment(),
                replySaveReqDto.getBoardId(),
                principalId);
        if (result != 1) {
            throw new CustomApiException("댓글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void 댓글삭제(int id, int userId) {
        Reply ReplyPS = replyRepository.findById(id);
        if (ReplyPS == null) {
            throw new CustomApiException("없는 댓글을  삭제할 수 없습니다");
        }
        if (ReplyPS.getUserId() != userId) {
            throw new CustomApiException("해당 댓글을  삭제할 수 없습니다", HttpStatus.FORBIDDEN);

        }

        try {
            replyRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
            // 로그를 남겨야 함 (DB or File)
        }
    }
}