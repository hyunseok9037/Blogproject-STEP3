package shop.mtcoding.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardReq.BoardupdateReqDto;
import shop.mtcoding.blog.dto.board.BoardResp;
import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailRespDto;
import shop.mtcoding.blog.dto.reply.ReplyResp.ReplyDetailRespDto;
import shop.mtcoding.blog.model.User;

@Transactional // 메서드 실행 직후 롤백!! //단점: auto_increment 초기화가 안된다
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    private MockHttpSession mockSessin;

    @Autowired
    private ObjectMapper om;

    @BeforeEach // Test 메서드 실행 직전 마다에 호출됨
    public void setUp() {
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setEmail("ssar@nate.com");
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        mockSessin = new MockHttpSession();
        mockSessin.setAttribute("principal", user);
    }

    @Test
    public void update_test() throws Exception {
        // given
        int id = 1;
        BoardupdateReqDto boardupdateReqDto = new BoardupdateReqDto();
        boardupdateReqDto.setTitle("제목1-수정");
        boardupdateReqDto.setContent("내용1-수정");

        String requestBody = om.writeValueAsString(boardupdateReqDto);
        System.out.println("테스트:" + requestBody);

        // when
        ResultActions resultActions = mvc.perform(
                put("/board/" + id)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .session(mockSessin));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(1));
    }

    @Test
    public void delete_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(
                delete("/board/" + id).session(mockSessin));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(null);
        System.out.println("테스트:" + responseBody);

        /**
         * jsonPath
         * 최상위: $
         * 객체탐색:닷(.)
         * 배열:[0] 번지수
         * $[0].title
         */
        // then 302 가뜨는지
        resultActions.andExpect(jsonPath("$.code").value("1"));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void main_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(
                get("/"));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardResp.BoardMainRespDto> dtos = (List<BoardResp.BoardMainRespDto>) map.get("dtos");
        String responseBody = om.writeValueAsString(dtos);
        System.out.println("테스트 : " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(dtos.size()).isEqualTo(6);
        assertThat(dtos.get(0).getUsername()).isEqualTo("ssar");
        assertThat(dtos.get(0).getTitle()).isEqualTo("1번째 제목");
    }

    @Test
    public void detail_test() throws Exception {
        // given
        int id = 1;
        // when
        ResultActions resultActions = mvc.perform(
                get("/board/" + id));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardDetailRespDto boardDto = (BoardDetailRespDto) map.get("boardDto");
        List<ReplyDetailRespDto> replyDtos = (List<ReplyDetailRespDto>) map.get("replyDtos");
        // String boardJson = om.writeValueAsString(boardDto);
        // String replyListJson = om.writeValueAsString(replyDtos);
        // System.out.println("테스트:" + boardJson);
        // System.out.println("테스트:" + boardDto);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(boardDto.getUsername()).isEqualTo("ssar");
        assertThat(boardDto.getUserId()).isEqualTo(1);
        assertThat(boardDto.getTitle()).isEqualTo("1번째 제목");
        assertThat(replyDtos.get(1).getComment()).isEqualTo("댓글3");
        assertThat(replyDtos.get(1).getUsername()).isEqualTo("love");
    }

    @Test
    public void sava_test() throws Exception {
        // given
        String title = "";
        for (int i = 0; i < 99; i++) {
            title += "가";
        }

        String requestBody = "title=" + title + "&content=내용1";

        // when
        ResultActions resultActions = mvc.perform(
                post("/board")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .session(mockSessin));

        // then
        resultActions.andExpect(status().is3xxRedirection());
    }

}