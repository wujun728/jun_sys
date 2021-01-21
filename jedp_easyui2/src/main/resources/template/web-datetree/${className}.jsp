<#include "/macro.include"/>
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/include/taglibs.jsp"%>
<%@ include file="/include/meta.jsp"%>
<script id="pjs" type="text/javascript" src="${gloabpath?replace("/","")}/js/page/${className}.js"></script>
<script>
var ${classNameLower}Id="i_sy_${classNameLower}_datagrid";
var ${classNameLower}Dt,${classNameLower}UploadDg,${classNameLower}UploadFm;
var columns= [ [
				<#list table.columns as column>
				{
					field : '${column.columnName?uncap_first}',
					<#if column.columnName?uncap_first!="doroodoDate">
						<#list "${column.fieldRemarks}"?split("^") as p>
						<#if (p!="")>${p?replace("\\","")},</#if> 
						</#list>
						width : '150'
					<#else>
						<#list "${column.fieldRemarks}"?split("^") as p>
						<#if (p!="")>${p?replace("\\","")},</#if> 
						</#list>
						width : '150',
						formatter: function(value,row,index){
	               			if(value){
	               				var p="";
	    	            				var ys=value.split('年');
	    	            				var year=ys[0];
	    	            				p+=year;
	    	            				if(ys[1]){
	    		            				var yss=ys[1].split('月');
	    		            				var month=yss[0];
	    		            				p+=":"+month;
	    		            				if(yss[1]){
	    		            					var week=yss[1].split('周')[0].replaceAll('第','');
	    		            					p+=":"+week;
	    		            				}
	    	            				}
	    	            				row['doroodoDate']=value+'!'+p;
	               			}
	               			return value;
	               		} 
					</#if>
				 }<#if column_has_next>,</#if> 
				</#list>
            	] ];
$(function(){
	$('#i_sy_${classNameLower}_datagrid_add_dialog').dialog({
		onOpen:function(){
			${classNameLower}AddOnOpen();
		}
	});
	
	$('#i_sy_${classNameLower}_datagrid_edit_dialog').dialog({
		onOpen:function(){
			${classNameLower}EditOnOpen();
		}
	});
	pageView(${classNameLower}Id,columns);
	${classNameLower}onload();
});

//模式add
function pageView_add(){
	$('#'+${classNameLower}Id+'_add_dialog').dialog({
		noheader:true,
		fit:true,
		border:false,
		title:null,
		modal: true  
	});
	$('#'+${classNameLower}Id+'_add_btn a').off().click(function(){
		if(!AddBtnClick()) return;
		var setData={};
		for(var i=0; i<columns[0].length; i++){
			var field=columns[0][i].field;
			var title=columns[0][i].title;
			if(!checkFormField('#'+${classNameLower}Id+'_add_form_'+field)){log('['+title+']不能为空，请填写!'); return;}
			setData['${classNameLower}'+"."+field]=eGet('#'+${classNameLower}Id+'_add_form_'+field);
		}
		data(getUrl('${classNameLower}','Add'),setData,'json',null);
	});
	$('#'+${classNameLower}Id+'_add_dialog').dialog('open');
}

//模式edit
function pageView_edit(){
	data_={id:$.getUrlParam('id')};
	$.ajax({
		url : getUrl('${classNameLower}','Get_ById'),
		data : data_,
		dataType : 'json',
		type: "post", 
		async:true,
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		success : function(r) {
			setForm(r,${classNameLower}Id);
			if(r){if('info' in r){log(r.info);}};
		}
	});
	$('#'+${classNameLower}Id+'_edit_dialog').dialog({
		noheader:true,
		fit:true,
		border:false,
		title:null,
		modal: true  
	});
	$('#'+${classNameLower}Id+'_edit_btn a').off().click(function(){
		if(!EditBtnClick()) return;
		var setData={};
		for(var i=0; i<columns[0].length; i++){
			var field=columns[0][i].field;
			var title=columns[0][i].title;
			if(!checkFormField('#'+${classNameLower}Id+'_edit_form_'+field)){log('['+title+']不能为空，请填写!'); return;}
			setData['${classNameLower}'+"."+field]=eGet('#'+${classNameLower}Id+'_edit_form_'+field);
		}
		data(getUrl('${classNameLower}','Update'),setData,'json',null);
	});
	$('#'+${classNameLower}Id+'_edit_dialog').dialog('open');
}

