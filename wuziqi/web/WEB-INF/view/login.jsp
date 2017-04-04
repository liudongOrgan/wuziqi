<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<jsp:include page="target.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="icon" href="${ctx }/static/picture/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="${ctx }/static/picture/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="${ctx }/static/js/jquery-3.1.1.min.js"></script>

<title>登录</title>

<meta name="viewport" content="width=device-width" />
<script>

	function submit(){
		var name = $("#name").val();
		if(null == name || "" == name.trim()){
			alert('请输入昵称！');
			return;
		}
		var url = "${ctx}/login/data";
		var data = {
			"name" : name
		}
		var callback = function(data){
			if("created" == data['status']){
				window.location.href="/index";
				return;
			}
			if("long" == data['status']){
				alert('昵称太长！');
				return;
			}
			if("empty" == data['status']){
				alert('请输入昵称！');
				return;
			}
			if("exists" == data['status']){
				alert('昵称已存在！');
				return;
			}
			if("success" == data['status']){
				window.location.href="/index";
			}
		}
		sendPost(url, data, callback);
	}
	
	
	function sendPost(url, data, callback) {
		$.ajax({
			url : url,
			type : 'POST', //GET
			async : false, //或false,是否异步
			data : data,
			//dataType : 'json', //返回的数据格式：json/xml/html/script/jsonp/text
			success : function(data, textStatus, jqXHR) {
				callback(data);
			},
			error : function(xhr, textStatus) {
				alert("通信出现错误！");
				console.log('错误');
				console.log(xhr);
				console.log(textStatus);
			}
		});
	}
</script>
<style type="text/css">
body,html{width:100%;height:100%;}
</style>
</head>

<body >
	<p>登录五子棋-设置昵称：</p>
	<div style="text-align: center;">
	<input id="name" type="text" placeholder="请输入昵称" style="width:100%;height:50px;">
	<input type="button" value="提交 " onclick="submit()" style="witdh:100px;height:50px;margin:20px auto;"/>
	</div>
</body>
</html>
