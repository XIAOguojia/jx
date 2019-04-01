app.controller('cartController', function ($scope, cartService) {
    //查询购物车列表
    $scope.findCartList = function () {
        cartService.findCartList().success(
            function (response) {
                $scope.cartList = response;
                //求合计数
                $scope.totalValue = cartService.sum($scope.cartList);
            }
        )
    };

    //购物车内商品数量加减
    $scope.addGoodsToCartList = function (itemId,num) {
        cartService.addGoodsToCartList(itemId, num).success(
            function (response) {
                if (response.success) {
                    $scope.findCartList();//刷新列表
                } else {
                    alert(response.message);//弹出错误提示
                }

            }
        )
    };


});
