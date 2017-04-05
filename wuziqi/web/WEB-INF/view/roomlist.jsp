<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
})
.on( "click", ".hide-page-loading-msg", function() {
	$.mobile.loading( "hide" );
});
</script>
</head>
<body>
<div data-role="page" class="">
	<div data-role="panel" id="panel" data-position="left"  data-display="overlay" >
		 <ul data-role="listview"  >
		    <li id="skull"><a href="#">创 建 房 间 </a></li>
		    <li id="skull"><a href="#">个 人 信 息</a></li>
		</ul>
	</div>
	<div data-role="header">
      <a data-iconpos="notext" href="#panel" data-role="button" data-icon="grid"></a>
      <h1>房 间 列 表</h1>
      <a data-iconpos="notext" data-role="button" data-icon="home" title="Home">Home</a>
    </div>
    
    <div data-role="content" role="main">
    	 
		<br/>
		<div data-theme="a" data-form="ui-body-a" class="ui-body ui-body-a ui-corner-all">
		    <div style="height:50px;">
		    	<div style="float:left">
		    		<a href="#" class="ui-btn ui-icon-recycle ui-btn-icon-notext ui-corner-all"  data-icon="recycle" data-textonly="false" data-textvisible="false" data-msgtext="" data-inline="true">No text</a>
					<div data-demo-html="true" data-demo-js="true">
				<button class="show-page-loading-msg" data-textonly="false" data-textvisible="false" data-msgtext="" data-inline="true">Icon (default)</button>
				<button class="show-page-loading-msg" data-textonly="false" data-textvisible="true" data-msgtext="" data-inline="true">Icon + text</button>
				<button class="show-page-loading-msg" data-textonly="true" data-textvisible="true" data-msgtext="Text only loader" data-inline="true">Text only</button>
				<button class="hide-page-loading-msg" data-inline="true" data-icon="delete">Hide</button>

			</div>
		        </div>
		        <div style="float:right;line-height:50px;">
		            	正在游戏中
				</div>		    
		    </div>
			<p>房间名:房间1</p>
			<p>
				玩家1: 你好&nbsp;&nbsp;玩家2:你好2
			</p>
			<button class="ui-btn ui-corner-all">进 入 房 间</button>
		</div>
		<br/>
		<div data-theme="a" data-form="ui-body-a" class="ui-body ui-body-a ui-corner-all">
		    <div style="height:50px;">
		    	<div style="float:left">
		    		<a href="#" class="ui-btn ui-icon-recycle ui-btn-icon-notext ui-corner-all"  data-icon="recycle" >No text</a>
		        </div>
		        <div style="float:right;line-height:50px;">
		            	正在游戏中
				</div>		    
		    </div>
			<p>房间名:房间1</p>
			<p>
				玩家1: 你好&nbsp;&nbsp;玩家2:你好2
			</p>
			<button class="ui-btn ui-corner-all">进 入 房 间</button>
		</div>
		<br/>
		<div data-theme="a" data-form="ui-body-a" class="ui-body ui-body-a ui-corner-all">
		    <div style="height:50px;">
		    	<div style="float:left">
		    		<a href="#" class="ui-btn ui-icon-recycle ui-btn-icon-notext ui-corner-all"  data-icon="recycle" >No text</a>
		        </div>
		        <div style="float:right;line-height:50px;">
		            	正在游戏中
				</div>		    
		    </div>
			<p>房间名:房间1</p>
			<p>
				玩家1: 你好&nbsp;&nbsp;玩家2:你好2
			</p>
			<button class="ui-btn ui-corner-all">进 入 房 间</button>
		</div>
		<br/>

    </div>
</div>
</body>

</html>