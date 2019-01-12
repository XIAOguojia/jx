//品牌控制层
app.controller("brandController",function ($scope,$controller,brandService) {
    $controller('baseController',{$scope:$scope});//继承

    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        )
    };


    //分页
    $scope.findPage=function(page,size){
        brandService.findPage(page,size).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    };

    //新增或修改
    $scope.save=function () {
        var object = null;
        if ($scope.entity.id != null){
            object = brandService.update($scope.entity);
        }else {
            object = brandService.add($scope.entity);
        }
        object.success(
            function (response) {
                if (response.success){
                    //添加成功后重新加载
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            }
        )
    };

    //查找实体
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        )
    };



    //删除
    $scope.del = function () {
        brandService.del($scope.selectIds).success(
            function (response) {
                if (response.success){
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            }
        )
    };

    $scope.searchEntity={};//定义搜索对象
    //条件查询
    $scope.search = function (page,size) {
        brandService.search(page,size,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }


});