//模式list
function pageView_list(){
	var ${classNameLower}DataGrid = {
			id:${classNameLower}Id,
			url:'${gloabpath?replace("/","")}'+actionUrl('${namespace}','${classNameLower}','List')+'?year='+new Date().getFullYear(),
			dId:'id',
			columns:columns,
			dTreeId:'doroodoDate',//需要修改
			etype:eType.TreeGrid,
			dExpandUrl:'${gloabpath?replace("/","")}'+actionUrl('/sys/','${classNameLower}','List_tree')+'?year=',
			dExpandId:'doroodoDate'//需要修改
			
	};
	
	${classNameLower}UploadDg = $('#i_sy_${classNameLower}_datagrid_upload_dialog');
	${classNameLower}UploadFm =$('#i_sy_${classNameLower}_datagrid_upload_dialog_form');
	${classNameLower}UploadFm.attr('action','${gloabpath?replace("/","")}'+actionUrl('${namespace}','${classNameLower}','Upload'));
	
	${classNameLower}Dt=gGrid2(${classNameLower}DataGrid);	
	var straddfun="dorow(${classNameLower}Id,'','${gloabpath?replace("/","")}"+actionUrl('${namespace}','${classNameLower}','Add')+"',updateFun,'c')";
	gDataGridToolbarBtn(${classNameLower}Id,'icon-add',straddfun,"新增");
	var stredit="dorow(${classNameLower}Id,'','${gloabpath?replace("/","")}"+actionUrl('${namespace}','${classNameLower}','Update')+"',updateFun,'u')";
	gDataGridToolbarBtn(${classNameLower}Id,'icon-edit',stredit,"修改");
	var strdelfun="dorow(${classNameLower}Id,'您是否确定要删除选择的数据','${gloabpath?replace("/","")}"+actionUrl('${namespace}','${classNameLower}','Delete')+"',updateFun,'d')";
	gDataGridToolbarBtn(${classNameLower}Id,'icon-remove',strdelfun,"删除");
	var strexcelfun="dorow(${classNameLower}Id,'您确定要导出数据','${gloabpath?replace("/","")}"+actionUrl('${namespace}','${classNameLower}','Excel')+"',updateFun,'e')";
	gDataGridToolbarBtn(${classNameLower}Id,'icon-page_white_excel',strexcelfun,"导出");
	gDataGridToolbarBtn(${classNameLower}Id,'icon-page_white_excel','upLoadFun()',"导入");
	var datetreefun = "getThext()";
	gDataGridToolbarBtn(datetreeTestId,'icon-page_white_excel',datetreefun,"说明");
}

function getThext(){
	 var setData = {};
	 setData.name = "此处填写组件文档名称";//需要修改
	data(getUrl('documents','Get_ByName'),setData,'json',updateFun);
} 

function updateFun(d){
	 datetreedue('文档说明','/work/datetree_explain.jsp',d.content);
}

function datetreedue(title,url,thext){
	var datetree=$.window({
		title :title,
		url : top.sysPath+url,
		isIframe : true,
		height : 400,
		width : 800,
		winId : 'datetree'+new Date().getTime(),
		target : 'self',
		maximizable : false,
		onComplete: function() {
			var obj = datetree.find('iframe')[0].contentWindow;
			obj.setContent(thext);
		}
	});
}

function upLoadFun(){
	${classNameLower}UploadDg.dialog('open');
}

function submitUploadForm(){
	${classNameLower}UploadFm.form('submit',{
		success:function(d){
			${classNameLower}Dt.datagrid('reload');
			${classNameLower}UploadDg.dialog('close');
			d=$.parseJSON(d);
			log(d.info);
			}
	});
}

