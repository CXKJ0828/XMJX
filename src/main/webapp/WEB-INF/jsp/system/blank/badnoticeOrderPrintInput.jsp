<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div style="padding:10px 0 10px 60px">
    <form id="ff"  method="post">
        <table>
            <tr>
                <td>实测尺寸:</td>
                <td>
                    <input id="orderdetailsId" style="display: none" type="text" value="${orderdetailsId}" >
                    <input id="id" name="badnoticeFormMap.id" style="display: none" type="text" value="${entity.id}" >
                    <input id="workersubmitId" style="display:none" name="badnoticeFormMap.workersubmitId" type="text" value="${workersubmitId}" >
                    <input style="width:300px;" type="text" name="badnoticeFormMap.trueSize"value="${entity.trueSize}" ></input></td>
            </tr>
            <tr>
                <td>判定结果:</td>
                <td><input style="width:300px;" type="text" name="badnoticeFormMap.result"value="${entity.result}" ></input></td>
            </tr>
            <tr>
                <td>备注:</td>
                <td><textarea name="badnoticeFormMap.remarks" id="remarks" style="height:60px;width:300px;" ></textarea></td>
            </tr>
            <script>
                setTextareaValue('remarks','${entity.remarks}');
            </script>
        </table>
    </form>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="printOrder()">打印</a>
</div>
<div id="printOrder">

</div>
<script>
    function printOrder() {
        var row=getDatagridJsonByIndexAndId("tt",editId);
        var workersubmitId=getValueById("workersubmitId");
        var orderdetailsId=getValueById("orderdetailsId");
        $("#printOrder").window({
            width:800,
            title:'废品通知单',
            modal: true,
            top:10,
            href: rootPath + '/blank/badnoticeOrderUI.shtml?orderdetailsId='+orderdetailsId+"&workersubmitId="+workersubmitId,
            onClose:function () {

            }
        });
    }

    function submitForm(){
        $('#ff').form({
            url:rootPath + '/blank/badprintorderEditEntity.shtml?origin=废品通知单',
            onSubmit: function(){
                return $(this).form('validate');
            },
            success:function(data){
                showMessagerCenter("提示","操作成功");
            }
        });
        $('#ff').submit();
    }
    function clearForm(){
        $('#ff').form('clear');
    }
</script>