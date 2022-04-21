<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div style="padding:10px 0 10px 60px">
    <form id="ff"  method="post">
        <table>
            <tr>
                <td>数量:</td>
                <input style="display: none" type="text" id="heattreatId" value="${heattreatId}" ></input>
                <td><input style="width:300px;" type="text" id="amount" onchange="getWeight()"></input></td>
            </tr>
            <tr>
                <td>总重量:</td>
                <td>
                    <input style="width:300px;" id="allWeight"  type="text" ></input></td>
            </tr>
            <tr>
                <td>材料单价:</td>
                <td>
                    <input style="width:300px;"  type="text"  value='7.8' disabled></input></td>
            </tr>
            <tr>
                <td>扣款比例:</td>
                <td>
                    <select class="easyui-combobox"  disabled  id="ratio" style="line-height:22px;width:100px;">
                        <option value="0.9">90%</option>
                    </select>
            </tr>
            <tr>
                <td>不合格原因:</td>
                <td>
                    <input style="width:300px;" id="reason"  type="text"  ></input></td>
            </tr>
            <tr>
                <td>主管意见:</td>
                <td><textarea  id="directorOpinion"  style="height:60px;width:300px;" ></textarea></td>
            </tr>
            <tr>
                <td>老板审批意见:</td>
                <td><textarea  id="bossOpinion"  style="height:60px;width:300px;" ></textarea></td>
            </tr>
        </table>
    </form>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="printOrder()">确定</a>
</div>
<div id="printOrder">

</div>
<script>
    function getWeight() {
        communateGet(rootPath +'/heattreat/calculationAllWeight.shtml?badAmount='+getValueById('amount')+'&heattreatId='+getValueById('heattreatId'),function back(data){
            setContentToInputById('allWeight',data);
        });
    }

    function printOrder() {
        var reason=$("#reason").val();
        var directorOpinion=$("#directorOpinion").val();
        var bossOpinion=$("#bossOpinion").val();
        $("#printOrder").window({
            width:800,
            title:'废品通知单',
            modal: true,
            top:10,
            href: rootPath + '/heattreat/badnoticeOrderNoIdUI.shtml?heattreatId='+getValueById('heattreatId')
            +"&allWeight="+getValueById("allWeight")
            +"&amount="+getValueById("amount")
            +"&reason="+reason
            +"&directorOpinion="+directorOpinion
            +"&bossOpinion="+bossOpinion,
            onClose:function () {

            }
        });
    }
</script>