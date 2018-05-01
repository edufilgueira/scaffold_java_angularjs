appTeste.controller("clienteController", function($scope, $http){
	
	$scope.clientes=[];
	$scope.cliente={};

	
	carregar= function (){
		$http({method:'GET', url:'http://localhost:8080/cliente/buscartodos'})
		.then(function (response){
			$scope.clientes=response.data;
			
		} , function (response){
			console.log(response.data);
			console.log(response.status);
			
		});
	};
	
	$scope.salvar= function (){
		
		//if ($scope.frmCedula.$valid){
			$http({method:'POST', url:'http://localhost:8080/cliente/salvar',data:$scope.assunto})
			.then(function (response){
				//$scope.assuntos.push(response.data) ;
				carregar();
				$scope.cancelar();
				//$scope.frmCedula.$setPristine(true);
				
				
			} , function (response){
				console.log(response.data);
				console.log(response.status);
				
			});
		
		//}else {
		//	window.alert("Dados inválidos!");
		//}
	}
	
	$scope.excluir=function(assunto){
		$http({method:'DELETE', url:'http://localhost:8080/assuntoapi/'+assunto.id})
		.then(function (response){
			
			pos = $scope.assuntos.indexOf(assunto);
			$scope.assuntos.splice(pos , 1);
			
		} , function (response){
			console.log(response.data);
			console.log(response.status);
			
		});	
		
	};
	
	
	$scope.alterar= function (cli){
		$scope.assunto = angular.copy(cli);
	};
	
	
	$scope.cancelar=function (){
		$scope.assunto={};
	};
	
	carregar();	
	

	
});appTeste.controller("classeController", function($scope, $http){
	
	$scope.classes=[];
	$scope.classe={};

	
	carregar= function (){
		$http({method:'GET', url:'http://localhost:8080/classe/buscartodos'})
		.then(function (response){
			$scope.classes=response.data;
			
		} , function (response){
			console.log(response.data);
			console.log(response.status);
			
		});
	};
	
	$scope.salvar= function (){
		
		//if ($scope.frmCedula.$valid){
			$http({method:'POST', url:'http://localhost:8080/classe/salvar',data:$scope.assunto})
			.then(function (response){
				//$scope.assuntos.push(response.data) ;
				carregar();
				$scope.cancelar();
				//$scope.frmCedula.$setPristine(true);
				
				
			} , function (response){
				console.log(response.data);
				console.log(response.status);
				
			});
		
		//}else {
		//	window.alert("Dados inválidos!");
		//}
	}
	
	$scope.excluir=function(assunto){
		$http({method:'DELETE', url:'http://localhost:8080/assuntoapi/'+assunto.id})
		.then(function (response){
			
			pos = $scope.assuntos.indexOf(assunto);
			$scope.assuntos.splice(pos , 1);
			
		} , function (response){
			console.log(response.data);
			console.log(response.status);
			
		});	
		
	};
	
	
	$scope.alterar= function (cli){
		$scope.assunto = angular.copy(cli);
	};
	
	
	$scope.cancelar=function (){
		$scope.assunto={};
	};
	
	carregar();	
	

	
});