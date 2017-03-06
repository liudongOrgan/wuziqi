<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/static/js/jquery-3.1.1.min.js"></script>
<style type="text/css">
* {
	box-sizing: border-box;
}

.box {
	width: 80%;
	height: 300px;
	border: 1px solid black;
	margin: 10px auto;
	white-space: nowrap;
	font-size: 30px;
}

.box .roome_name {
	float: left;
	width: 100%;
	height: 20%;
}

.box .status {
	top: 0px;
	display: inline-block;
	width: 49%;
	height: 60%;
	float: left;
}

.box .humber {
	top: 0px;
	width: 49%;
	height: 60%;
	float: right;
}

.box .contorl {
	width: 100%;
	float: left;
	text-align: right;
}

.box .contorl input {
	height: 55px;
	width: 200px;
	font-size: 20px;
}

#rooomList {
	width: 100%;
}

.btn {
	height: 55px;
	width: 200px;
}
</style>

<script type="text/javascript">


	function enterRoom(us) {
		var roomname = $(us).parent().find("#roomName").val();
		var data = {
			"roomname" : roomname
		};
		var url = "enterroom";
		var callback = function(data) {
			if ("success" == data.status) {
				alert("进入房间成功！");
				loadCreateRoom();
			} else {
				alert("进入房间失败！");
			}
		};
		sendPost(url, data, callback);
	}

	function backRoom(us) {
		var roomname = $(us).parent().find("#roomName").val();
		var data = {
			"roomname" : roomname
		};
		var url = "backroom";
		var callback = function(data) {
			if ("success" == data.status) {
				alert("退出房间成功！");
				loadCreateRoom();
			} else {
				alert("退出房间失败！");
			}
		};
		sendPost(url, data, callback);
	}

	$(function() {
		if (false == initUserName()) {
			createUserName();
		}
		loadRoomList();
		loadCreateRoom();
		checkStart();
	});

	var user = null;
	function initUserName() {
		var created = false;
		var data = {
		};
		var url = "getuserinfo";
		var callback = function(data) {
			if (null == data || null == data['content']) {
				created = false;
			} else {
				user = data['content'];
				$("#username").html(user['userName']);
				created = true;
				$("#createusername").hide();
			}
		};
		sendPost(url, data, callback);
		return created;
	}

	function createUserName() {
		var name = prompt("给自己起一个响亮的大名吧:");
		if (null == name || '' == name) {
			alert("不能为空哦,请重试！");
			return;
		}
		var data = {
			"username" : name
		};
		var url = "createusername";
		var callback = function(data) {
			if ("created" == data['status']) {
				alert("已经创建昵称！");
			}
			if ("exists" == data['status']) {
				alert("该昵称已被别人占用！");
			}
			if ("success" == data['status']) {
				alert('创建昵称成功')
				initUserName();
			}
		};
		sendPost(url, data, callback);

	}



	function loadCreateRoom() {
		var url = "getcreatedroom";
		var data = {};
		var callback = function(data) {
			$("#myRoom").html('');
			if (null == data || null == data.content) {
				return;
			}
			var content = data['content'];

			var dom = getRoomDomByRoom(content);
			$("#myRoom").append(dom);
			dom.show();
			$(dom).find(".enter").hide();
			$(dom).find(".back").show();

		};
		sendPost(url, data, callback);
	}

	function loadRoomList() {
		var url = "loadroomlist";
		var data = {};
		var callback = function(data) {
			$("#rooomList").html('');
			if (null == data || null == data.content || 0 == data.content.length) {
				return;
			}
			var content = data['content'];
			for (var i in content) {
				var item = content[i];
				var dom = getRoomDomByRoom(item);
				$("#rooomList").append(dom);
				dom.show();
				$(dom).find(".back").hide();
				$(dom).find(".enter").show();
			}
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
		var url = "createroom";
		var callback = function(data) {
			if ("created" == data['status']) {
				alert("已经创建房间！");
			}
			if ("exists" == data['status']) {
				alert("房间名称重复请重试！");
			}
			if ("success" == data['status']) {
				var dom = getRoomDomByRoom(data['content']);
				$("#myRoom").append(dom);
				dom.show();
				$(dom).find(".enter").hide();
			}
		};
		sendPost(url, data, callback);
		loadRoomList();
		loadCreateRoom();
	}

	function getRoomDomByRoom(room) {
		var dom = $("#roomTpl").clone();
		var status = [ '等待加入', '等待开始', '进行中' ];
		var stval = status[room.status];
		var roomName = room['roomeName'];
		var user1 = room['user1Name'];
		var user2 = room['user2Name'];
		$(dom).find(".roomname").html(roomName);
		$(dom).find(".status").html(stval);
		$(dom).find(".peo1").html(user1);
		$(dom).find(".peo2").html(user2);
		$(dom).find("#roomName").val(roomName);
		return dom;
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
	
	
	function checkStart(){
		var data = {
		};
		var url = "checkstart";
		var callback = function(data) {
			if ("success" == data.status) {
				alert("游戏开始！");
				window.location.href = "wuziqi";
				return;
			}
			loadCreateRoom();
		};
		sendPost(url, data, callback);
		
		setTimeout("checkStart()",1000);
	}
</script>

</head>

<body>
	<div>
		<input type="button" value="创建房间" onclick="createRoom()"
			style="margin:auto;width:200px; height:55px;" /> <input
			id="createusername" class="btn" onclick="createUserName()"
			type="button" value="创建昵称" /> <span id="username"></span>
	</div>
	已创建房间列表：
	<div id="myRoom"></div>
	<br> 系统房间列表：
	<div id="rooomList"></div>
	<div id="roomTpl" class="box" style="display:none;">
		<div class="room_name">
			房间名称:<span class="roomname"></span>
		</div>
		<div class="status">进行中</div>
		<div class="humber">
			<p class="p1">
				玩家1：<span class="peo1"></span>
			</p>
			<p class="p2">
				玩家2：<span class="peo2"></span>
			</p>
		</div>
		<div class="contorl">
			<input class="enter" type="button" value="进入"
				onclick="enterRoom(this)" /> <input onclick="backRoom(this)"
				class="back" type="button" value="退出"> <input id="roomName"
				value="" type="hidden" />
		</div>
	</div>
</body>




</html>
