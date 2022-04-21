<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<style>
    .tdShow{
        width:20%;
        height: 40px;
        text-align: left;
    }
    .spanShow{
        width: 30%!important;
    }
    .inputShow{
        width: 70%;
    }
</style>
<body class="easyui-layout">
<input type="text" id="goodId" style="display: none" value="${goodId}">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',split:true" >
    </div>
    <div data-options="region:'center'" style="height:auto;">
        <table id="goodprocessList"
               class="easyui-datagrid"
               style="width:auto;height:auto"
               data-options="rownumbers:true,
            fitColumns:true,
            singleSelect:true,
           url:'${pageContext.request.contextPath}/good/getGoodProcessByGoodId.shtml?id='+${goodId}">
            <thead>
            <tr>
                <th data-options="field:'id',hidden:true,width:100,align:'center',editor:'text'">编码</th>
                <th data-options="field:'goodId',hidden:true,width:100,align:'center',editor:'text'">产品编号</th>
                <th data-options="field:'processId',hidden:true,width:100,align:'center',editor:'text'">工序编码</th>
                <th class="easyui-combogrid" data-options="field:'processName',width:100,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 600,
			idField: 'name',
			textField: 'name',
			url: '${pageContext.request.contextPath}/process/processSelect.shtml',
			mode: 'remote',
			columns: [[
				{field:'id',hidden:true,title:'编码',width:150,align:'center'},
               {field:'name',title:'名称',width:150,align:'center'},
                {field:'artificial',title:'人工',width:150,align:'center'},
                {field:'remark',title:'备注',width:150,align:'center'},
                {field:'isMust',title:'是否必须',width:150,align:'center'},
                {field:'multiple',title:'倍数',width:150,align:'center'},
			]],
			fitColumns: true,
			onSelect:selectProcess
			}
		}">工序</th>
                <th data-options="field:'artificial',width:100,align:'center',editor:'text'">人工</th>
                <th data-options="field:'remark',width:100,align:'center',editor:'text'">备注</th>
                <th data-options="field:'isMust',width:100,align:'center',editor:'text'">是否必须</th>
                <th data-options="field:'multiple',width:100,align:'center',editor:'text'">倍数</th>
                <th data-options="field:'modifyTime',width:100,align:'center',editor:'text'">修改时间</th>
                <th data-options="field:'userName',width:100,align:'center',editor:'text'">修改用户</th>
            </tr>
            </thead>
        </table>
        <script>
            initComboGridEditor();
            function selectProcess(rowIndex, rowData) {
                if(checkIsSelectEd(editId)){
                    setContentToDatagridEditorText('goodprocessList',editId,"processId",rowData.id);
                    setContentToDatagridEditorText('goodprocessList',editId,"processName",rowData.name);
                    setContentToDatagridEditorText('goodprocessList',editId,"artificial",rowData.artificial);
                    setContentToDatagridEditorText('goodprocessList',editId,"remark",rowData.remark);
                    setContentToDatagridEditorText('goodprocessList',editId,"isMust",rowData.isMust);
                    setContentToDatagridEditorText('goodprocessList',editId,"multiple",rowData.multiple);
                }
            }
            
            $("#goodprocessList").datagrid({
                onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
                    e.preventDefault(); //阻止浏览器捕获右键事件
                    if(editId!=null){
                        endEditDatagridByIndex('goodprocessList',editId)
                    }
                    clearSelectAndSelectRowDatagrid('goodprocessList',rowIndex);
                    editId=rowIndex;
                    showMenu("rightMenuGoodCode",e.pageX,e.pageY);
                    e.preventDefault();  //阻止浏览器自带的右键菜单弹出
                },
                onHeaderContextMenu: function (e, field){ //右键时触发事件                      
                    e.preventDefault(); //阻止浏览器捕获右键事件
                    if(editId!=null){
                        endEditDatagridByIndex('goodprocessList',editId)
                    }
                    showMenu("rightMenuHeaderGoodCode",e.pageX,e.pageY);
                    e.preventDefault();  //阻止浏览器自带的右键菜单弹出
                }
            })
            function addContentProcess() {
                if(editId!=null||editId!=""){
                    endEditDatagridByIndex("goodprocessList",editId);
                }
                $('#goodprocessList').datagrid('appendRow',
                    {
                        "goodId":${goodId}
                    }
                );
                var rows = getAllRowsContent("goodprocessList");
                editId=rows.length-1;
                beginEditDatagridByIndex('goodprocessList',editId);
            }
            function editProcess() {
                beginEditDatagridByIndex('goodprocessList',editId);
            }
            function delProcess() {
                var rowData=getDatagridJsonByIndexAndId("goodprocessList",editId);
                var url='${pageContext.request.contextPath}/good/deleteGoodProcessEntity.shtml?id='+rowData.id;
                if(editId!=null){
                    communateGet(url,function back(data){
                        reloadDatagridMessage('goodprocessList');
                    });
                }else{
                    $.messager.alert('警告','不存在可以删除数据','warning');
                }
            }
        </script>
    </div>
</div>
<%@include file="/rightOpration.jsp"%>

<div id="rightMenuGoodCode" class="easyui-menu" style="width: 50px; display: none;">    
    <div  data-options="iconCls:'icon-add'" onclick="addContentProcess()">新增行</div>
    <div  data-options="iconCls:'icon-edit'" onclick="editProcess()">修改</div>
    <div  data-options="iconCls:'icon-remove'" onclick="delProcess()">删除</div>         
</div>
<div id="rightMenuHeaderGoodCode" class="easyui-menu" style="width: 50px; display: none;">    
    <div  data-options="iconCls:'icon-add'" onclick="addContentProcess()">新增行</div>
</div>
</body>