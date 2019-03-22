app.controller('itemController', function ($scope) {
    $scope.num = 1;

    $scope.addNum = function (x) {
        $scope.num = parseInt(getdata()) + x;
        if ($scope.num < 1) {
            $scope.num = 1;
        }

        $("#iupt").load(location.href + " #iupt");

    };

    //获取输入框的内容
    function getdata() {
        var str = document.getElementById("iupt");
        return str.value;
    };

    $scope.specificationItems = {};//记录用户选择的规格

    //用户选择规格
    $scope.selectSpecification = function (name, value) {
        $scope.specificationItems[name] = value;
        searchSku();
    };

    //判断某规格选项是否被用户选中
    $scope.isSelected = function (name, value) {
        if ($scope.specificationItems[name] == value) {
            return true;
        } else {
            return false;
        }
    };

    $scope.loadSku = function () {
        $scope.sku = skuList[0];
        $scope.specificationItems = JSON.parse(JSON.stringify($scope.sku.spec));
    };

    searchSku = function () {
        for (var i = 0; i < skuList.length; i++) {
            if (matchObject(skuList[i].spec, $scope.specificationItems)){
                $scope.sku = skuList[i];
                return;
            }
        }

        //没找到对应规格的sku
        $scope.sku = {id: 0, 'title': '-------', 'price': 0};
    };

    matchObject = function (map1, map2) {
        for (var k in map1) {
            if (map1[k] != map2[k]) {
                return false;
            }
        }

        for (var k in map2) {
            if (map1[k] != map2[k]) {
                return false;
            }
        }
        return true;

    };


    //添加商品到购物车
    $scope.addToCart = function () {
        alert('skuid:' + $scope.sku.id);
    };


});