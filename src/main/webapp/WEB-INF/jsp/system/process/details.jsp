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
<div data-options="region:'south',split:true">

        <table border="1"  cellspacing="5" style="border-color:gainsboro">
            <tbody>
            <tr>
                <td class="tdShow"><span class="spanShow">编码：</span><input style="display: none" id="id" type="text" class="inputShow" value="${process.id}">
                    <input id="code" type="text" class="inputShow" value="${process.code}"> </td>
                <td class="tdShow"><span class="spanShow">名称：</span><input id="name" type="text" class="inputShow" value="${process.name}"> </td>
                <td class="tdShow"><span class="spanShow">简称：</span><input id="simpleName" type="text" class="inputShow" value="${process.simpleName}"> </td>
                <td class="tdShow"><span class="spanShow">组织：</span><input id="organization" type="text" class="inputShow" value="${process.organization}"> </td>
                <td class="tdShow"><span class="spanShow">地址：</span><input id="address" type="text" class="inputShow" value="${process.address}"> </td>
            </tr>
            <tr>
                <td class="tdShow"><span class="spanShow">单件加工时间：</span><input id="oneTime" type="text" class="inputShow" value="${process.oneTime}"> </td>
                <td class="tdShow"><span class="spanShow">单价时间单位：</span><input id="oneTimeUnit" type="text" class="inputShow" value="${process.oneTimeUnit}"> </td>
                <td class="tdShow"><span class="spanShow">单件成本：</span><input id="oneCost" type="text" class="inputShow" value="${process.oneCost}"> </td>
                <td class="tdShow"><span class="spanShow">备注：</span><input id="remark" type="text" class="inputShow" value="${process.remark}"> </td>
                <td class="tdShow"><span class="spanShow">状态：</span>
                    <span class="radioSpan" id="isUse" onclick="radioSelectSetEditor('isUse','tt',editId}">
                            <input type="radio"  name="isUse" value="可用">可用</input>
                        <input type="radio" name="isUse" value="不可用">不可用</input>
                        </span>
                </td>
            </tr>
            </tbody>
        </table>
</div>
<div data-options="region:'center'" style="height:auto;">
    <table id="childs" class="easyui-datagrid" style="width:auto;height:auto" data-options="
    fitColumns:true,singleSelect:true
    ,url:'${pageContext.request.contextPath}/process/getChildProcess.shtml?id='+getValueById('id')">
        <thead>
        <tr>
            <th data-options="field:'id',width:100,hidden:true,align:'center',editor:'text'">编码</th>
            <th data-options="field:'code',width:100,align:'center',editor:'text'">编码</th>
            <th data-options="field:'parentId',width:200,align:'center',editor:'text'">父编码</th>
            <th data-options="field:'name',width:200,align:'center',editor:'text'">名称</th>
            <th data-options="field:'simpleName',width:150,align:'center',editor:'text'">简称</th>
            <th data-options="field:'address',width:200,align:'center',editor:'text'">地址</th>
            <th data-options="field:'remark',width:200,align:'center',editor:'text'">备注</th>
            <th data-options="field:'organization',width:100,align:'center',editor:'text'">组织</th>
        </tr>
        </thead>
    </table>
</div>
<div id="rightMenu" class="easyui-menu" style="width: 50px; display: none;">    
    <div  data-options="iconCls:'icon-add'" onclick="addContent()">新增行</div>
                    <div  data-options="iconCls:'icon-edit'" onclick="edit()">修改</div>
                    <div  data-options="iconCls:'icon-remove'" onclick="delProcess()">删除</div>         
</div>
<div id="rightMenuHeader" class="easyui-menu" style="width: 50px; display: none;">    
    <div  data-options="iconCls:'icon-add'" onclick="addContent()">新增行</div>
</div>
<script>
    setRadioCheckedByIdAndContent("isUse",'${process.isUse}');
    var editId;
    $("#childs").datagrid({
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            if(editId!=null){
                endEditDatagridByIndex('childs',editId)
            }
            clearSelectAndSelectRowDatagrid('childs',rowIndex);
            editId=rowIndex;
            showMenu("rightMenu",e.pageX,e.pageY);
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出
        },
        onHeaderContextMenu: function (e, field){ //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            if(editId!=null){
                endEditDatagridByIndex('childs',editId);
            }
            showMenu("rightMenuHeader",e.pageX,e.pageY);
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出
        }
    })
    function delProcess() {
        var rowData=getDatagridJsonByIndexAndId("childs",editId);
        var url='${pageContext.request.contextPath}/process/deleteProcessEntity.shtml?processId='+rowData.id+"&processParentId="+getValueById("id");
        if(editId!=null){
            communateGet(url,function back(data){
                reloadDatagridMessage('childs');
            });
        }else{
            $.messager.alert('警告','不存在可以删除数据','warning');
        }
    }
    function addContent() {
        $('#childs').datagrid('appendRow',
            {
            }
        );
        var rows = getAllRowsContent("childs");
        editId=rows.length-1;
        beginEditDatagridByIndex('childs',editId);
    }
    function edit() {
        beginEditDatagridByIndex('childs',editId);
    }

</script>

</body>