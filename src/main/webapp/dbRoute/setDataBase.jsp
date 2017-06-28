<%@ page contentType="text/html;charset=UTF-8" import="java.lang.String" language="java" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <title>Settings</title>
    <script type="text/javascript" src="<%=path%>/dbRoute/easyui/jquery.min.js" ></script>
    <script type="text/javascript" src="<%=path%>/dbRoute/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=path%>/dbRoute/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link href="<%=path%>/dbRoute/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="<%=path%>/dbRoute/easyui/themes/icon.css" rel="stylesheet" type="text/css" />
    <link href="<%=path%>/dbRoute/easyui/demo/demo.css" rel="stylesheet" type="text/css"  />
    <link href="<%=path%>/dbRoute/simpleui/css/common.css" rel="stylesheet" type="text/css"  />
    <link href="<%=path%>/dbRoute/simpleui/css/FormText.css" rel="stylesheet" type="text/css"  />
    <style rel="stylesheet">
        #tenantDialog input {
            height: 32px;
            width: 300px;
        }
    </style>
    <script type="text/javascript">
        window.contextPath="<%=request.getContextPath()%>";
        $(function () {
            var tenant = {
                datagrid:null,
                init:function () {

                    $("#dbType,#dbTypeQry").combobox({
                        data:[{
                            "id":"oracle",
                            "text":"oracle"
                        },{
                            "id":"mysql",
                            "text":"mysql"
                        }],
                        editable : false,
                        valueField : "id",
                        textField : "text",
                        height:32,
                        panelHeight:100
                    });

                    $("#isEffective,#isEffectiveQry").combobox({
                        data:[{
                            "id":"1",
                            "text":"1"
                        },{
                            "id":"0",
                            "text":"0"
                        }],
                        editable : false,
                        valueField : "id",
                        textField : "text",
                        height:32,
                        panelHeight:100
                    });

                    tenant.datagrid = $("#tenant_list").datagrid({
                        nowrap:false,
                        striped:true,
                        fit:true,
                        checkbox:true,
                        idField:"id",
                        singleSelect:true,
                        columns:[[
                            {field:'id',checkbox:true},
                            {field:'tenant',title:'租户标识',width:150,align:'center'},
                            {field:'username',title:'数据库用户名',width:150,align:'center'},
                            {field:'password',title:'数据库密码',width:150,align:'center'},
                            {field:'url',title:'数据库连接串',width:400,align:'center'},
                            {field:'dbtype',title:'数据库类型',width:100,align:'center'},
                            {field:'iseffective',title:'是否有效',width:100,align:'center'}
                        ]],
                        title:"租户列表",
                        pagination:true,
                        singleSelect:true,
                        pageNumber:1,
                        pageList:[5,10,15],
                        pageSize:5,
                        toolbar:[{
                            text:"新增",
                            iconCls:"icon-add",
                            handler:function() {
                                tenant.modify();
                            }
                        },"-",{
                            text:"查询",
                            iconCls:"icon-search",
                            handler:function() {
                                tenant.search();
                            }
                        },"-", {
                            text:"修改",
                            iconCls:"icon-edit",
                            handler:function(){
                                var selected = tenant.datagrid.datagrid("getSelected");
                                if (selected){
                                    tenant.modify(selected);
                                } else {
                                    $.messager.alert("提示", "请选择一条要修改的记录！", "warning");
                                }
                            }
                        }, "-",{
                            text:"重置",
                            iconCls:"icon-redo",
                            handler:function(){
                                $("#queryForm").form("clear");
                                tenant.search();
                            }
                        },"-"]
                    });
                    tenant.search();
                },
                search:function () {
                    tenant.datagrid.datagrid({
                        url:window.contextPath+"/route/queryPage",
                        queryParams:{
                            tenant:$("#tenantIdQry").val(),
                            dbtype:$("#dbTypeQry").combobox("getValue"),
                            iseffective:$("#isEffectiveQry").combobox("getValue")
                        }
                    });
                },
                modify:function (selected) {
                    var needAdd = false;
                    $("#tenantDialog").show();
                    var dialog = $("#tenantDialog").dialog({
                        title:"用户信息查看",
                        maximized:false,/*不需要最大化*/
                        modal:true,
                        closable:false,/*不需要关闭*/
                        width:500,
                        height:400,
                        buttons:[{
                            text:"确定",
                            iconCls:"icon-save",
                            handler:function(){
                                if (tenant.validate()) {
                                    tenant.add({
                                        id:$("#tenantId").val(),
                                        tenant:$("#tenant").val(),
                                        username:$("#username").val(),
                                        password:$("#password").val(),
                                        url:$("#url").val(),
                                        dbtype:$("#dbType").combobox("getText"),
                                        iseffective:$("#isEffective").combobox("getText"),
                                        needAdd:needAdd
                                    });
                                    dialog.dialog("close");
                                    tenant.search();
                                }else {
                                    $.messager.alert({title:"温馨提示",msg:"请核对数据后提交！"});
                                }
                            }
                        },{
                            text:"关闭",
                            iconCls:"icon-cancel",
                            handler:function(){
                                dialog.dialog("close");
                                $("#tenantDialog").hide();
                            }
                        }]
                    });
                    dialog.dialog("open");

                    if (selected) {
                        $("#tenantId").val(selected["id"]);
                        $("#tenant").val(selected["tenant"]);
                        $("#username").val(selected["username"]);
                        $("#password").val(selected["password"]);
                        $("#url").val(selected["url"]);
                        $("#dbType").combobox("setText", selected["dbtype"]);
                        $("#isEffective").combobox("setText", selected["iseffective"]);
                    }else{
                        $("#tenantDialog form").form("clear");
                        $.ajax({
                            dataType : "json",
                            type : "POST",
                            url : window.contextPath +"/route/getId",
                            success : function(data) {
                                if (data) {
                                    $("#tenantId").val(data);
                                    needAdd = true;
                                }
                            }
                        });
                    }
                },
                add:function (params) {
                    $.ajax({
                        dataType : "json",
                        type : "POST",
                        url : window.contextPath +"/route/save",
                        data : params,
                        success : function(data) {
                            $.messager.show({title : "提示", msg : "保存成功", showType : "show" });
                        }
                    });
                },
                validate:function () {
                    var rstFlag = true;
                    if (!$("#url").val()||
                        !$("#username").val()||
                        !$("#password").val()||
                        !$("#tenant").val()||
                        !$("#dbType").combobox("getText")||
                        !$("#isEffective").combobox("getText")) {
                        rstFlag = false;
                    }
                    return rstFlag;
                }
            };
            tenant.init();
        });
    </script>
