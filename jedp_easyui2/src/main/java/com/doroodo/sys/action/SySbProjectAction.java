package com.doroodo.sys.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.doroodo.base.action.BaseAction;
import com.doroodo.config.SysVal;
import com.doroodo.base.util.excelTools.Excel;
import com.doroodo.base.util.excelTools.Table;

import com.doroodo.base.model.*;
import com.doroodo.sys.model.*;
import com.doroodo.sys.service.*;

@Controller
@ParentPackage(value="sys")   
@InterceptorRef("mydefault")
public class SySbProjectAction extends BaseAction{
	@Autowired
	private SySbProjectService sySbProjectService;
	private SySbProject sySbProject;
	private String EXCEL_TITLE = "";//请输入导出的excel表表名
	private String tableHtml="";
	private String tableTitle="";
	public String getTableHtml() {
		return tableHtml;
	}

	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	public void setTableHtml(String tableHtml) {
		this.tableHtml = tableHtml;
	}
	
	public SySbProject getSySbProject(){
		return sySbProject;
	}
	
	public void setSySbProject(SySbProject sySbProject){
		this.sySbProject=sySbProject;
	}
	
	@Action("/sys/sySbProject_Add")
	public void sySbProjectAdd(){
		sySbProjectService.saveOrUpdate(sySbProject);
		if(sySbProject.getId()!=null){
			Map m=new HashMap();
			m.put("info", SysVal.ADD_SUC);
			m.put("flowId", sySbProject.getId());
			this.writeJson(m);
		}else{
			writeMsg(SysVal.ADD_ER);
		}
	}
	
	@Action("/sys/sySbProject_Add_HasFiles")
	public void sySbProjectAddHasFiles(){
		sySbProjectService.saveOrUpdate(sySbProject);
		Map m=new HashMap();
		if(sySbProject.getId()!=null){
			m.put("info", SysVal.ADD_SUC);
			m.put("flowId", sySbProject.getId());
			m.put("fileid", "sySbProject-"+sySbProject.getId());
			this.writeJson(m);
		}else{
			m.put("info", SysVal.ADD_ER);
			this.writeJson(m);
		}
	}
	
	@Action("/sys/sySbProject_Delete_HasFiles")
	public void sySbProjectDeleteHasFiles(){
		if(this.getIds().trim()=="")return;
		sySbProjectService.delete(this.getIds());
		Map m=new HashMap();
		m.put("info", SysVal.DEL_SUC);
		String[] ids=this.getIds().split(",");
		String fileids="";
		for(int i=0;i<ids.length;i++){
			String fileid=ids[i];
			if(!fileid.isEmpty()){
				fileids+= "sySbProject-"+fileid+",";
			}
		}
		m.put("fileids", fileids);
		this.writeJson(m);
	}
	
	@Action("/sys/sySbProject_List")
	public void sySbProjectList(){
		if(sySbProject!=null){
			this.writeJson(sySbProjectService.dataGrid(this.getPage(), this.getRows(),sySbProject));
		}else{
			this.writeJson(sySbProjectService.dataGrid(this.getPage(), this.getRows(), this.getSearchName(), this.getSearchKey()));
		}
	}
	
	@Action("/sys/sySbProject_List_All")
	public void sySbProjectListAll(){
		this.writeJson(sySbProjectService.listAll());
	}
	
	@Action("/sys/sySbProject_ComboBox")
	public void sySbProjectComboBox(){
		List<SySbProject> l = sySbProjectService.listAll();
		List<comboBox> l_=new ArrayList<comboBox>();
		for(int i=0;i<l.size();i++){
			comboBox cb = new comboBox();
			SySbProject obj=l.get(i);
			cb.setId(obj.getId().toString());
			cb.setText(obj.getId().toString());//请按需修改!
			l_.add(cb);
		}
		this.writeJson(l_);
	}
	
	@Action("/sys/sySbProject_Delete")
	public void sySbProjectDelete(){
		if(this.getIds().trim()=="")return;
		sySbProjectService.delete(this.getIds());
		writeMsg(SysVal.DEL_SUC);
	}
	
	@Action("/sys/sySbProject_Update")
	public void sySbProjectUpdate(){
		if(sySbProject!=null) sySbProjectService.saveOrUpdate(sySbProject);
		writeMsg(SysVal.EDIT_SUC);
	}
	
