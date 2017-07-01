$(function () {

	$("#confirm").on("click",function(){
		var files =  $("input").eq(0).val();
		if(files==""){
			alert("请先上传文件");
		}
	});
	$("#cancel").on("click",function(){
	//	webform.close();
	});

	$("#fileUpload").change(function(){ 
		var val = $(this).val();
		var fileName  = val.split("\\")[val.split("\\").length-1];
		if (val.indexOf("xls") != -1 || val.indexOf("xlsx") != -1) {
			$("input").eq(0).val(fileName);
	
			$('#formFile').ajaxSubmit({
				url:'excel_targetFile.action',
				type:'POST',
			//	data:{scmd:''}, 需要传什么参数自己配置
				success:function(data){

					$("#confirm").on("click",function(){

					
						alert("导入成功");
					});
					$("#cancel").on("click",function(){

						alert("取消导入");
					});
				},
				error:function(xhr){
				      alert("上传出错");
				}                              
			});

		} else {
			alert("请选择正确的文件格式！");
			//清空上传路径
			$(this).val("");
			return false;
		}

	});



	
	
})