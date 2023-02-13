package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.ReplyRepository;

@Slf4j
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

    @Transactional
    public void 댓글삭제(int id, int principalId) {
        Reply reply = replyRepository.findById(id);
        if (reply == null) {
            throw new CustomApiException("없는 댓글을  삭제할 수 없습니다"); // 생성자 오버로딩
        }
        if (reply.getUserId() != principalId) {
            throw new CustomApiException("해당 댓글을  삭제할 수 없습니다", HttpStatus.FORBIDDEN);

        }
        // 1.인증 ok , 2. 댓글존재유무확인, 3.권한ok
        // 제어가 안될때 try catch 로 묶는다

        try {
            replyRepository.deleteById(id);
        } catch (Exception e) {
            log.error("서버에러:" + e.getMessage());
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR);
            // 로그를 남겨야 함 (DB or File)
        }
    }
}