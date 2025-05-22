package com.example.fateccarona.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NominatimResult {
	
	  @JsonProperty("place_id")
	    private Long idLocal;

	    @JsonProperty("licence")
	    private String licenca;

	    @JsonProperty("osm_type")
	    private String osmType;

	    @JsonProperty("osm_id")
	    private Long osmId;

	    @JsonProperty("lat")
	    private String lat;

	    @JsonProperty("lon")
	    private String lon;

	    @JsonProperty("class")
	    private String classe;

	    @JsonProperty("type")
	    private String tipo;

	    @JsonProperty("place_rank")
	    private Integer classificacaoDeLugar;

	    @JsonProperty("importance")
	    private Double importancia;

	    @JsonProperty("addresstype")
	    private String addressType;

	    @JsonProperty("name")
	    private String name;

	    @JsonProperty("display_name")
	    private String displayName;

	    @JsonProperty("boundingbox")
	    private List<String> boundingBox;
}
