package com.sheena.microservices.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sheena.microservices.models.CatalogItem;
import com.sheena.microservices.models.Movie;
import com.sheena.microservices.models.Rating;
import com.sheena.microservices.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(String userId)
	{
		
		/*List<Rating> ratings = Arrays.asList(
			new Rating("1234",4),
			new Rating("5678",5)
		);
		*/
		
		//second argument of the rest template function is the type u gotta cast the return value to 
		//ratings service will return all the movies (ID and Ratings) that a user has watched
		UserRating ratings = restTemplate.getForObject( "http://localhost:8081/users/"+userId,UserRating.class);
		//movie-info service will return movie_name corresponding to every movie id
		return ratings.getUserRating().stream().map(rating -> {
		Movie movie=restTemplate.getForObject( "http://localhost:8081/movies/"+rating.getMovieId(),Movie.class);
		return new CatalogItem(movie.getMovieName(),"Test" ,rating.getRating());
		})
				.collect(Collectors.toList());
		
	}
}
