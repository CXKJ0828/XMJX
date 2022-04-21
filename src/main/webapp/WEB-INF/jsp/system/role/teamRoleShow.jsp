<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<table>
    <tr>
        <td>选择员工：
            <input id="userIdsContent" style="display: none"  type="text" value="${teamFormMap.userIds}">
            <select id="userIds" style="width:500px" class="easyui-combogrid"  data-options="
                        panelWidth: 500,
                        idField: 'id',
                        multiple: true,
                        textField: 'accountName',
                        url: '${pageContext.request.contextPath}/user/userSelect.shtml',
                        method: 'get',
                        columns: [[
                            {field:'id',checkbox:true,title:'编号',width:80},
                            {field:'accountName',title:'账号',width:120},
                            {field:'userName',title:'姓名',width:120},

                        ]],
                        fitColumns: true,
                        onLoadSuccess : function(){
                        var userIdsContent=getValueById('userIdsContent');
                                    setContentsToCombogrid('userIds',userIdsContent.split(','));
                        }"></select>
        </td>
    </tr>
    <tr>
        <td>
            <a href="javascript:void(0)" onclick="teamSubmit()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">提交</a>
        </td>
    </tr>
    <script>
        function teamSubmit() {
            var userIds=getContentsBycombogrid("userIds");
            var data={
                userIds:userIds,
                roleId:${roleId}
            }
            var url='${pageContext.request.contextPath}/role/teamRoleEntity.shtml';
            communatePost(url,ListToJsonString(data),function back(data){
                showMessagerCenter("提示","分配班组成功");
                $('#process').window('close');
            })
        }
    </script>
</table>