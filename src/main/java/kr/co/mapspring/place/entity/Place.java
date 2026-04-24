package kr.co.mapspring.place.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "place")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Place {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "place_id", nullable = false, updatable = false)
	private Long placeId;
	
	@Column(name = "google_place_id", nullable = false, unique = true)
	private String googlePlaceId;
	
	@Column(name = "place_name", nullable = false, length = 100)
	private String placeName;
	
	@Column(name = "place_description", nullable = false, columnDefinition = "TEXT")
	private String placeDescription;
	
	@Column(name = "place_address", nullable = false, length = 255)
	private String placeAddress;
	
	@Column(name = "latitude", nullable = false, precision = 11, scale = 8)
	private BigDecimal latitude;
	
	@Column(name = "longitude", nullable = false, precision = 11, scale = 8)
	private BigDecimal longitude;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id", nullable = false)
	private Region region;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scenario_id", nullable = false)
	private Scenario scenario;
	
	public static Place create (String googlePlaceId,
						   String placeName,
						   String placeAddress,
						   String placeDescription,
						   BigDecimal latitude,
						   BigDecimal longitude,
						   Scenario scenario,
						   Region region) {

		Place place = new Place();
		place.googlePlaceId = googlePlaceId;
		place.placeName = placeName;
		place.placeAddress = placeAddress;
		place.placeDescription = placeDescription;
		place.latitude = latitude;
		place.longitude = longitude;
		place.scenario = scenario;
		place.region = region;
		return place;
	}
	
	// 테스트 코드 실행용
	public static Place testOf (Long placeId,
							    String googlePlaceId,
							    String placeName,
							    String placeAddress,
							    String placeDescription,
							    BigDecimal latitude,
							    BigDecimal longitude,
							    Region region,
							    Scenario scenario)
	{
		Place place = new Place();
		place.placeId = placeId;
		place.googlePlaceId = googlePlaceId;
		place.placeName = placeName;
		place.placeAddress = placeAddress;
		place.placeDescription = placeDescription;
		place.latitude = latitude;
		place.longitude = longitude;
		place.region = region;
		place.scenario = scenario;
		return place;
		
	}

	public void update(String placeName, 
		  	   String placeDescription, 
		  	   Scenario scenario) 
{
	this.placeName = placeName;
	this.placeDescription = placeDescription;
	this.scenario = scenario;
}
	
}
