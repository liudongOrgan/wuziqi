<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en"><head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="/static/js/jquery-3.1.1.min.js"></script>
	<title>五子棋</title>
	<style type="text/css">
		*{
			margin:0;
			padding:0;
			-moz-user-select:none;/*火狐*/
			-webkit-user-select:none;/*webkit浏览器*/
			-ms-user-select:none;/*IE10*/
			-khtml-user-select:none;/*早期浏览器*/
			user-select:none;
		}
		.gobang{
			margin:10px auto;
			width:642px;
			height: 642px;
			/*border:1px solid;*/
			background: url(/static/picture/bak.jpg);
			overflow: hidden;
		}
		.text{
			margin:0 auto;
			width:100px;
			height:40px;
			text-align: center;
			color:#f00;
			border:1px solid red;
			line-height: 40px;
			display: block;
		}
		#can{
			margin:0px auto;
			border:1px solid green;
			display: block;
		}
	</style>
</head>
<body>

<canvas class="text">PK</canvas>
<div class="gobang">
 	<canvas id="can" width="640" height="640" style="position:absolute;z-index:1;">
		您的浏览器不支持canvas
	</canvas>
	<img id="hoverImg" src="/static/picture/hover.png" style="position:absolute;" />
</div>
<script>
	function playSound(){
		var audio = '<audio id="bgMusic" src="/static/sound/sound.mp3" style="display:none;" autoplay="autoplay"></audio>';
		$("audio").each(function(){
			$(this).remove();
		});
		$("body").append(audio);
		var bgMusic = document.getElementById("bgMusic");
		bgMusic.play();
	}


	var text = document.getElementsByClassName('text');



	//定义二维数组作为棋盘
	var maps = new Array(16);
	var len = maps.length;
	// alert(len)
	for (var i = 0; i < len; i++) {
		maps[i] = new Array();
		for (var j = 0; j < len; j++) {
			maps[i][j] = 0;
		// console.log(maps[i][j]);
		}
	}

	//初始化棋子
	var black = new Image();
	var white = new Image();
	var clientWidth = document.documentElement.clientWidth;
	black.src = "/static/picture/black.png";
	white.src = "/static/picture/white.png";
	//棋盘初始化
	var can = document.getElementById('can');
	var ctx = can.getContext("2d"); //获取该canvas的2D绘图环境对象
	ctx.strokeStyle = "#333";
	for (var m = 0; m < len - 1; m++) {
		for (var n = 0; n < len - 1; n++) {
			ctx.strokeRect(m * 40 + 20, n * 40 + 20, 40, 40); //绘制40的小正方形
		}
	}
	//绘制文字
	var can1 = document.getElementsByClassName('text');
	var ctx1 = can1[0].getContext("2d");

	ctx1.beginPath();
	ctx1.font = ("100px Georgia");
	ctx1.fillStyle = "#F70707";
	// ctx1.fillText("Hello",40,100);

	var isBlack = true;
	var select = {
		isSelcted : false,
		x : 0,
		y : 0
	};
	function hover(obj, e) {
		//获取棋盘偏移量
		var l = obj.offsetLeft + 20;
		var t = obj.offsetTop + 20;
		//获取点击相对棋盘坐标
		var x = e.clientX - l;
		var y = e.clientY - t;
		var row,
			col,
			index = 0;
		if (x % 40 < 20) {
			col = parseInt(x / 40);
		} else {
			col = parseInt(x / 40) + 1;
		}
		row = y % 40 < 20 ? parseInt(y / 40) : parseInt(y / 40) + 1;
		var left = (col - 1) * 40 + 20 + l + 2; // 左边到棋盘20px  hover图标宽36px
		var top = (row - 1) * 40 + 20 + t + 2;
		$("#hoverImg").css("top", top);
		$("#hoverImg").css("left", left);
	}

	//下子
	can.onclick = function play(e) {
		// alert(e.clientX);
		//获取棋盘偏移量
		var l = this.offsetLeft + 20;
		var t = this.offsetTop + 20;
		//获取点击相对棋盘坐标
		var x = e.clientX - l;
		var y = e.clientY - t;
		// alert(x);
		var row,
			col,
			index = 0;

		if (x % 40 < 20) {
			col = parseInt(x / 40);
		} else {
			col = parseInt(x / 40) + 1;
		}
		row = y % 40 < 20 ? parseInt(y / 40) : parseInt(y / 40) + 1;
		if (true == select.isSelcted && row == select.x && col == select.y) {
			select.isSelcted = false;
		} else {
			select.isSelcted = true;
			select.x = row;
			select.y = col;
			hover(this, e);
			return;
		}
		playSound();

		if (maps[row][col] === 0) {
			if (isBlack) {
				ctx.drawImage(black, col * 40, row * 40); //下黑子
				isBlack = false;
				maps[row][col] = 2; //黑子为2
				iswin(2, row, col);
			} else {
				ctx.drawImage(white, col * 40, row * 40);
				isBlack = true;
				maps[row][col] = 1; //白子为1
				iswin(1, row, col);
			}
		}
	}
	function iswin(t, row, col) {
		var orgrow,
			orgcol,
			total;
		reset();
		// alert(total);

		//判断每行是否有五个
		while (col > 0 && maps[row][col - 1] == t) { //当前子左边还有
			total++;
			col--;

		}
		;
		row = orgrow;
		col = orgcol;
		while (col + 1 < 16 && maps[row][col + 1] == t) { //当前子右边还有
			col++;
			total++;
		}
		;
		// alert(total);
		celebrate();

		//判断每列是否有五个
		reset();

		while (row > 0 && maps[row - 1][col] == t) { //当前子上面还有
			total++;
			row--;
		}
		row = orgrow;
		col = orgcol;
		while (row + 1 < 16 && maps[row + 1][col] == t) { //下面
			total++;
			row++;
		}
		celebrate();

		//左上 右下有没有五个
		reset();
		while (row > 0 && col > 0 && maps[row - 1][col - 1] == t) { //左上1
			row--;
			col--;
			total++;
		}
		row = orgrow;
		col = orgcol;
		while (row + 1 < 16 && col + 1 < 16 && maps[row + 1][col + 1] == t) { //右下1
			row++;
			col++;
			total++;
		}
		// alert(total)
		celebrate();

		//左下 右上有没有五个
		reset();
		// alert(total);
		while (row > 0 && col + 1 < 16 && maps[row - 1][col + 1] == t) { //右上
			row--;
			col++;
			total++;
		}
		row = orgrow;
		col = orgcol;
		while (row + 1 < 16 && col > 0 && maps[row + 1][col - 1] == t) { //左下
			row++;
			col--;
			total++;
		}
		// alert(total);
		celebrate();

		function celebrate() { //显示哪边赢
			if (total >= 5) {
				if (t == 1) {
					// alert("白子赢");
					// text[0].innerHTML="白子赢";
					// cxt1.clearRect(0,0,can1.width,can1.height);
					ctx1.clearRect(0, 0, can1[0].width, can1[0].height);
					ctx1.fillText("白子赢", 0, 100);
				} else {
					// alert("黑子赢");
					// text[0].innerHTML="黑子赢";
					// cxt1.clearRect(0,0,can1.width,can1.height);
					ctx1.clearRect(0, 0, can1[0].width, can1[0].height);
					ctx1.fillText("黑子赢", 0, 100);
				}
			}
		}
		function reset() {
			orgrow = row;
			orgcol = col;
			total = 1;
		}
	}
</script>


</body></html>