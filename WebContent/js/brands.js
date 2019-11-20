COLDIGO.marca = new Object();

$(document)
		.ready(
				function() {

					COLDIGO.marca.inserir = function() {

						var marca = document.frmInserirMarcas.marca.value
						if (marca != undefined && marca != "") {
							$
									.ajax({
										type : "POST",
										url : COLDIGO.PATH + "marca/inserir",
										data : JSON.stringify(marca),
										success : function(msg) {
											COLDIGO.exibirAviso(msg)
											COLDIGO.marca.buscar()
										},
										error : function(info) {
											COLDIGO
													.exibirAviso("Erro ao cadastrar marca: "
															+ info.responseText);
										}
									})
						} else {
							alert("Preencha todos os campos")
						}
					}
					COLDIGO.marca.buscar = function() {
						var valorBusca = $("#campoBuscaMarca").val();
						$.ajax({
							type : "GET",
							url : COLDIGO.PATH + "marca/buscarPorNome",
							data : "valorBusca=" + valorBusca,
							success : function(listaMarcas) {
								$("#listaMarcas").html(
										COLDIGO.marca.exibir(listaMarcas))
							},
							error : function() {
								COLDIGO
										.exibirAviso("Erro ao buscar marcas: "
												+ info.status + " - "
												+ info.statusText);
							}
						})
					}

					COLDIGO.marca.exibir = function(listaMarcas) {

						var tabela = "<table>" + "<tr>" + "<th> Status </th>"
								+ "<th> Marca </th>"
								+ "<th class='acoes'> Ações </th>" + "</tr>"
								

						if (listaMarcas != undefined && listaMarcas.length > 0) {

							for (var i = 0; i < listaMarcas.length; i++) {
								var status = "";
								if(listaMarcas[i].status == 1){
									status = "checked"
								}
								tabela += "<tr>"
										+ "<td>"
										+ "<input type='checkbox' class='switch slider round' onclick='COLDIGO.marca.onoff("
										+ listaMarcas[i].id
										+ ")' id='"
										+ listaMarcas[i].id
										+ "' "+status+">"
										+ "</td>"
										+ "<td>"
										+ listaMarcas[i].nome
										+ "</td>"
										+ "<td>"
										+ "<a onclick=\"COLDIGO.marca.exibirEdicao('"
										+ listaMarcas[i].id
										+ "')\"><img src='../../imgs/edit.png' alt='Editar registro'></a>"
										+ "<a onclick=\"COLDIGO.marca.deletar('"
										+ listaMarcas[i].id
										+ "')\"><img src='../../imgs/delete.png' alt='Deletar registro'></a>"
										+ "</td>" + "</tr>"
										
								
							}

						} else if (listaMarcas == "") {
							tabela += "<tr><td colspan='3'>Nenhum registro encontrado</td></tr>"
						}
						tabela += "</table>"

						return tabela;
					}

					COLDIGO.marca.deletar = function(id) {
						var modalExcluirMarca = {
							title : "Confirmar",
							height : 200,
							width : 300,
							modal : true,
							buttons : {
								"Sim" : function() {
									$
											.ajax({
												type : "DELETE",
												url : COLDIGO.PATH
														+ "marca/deletar/" + id,
												success : function(msg) {
													COLDIGO.exibirAviso(msg)
													COLDIGO.marca.buscar()
													$("#modalExcluirMarca")
															.dialog("close");
												},
												error : function(info) {
													COLDIGO
															.exibirAviso("Erro ao excluir marca: "
																	+ info.responseText);
													$("#modalExcluirMarca")
															.dialog("close");
												}
											})
								},
								"Não" : function() {
									$("modalExckuirMarca").dialog("close");
								}
							}
						}
						$("#modalExcluirMarca").dialog(modalExcluirMarca);
					}
					COLDIGO.marca.exibirEdicao = function(id) {
						$
								.ajax({
									type : "GET",
									url : COLDIGO.PATH + "marca/buscarPorId/"
											+ id,
									success : function(marca) {
										marca = JSON.parse(marca)
										document.frmEditaMarca.idMarca.value = marca.id;
										document.frmEditaMarca.marca.value = marca.nome;
										var modalEditaMarca = {

											title : "Editar marca",
											height : 200,
											width : 400,
											modal : true,
											buttons : {
												"Salvar" : function() {
													COLDIGO.marca.editar()
													$(this).dialog("close")
												},
												"Cancelar" : function() {
													$(this).dialog("close")
												}
											},
										}
										$("#modalEditaMarca").dialog(
												modalEditaMarca);
									},
									error : function(info) {
										COLDIGO
												.exibirAviso("ERRO AO BUSCAR MARCAAAAAAAAAAH!")
									}
								})
					}
					COLDIGO.marca.editar = function() {
						var marca = new Object()
						marca.id = document.frmEditaMarca.idMarca.value
						marca.nome = document.frmEditaMarca.marca.value
						$
								.ajax({
									type : "PUT",
									url : COLDIGO.PATH + "marca/editar",
									data : JSON.stringify(marca),
									success : function(msg) {

										COLDIGO.exibirAviso(msg)
										COLDIGO.marca.buscar()
										$(this).dialog("close")

									},
									error : function(msg) {

										COLDIGO
												.exibirAviso("Erro ao cadastrar marca: "
														+ info.status
														+ " - "
														+ info.statusText);

									}
								})
					}

					COLDIGO.marca.onoff = function(id) {
						var status
						if (document.getElementById(id).checked == false) {
							status = 0;
						} else {
							status = 1;
						}
						
						var marca = new Object();
						
						marca.id = id
						marca.status = status
						
						$.ajax({
							type : "PUT",
							url : COLDIGO.PATH + "marca/onoff",
							data : JSON.stringify(marca),
							success : function(msg) {
								COLDIGO.exibirAviso(msg)
								COLDIGO.marca.buscar()
							},
							error : function(info) {
								COLDIGO.exibirAviso("ERRO! " +info.responseText)
								document.getElementById(id).checked =! document.getElementById(id).checked
							}

						})

					}

					COLDIGO.marca.buscar()

				})