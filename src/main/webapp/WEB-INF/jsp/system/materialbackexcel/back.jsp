<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div style="padding:10px 0 10px 60px">
    <form id="ff"  method="post">
        <input style="display: none"  name="materialBackExcelDetailsFormMap.materialbackExcelId" value="${id}">
        <table>
            <tr>
                <td>本次回料米数:</td>
                <td><input class="easyui-validatebox" type="number" name="materialBackExcelDetailsFormMap.length"></input></td>
            </tr>
            <tr>
                <td>本次回料重量:</td>
                <td><input class="easyui-validatebox" type="number" name="materialBackExcelDetailsFormMap.weight"></input></td>
            </tr>
        </table>
    </form>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">确定</a>
</div>
<script>
    function submitForm(){
            submitFormNow('ff','/materialbackexcel/backEntity.shtml',function (data) {
                var jsonObj = eval( '(' + data + ')' ); // eval();方法
                if(jsonObj=="success"){
                    showMessagerCenter("提示","回料成功");
                    $('#backWindow').window('close');
                    window.parent.reload();
                }
            })
    }
</script>