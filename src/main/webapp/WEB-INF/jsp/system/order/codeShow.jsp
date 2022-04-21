<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<%@include file="/common/print.jspf"%>
<style>
    .tdShow{
        width:20%;
        height: 40px;
        text-align: left;
    }
    .spanShow{
        width: 30%!important;
    }
</style>
<body>
<div  style="height:200px;text-align: left">
        <div  id="printCodeContent" style="text-align: left">
                <table style="width: 100%;text-align: left;table-layout:fixed;" >
                        <tr style="width: 100%">
                                <td style="width: 40%;font-size: 13px">工号:${entity.orderCode}</td>
                                <td style="width: 60%;font-size: 13px;word-break:keep-all;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">品名:${entity.name}</td>
                        </tr>
                        <tr>
                                <td style="font-size: 13px">数量:${entity.orderAmount}台</td>
                                <td style="font-size: 13px">合同期:${entity.time}</td>
                        </tr>
                        <tr>
                                <td style="text-align: left;"><img  src="data:image/png;base64,${img}"></td>
                                <td>
                                        <table  style="text-align: left">
                                                <tr>
                                                        <td style="font-size: 13px">识别码:${entity.batchNumber}</td>
                                                </tr>
                                                <tr>
                                                        <td style="font-size: 13px">件号:${entity.goodId}</td>
                                                </tr>
                                                <tr>
                                                        <td style="font-size: 13px">件名:${entity.goodName}</td>
                                                </tr>
                                                <tr>
                                                        <td style="font-size: 13px">数量:${entity.amount}件</td>
                                                </tr>
                                        </table>
                                </td>
                        </tr>
                </table>

                </table>
        </div>
        <a href="#" style="text-align: center"  class="easyui-linkbutton" onclick="print()" data-options="iconCls:'printer'">确认打印</a>
</div>
<script>
    function print() {
        $("#printCodeContent").jqprint({
            debug: false,
            importCSS: true,
            printContainer: true,
            operaSupport: false
        });
    }
</script>
</body>