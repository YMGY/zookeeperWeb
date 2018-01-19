<!-- Menu Bar -->
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">

        <div class="navbar-header">
            <a href="/home/index"><img src="/images/shcem.ico"/></a>
            &nbsp;&nbsp;
        </div>

        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">

                <li><a href="#" data-toggle="modal" data-target="#addNodeModal">新增节点</a></li>
                <li><a href="#" data-toggle="modal" data-target="#addPropertyModal" id="addPropertyBtn">新增属性</a></li>
                <li><a href="javascript:void(0)" @click="delNodeBtn()">删除节点</a></li>
                <li><a href="#" data-toggle="modal" data-target="#importModal">导入</a></li>
                <li><a href="/export?zkPath=" target="_blank">输出</a></li>
                <li><a href="#" data-toggle="modal" data-target="#searchModal">搜索</a></li>
                <li><a href="/history">History</a></li>
                <li><a href="/monitor">Monitor</a></li>

            </ul>
            <ul class="nav navbar-nav navbar-right">
                <#if authName??>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <span class="glyphicon glyphicon-user"></span>&nbsp;${authName}&nbsp;<b class="caret"></b>&nbsp;</a>
                    <ul class="dropdown-menu">
                        <li><a href="/logout">Logout</a></li>
                    </ul>
                </li>
                <#else>
                <li><a href="/login">Login</a></li>
                </#if> 

            </ul>
        </div>

    </div>
</div>


