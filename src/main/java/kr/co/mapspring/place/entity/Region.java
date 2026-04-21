package kr.co.mapspring.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter	
public class Region {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "place_id", nullable = false, updatable = false)
	private Long regionId;
	
	@Column(name = "country", nullable  = false, length = 50)
	private String country;
	
	@Column(name = "city", nullable = false, length = 50)
	private String city;
	
	

}
