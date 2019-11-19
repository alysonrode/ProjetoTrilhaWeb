//cria um objeto codigo
COLDIGO.produto = new Object();

$(document).ready(function(){
	COLDIGO.produto.carregarMarcas = function(id){
		if(id!=undefined){
			select = "#selMarcaEdicao"
		}else{
			select = "#selMarca"
		}
		$.ajax({
			type: "GET",
			url: COLDIGO.PATH+"marca/buscar",
			success: function (marca) {
				//Aqui, ele verifica se a lista de marcas não está vazia, e limpa o campo selMarca, e adiciona um campo chamado Escolha, com um valor vazio.
				//Após isso, ele começa a executar um for, que vai adicionando os elementos com seus respectivos valores.
				
				if(marca!=""){
				
					$(select).html("");
					var option = document.createElement("option");
					option.setAttribute ("value", "");
					option.innerHTML=("Escolha");
					$(select).append(option);
					
					for (var i = 0; i < marca.length; i++){
						var option = document.createElement("option");
						option.setAttribute ("value", marca[i].id);
						
						if ((id!=undefined) && (id==marca[i].id))
							option.setAttribute ("selected", "selected")
						
						option.innerHTML = (marca[i].nome);
						$(select).append(option);
					}
					//Caso a lista esteja vazia, ele irá criar um elemento com a seguinte mensagem mostrada na linha 39
				}else{
					$(select).html("");
					
					var option = document.createElement("option");
					option.setAttribute ("value", "");
					option.innerHTML= ("Cadastre uma marca primeiro!");
					$(select).append(option);
					$(select).addClass("aviso");
					
				}
			},
		
			
			//Caso aconteça qualquer erro, essa função irá mostrar ao usuário que ocorreu um erro ao carregar as marcas.
			error: function (info){
				
				COLDIGO.exibirAviso("Erro ao buscar marcas " +info.status + "-" +info.statusText);
				
				$(select).html("");
				var option = document.createElement("Option");
				option.setAttribute("value", "");
				option.innerHTML = ("Erro ao carregar marcas");
			
				$(select).append(option);
				$(select).addClass("aviso");
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
					COLDIGO.produto.buscar();
				},
				error: function (info){
					COLDIGO.exibirAviso("Erro ao cadastrar um novo produto: " + info.status + " - " +info.statusText);
				}
			});
	
		}
	}
		//Busca no banco de dados os produtos que atendem a solicitação do usuário.
		COLDIGO.produto.buscar = function(){
			
			var valorBusca = $("#campoBuscaProduto").val();
			$.ajax({
				type:"GET",
				url: COLDIGO.PATH + "produto/buscar",
				data: "valorBusca="+valorBusca,
				success: function(dados){	
					
					//https://i.kym-cdn.com/photos/images/original/001/514/213/b94.png
					$("#listaProdutos").html(COLDIGO.produto.exibir(dados));
			
				},
				error: function(info){
					COLDIGO.exibirAviso("Erro ao consultar os contatos: "+ info.status + " - " + info.statusText);
				}
				
			
			});
			
		}
			
			COLDIGO.produto.exibir = function(listaDeProdutos){
				
				var tabela = "<table>" +
				"<tr>" +
				"<th> Categoria </th>"+
				"<th> Marca </th>"+
				"<th> Modelo </th>"+
				"<th> Cap (L) </th>"+
				"<th> Valor </th>"+
				"<th class='acoes'> Ações </th>"+
				"</tr>"
				
				if(listaDeProdutos != undefined && listaDeProdutos.length > 0){
					
					for(var i=0; i < listaDeProdutos.length; i++){
						tabela += "<tr>" +
								"<td>"+listaDeProdutos[i].categoria+"</td>"+
								"<td>"+listaDeProdutos[i].marcaNome+"</td>"+
								"<td>"+listaDeProdutos[i].modelo+"</td>"+
								"<td>"+listaDeProdutos[i].capacidade+"</td>"+
								"<td>R$"+COLDIGO.formatarDinheiro(listaDeProdutos[i].valor)+"</td>"+
								"<td>" +
									"<a onclick=\"COLDIGO.produto.exibirEdicao('"+listaDeProdutos[i].id+"')\"><img src='../../imgs/edit.png' alt='Editar registro'></a>" +
									"<a onclick=\"COLDIGO.produto.excluir('"+listaDeProdutos[i].id+"')\"><img src='../../imgs/delete.png' alt='Deletar registro'></a>" +
								"</td>" +
								"</tr>"
					}
					
					
				}else if (listaDeProdutos == ""){
					tabela += "<tr><td colspan='6'>Nenhum registro encontrado</td></tr>"
				}
				tabela += "</table>" 
				
					return tabela;	
			}
	
	COLDIGO.produto.buscar();
	
	
		//Exclui o produto selecionado
	COLDIGO.produto.excluir = function(id){
		
		var modalExcluiProduto = {
				title: "Confirmar",
				height: 200,
				width: 300,
				modal: true,
				buttons: {
					"Sim": function(){
						$.ajax({
							type:"DELETE",
							url: COLDIGO.PATH + "produto/excluir/" +id,
							success: function(msg){
								COLDIGO.exibirAviso(msg)
								COLDIGO.produto.buscar()
								$(".modalExcluirProduto").dialog("close");
							},
							error: function(info){
								COLDIGO.exibirAviso("Erro ao excluir produto: " + info.status + " - " + info.statusText)
							}
								
					
						})
					},
					
					"Cancelar": function(){
					
						$(this).dialog("close")
					
					}
				
			}
		}
		$(".modalExcluirProduto").dialog(modalExcluiProduto);
		
	
		
		
	}
	COLDIGO.produto.exibirEdicao = function(id){
		$.ajax({
			type: "GET",
			url: COLDIGO.PATH + "produto/buscarPorId",
			data: "id=" +id,
			success: function(produto){
				document.frmEditaProduto.idProduto.value = produto.id;
				document.frmEditaProduto.modelo.value = produto.modelo;
				document.frmEditaProduto.capacidade.value = produto.capacidade;
				document.frmEditaProduto.valor.value = produto.valor;
				
				var selCategoria = document.getElementById('selCategoriaEdicao');
				for (var i = 0; i < selCategoria.length; i++){
				if (selCategoria.options[i].value == produto.categoria){
					selCategoria.options[i].setAttribute("selected", "selected");
				}else{
					selCategoria.options[i].removeAttribute("selected");
				}
			}
				COLDIGO.produto.carregarMarcas(produto.marcaId);
				
				var modalEditaProduto = {
					title: "Editar produto",
					height: 400,
					width: 550,
					modal: true,
					buttons:{
						"Salvar": function(){
							COLDIGO.produto.editar()
						},
						"Cancelar": function(){
							$(this).dialog("close")
						}
					},
						close: function(){
							
						}
					
				}
				
				$("#modalEditaProduto").dialog(modalEditaProduto);
				
			},
			error: function(info){
				COLDIGO.exibirAviso("Erro ao buscar produto para edição" + info.status + " - " + info.statusText)
			}
		});
	}
	COLDIGO.produto.editar = function(){
		var produto = new Object()
		produto.id = document.frmEditaProduto.idProduto.value;
		produto.categoria = document.frmEditaProduto.categoria.value;
		produto.marcaId = document.frmEditaProduto.marcaId.value;
		produto.modelo = document.frmEditaProduto.modelo.value;
		produto.capacidade = document.frmEditaProduto.capacidade.value;
		produto.valor = document.frmEditaProduto.valor.value;
	
		$.ajax({
			
			type: "PUT",
			//em minusculo
			url: COLDIGO.PATH + "produto/alterar",
			data: JSON.stringify(produto),
			success: function(msg){
				COLDIGO.exibirAviso(msg)
				COLDIGO.produto.buscar();
				$("#modalEditaProduto").dialog("close");
			},
			error: function(){
				COLDIGO.exibirAviso("Erro ao editar produto " +info.status + " - " + info.statusText)
			},
		})
		
		
	}

})
	