<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div style="padding:10px 0 10px 60px">
    <form id="ff"  method="post">
        <input style="display: none" id="toolId" name="toolOutFormMap.toolId" value="${toolId}">
        <table>
            <tr>
                <td>领取人:</td>
                <td>  <select id="userId"   name="toolOutFormMap.userId" style="width:200px" class="easyui-combogrid"  data-options="
                        panelWidth: 200,
                        idField: 'id',
                        textField: 'userName',
                         required:true,
                        mode: 'remote',
                        url: '${pageContext.request.contextPath}/user/userSelect.shtml',
                        method: 'get',
                        columns: [[
                            {field:'id',hidden:true,title:'编号',width:80},
                            {field:'accountName',title:'账号',width:120},
                            {field:'userName',title:'姓名',width:120},

                        ]],
                        fitColumns: true"></select></td>
            </tr>
            <tr>
                <td>领取时间:</td>
                <td><input  id="time" name="toolOutFormMap.time" style="width: 200px" type="text"></td>
            </tr>
            <tr>
                <td>领取数量:</td>
                <td><input class="easyui-validatebox"  id="amount" name="toolOutFormMap.amount" style="width: 200px" type="text" data-options="required:true" /></td>
            </tr>
            <tr>
                <td>备注:</td>
                <td><input  id="remarks" name="toolOutFormMap.remarks" style="width: 200px" type="text"></td>
            </tr>
        </table>
    </form>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">确定</a>
</div>
<script>
    initInputDataInput("time");
    function submitForm(){
            submitFormNow('ff','/toolOut/addEntity.shtml',function (data) {
                $('#outWindow').window('close');
                window.parent.reload();
            })
    }
</script>