package com.example.fateccarona.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.fateccarona.models.NominatimResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeoLocationService {

    private final String baseUrl = "https://nominatim.openstreetmap.org/search?q=";
    private static final double RAIO_TERRA = 6371; 
   	private static final double PI = Math.PI; 
   	
   	
    public Optional<NominatimResult> buscarLocal(String local) {
        try {
            String localEncoded = URLEncoder.encode(local, StandardCharsets.UTF_8);
            String urlString = baseUrl + localEncoded + "&format=json";
            URL url = new URL(urlString);

            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            if (conexao.getResponseCode() != 200) {
                return Optional.empty();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder retorno = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                retorno.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            NominatimResult[] resultados = objectMapper.readValue(retorno.toString(), NominatimResult[].class);

            if (resultados.length > 0) {
                return Optional.of(resultados[0]); // retorna o primeiro resultado
            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    


	public  double calcularDistancia( double latitude1,double longitude1,double latitude2,double longitude2){
	    latitude1 = transformarEmRadianos(latitude1);
       longitude1 = transformarEmRadianos(longitude1);
       latitude2 = transformarEmRadianos(latitude2);
       longitude2 = transformarEmRadianos(longitude2);
	    
	    double diferencaLatitude = calcularDiferencaValores( latitude1 , latitude2);
	    double diferencaLongitude = calcularDiferencaValores(longitude1 , longitude2);

	    double valorSenoLatitudeDividido2 = elevarAoQuadrado(transformarSeno(diferencaLatitude/2));


	    double valorSenoLongitudeDividido2 = elevarAoQuadrado(transformarSeno(diferencaLongitude/2));
	    
	    double cossenoLatitude1 = transformarCosseno(latitude1);
	    double cossenoLatitude2 = transformarCosseno(latitude2);
	    double a  = Math.asin(Math.sqrt(valorSenoLatitudeDividido2 + cossenoLatitude1 * cossenoLatitude2 * valorSenoLongitudeDividido2 ));
	    double resultado = 2 * RAIO_TERRA * a ;
	    return  resultado;

	}
	public  double elevarAoQuadrado(double valor){
	    return Math.pow(valor,2);
	}
	public static double transformarSeno(double valor){
	    return  Math.sin(valor);
	}


	public  double transformarCosseno(double valor){
	    return Math.cos(valor);
	}
	public  double transformarEmRadianos(double valor){
	    double resultado = valor * (PI / 180);
	    return resultado;
	}
	
	public double calcularDiferencaValores(double valor1,double  valor2) {
		return valor2 - valor1;
	}
}
