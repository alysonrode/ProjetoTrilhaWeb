public class Angulos {
	
	public static int transformaMnutoEmAngulo(int minuto){

//		Calcula os angulos dos ponteiros em relação ao relógio, levando como consideração 00:00 como 0º.
//		Como os primeiros 15 min são referente a 45º, separei os intevalos de tempo de 15 em 15min, para 
//		que cada intervalo leve os intevalos anteriores em consideração, e assim fechar os 360º, com cada
//		intervalo com sua angulação correta, sendo:
//		15 min -- 45º
//		30 min -- 180º
//		45 min -- 225º
//		60 min -- 360º

		int anguloPonteiroMinuto = 0;
		
		if (minuto <= 15) {
			anguloPonteiroMinuto = minuto * 3;
		}
		else if(minuto > 15 && minuto <= 30) {
			int newMinuto = minuto - 15;
			anguloPonteiroMinuto = (newMinuto * 6) + 45;
		}
		else if(minuto > 30 && minuto <= 45) {
			int newMinuto = minuto - 30;
			anguloPonteiroMinuto = (newMinuto * 3) + 180;
		}
		else if(minuto > 45 && minuto <=59) {
			int newMinuto = minuto - 45;
			anguloPonteiroMinuto = (newMinuto * 6) + 225;
		}
//		Retorna o angulo do ponteiro dos minutos.		
		return anguloPonteiroMinuto;
	}
	
	public static int transformaHoraEmAngulo(int hora){
//		Basicamente, dividindo 360º em 12hrs temos 30, ou seja, cada hora equivale à 30º, como isso temos: 		
		int angulo = 30;
		int anguloPonteiroHora = hora * angulo;
// 		Retorna o angulo do ponteiro da hora.
		return anguloPonteiroHora;
	}
	
	public static int calculaAnguloEntrePonteiros(int anguloHora, int anguloMinuto){
//		calcula a diferença entre os angulos dos ponteiros.		
		int anguloEntrePonteiros = anguloHora - anguloMinuto;
//		Se o calculo dos angulos for negativo, essa validação irá transforma-lo em positivo.
		if (anguloEntrePonteiros < 0) {
			anguloEntrePonteiros = anguloEntrePonteiros * -1;
			return anguloEntrePonteiros;
		}else {
			return anguloEntrePonteiros;
		}
		
	}
	
}
