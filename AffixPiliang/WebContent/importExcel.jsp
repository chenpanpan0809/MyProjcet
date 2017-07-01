<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="./js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="./js/jquery.form.js" type="text/javascript"></script>
<script src="./js/excelJs.js" type="text/javascript"></script>
<style type="text/css">
.file {
    position: relative;
    display: inline-block;
    background: #D0EEFF;
    border: 1px solid #99D3F5;
    border-radius: 4px;
    overflow: hidden;
    color: #1E88C7;
    text-decoration: none;
    text-indent: 0;
    line-height: 20px;
    top:6px;
}
.file input {
    position: absolute;
    font-size: 100px;
    right: 0;
    top: 0;
    opacity: 0;
} 
.file:hover {
    background: #AADFFD;
    border-color: #78C3F3;
    color: #004974;
    text-decoration: none;
}
.positionFile{
margin-left: 10px;
margin-top: 30px;
}
.poConfirm{
margin-top: 40px;
margin-left: 65px;
}

</style>
</head>
<body >

<form id="formFile">
<input id="textId" type="text" value="" class="positionFile"  readonly="readonly">
<a href="javascript:;" class="file">点击上传文件
<input type="file" id="fileUpload" name="files" >
</a>
<br>
<input type="button" id="confirm" value="确定" class="co-win-button poConfirm">
<input type="button" id="cancel" value="取消" class="co-win-button poConfirm">
</form>
</body>
</html>