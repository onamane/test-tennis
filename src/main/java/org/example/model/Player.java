package org.example.model;

import lombok.Builder;
import lombok.Data;

/**
 * Definition of a Player from a tennis game
 */
@Builder
@Data
public class Player {
	private String name;
	private Integer points;
	private Boolean advantage;
}
