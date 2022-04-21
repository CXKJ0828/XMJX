<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/department/list.js"></script>
<style>
    .tdShow{
        width:20%;
        height: 20px;
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
<div data-options="region:'south',split:true" style="height:180px;">
    <div class="easyui-tabs" style="height:200px">
        <div title="基本资料" style="padding:5px">
            <table border="1"  cellspacing="0" style="border-color:gainsboro;">
                <tbody>
                <tr>
                    <td class="tdShow"><span class="spanShow">部门编码：</span><input id="code" type="text" class="inputShow easyui-validatebox" data-options="required:true" onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">上级编码：</span><input id="parentId" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">部门全称：</span><input id="fullName" type="text" class="inputShow"  onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">部门简称：</span><input id="simpleName" type="text" class="inputShow"  onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">英文简称：</span><input id="englishName" type="text" class="inputShow"  onchange="changeId(this.id)"> </td>
                </tr>
                <tr>
                    <td class="tdShow"><span class="spanShow">员工人数：</span><input id="peopleNum" type="number" value="0" class="inputShow" onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">&nbsp;&nbsp;&nbsp;&nbsp;负责人：</span><input id="leader" type="text"class="inputShow" onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">&nbsp;&nbsp;联系人：</span><input id="contacts" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电话：</span><input id="phone" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                    <td class="tdShow"><span class="spanShow">成本类型：</span><input id="costType" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                </tr>
                <tr>
                    <td class="tdShow"><span class="spanShow">状态：</span>
                        <span class="radioSpan" id="isUse" onclick="radioSelectSetEditor('isUse','tt',editId)">
                            <input type="radio"  name="isUse" value="可用">可用</input>
                            <input type="radio" name="isUse" value="不可用">不可用</input>
                        </span>
                    </td>
                    <td class="tdShow"><span class="spanShow">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;组织：</span><input id="organization" type="text" class="inputShow" onchange="changeId(this.id)"> </td>
                    <td class="tdShow" colspan="3"><span class="spanShow">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备注：</span><input id="remark" type="text"class="inputShow" onchange="changeId(this.id)"></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div title="对应仓库及抛转资料" style="padding:10px">
            <ul class="easyui-tree" ></ul>
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
<div data-options="region:'center'" style="height:250px;">
    <table id="tt" style="width: 2000px"></table>
    <div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </div>
</div>

</body>