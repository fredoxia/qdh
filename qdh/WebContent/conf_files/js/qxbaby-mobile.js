
function renderPopup(errorHeader, msg){
	  if (errorHeader != ""){
		  $("#errorHeader").html(errorHeader);
	  } else 
		  $("#errorHeader").html("提示信息");
	  $("#errorMsg").html(msg);
	  $("#msgPopup").popup("open");
}
