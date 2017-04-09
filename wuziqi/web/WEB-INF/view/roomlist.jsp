<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="target.jsp"></jsp:include>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" href="${ctx }/static/picture/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="${ctx }/static/picture/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" href="${ctx }/static/jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.css">
<script type="text/javascript" src="${ctx }/static/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript"  src="${ctx }/static/jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.js"></script>
<script type="text/javascript"  src="${ctx }/static/js/common.js"></script>
<style>
#panel .ui-icon-carat-r:after{
	display:none !important;
}
.roomtable{width:100%}
.roomtable td{width:50%}
</style>

<script>
$( document ).on( "click", ".show-page-loading-msg", function() {
	var $this = $( this ),
		theme = $this.jqmData( "theme" ) || $.mobile.loader.prototype.options.theme,
		msgText = $this.jqmData( "msgtext" ) || $.mobile.loader.prototype.options.text,
		textVisible = $this.jqmData( "textvisible" ) || $.mobile.loader.prototype.options.textVisible,
		textonly = !!$this.jqmData( "textonly" );
		html = $this.jqmData( "html" ) || "";
	$.mobile.loading( "show", {
			text: msgText,
			textVisible: textVisible,
			theme: theme,
			textonly: textonly,
			html: html
	});
}).on( "click", ".hide-page-loading-msg", function() {
	$.mobile.loading( "hide" );
});
$(function(){
	loadRoomList();
});
function loadRoomList() {
	var url = "${ctx}/loadroomlist";
	var data = {};
	var callback = function(data) {
		$("#roomContent").html('<br>');
		if (null == data || null == data.content || 0 == data.content.length) {
			return;
		}
		var content = data['content'];
		for (var i in content) {
			var item = content[i];
			var dom = getRoomDomByRoom(item);
			$("#roomContent").append(dom);
			$("#roomContent").append('<br>');
			dom.show();
		}
	};
	sendPost(url, data, callback);
}
function getRoomDomByRoom(room) {
	var dom = $("#roomTmp").clone();
	var status = ['', '等待加入', '正在游戏中'  ];
	var stval = status[room.status];
	var roomName = room['roomeName'];
	var user1 = room['user1Name'];
	var user2 = room['user2Name'];
	var createDate = room['createDate'];
	$(dom).find("#roomName").html(roomName);
	$(dom).find("#gameStatus").html(stval);
	$(dom).find("#user1Name").html(user1);
	$(dom).find("#user2Name").html(user2);
	$(dom).find("#createDate").html(createDate);
	$(dom).find("#roomId").val(roomName);
	$(dom).attr("id", roomName);
	return dom;
}
function loadCreatedRoom() {
	return;
	var url = "${ctx}/getcreatedroom";
	var data = {};
	var callback = function(data) {
		$("#roomContent").html('<br>');
		if (null == data || null == data.content) {
			return;
		}
		var content = data['content'];

		var dom = getRoomDomByRoom(content);
		$("#roomContent").append(dom);
		$("#roomContent").append("<br>");
		dom.show(); 
	};
	sendPost(url, data, callback);
}

function createRoom() {
	var name = prompt("请输入房间名:");
	if (null == name || '' == name) {
		alert("输入名称不能为空,请重试！");
		return;
	}
	var data = {
		"roomName" : name
	};
	var url = "${ctx}/createroom";
	var callback = function(data) {
		if ("created" == data['status']) {
			alert("已经创建房间！即将进入房间");
			window.location.href = "${ctx}/wuziqi";
		}
		if ("exists" == data['status']) {
			alert("房间名称重复请重试！");
		}
		if ("success" == data['status']) {
			window.location.href = "${ctx}/wuziqi";
		}
	};
	sendPost(url, data, callback);
}
function enterRoom(us) {
	var roomname = $(us).parent().find("#roomId").val();
	var data = {
		"roomname" : roomname
	};
	var url = "${ctx}/enterroom";
	var callback = function(data) {
		if ("success" == data.status) {
			alert("进入房间成功！");
			window.location.href = "${ctx}/wuziqi";
		} else {
			alert("进入房间失败！");
			window.location.reload();
		}
	};
	sendPost(url, data, callback);
}

function exitSystem(){
	var data = {};
	var url = "${ctx}/exit";
	var callback = function(){
		alert('操作成功!');
		window.location.href = "${ctx}/login/page";
	}
	sendPost(url, data, callback);
}

function alertUserName(){
	var data = {};
	var url = "${ctx}/getuserinfo";
	var callback = function(data) {
		if (null == data || null == data['content']) {
		} else {
			user = data['content'];
			alert("昵称：" + user['userName']);
		}
	};
	sendPost(url, data, callback);
}
</script>
</head>
<body>
<div data-role="page" class="">
	<div data-role="panel" id="panel" data-position="left"  data-display="overlay" >
		 <ul data-role="listview"  >
		    <li id="skull"><a href="javascript:createRoom()">创 建 房 间 </a></li>
		    <li id="skull"><a href="javascript:alertUserName()">个 人 信 息</a></li>
		    <c:if test="${room != null}">
		    	<li id="skull"><a href="#" onclick="window.location.href = '${ctx}/wuziqi'">进入游戏中房间</a></li>
		    </c:if>
		    <li id="skull"><a href="javascript:exitSystem()">退&nbsp;&nbsp;出</a></li>
		</ul>
	</div>
	<div data-role="header">
      <a data-iconpos="notext" href="#panel" data-role="button" data-icon="grid"></a>
      <h1>房 间 列 表</h1>
      <a data-iconpos="notext" onclick="window.location.reload();" data-role="button" data-icon="recycle" title="Home">Home</a>
    </div>
    	    <div data-demo-html="true" data-demo-js="true" style="display:none">
				<button class="show-page-loading-msg" data-textonly="false" data-textvisible="false" data-msgtext="" data-inline="true">Icon (default)</button>
				<button class="show-page-loading-msg" data-textonly="false" data-textvisible="true" data-msgtext="" data-inline="true">Icon + text</button>
				<button class="show-page-loading-msg" data-textonly="true" data-textvisible="true" data-msgtext="Text only loader" data-inline="true">Text only</button>
				<button class="hide-page-loading-msg" data-inline="true" data-icon="delete">Hide</button>
			</div>
		<div id="roomTmp" style="display:none;" data-theme="a" data-form="ui-body-a" class="ui-body ui-body-a ui-corner-all">
		    <div style="height:50px;">
		    	<div style="float:left">
		    		<a href="#" class="ui-btn ui-icon-recycle ui-btn-icon-notext ui-corner-all"  data-icon="recycle" data-textonly="false" data-textvisible="false" data-msgtext="" data-inline="true">No text</a>
		        </div>
		        <div style="float:right;line-height:50px;"> <span id="gameStatus"></span> </div>		    
		    </div>
		    <table class="roomtable">
		    	<tr> 
		    		<td>房间名：<span id="roomName"></span></td>
		    		<td>创建时间：<span id="createDate"></span></td>
		    	</tr>
		    	<tr>
		    		<td>玩家1：<span id="user1Name"></span></td>
		    		<td>玩家2：<span id="user2Name"></span></td>
		    	</tr>
		    </table>
		 
			<input type="hidden" value="" id="roomId" />
			<button class="ui-btn ui-corner-all" onclick="enterRoom(this)">进 入 房 间</button>
		</div>
			
    <div id="roomContent" data-role="content" role="main">
		 <br/>
    </div>
</div>
</body>

</html>