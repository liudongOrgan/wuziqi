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