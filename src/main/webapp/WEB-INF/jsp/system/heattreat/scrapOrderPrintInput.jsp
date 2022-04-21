<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div style="padding:10px 0 10px 60px">
    <form id="ff"  method="post">
        <table>
            <tr>
                <td>废品原因:</td>
                <td>
                    <input id="id" name="scrapFormMap.id" style="display: none" type="text" value="${entity.id}" >
                    <input id="workersubmitHeattreatId" style="display:none" name="scrapFormMap.workersubmitHeattreatId" type="text" value="${workersubmitHeattreatId}" >
                    <input  type="text" style="width:300px;"  name="scrapFormMap.badReason"value="${entity.badReason}" ></input></td>
            </tr>
            <tr>
                <td>材料费:</td>
                <td>
                    <input style="width:300px;"  type="text" name="scrapFormMap.materialCost"  id='materialCost' value="${entity.materialCost}" ></input></td>
            </tr>
            <tr>
                <td>锻造费:</td>
                <td>
                    <input style="width:300px;"  type="text" name="scrapFormMap.forgingCost"  id='forgingCost' value="${entity.forgingCost}" ></input></td>
            </tr>
            <tr>
                <td>调质费:</td>
                <td>
                    <input style="width:300px;"  type="text" name="scrapFormMap.adjustmentCost"  id='adjustmentCost' value="${entity.adjustmentCost}" ></input></td>
            </tr>
            <tr>
                <td>下料费:</td>
                <td>
                    <input style="width:300px;"  type="text" name="scrapFormMap.cuttingCost"  id='cuttingCost' value="${entity.cuttingCost}" ></input></td>
            </tr>
            <tr>
                <td>车工费:</td>
                <td>
                    <input style="width:300px;" type="text" name="scrapFormMap.carriageCost"  id='carriageCost' value="${entity.carriageCost}" ></input></td>
            </tr>
            <tr >
                <td>合计:</td>
                <td>
                    <input  type="text" id='allCost' name="scrapFormMap.allCost"value="${entity.allCost}" ></input></td>
            </tr>
            <tr>
                <td>检验结论:</td>
                <td>
                    <input style="width:300px;"  type="text" name="scrapFormMap.conclusion" value="${entity.conclusion}" ></input></td>
            </tr>
            <tr>
                <td>批准人:</td>
                <td>
                    <input style="width:300px;"  type="text" name="scrapFormMap.approvalName" value="${entity.approvalName}" ></input></td>
            </tr>
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
        var workersubmitHeattreatId=getValueById("workersubmitHeattreatId");
        $("#printOrder").window({
            width:800,
            title:'外协废品单',
            modal: true,
            top:10,
            href: rootPath + '/heattreat/scrapOrderPrintShowUI.shtml?workersubmitHeattreatId='+workersubmitHeattreatId,
            onClose:function () {

            }
        });
    }

    function submitForm(){
        $('#ff').form({
            url:rootPath + '/heattreat/badprintorderEditEntity.shtml?origin=外协废品单',
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