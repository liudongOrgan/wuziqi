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