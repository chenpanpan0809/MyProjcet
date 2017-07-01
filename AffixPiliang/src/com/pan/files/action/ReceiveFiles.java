package com.pan.files.action;

import java.io.File;

import org.apache.jasper.tagplugins.jstl.core.Out;

public class ReceiveFiles {
		private static final long serialVersionUID = 1L;

		private File  files; 			// 用户上传的文件 这里名字需跟前台控件的name一致
		private String filesFileName; 	// 上传文件的文件名
		private String filesContentType;	// 上传文件的类型
		private String   filesDescription;	// 用户的描述
	
		
		public void execute(){
		File sPath = this.getFiles();
			
		
		}
		
		public File getFiles() {
			return files;
		}

		public void setFiles(File files) {
			this.files = files;
		}

		public String getFilesFileName() {
			return filesFileName;
		}

		public void setFilesFileName(String filesFileName) {
			this.filesFileName = filesFileName;
		}

		public String getFilesContentType() {
			return filesContentType;
		}

		public void setFilesContentType(String filesContentType) {
			this.filesContentType = filesContentType;
		}

		public String getFilesDescription() {
			return filesDescription;
		}

		public void setFilesDescription(String filesDescription) {
			this.filesDescription = filesDescription;
		}
		
	
}
