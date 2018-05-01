var appTeste = angular.module("appTeste", [ 'ngRoute' ]);

appTeste.config(function($routeProvider, $locationProvider) {

	$routeProvider
	.when("/cliente", {
		templateUrl : 'view/cliente.html',
		controller : 'clienteController'
	})	.when("/classe", {
		templateUrl : 'view/classe.html',
		controller : 'classeController'
	}).otherwise({
		rediretTo : '/'
	});
	
	//$locationProvider.html5Mode(true);
	
});