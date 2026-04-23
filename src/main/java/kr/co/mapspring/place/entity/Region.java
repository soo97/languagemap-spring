package kr.co.mapspring.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
	
	// 테스트 코드 실행용
	public static Region from(Long regionId) {
		Region region = new Region();
		region.regionId = regionId;
		return region;
	}
	
	

}
