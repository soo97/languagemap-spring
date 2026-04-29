package kr.co.mapspring.place.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Region {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "region_id", nullable = false, updatable = false)
	private Long regionId;
	
	@Column(name = "country", nullable  = false, length = 50)
	private String country;
	
	@Column(name = "city", nullable = false, length = 50)
	private String city;
	
	@Column(name = "latitude", nullable = false, precision = 11, scale = 8)
	private BigDecimal latitude;
	
	@Column(name = "longitude", nullable = false, precision = 11, scale = 8)
	private BigDecimal longitude;
	
	
	
	public static Region create(String country,
								String city,
								BigDecimal latitude,
								BigDecimal longitude) {
		Region region = new Region();
		region.country = country;
		region.city = city;
		region.latitude = latitude;
		region.longitude = longitude;
		
		return region;
}

	public void update(String country,
					   String city,
					   BigDecimal latitude,
					   BigDecimal longitude) {
		this.country = country;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
}
	
	// 테스트 코드 실행용
	public static Region withId(Long regionId) {
		Region region = new Region();
		region.regionId = regionId;
		
		return region;
	}

	// CoachingEntryServiceTest 테스트 코드 실행용
	public static Region testOf(long regionId,
								String country,
								String city) {
		Region region = new Region();
		region.regionId = regionId;
		region.country = country;
		region.city = city;
		
		return region;
	}
	
	

}
