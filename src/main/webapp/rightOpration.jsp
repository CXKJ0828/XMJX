<%--
  Created by IntelliJ IDEA.
  User: 88888888
  Date: 2019/9/30
  Time: 15:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<div id="rightMenu" class="easyui-menu" style="width: 50px; display: none;">    
    <div  data-options="iconCls:'icon-add'" onclick="addContent()">新增行</div>
                    <div  data-options="iconCls:'icon-edit'" onclick="edit()">修改</div>
                    <div  data-options="iconCls:'icon-remove'" onclick="delEntity()">删除</div>         
</div>
<div id="rightMenuHeader" class="easyui-menu" style="width: 50px; display: none;">    
    <div  data-options="iconCls:'icon-add'" onclick="addContent()">新增行</div>
</div>
<div id="rightMenu2" class="easyui-menu" style="width: 50px; display: none;">    
    <div  data-options="iconCls:'icon-add'" onclick="addContent2()">新增行</div>
                    <div  data-options="iconCls:'icon-edit'" onclick="edit2()">修改</div>
                    <div  data-options="iconCls:'icon-remove'" onclick="del2()">删除</div>         
    <div  data-options="iconCls:'shape_square_go'" onclick="check()">审核通过</div>         
</div>
<div id="rightMenuHeader2" class="easyui-menu" style="width: 50px; display: none;">    
    <div  data-options="iconCls:'icon-add'" onclick="addContent2()">新增行</div>
</div>