package shop.mtcoding.blog.handler.ex;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {

    private HttpStatus status;

    public CustomApiException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }

    // 생성자 오버로딩을 한다. this로 나를 때린다
    public CustomApiException(String msg) {
        this(msg, HttpStatus.BAD_REQUEST);
    }
}
