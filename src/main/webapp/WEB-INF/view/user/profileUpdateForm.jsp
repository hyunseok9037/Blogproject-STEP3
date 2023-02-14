<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <%@ include file="../layout/header.jsp" %>

        <style>
            .container {
                display: flex;
                flex-direction: column;
                align-items: center;
            }

            h2 {
                margin-top: 2rem;
            }

            form {
                width: 50%;
                margin-top: 2rem;
                display: flex;
                flex-direction: column;
                align-items: center;
                border: 1px solid gray;
                padding: 1rem;
                border-radius: 10px;
            }

            .form-group {
                margin-bottom: 1rem;
                text-align: center;
            }

            .form-group img {
                width: 320px;
                height: 270px;
                border-radius: 50%;
                margin-bottom: 1rem;
                border: 1px solid gray;
            }

            .btn {
                margin-top: 1rem;
                width: 20%;
            }
        </style>

        <div class="container my-3">
            <h2 class="text-center">프로필 사진 변경</h2>
            <form action="/user/profileUpdate" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <img src="/images/dora.png" alt="Current Photo" class="img-fluid" id="imagePreview">
                </div>
                <div class="form-group">
                    <input type="file" class="form-control" id="profile" name="profile" onchange="chooseImage(this)">
                </div>
                <button type="submit" class="btn btn-primary">사진변경</button>
            </form>
        </div>

        <script> //사진변경 할때
            function chooseImage(obj) {
                // console.log(obj);
                // console.log(obj.files);
                let f = obj.files[0];
                console.log(f);

                let reader = new FileReader(); //버퍼로 파일을 읽는다
                reader.readAsDataURL(f); //싱글쓰레드 기다리고 있다. 아무것도 못한다 클릭x 이벤트 큐에 등록한다.

                //콜스택이 다 비워지고, 이벤트 루프로가서 readAsDataURL 이벤트가 끝나면 콜백시켜주는 함수 (행위를 등록한다)
                reader.onload = function (e) {
                    console.log(e); //파일이 전송 되었다는 내용 
                    console.log(e.target.result);
                    $("#imagePreview").attr("src", e.target.result)
                };
            }
        </script>

        <%@ include file="../layout/footer.jsp" %>