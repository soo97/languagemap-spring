package kr.co.mapspring.place.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.mapspring.place.dto.SavePlaceDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "place")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Place {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "place_id", nullable = false, updatable = false)
	@Setter
	private Long placeId;
	
	@Column(name = "google_place_id", nullable = false, unique = true, length = 150)
	private String googlePlaceId;
	
	@Column(name = "place_name", nullable = false, length = 100)
	private String placeName;
	
	@Column(name = "place_description", nullable = false, columnDefinition = "TEXT")
	private String placeDescription;
	
	@Column(name = "latitude", nullable = false, precision = 11, scale = 8)
	private BigDecimal latitude;
	
	@Column(name = "longitude", nullable = false, precision = 11, scale = 8)
	private BigDecimal longitude;
	
	@Column(name = "category", nullable = false, length = 50)
	private String category;  
	
	public static Place placeFrom(SavePlaceDto.RequsetSaveDto request) {
		return Place.builder()
  			  .googlePlaceId(request.getGooglePlaceId())
  			  .category(request.getCategory())
  			  .latitude(request.getLatitude())
  			  .longitude(request.getLongitude())
  			  .placeDescription(request.getPlaceDescription())
  			  .placeName(request.getPlaceName())
  			  .build();
	}
	
	
	
	

}
