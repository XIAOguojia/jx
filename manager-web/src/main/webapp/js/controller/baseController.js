app.controller('baseController',function ($scope) {
    //分页控件配置 currentPage：当前页码 totalItems:总条数 itemsPerPage:每页记录数
    //perPageOptions：页码选项 onChange：更改页面时触发事件
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    };

    //重新加载列表 数据
    $scope.reloadList=function(){
        //切换页码
        $scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    //用户勾选的要删除的品牌的id数组
    $scope.selectIds = [];

    //更新复选
    $scope.updateSelection =function ($event,id) {
        if ($event.target.checked){
            //如果是被选中,则增加到数组
            $scope.selectIds.push(id);
        }else {
            //找到位置
            var index = $scope.selectIds.indexOf(id);
            //删除
            $scope.selectIds.splice(index,1);
        }
    };
});