	@Action("/sys/sySbProject_Excel")
	public void sySbProjectExcel(){
		List<Object> list = new ArrayList();
		if(this.getExcelExportAll().equalsIgnoreCase("false")){
			list = sySbProjectService.dataGrid(this.getPage(), this.getRows(), this.getSearchName(), this.getSearchKey()).getRows();//获取数据
		}else if(this.getExcelExportAll().equalsIgnoreCase("true")){
			List l = sySbProjectService.listAll();//获取数据
			list=l;
		}
		super.excel(EXCEL_TITLE,list);
	}
	
	@Action("/sys/sySbProject_FormFile")
	public void sySbProjectFormFile(){
		getSession().put("tableHtml", tableHtml); 
		getSession().put("tableTitle", tableTitle); 
	}
	
	@Action("/sys/sySbProject_Upload")
	public void sySbProjectUpload() throws IOException{
		String msg=SysVal.UPDATA_SUC;
		List<File> fileGroup=this.getFileGroup();
		List<String> fileGroupFileName=this.getFileGroupFileName();
		if(fileGroup==null||fileGroupFileName==null){this.writeMsg(SysVal.NOFILE); return;}
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");//设置日期格式
		try {
			Map< String, String> map=this.uploadFiles(fileGroup,fileGroupFileName,"");
			for(int i=0;i<fileGroupFileName.size();i++){
				SyFile syFile_=new SyFile();
				String fileName=fileGroupFileName.get(i);
				String sysName=map.get(fileName);
				String createTime=df.format(new Date());
				syFile_.setCreatetime(createTime);
				syFile_.setFilename(fileName);
				syFile_.setSysname(sysName);
				syFile_.setUserid(this.getLoginUserId());
				syFileService.saveOrUpdate(syFile_);
				Excel e = new Excel(fileName,new FileInputStream(fileGroup.get(i)));
				Table t=e.getSheet(0).getAsTable();
				String[] ps=t.getHeader();
				sySbProject=new SySbProject();
				 for(int ei=0;ei<t.getRowSize();ei++){//读出行
					 for(int ej=0;ej<t.getColSize();ej++){//读出列
						 setPValue(sySbProject,ps[ej],t.getCell(ei,ej));
					 }
					 sySbProject.setId(null);
					 sySbProjectService.saveOrUpdate(sySbProject);
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg=SysVal.UPDATA_ER;
		}finally{
			writeMsg(msg);
		}
	}
	
	private void setPValue(SySbProject sySbProject,String p,String v){
		Field field = null;
		try {
			field = sySbProject.getClass().getDeclaredField(p);
			field.setAccessible(true);  
			if("java.lang.integer".equalsIgnoreCase(field.getType().getName())){
				field.set(sySbProject,Float.valueOf(v).intValue());
			}else{
				field.set(sySbProject,v);  
			}
		} catch (Exception e) {
			System.out.println("++++++++++++++++++++您需要转下类型，doroodo平台缺少您说的这个类型，烦请加下，或者联系下维护人员!++++++++++++++++++++");
			e.printStackTrace();
		}
	}
	
	@Action("/sys/sySbProject_Get_ById")  
	public void sySbProjectGetById(){
		SySbProject sySbProject=new SySbProject();
		sySbProject.setId(Integer.parseInt(this.getId()));
		try{
		sySbProject=sySbProjectService.get(sySbProject).get(0);
		if(sySbProject==null){
			this.writeMsg(SysVal.GET_ER);
			return;
		}else{
			this.writeJson(sySbProject);
		}}
		catch(Exception e){
			this.writeMsg(SysVal.GET_ER);
		}
	}
	
	@Action("/sys/sySbProject_Get_ByObj")  
	public void sySbProjectByObj(){
		this.writeJson(sySbProjectService.get(sySbProject));
	}
	
	//检查字段是否唯一
	private String isSingle(SySbProject sySbProject,String fieldName,String fieldValue){
		String result=null;
		List<SySbProject> lsList = sySbProjectService.get(sySbProject);
		if(sySbProjectService.get(sySbProject).size()>0) {
			result=fieldName+"["+fieldValue+"]"+SysVal.READHASE;
			return result;
		}
		return result;
	}

}
