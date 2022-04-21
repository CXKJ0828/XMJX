<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<%@include file="/common/print.jspf"%>
<style>
    .tdshow{
        font-size: 16px
    }
</style>
<body>
<input type="text" id="ids" style="display: none" value="${ids}">
<input type="text" id="error" style="display: none"  value="${error}">
<input type="text" id="codes" style="display: none"  value="${codes}">
<script>
    var error=getValueById("error");
    if(error!=null&&error!=''){
        showErrorAlert("警告",error);
    }
</script>
<a href="#"  class="easyui-linkbutton" onclick="printRows()" data-options="iconCls:'printer'">确认打印</a>
<table style="width: 100%" id="contentCode1">
    <tr>
        <td><img style="float: right;margin-right: 10px"  src="data:image/png;base64,${img}">
        </td>
    </tr>
    <tr>
        <td style="text-align:center;"><span style="text-align:center;font-size: 18px;">${companyName}调质单</span></td>
    </tr>
    <tr>
        <td>
            <span style="float: left;font-size: 14px;">下料编号:${code} 吨数合计:${weightAll}</span>
            <span style="float: right;font-size: 14px;">打印日期:${nowDay}</span></td>
    </tr>
    <tr >
        <td>
            <table id="printRows" border="2" cellspacing="0" style="width: 100%;word-break:keep-all">
                <tr>
                    <td style="font-size:15px;text-align: center">客户</td>
                    <td style="font-size:15px;text-align: center">订单编号</td>
                    <td style="font-size:15px;text-align: center">来单日期</td>
                    <td style="font-size:15px;text-align: center">产品名称</td>
                    <td style="font-size:15px;text-align: center">图号</td>
                    <td style="font-size:15px;text-align: center">成品尺寸</td>
                    <td style="font-size:15px;text-align: center">订货数量</td>
                    <td style="font-size:15px;text-align: center">下料尺寸</td>
                    <td style="font-size:15px;text-align: center">下料数量</td>
                    <td style="font-size:15px;text-align: center">交货日期</td>
                    <td style="font-size:15px;text-align: center">材质</td>
                    <td style="font-size:15px;text-align: center">吨数</td>
                </tr>
                <c:forEach items="${rows}" var="order" varStatus="status">
                <tr>
                    <td style="font-size:15px;text-align: center">${order.clientFullName}</td>
                    <td style="font-size:15px;text-align: center">${order.contractNumber}</td>
                    <td style="font-size:15px;text-align: center">${order.makeTime}</td>
                    <td style="font-size:15px;text-align: center">${order.goodName}</td>
                    <td style="font-size:15px;text-align: center">${order.mapNumber}</td>
                    <td style="font-size:15px;text-align: center">${order.goodSize}</td>
                    <td style="font-size:15px;text-align: center">${order.orderAmount}</td>
                    <td style="font-size:15px;text-align: center">${order.blankSize}</td>
                    <td style="font-size:15px;text-align: center">${order.amount}</td>
                    <td style="font-size:15px;text-align: center">${order.deliveryTime}</td>
                    <td style="font-size:15px;text-align: center">${order.materialQualityName}</td>
                    <td style="font-size:15px;text-align: center">${order.weight}</td>
                    </c:forEach>
            </table>
        </td>
    </tr>
</table>
<script>
    function printRows() {
        $("#contentCode1").jqprint({
            debug: false,
            importCSS: true,
            printContainer: true,
            operaSupport: false
        });
//        communateGet(rootPath +'/blank/modifyPrintTime.shtml?ids='+getValueById("ids"),function back(data){
//            if(data=='success'){
                reloadDatagridMessage('tt');
//            }else{
//                showErrorAlert("操作",data);
//            }
//        });
    }
</script>
</body>