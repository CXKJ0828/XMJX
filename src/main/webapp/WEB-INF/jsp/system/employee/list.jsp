<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/employee/list.js"></script>
<style>
    .tdShow{
        width:20%;
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
<div data-options="region:'south',split:true" >
    <div class="easyui-tabs" style="height:120px">
        <div title="职工资料编辑" style="padding:1px">
            <table border="1"  cellspacing="0" style="border-color:gainsboro;">
                <tbody>
                <tr>
                    <td class="tdShow"><span class="spanShow">编号：</span><input id="id" type="text" class="inputShow easyui-validatebox" data-options="required:true" onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">姓名：</span><input id="name" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">性别：</span>
                        <span class="radioSpan" id="sex"  onclick="radioSelectSetEditor('sex','tt',editId)">
                            <input type="radio"  name="sex" value="男">男</input>
                            <input type="radio" name="sex" value="女">女</input>
                        </span> </td>
                    <td class="tdShow"><span class="spanShow">身份证：</span><input id="IDcard" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                </tr>
                <tr>
                    <td class="tdShow"><span class="spanShow">岗位：</span><input id="title" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">岗位工资：</span><input id="wages" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">所属角色：</span><input  id="roleId" class="inputShow" onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">部门：</span><input id="department" type="text" onchange="changeId(this.id)"> </td>
                </tr>
                <tr>
                    <td class="tdShow"><span class="spanShow">年龄：</span><input id="age" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">联系电话：</span><input id="fixedPhone" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                    <td class="tdShow" colspan="2"><span class="spanShow">备注：</span><input id="remark" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                </tr>
                <script>
                    function checkbox(id) {
                        setContentToDatagridEditorText('tt',editId,id,$("#"+id).is(':checked'));
                    }
                </script>
                <script>
                    $('#roleId').combobox({
                        url:'${pageContext.request.contextPath}/role/getSelectList.shtml',
                        valueField:'id',
                        textField:'text',
                        width:145,
                        onSelect: function(record){
                            if(checkIsSelectEd(editId)){
                                setContentToInputById("roleName",record.text);
                                setContentToDatagridEditorText('tt',editId,'roleId',record.id);
                                setContentToDatagridEditorText('tt',editId,'roleName',record.text);
                            }
                        }
                    });
                </script>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>
    var editId;
    function changeId(id) {
       if(checkIsSelectEd(editId)){
           setContentToDatagridEditorText('tt',editId,id,getValueById(id));
       }
    }
</script>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" style="width: 4000px"></table>
    <div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="resetPassword()">密码重置</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="createAccount()">创建账号</a>
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </div>
</div>

</body>