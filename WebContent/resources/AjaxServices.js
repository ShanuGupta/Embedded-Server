var url = /sg/hello.js;
var successCB = function(data){
	document.write(data);
};
var failureCB = function(error){
	console.log("Unable to get data from the service");
};
var requestConfig = {
		dataType : json,
		url : url,
		asyn : true,
		type : "GET"
};
var promise = $.ajax(requestConfig);
promise.done(successCB);
promise.fail(failureCB);