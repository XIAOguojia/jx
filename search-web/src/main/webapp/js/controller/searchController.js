app.controller('searchController', function ($scope, $location, searchService) {

    //搜索
    $scope.search = function () {
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                buildPageLabel();
            }
        );
    };

    //定义搜索对象
    $scope.searchMap = {
        'keywords': '',//搜索关键字
        'category': '',//按分类搜索
        'brand': '',//按品牌搜索
        'spec': {},//按规格搜索
        'price': '',//按价格区间搜索
        'pageNo': 1,//页码
        'pageSize': 15,//页面大小
        'sort': '',//排序方式
        'sortField': ''//排序字段
    };

    //添加搜索项
    $scope.addSearchItem = function (key, value) {
        if (key == 'category' || key == 'brand' || key == 'price') {
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
        //执行搜索
        $scope.search();
    };

    //移出搜索项
    $scope.removeSearchItem = function (key) {
        if (key == 'category' || key == 'brand' || key == 'price') {
            $scope.searchMap[key] = "";
        } else {
            //移出该属性
            delete $scope.searchMap.spec[key];
        }
        //执行搜索
        $scope.search();
    };

    //构建分页标签
    buildPageLabel = function () {
        //新增分页栏属性
        $scope.pageLabel = [];
        //开始页码
        var firstPage = 1;
        //最后页码
        var maxPageNo = $scope.resultMap.totalPages;
        //截止页码
        var lastPageNo = maxPageNo;
        //前边有点
        $scope.firstDot = true;
        $scope.lastDot = true;
        //后面有点
        //如果总页数大于5页,显示部分页码
        if ($scope.resultMap.totalPages > 5) {
            //如果当前页小于等于3
            if ($scope.searchMap.pageNo <= 3) {
                //前5页
                lastPageNo = 5;
                $scope.firstDot = false;
            }//如果当前页大于等于最大页码-2
            else if ($scope.searchMap.pageNo >= maxPageNo - 2) {
                //后5页
                firstPage = maxPageNo - 4;
                $scope.lastDot = false;
            } else {
                //显示当前页为中心的5页
                firstPage = $scope.searchMap.pageNo - 2;
                lastPageNo = $scope.searchMap.pageNo + 2;

            }
        } else {
            $scope.firstDot = false;
            $scope.lastDot = false;
        }

        //循环产生标签页码
        for (var i = firstPage; i <= lastPageNo; i++) {
            $scope.pageLabel.push(i);
        }
    };

    //根据输入页码进行查询
    $scope.queryByPage = function (pageNo) {
        //页码验证
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            alert("输入页码错误");
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    };

    //判断当前页是否为第一页
    $scope.isFirstPage = function () {
        if ($scope.searchMap.pageNo == 1) {
            return true;
        } else {
            return false;
        }
    };

    //判断当前页是否最后一页
    $scope.isLastPage = function () {
        if ($scope.searchMap.pageNo == $scope.resultMap.totalPages) {
            return true;
        } else {
            return false;
        }
    };

    //排序查找
    $scope.sortSearch = function (sort, sortField) {
        $scope.searchMap.sort = sort;
        $scope.searchMap.sortField = sortField;
        $scope.search();
    };

    //判断关键字是否为品牌
    $scope.KeywordsIsBrand = function () {
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text) >= 0) {
                return true;
            }
        }
        return false;
    };

    //加载查询字符串
    $scope.loadkeywords=function(){
        $scope.searchMap.keywords=  $location.search()['keywords'];
        $scope.search();
    };

});