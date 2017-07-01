/**
 * 
 */
var i=0;
var j=0;
$(function(){
	
	$("#fileMutiply").change(function eventStart(){
		var ss =this.files; //获取当前选择的文件对象
		 for(var m=0;m<ss.length;m++){  //循环添加进度条
				 
 			    efileName = ss[m].name ;
 		if (ss[m].size> 1024 * 1024){
 		   sfileSize = (Math.round(ss[m].size /(1024 * 1024))).toString() + 'MB';
			}
		else{
			sfileSize = (Math.round(ss[m].size/1024)).toString() + 'KB';
			}
 	
 			
 		$("#test").append(
 		"<li  id="+m+"file><div class='progress'><div id="+m+"barj class='progressbar'></div></div><span class='filename'>"+efileName+"</span><span id="+m+"pps class='progressnum'>"+(sfileSize)+"</span></li>");
	 
				 }
		 sendAjax();
		  function sendAjax() {  
		  		if(j>=ss.length)   //采用递归的方式循环发送ajax请求
		        {  
		  		$("#fileMutiply").val("");
		  		     j=0;
		            return;  
		        }
		  	 var formData = new FormData();
		  	 formData.append('files', ss[j]);  //将该file对象添加到formData对象中
					$.ajax({
						url:'fileUpLoad.action',
						type:'POST',
						cache: false,
						data:{},//需要什么参数，自己配置
						data: formData,//文件以formData形式传入
					    processData : false, 
					   //必须false才会自动加上正确的Content-Type 
					   contentType : false , 
					/*   beforeSend:beforeSend,//发送请求
					   complete:complete,//请求完成
		*/  		  xhr: function(){      //监听用于上传显示进度
						    var xhr = $.ajaxSettings.xhr();
						    if(onprogress && xhr.upload) {
						     xhr.upload.addEventListener("progress" , onprogress, false);
						     return xhr;
						    }
						   } ,
		                success:function(data){
		             
		                
		                	$(".filelist").find("#"+j+"file").remove();//移除进度条样式
		                 	j++; //递归条件
		                	sendAjax();
		                	
		           //     }
		                	
		                },
		                error:function(xhr){
		                  alert("上传出错");
		                }                              
		            });
				
		  	
		  	  } 
		
	})
	
	
  	 function onprogress(evt){
 		
		  var loaded = evt.loaded;     //已经上传大小情况 
		  var tot = evt.total;      //附件总大小 
		  var per = Math.floor(100*loaded/tot);  //已经上传的百分比 
	     $(".filelist").find("#"+j+"pps").text(per +"%");
		 $(".filelist").find("#"+j+"barj").width(per+"%");
		 };
	
	
})