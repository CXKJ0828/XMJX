<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<table id="processTable"
       class="easyui-datagrid"
       data-options="rownumbers:true,
        toolbar: '#processBar',
           fit:true,
            fitColumns:true,
           url:'${pageContext.request.contextPath}/process/allProcessByRoleIdSelect.shtml?roleId='+${roleId},
             onLoadSuccess: function (data) {
            if (data) {
                $.each(data.rows, function (index, item) {
                    if (item.checked == 1) {
                        $('#processTable').datagrid('checkRow', index);
                    }
                });
            }
        }
">
    <thead>
    <tr>
        <th field="ck" checkbox="true"></th>
        <th data-options="field:'id',hidden:true,width:50,align:'center'">编号</th>
        <th data-options="field:'code',width:200,align:'center',editor:'text'">编码</th>
        <th data-options="field:'name',width:200,align:'center',editor:'text'">名称</th>
        <th data-options="field:'simpleName',width:200,align:'center',editor:'text'">简称</th>
        <th data-options="field:'address',width:200,align:'center',editor:'text'">地址</th>
        <th data-options="field:'remark',width:100,align:'center',editor:'text'">备注</th>
    </tr>
    </thead>
</table>
<div id="processBar" style="height:auto">
        <a href="javascript:void(0)" onclick="processSubmit()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">提交</a>
    </form>
</div>
<script>
    function processSubmit() {
        var rows=$('#processTable').datagrid('getChecked');
        var data={
            processSelect:rows,
            roleId:${roleId}
        }
        var url='${pageContext.request.contextPath}/role/processRoleEntity.shtml';
        communatePost(url,ListToJsonString(data),function back(data){
            showMessagerCenter("提示","分配工序成功");
            $('#process').window('close');
        })
    }
</script>