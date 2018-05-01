package com.scaffold;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScaffoldController {
	
	@RequestMapping(method=RequestMethod.GET, value="/scaffold")
	public void GerarCodigo() throws IOException {
		List<Modelo> modelos = new ArrayList<>();
		Map<String, String> atributos = new HashMap<String, String>();
		
		FileReader arquivo = new FileReader(new File("mapa.txt"));
		BufferedReader ler_arquivo = new BufferedReader(arquivo);
		String linha = ler_arquivo.readLine();
		Modelo modelo = null;
		while(linha != null) 
		{
			if(linha.contains("-"))
				linha = ler_arquivo.readLine();
			if(linha.contains("{"))
			{
				modelo = new Modelo();
				linha = ler_arquivo.readLine();
				modelo.setPacote(linha);
				System.out.println(linha);
				linha = ler_arquivo.readLine();
				modelo.setModelo(linha);
				System.out.println(linha);
				linha = ler_arquivo.readLine();
				
				modelos.add(modelo);
			}
			if(linha.contains("["))
				linha = ler_arquivo.readLine();
			
			if(!linha.equals("[")) {
				String[] divideLinha = linha.split(":");
				atributos.put(divideLinha[0], divideLinha[1]);
				System.out.println(divideLinha[0]+":"+divideLinha[1]);
			}
			linha = ler_arquivo.readLine();
			iniciarGeracao(modelo,atributos);
		}
		ler_arquivo.close();
		
		gerarDependencias(modelos);
	}
	
	private void gerarDependencias(List<Modelo> modelos) throws IOException {
		gerarJsApp(modelos);
		gerarMain(modelos);
		gerarControllerJs(modelos);
	}

	private void iniciarGeracao(Modelo modelo, Map<String, String> atributos) throws IOException {
		novoDiretorio("scaffold");
		modelo.setAtributos(atributos);
		gerarModel(modelo);
		gerarRepository(modelo);
		gerarService(modelo);
		gerarController(modelo);
	}
	
	private void novoDiretorio(String caminho) {
		File diretorio = new File(caminho);
        diretorio.mkdir();
	}
	
	private void gerarModel(Modelo modelo) throws IOException {
		novoDiretorio("scaffold\\model");
		FileWriter arquivo;

		arquivo = new FileWriter("scaffold/model/"+new File(modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+".java"));
		BufferedWriter novaLinha = new BufferedWriter(arquivo);
		novaLinha.write("package "+modelo.getPacote().toLowerCase()+".model;");
		novaLinha.newLine();
		novaLinha.newLine();
		novaLinha.write("import javax.persistence.Entity;");
		novaLinha.newLine();
		novaLinha.write("import javax.persistence.GeneratedValue;");
		novaLinha.newLine();
		novaLinha.write("import javax.persistence.GenerationType;");
		novaLinha.newLine();
		novaLinha.write("import javax.persistence.Id;");
		novaLinha.newLine();
		novaLinha.write("import javax.persistence.JoinColumn;");
		novaLinha.newLine();
		novaLinha.write("import javax.persistence.ManyToOne;");
		novaLinha.newLine();
		novaLinha.write("import javax.persistence.SequenceGenerator;");
		novaLinha.newLine();
		novaLinha.newLine();
		novaLinha.write("@Entity");
		novaLinha.newLine();
		novaLinha.write("public class "+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+" {");
		novaLinha.newLine();
		novaLinha.newLine();
		//Gerar classe
		for (String key : modelo.getAtributos().keySet()) {
			String value =  modelo.getAtributos().get(key);
			if(value.equals("id")) {
				novaLinha.write("	@Id");
				novaLinha.newLine();
				novaLinha.write("	@SequenceGenerator(name = \""+modelo.getModelo().toLowerCase()+"_seq\", sequenceName = \""+modelo.getModelo().toLowerCase()+"_seq\", allocationSize=1)");
				novaLinha.newLine();
				novaLinha.write("	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = \""+modelo.getModelo().toLowerCase()+"_seq\")");
				novaLinha.newLine();
				novaLinha.write("	private "+key.substring(0,1).toUpperCase()+key.substring(1)+" "+value.toLowerCase()+";");
				novaLinha.newLine();
				novaLinha.newLine();
			}
			else {
				if(!key.contains("(")) {
					novaLinha.write("	private "+key.substring(0,1).toUpperCase()+key.substring(1)+" "+value.toLowerCase()+";");
					novaLinha.newLine();
					novaLinha.newLine();
				}
				else {
					key = key.substring(1, key.length()-1);
					novaLinha.write("	@JoinColumn(nullable = false)");
					novaLinha.newLine();
					novaLinha.write("	@ManyToOne");
					novaLinha.newLine();
					novaLinha.write("	private "+key.substring(0,1).toUpperCase()+key.substring(1)+" "+value.toLowerCase()+";");
					novaLinha.newLine();
					novaLinha.newLine();
				}
			}
		}
		novaLinha.newLine();
		novaLinha.newLine();
		
		//Gerar Get e Sets
		for (String key : modelo.getAtributos().keySet()) 
		{
			String value =  modelo.getAtributos().get(key);
			if(key.contains("("))
				key = key.substring(1, key.length()-1);
			
			novaLinha.write("	public "+key.substring(0,1).toUpperCase()+key.substring(1)+" get"+value.substring(0,1).toUpperCase()+value.substring(1)+"() {");
			novaLinha.newLine();
			novaLinha.write("		return "+value.toLowerCase()+";");
			novaLinha.newLine();
			novaLinha.write("	}");
			novaLinha.newLine();
			novaLinha.write("	public void set"+value.substring(0,1).toUpperCase()+value.substring(1)+"("+key.substring(0,1).toUpperCase()+key.substring(1)+" "+value.toLowerCase()+") {");
			novaLinha.newLine();
			novaLinha.write("		this."+value.toLowerCase()+" = "+value.toLowerCase()+";");
			novaLinha.newLine();
			novaLinha.write("	}");
			novaLinha.newLine();
		}
		novaLinha.write("}");
		novaLinha.close();

	}
	
	
	private void gerarRepository(Modelo modelo) throws IOException {
		novoDiretorio("scaffold\\repository");
		
		FileWriter arquivo;
		arquivo = new FileWriter(new File("scaffold/repository/"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"Repository.java"));
		BufferedWriter novaLinha = new BufferedWriter(arquivo);
		String codigo = "package "+modelo.getPacote()+".repository;\r\n" + 
				"import org.springframework.data.jpa.repository.JpaRepository;\r\n" + 
				"import org.springframework.stereotype.Repository;\r\n" + 
				"\r\n" + 
				"import "+modelo.getPacote()+".model."+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+";\r\n" +
				"\r\n" +
				"@Repository\r\n" + 
				"public interface "+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"Repository extends JpaRepository<"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+", Integer>{\r\n" +
				"\r\n" + 
				"}\r\n" + 
				"";
		novaLinha.write(codigo);
		novaLinha.close();
	}
	
	private void gerarService(Modelo modelo) throws IOException {
		novoDiretorio("scaffold\\service");
		
		FileWriter arquivo;
		arquivo = new FileWriter(new File("scaffold/service/"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"Service.java"));
		BufferedWriter novaLinha = new BufferedWriter(arquivo);
		String codigo = "package "+modelo.getPacote()+".service;\r\n" + 
				"\r\n" + 
				"import java.util.Collection;\r\n" + 
				"\r\n" + 
				"import org.springframework.beans.factory.annotation.Autowired;\r\n" + 
				"import org.springframework.stereotype.Service;\r\n" + 
				"\r\n" + 
				"import "+modelo.getPacote()+".model."+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+";\r\n" + 
				"import "+modelo.getPacote()+".repository."+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"Repository;\r\n" + 
				"\r\n" + 
				"@Service\r\n" + 
				"public class "+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"Service {\r\n" + 
				"\r\n" + 
				"	@Autowired\r\n" + 
				"	"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"Repository "+modelo.getModelo().toLowerCase()+"Repository;\r\n" + 
				"\r\n" + 
				"	public "+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+" salvar("+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+" "+modelo.getModelo().toLowerCase()+") {\r\n" + 
				"\r\n" + 
				"		"+modelo.getModelo().toLowerCase()+"Repository.save("+modelo.getModelo().toLowerCase()+");\r\n" + 
				"		return "+modelo.getModelo().toLowerCase()+";\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"	public Collection<"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"> buscarTodos() {\r\n" + 
				"		return  "+modelo.getModelo().toLowerCase()+"Repository.findAll();\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"	public void excluir("+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+" "+modelo.getModelo().toLowerCase()+") {\r\n" + 
				"		"+modelo.getModelo().toLowerCase()+"Repository.delete("+modelo.getModelo().toLowerCase()+");\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"	public "+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+" buscarPorId(Integer id) {\r\n" + 
				"		return "+modelo.getModelo().toLowerCase()+"Repository.findOne(id); \r\n" + 
				"	}\r\n" + 
				"	\r\n" +  
				"}";
		novaLinha.write(codigo);
		novaLinha.close();
	}
	
	private void gerarController(Modelo modelo) throws IOException {
		novoDiretorio("scaffold\\controller");
		FileWriter arquivo;
		arquivo = new FileWriter(new File("scaffold/controller/"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"Controller.java"));
		BufferedWriter novaLinha = new BufferedWriter(arquivo);
		String codigo = "package "+modelo.getPacote()+".controller;\r\n" + 
				"\r\n" + 
				"import java.util.Collection;\r\n" + 
				"\r\n" + 
				"import org.springframework.beans.factory.annotation.Autowired;\r\n" + 
				"import org.springframework.http.HttpStatus;\r\n" + 
				"import org.springframework.http.MediaType;\r\n" + 
				"import org.springframework.http.ResponseEntity;\r\n" + 
				"import org.springframework.web.bind.annotation.PathVariable;\r\n" + 
				"import org.springframework.web.bind.annotation.RequestBody;\r\n" + 
				"import org.springframework.web.bind.annotation.RequestMapping;\r\n" + 
				"import org.springframework.web.bind.annotation.RequestMethod;\r\n" + 
				"import org.springframework.web.bind.annotation.RestController;\r\n" + 
				"\r\n" + 
				"import "+modelo.getPacote()+".model."+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+";\r\n" + 
				"import "+modelo.getPacote()+".service."+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"Service;\r\n" + 
				"\r\n" + 
				"@RestController\r\n" + 
				"public class "+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"Controller {\r\n" + 
				"\r\n" + 
				"	@Autowired\r\n" + 
				"	"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"Service "+modelo.getModelo().toLowerCase()+"Service;\r\n" + 
				"	\r\n" + 
				"	\r\n" + 
				"	@RequestMapping(method=RequestMethod.POST, value=\"/"+modelo.getModelo().toLowerCase()+"/salvar\", consumes=MediaType.APPLICATION_JSON_VALUE)\r\n" + 
				"	public ResponseEntity<"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+">salvar(@RequestBody "+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+" "+modelo.getModelo().toLowerCase()+") {\r\n" + 
				"\r\n" + 
				"		"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+" alterado = "+modelo.getModelo().toLowerCase()+"Service.salvar("+modelo.getModelo().toLowerCase()+");\r\n" + 
				"		return new ResponseEntity<"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+">(alterado, HttpStatus.CREATED);\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"	@RequestMapping(method=RequestMethod.GET, value=\"/"+modelo.getModelo().toLowerCase()+"/buscartodos\", produces=MediaType.APPLICATION_JSON_VALUE)\r\n" + 
				"	public ResponseEntity<Collection<"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+">> buscarTodos() {\r\n" + 
				"		\r\n" + 
				"		return new ResponseEntity<>("+modelo.getModelo().toLowerCase()+"Service.buscarTodos(), HttpStatus.OK);\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"	@RequestMapping(method=RequestMethod.DELETE, value=\"/"+modelo.getModelo().toLowerCase()+"/excluir/{id}\")\r\n" + 
				"	public ResponseEntity<String> excluir(@PathVariable Integer id) {\r\n" + 
				"		\r\n" + 
				"		"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+" "+modelo.getModelo().toLowerCase()+" = "+modelo.getModelo().toLowerCase()+"Service.buscarPorId(id);\r\n" + 
				"		"+modelo.getModelo().toLowerCase()+"Service.excluir("+modelo.getModelo().toLowerCase()+");\r\n" + 
				"		if("+modelo.getModelo().toLowerCase()+" == null)\r\n" + 
				"		{\r\n" + 
				"			return new ResponseEntity<>(HttpStatus.NOT_FOUND);\r\n" + 
				"		}\r\n" + 
				"		else\r\n" + 
				"			return new ResponseEntity<>(HttpStatus.OK);\r\n" + 
				"\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"	@RequestMapping(method=RequestMethod.PUT, value=\"/"+modelo.getModelo().toLowerCase()+"/alterar\", consumes=MediaType.APPLICATION_JSON_VALUE)\r\n" + 
				"	public ResponseEntity<"+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+"> alterar(@RequestBody "+modelo.getModelo().substring(0,1).toUpperCase()+modelo.getModelo().substring(1)+" "+modelo.getModelo().toLowerCase()+") {\r\n" + 
				"		\r\n" + 
				"		return new ResponseEntity<>("+modelo.getModelo().toLowerCase()+"Service.salvar("+modelo.getModelo().toLowerCase()+"), HttpStatus.OK);\r\n" + 
				"	}\r\n" + 
				"}\r\n" + 
				"";
		novaLinha.write(codigo);
		novaLinha.close();
	}
	
	private String retornarNomeProjeto(Modelo modelo) {
		String lista = modelo.getPacote();
		lista = lista.trim();
		lista = lista.replace(".", ":");
		String[] packageProjetos =  lista.split(":");
		String packageProjeto = packageProjetos[1];
		return packageProjeto;
	}
	
	private String primeiraLetraMaiuscula(String texto) {
		texto = texto.toLowerCase();
		texto = texto.substring(0,1).toUpperCase()+texto.substring(1);
		return texto;
	}
	
	private void gerarJsApp(List<Modelo> modelos) throws IOException {
		novoDiretorio("scaffold\\js");
		
		String nomeProjeto = retornarNomeProjeto(modelos.get(0));
		nomeProjeto = primeiraLetraMaiuscula(nomeProjeto);
		
		FileWriter arquivo;
		arquivo = new FileWriter(new File("scaffold/js/app.js"));
		BufferedWriter novaLinha = new BufferedWriter(arquivo);
		String codigo = "var app"+nomeProjeto+" = angular.module(\"app"+nomeProjeto+"\", [ 'ngRoute' ]);\r\n" + 
				"\r\n" + 
				"app"+nomeProjeto.substring(0,1).toUpperCase()+nomeProjeto.substring(1)+".config(function($routeProvider, $locationProvider) {\r\n" + 
				"\r\n" + 
				"	$routeProvider\r\n";
				
				for(int i = 0; i < modelos.size(); i++) {
					codigo += "	.when(\"/"+modelos.get(i).getModelo().toLowerCase()+"\", {\r\n" + 
					"		templateUrl : 'view/"+modelos.get(i).getModelo().toLowerCase()+".html',\r\n" + 
					"		controller : '"+modelos.get(i).getModelo().toLowerCase()+"Controller'\r\n" + 
					"	})";
				}
				
				codigo += ".otherwise({\r\n" + 
				"		rediretTo : '/'\r\n" + 
				"	});\r\n" + 
				"	\r\n" + 
				"	//$locationProvider.html5Mode(true);\r\n" + 
				"	\r\n" + 
				"});";
		novaLinha.write(codigo);
		novaLinha.close();
	}
	
	private void gerarControllerJs(List<Modelo> modelos) throws IOException {
		novoDiretorio("scaffold\\js\\controller");
		
		String nomeProjeto = retornarNomeProjeto(modelos.get(0));
		nomeProjeto = primeiraLetraMaiuscula(nomeProjeto);
		String nomeModelo = modelos.get(0).getModelo().toLowerCase();
		
		String codigo = "";
		for(int i = 0; i < modelos.size(); i++) {
			FileWriter arquivo;
			arquivo = new FileWriter(new File("scaffold/js/controller/"+modelos.get(i).getModelo().toLowerCase()+"-controller.js"));
			BufferedWriter novaLinha = new BufferedWriter(arquivo);
			
			codigo += "app"+nomeProjeto+".controller(\""+modelos.get(i).getModelo().toLowerCase()+"Controller\", function($scope, $http){\r\n" + 
					"	\r\n" + 
					"	$scope."+modelos.get(i).getModelo().toLowerCase()+"s=[];\r\n" + 
					"	$scope."+modelos.get(i).getModelo().toLowerCase()+"={};\r\n" + 
					"\r\n" + 
					"	\r\n" + 
					"	carregar= function (){\r\n" + 
					"		$http({method:'GET', url:'http://localhost:8080/"+modelos.get(i).getModelo().toLowerCase()+"/buscartodos'})\r\n" + 
					"		.then(function (response){\r\n" + 
					"			$scope."+modelos.get(i).getModelo().toLowerCase()+"s=response.data;\r\n" + 
					"			\r\n" + 
					"		} , function (response){\r\n" + 
					"			console.log(response.data);\r\n" + 
					"			console.log(response.status);\r\n" + 
					"			\r\n" + 
					"		});\r\n" + 
					"	};\r\n" + 
					"	\r\n" + 
					"	$scope.salvar= function (){\r\n" + 
					"		\r\n" + 
					"		//if ($scope.frmCedula.$valid){\r\n" + 
					"			$http({method:'POST', url:'http://localhost:8080/"+modelos.get(i).getModelo().toLowerCase()+"/salvar',data:$scope.assunto})\r\n" + 
					"			.then(function (response){\r\n" + 
					"				//$scope.assuntos.push(response.data) ;\r\n" + 
					"				carregar();\r\n" + 
					"				$scope.cancelar();\r\n" + 
					"				//$scope.frmCedula.$setPristine(true);\r\n" + 
					"				\r\n" + 
					"				\r\n" + 
					"			} , function (response){\r\n" + 
					"				console.log(response.data);\r\n" + 
					"				console.log(response.status);\r\n" + 
					"				\r\n" + 
					"			});\r\n" + 
					"		\r\n" + 
					"		//}else {\r\n" + 
					"		//	window.alert(\"Dados inválidos!\");\r\n" + 
					"		//}\r\n" + 
					"	}\r\n" + 
					"	\r\n" + 
					"	$scope.excluir=function(assunto){\r\n" + 
					"		$http({method:'DELETE', url:'http://localhost:8080/assuntoapi/'+assunto.id})\r\n" + 
					"		.then(function (response){\r\n" + 
					"			\r\n" + 
					"			pos = $scope.assuntos.indexOf(assunto);\r\n" + 
					"			$scope.assuntos.splice(pos , 1);\r\n" + 
					"			\r\n" + 
					"		} , function (response){\r\n" + 
					"			console.log(response.data);\r\n" + 
					"			console.log(response.status);\r\n" + 
					"			\r\n" + 
					"		});	\r\n" + 
					"		\r\n" + 
					"	};\r\n" + 
					"	\r\n" + 
					"	\r\n" + 
					"	$scope.alterar= function (cli){\r\n" + 
					"		$scope.assunto = angular.copy(cli);\r\n" + 
					"	};\r\n" + 
					"	\r\n" + 
					"	\r\n" + 
					"	$scope.cancelar=function (){\r\n" + 
					"		$scope.assunto={};\r\n" + 
					"	};\r\n" + 
					"	\r\n" + 
					"	carregar();	\r\n" + 
					"	\r\n" + 
					"\r\n" + 
					"	\r\n" + 
					"});";
			novaLinha.write(codigo);
			novaLinha.close();
		}		
				
		
	}

	private void gerarMain(List<Modelo> modelos) throws IOException {
		novoDiretorio("scaffold\\js");
		
		String nomeProjeto = retornarNomeProjeto(modelos.get(0));
		nomeProjeto = primeiraLetraMaiuscula(nomeProjeto);
		
		FileWriter arquivo;
		arquivo = new FileWriter(new File("scaffold/js/main.js"));
		BufferedWriter novaLinha = new BufferedWriter(arquivo);
		String codigo = "app"+nomeProjeto+".controller(\"main\", function($scope, $location, $route, $routeParams){\r\n" + 
				"	$scope.$location= $location;\r\n" + 
				"	$scope.$route=$route;\r\n" + 
				"	$scope.$routeParams= $routeParams;\r\n" + 
				"});";
		novaLinha.write(codigo);
		novaLinha.close();
	}
}