</head>
<body class="easyui-layout">
    <div data-options="region:'north',border:false" style="overflow: hidden;">
        <div class="operation_table">
            <div class="title">
                <div class="text">条件过滤查询</div>
            </div>
            <div class="input" >
                <form id="queryForm" style="margin-bottom: 5px;">
                    <table>
                        <tr>
                            <th>租户标示</th>
                            <td><input type="text" id="tenantIdQry" /></td>
                            <th>数据库类型</th>
                            <td><select id="dbTypeQry" ></select></td>
                            <th>是否有效</th>
                            <td><select id="isEffectiveQry" name="serviceNo"></select></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
    <div data-options="region:'center',border:false">
        <table id="tenant_list" border="false"></table>
    </div>
    <div id="tenantDialog" style="width: 400px;display: none;">
        <div class='input'>
            <form>
                <table width="100%">
                    <tr>
                        <th><span style="color: red">*</span>ID:</th>
                        <td><input id="tenantId" type="text" readonly/></td>
                    </tr>
                    <tr>
                        <th><span style="color: red">*</span>租户标识:</th>
                        <td><input id="tenant" type="text"/></td>
                    </tr>
                    <tr>
                        <th><span style="color: red">*</span>用户名:</th>
                        <td><input id="username" type="text"/></td>
                    </tr>
                    <tr>
                        <th><span style="color: red">*</span>密码:</th>
                        <td><input id="password" type="text"/></td>
                    </tr>
                    <tr>
                        <th><span style="color: red">*</span>连接串:</th>
                        <td><textarea style="height: 32px;width: 300px;" id="url"></textarea></td>
                    </tr>
                    <tr>
                        <th><span style="color: red">*</span>数据库类型:</th>
                        <td><input id="dbType" type="text"/></td>
                    </tr>
                    <tr>
                        <th><span style="color: red">*</span>是否有效:</th>
                        <td><input id="isEffective" type="text"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</body>
</html>
