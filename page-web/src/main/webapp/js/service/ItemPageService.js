app.service('ItemPageService',function($http){
	
	
	this.genItemHtml =function(goodsId){
		return $http.post('goods/genItemHtml.do?goodsId'+goodsId);
	}
	
});