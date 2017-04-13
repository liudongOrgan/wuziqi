function sendPost(url, data, callback) {
	$.ajax({
	    url : url,
	    type : 'POST', // GET
	    async : false, // 或false,是否异步
	    data : data,
	    // dataType : 'json', //返回的数据格式：json/xml/html/script/jsonp/text
	    success : function(data, textStatus, jqXHR) {
		    callback(data);
	    },
	    error : function(xhr, textStatus) {
		    alert("通信出现错误！");
		    console.log('错误');
		    console.log(xhr);
		    console.log(textStatus);
		    window.location.reload();
	    }
	});
}

function checkAndKeep() {
	var url = checkAndKeepUrl;
	if (null == url || "" == url) {
		return;
	}
	var data = {};
	var callback = function(data) {
		if (null == data || null == data['status'] || 'fail' == data['status']) {
			alert("出现错误,即将跳转!");
			window.location.href = roomListUrl;
			return;
		}
	}
	sendPost(url, data, callback);
	setTimeout("checkAndKeep()", 5 * 60 * 1000); // 5分钟发送一次
}




//显示加载器  
function showTip(text) {  
    if(null == text){
    	text = "加载中"
    } 
    $.mobile.loading('show', {  
        text: text, //加载器中显示的文字  
        textVisible: true, //是否显示文字  
        theme: 'a',        //加载器主题样式a-e  
        textonly: true,   //是否只显示文字  
        html: ""           //要显示的html内容，如图片等  
    });
    setTimeout("$.mobile.loading('hide')", 2000);
}  












