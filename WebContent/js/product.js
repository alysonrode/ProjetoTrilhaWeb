//cria um objeto codigo
COLDIGO.produto = new Object();

$(document).ready(function(){
		//carrega as marcas registradas no banco de dados no select que está no formlário
	
	COLDIGO.produto.carregarMarcas = function(){
	alert("Tentando buscar marcas");
		$.ajax({
			type: "GET",
			url: COLDIGO.PATH+"marca/buscar",
			success: function (marca) {
				//Aqui, ele verifica se a lista de marcas não está vazia, e limpa o campo selMarca, e adiciona um campo chamado Escolha, com um valor vazio.
				//Após isso, ele começa a executar um for, que vai adicionando os elementos com seus respectivos valores.
				
				if(marca!=""){
				
					$("#selMarca").html("");
					var option = document.createElement("option");
					option.setAttribute ("value", "");
					option.innerHTML=("Escolha");
					$("#selMarca").append(option);
					
					console.log("cheguei")	
					for (var i = 0; i < marca.length; i++){
						console.log("cheguei")
						var option = document.createElement("option");
						option.setAttribute ("value", marca[i].id);
						option.innerHTML = (marca[i].nome);
						$("#selMarca").append(option);
						alert("Marca obtida com sucesso!")
					}
					//Caso a lista esteja vazia, ele irá criar um elemento com a seguinte mensagem mostrada na linha 39
				}else{
					$("#selMarca").html("");
					
					var option = document.createElement("option");
					option.setAttribute ("value", "");
					option.innerHTML= ("Cadastre uma marca primeiro!");
					$("#selMarca").append(option);
					$("#selMarca").addClass("aviso");
					
				}
			},
			
			//Caso aconteça qualquer erro, essa função irá mostrar ao usuário que ocorreu um erro ao carregar as marcas.
			error: function (info){
				
				COLDIGO.exibirAviso("Erro ao buscar marcas " +info.status + "-" +info.statusText);
				
				$("#selMarca").html("");
				var option = document.createElement("Option");
				option.setAttribute("value", "");
				option.innerHTML = ("Erro ao carregar marcas");
			
				$("#selMarca").append(option);
				$("#selMarca").addClass("aviso");
			}
		})
	}
	COLDIGO.produto.carregarMarcas();

	COLDIGO.produto.cadastrar = function(){

		var produto = new Object();
		produto.categoria = document.frmAddProduto.categoria.value;
		produto.marcaId = document.frmAddProduto.marcaId.value;
		produto.modelo = document.frmAddProduto.modelo.value;
		produto.capacidade = document.frmAddProduto.capacidade.value;
		produto.valor = document.frmAddProduto.valor.value;

		if((produto.categoria == "") ||(produto.marcaId=="")||(produto.modelo == "")||(produto.capacidade == "")||(produto.valor == "")){
			COLDIGO.exibirAviso("Preencha todos os campos!");
		}
		else{
			$.ajax({
				type: "POST",
				url: COLDIGO.PATH + "produto/inserir",
				data: JSON.stringify(produto),	
				success: function(msg) {
					COLDIGO.exibirAviso(msg);
					$("#addProduto").trigger("reset");
				},
				error: function (info){
					COLDIGO.exibirAviso("Erro ao cadastrar um novo produto: " + info.status + " - " +info.statusText);
				}
			});
	
		}
	}

});




