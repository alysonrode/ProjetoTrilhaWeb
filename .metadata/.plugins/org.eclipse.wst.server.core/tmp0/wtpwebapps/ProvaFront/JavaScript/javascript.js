function validaPedido(){

var nome = document.pedidos.txtnome.value;
var expRegNome = new RegExp("^[A-zÀ-ü]{3,}([ ]{1}[A-zÀ-ü]{2,})+$");

     if(!expRegNome.test(nome)){   
         alert("Preencha o nome corretamente");

         document.pedidos.txtnome.focus();

         return false;
     }

var data = document.pedidos.dateEntrega.value;
	
        if(data == ""){
          alert("Preencha a data");

          return false;
 	}
var escolha = document.pedidos.selectDoces.value
	if(escolha==""){
        alert("Selecione alguma opção");

        return false;
	}
var quantidade = document.pedidos.quantidade.value;
	if (quantidade == ""){
        alert("Poe a quantidade ai");

        return false;
        }
        if(quantidade < 1 || quantidade > 300){
                alert("Digite um valor válido, entre 1 e 300")
                return false;
        }
var endereco = document.pedidos.txtEndereco.value;
	if(document.pedidos.retirar.checked == false && endereco == ""){
        alert("Você deseja retirar em balcão, ou entrega? ");

        return false;
	}
var telefone = document.pedidos.txtTelefone.value
var expRegTel = new RegExp("^[(]{1}[0-9]{2}[)]{1}[0-9]{4,5}[-]{1}[0-9]{4}$");

        if(!expRegTel.test(telefone)){
                alert("Preencha o telefone corretamente")

                return false;
        }
return true
alert("Orçamento efetuado")
}