function getEditFormHtml(title,type){
	var form=$('#report').clone();
	var word=$('table',form);
	title=title+"详细资料";
	$('td', word).each(function() {
		var gobj = $(this);
		gobj.children().each(function(i,n){
			 var obj = $(n);
		     if(!obj.is('a')){
		    	var id=obj.attr('id');
		    	if(id){
		    		gobj.html(eGet('#'+id));
		    	}
		     }
		    });
	});
	form.children().each(function(i,n){
   	 $('*',$(n)).each(function(ii,nn){
   		 if($(nn).css("display")=='none'){
   			 $(nn).remove();
   		 }
   	 });
	    });
	$('script',form).remove();
	var setData={'tableHtml':'<div class="titlep">'+title+'</div>'+form.html(),'tableTitle':title};
		data(getUrl('${classNameLower}', 'FormFile'),setData,
		'json', function(d){if(type=='word'){
			window.open(top.sysPath+'/report/word.jsp',new Date().getTime());
		}else if(type=='excel'){
			window.open(top.sysPath+'/report/excel.jsp',new Date().getTime());
		}});
	
}
</script>
</head>
<body>
<div id="i_sy_${classNameLower}_datagrid_toolbar"
		style="padding: 2px 0">
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<tr>
				<td style="padding-left: 2px"
					id="i_sy_${classNameLower}_datagrid_toolbtn"></td>
				<td style="text-align: right; padding-right: 2px"><input
					class="easyui-searchbox" data-options="prompt:'请输入搜索关键词'"
					style="width: 200px" searcher="dSearch"
					id="i_sy_${classNameLower}_datagrid_searchbox"
					pdt="i_sy_${classNameLower}_datagrid"></input>
					<div id="i_sy_${classNameLower}_datagrid_dSComb"
						style="width: 120px"></div></td>
			</tr>
		</table>
	</div>
 <table  id="i_sy_${classNameLower}_datagrid"></table> 
 
 <div id="i_sy_${classNameLower}_datagrid_add_dialog"
		class="easyui-dialog"
		data-options="closed:true,modal:true,maximizable:true,resizable:true,title:'新建',buttons:'#i_sy_${classNameLower}_datagrid_add_btn'"
		align="right" style="width: 1000px; height: 1000px;">
		<div style="padding: 10px 0 10px 10px">
		<div class="titlep">新建</div>
<!-- 请填入新建表单html  start -->

<!-- 请填入新建表单html  end -->
</div>
</div>
<div id="i_sy_${classNameLower}_datagrid_edit_dialog"
		class="easyui-dialog"
		data-options="closed:true,modal:true,maximizable:true,resizable:true,title:'修改',buttons:'#i_sy_${classNameLower}_datagrid_edit_btn',toolbar:[{
				text:'导出',
				iconCls:'icon-page_white_excel',
				handler:function(){
					getEditFormHtml('编辑','excel');//请修改
				}
			},
			{
				text:'导出',
				iconCls:'icon-page_white_word',
				handler:function(){
					getEditFormHtml('编辑','word');//请修改
				}
			}]"
		align="right" style="width: 1000px; height: 1000px;">
		<div style="padding: 10px 0 10px 10px">
		<div class="titlep">编辑</div>
<div id="report">
<!-- 请填入编辑表单html  start -->

<!-- 请填入编辑表单html  end -->
<div id="report_ps"> 
</div>
</div>
</div>
</div>

<!-- 按钮 start -->
<div id="i_sy_${classNameLower}_datagrid_edit_btn">
		<a href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-edit',plain:true">修改</a>
	</div>
	<div id="i_sy_${classNameLower}_datagrid_add_btn">
		<a href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-edit',plain:true">确定</a>
	</div>
	<div id="i_flowtoobar"></div>
<!-- 按钮 end -->

<div id="i_sy_${classNameLower}_datagrid_upload_dialog" class="easyui-dialog" title="上传" style="width:400px;height:100px;"  
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true"> 
          <form id="i_sy_${classNameLower}_datagrid_upload_dialog_form" action="" enctype="multipart/form-data" method="post" >
    <input type="text" name="fileid"  style="display:none;"/>上传文件：<input type="file" name="fileGroup"></br><span style="color:red">注：由于服务器的空间有限，上传文件大小不能超过1G</span></br>
        	<input type="button" value="上传" onClick="submitUploadForm();" />
        	</form>
</div>  

</body>
</html>
	