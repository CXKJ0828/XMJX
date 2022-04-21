<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/client/list.js"></script>
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
<div data-options="region:'south',split:true" style="height:120px;">
    <div class="easyui-tabs">
        <div title="基本资料" style="padding:1px">
            <table border="1"  cellspacing="0" style="border-color:gainsboro;">
                <tbody>
                <tr>
                    <td class="tdShow"><span class="spanShow">单位名称：</span><input id="fullName" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">单位简称：</span><input id="simpleName" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">法人代表：</span><input id="leader" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">税号：</span><input id="taxNumber" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                </tr>
                <tr>
                    <td class="tdShow"><span class="spanShow">开户行1：</span><input id="bank1" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">账号1：</span><input id="account1" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">开户行2：</span><input id="bank2" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">账号2：</span><input id="account2" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                </tr>
                <tr>
                    <td class="tdShow"><span class="spanShow">税率：</span><input id="taxRate" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">地址：</span><input id="address" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow" colspan="2"><span class="spanShow">备注：</span><input id="remarks" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div title="联系资料" style="padding:5px">
            <table border="1"  cellspacing="0" style="border-color:gainsboro;">
                <tbody>
                <tr>
                    <td class="tdShow"><span class="spanShow">联系人：</span><input id="contacts" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">联系电话：</span><input id="phone" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">传真：</span><input id="fax" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                    <td class="tdShow"><span class="spanShow">不含税比率：</span><input id="taxFreeRatio" type="text"class="inputShow"  onchange="changeId(this.id)"></td>
                </tr>
                </tbody>
            </table>
        </div>
        <script>
            function checkbox(id) {
                setContentToDatagridEditorText('tt',editId,id,$("#"+id).is(':checked'));
            }
        </script>
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
<div data-options="region:'center'" style="height:200px;">
    <table id="tt" style="width: 4000px"></table>
    <div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </div>
</div>

</body>